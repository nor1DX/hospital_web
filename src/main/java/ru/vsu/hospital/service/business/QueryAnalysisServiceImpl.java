package ru.vsu.hospital.service.business;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.hospital.model.dto.QueryBenchmarkDto;

import java.util.List;
import java.util.function.Supplier;

@Service
public class QueryAnalysisServiceImpl implements QueryAnalysisService {

    @PersistenceContext
    private EntityManager em;

    private static final String SQL_BY_FIELD =
            "SELECT * FROM doctors WHERE specialization = 'Терапевт' LIMIT 100";

    private static final String SQL_SORTED =
            "SELECT * FROM doctors ORDER BY last_name ASC LIMIT 1000";

    private static final String SQL_JOIN = """
            SELECT mc.id, mc.diagnosis,
                   COALESCE(d.first_name || ' ' || d.last_name, 'Не назначен') AS doctor_name,
                   p.first_name || ' ' || p.last_name AS patient_name,
                   i.name AS illness_name
            FROM medical_cards mc
            LEFT JOIN doctors d ON mc.doctor_id = d.id
            LEFT JOIN patients p ON mc.patient_id = p.id
            LEFT JOIN illnesses i ON mc.illness_id = i.id
            LIMIT 1000""";

    private static final String SQL_AGGREGATION =
            "SELECT specialization, COUNT(*) FROM doctors GROUP BY specialization ORDER BY COUNT(*) DESC";

    @Override
    @SuppressWarnings("unchecked")
    public List<QueryBenchmarkDto> runAllBenchmarks(boolean withExplain) {
        List<Object> idList = em.createNativeQuery("SELECT id FROM doctors LIMIT 1").getResultList();
        String sampleId = idList.isEmpty() ? null : idList.get(0).toString();

        String sqlById = sampleId != null
                ? "SELECT * FROM doctors WHERE id = '" + sampleId + "'"
                : null;

        return List.of(
                benchmarkById(sampleId, withExplain),
                benchmark("byField",
                        "SELECT * FROM doctors WHERE specialization = 'Терапевт' LIMIT 100",
                        () -> em.createNativeQuery(SQL_BY_FIELD).getResultList(),
                        withExplain ? "EXPLAIN (ANALYZE, FORMAT TEXT) " + SQL_BY_FIELD : null),
                benchmark("sorted",
                        "SELECT * FROM doctors ORDER BY last_name ASC LIMIT 1000",
                        () -> em.createNativeQuery(SQL_SORTED).getResultList(),
                        withExplain ? "EXPLAIN (ANALYZE, FORMAT TEXT) " + SQL_SORTED : null),
                benchmark("join",
                        "SELECT mc.*, d.*, p.*, i.* FROM medical_cards mc LEFT JOIN doctors/patients/illnesses LIMIT 1000",
                        () -> em.createNativeQuery(SQL_JOIN).getResultList(),
                        withExplain ? "EXPLAIN (ANALYZE, FORMAT TEXT) " + SQL_JOIN : null),
                benchmark("aggregation",
                        "SELECT specialization, COUNT(*) FROM doctors GROUP BY specialization",
                        () -> em.createNativeQuery(SQL_AGGREGATION).getResultList(),
                        withExplain ? "EXPLAIN (ANALYZE, FORMAT TEXT) " + SQL_AGGREGATION : null)
        );
    }

    @SuppressWarnings("unchecked")
    private QueryBenchmarkDto benchmarkById(String sampleId, boolean withExplain) {
        if (sampleId == null) {
            return QueryBenchmarkDto.builder()
                    .queryType("byId")
                    .description("SELECT * FROM doctors WHERE id = ? (no data)")
                    .executionTimeMs(0)
                    .build();
        }

        String sql = "SELECT * FROM doctors WHERE id = '" + sampleId + "'";
        return benchmark("byId",
                "SELECT * FROM doctors WHERE id = '" + sampleId + "'",
                () -> em.createNativeQuery(sql).getResultList(),
                withExplain ? "EXPLAIN (ANALYZE, FORMAT TEXT) " + sql : null);
    }

    @SuppressWarnings("unchecked")
    private QueryBenchmarkDto benchmark(String type, String description,
                                        Supplier<Object> queryRunner, String explainSql) {
        long start = System.currentTimeMillis();
        queryRunner.get();
        long elapsed = System.currentTimeMillis() - start;

        List<String> plan = null;
        if (explainSql != null) {
            plan = ((List<Object>) em.createNativeQuery(explainSql).getResultList())
                    .stream()
                    .map(Object::toString)
                    .toList();
        }

        return QueryBenchmarkDto.builder()
                .queryType(type)
                .description(description)
                .executionTimeMs(elapsed)
                .explainPlan(plan)
                .build();
    }

    @Override
    @Transactional
    public void createIndexes() {
        em.createNativeQuery("CREATE INDEX IF NOT EXISTS idx_doctors_specialization ON doctors(specialization)").executeUpdate();
        em.createNativeQuery("CREATE INDEX IF NOT EXISTS idx_doctors_last_name ON doctors(last_name)").executeUpdate();
        em.createNativeQuery("CREATE INDEX IF NOT EXISTS idx_medical_cards_doctor_id ON medical_cards(doctor_id)").executeUpdate();
        em.createNativeQuery("CREATE INDEX IF NOT EXISTS idx_medical_cards_patient_id ON medical_cards(patient_id)").executeUpdate();
        em.createNativeQuery("CREATE INDEX IF NOT EXISTS idx_medical_cards_illness_id ON medical_cards(illness_id)").executeUpdate();
    }

    @Override
    @Transactional
    public void dropIndexes() {
        em.createNativeQuery("DROP INDEX IF EXISTS idx_doctors_specialization").executeUpdate();
        em.createNativeQuery("DROP INDEX IF EXISTS idx_doctors_last_name").executeUpdate();
        em.createNativeQuery("DROP INDEX IF EXISTS idx_medical_cards_doctor_id").executeUpdate();
        em.createNativeQuery("DROP INDEX IF EXISTS idx_medical_cards_patient_id").executeUpdate();
        em.createNativeQuery("DROP INDEX IF EXISTS idx_medical_cards_illness_id").executeUpdate();
    }
}
