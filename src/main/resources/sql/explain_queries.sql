-- ============================================================
-- Task 7: Query plan analysis — run these in pgAdmin Query Tool
-- Database: hospital
-- ============================================================

-- ============================================================
-- 0. Check current indexes
-- ============================================================
SELECT indexname, tablename, indexdef
FROM pg_indexes
WHERE schemaname = 'public'
  AND tablename IN ('doctors', 'patients', 'medical_cards', 'illnesses')
ORDER BY tablename, indexname;

-- ============================================================
-- 1. BEFORE optimization: EXPLAIN ANALYZE without custom indexes
--    (drop indexes first via DELETE http://localhost:8080/query-analysis/indexes)
-- ============================================================

-- 1a. Выборка по id (by primary key — always uses index)
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT)
SELECT * FROM doctors WHERE id = (SELECT id FROM doctors LIMIT 1);

-- 1b. Выборка по полю (by field — SEQ SCAN expected without index)
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT)
SELECT * FROM doctors WHERE specialization = 'Терапевт' LIMIT 100;

-- 1c. Выборка с сортировкой (with sort — SEQ SCAN + Sort expected without index)
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT)
SELECT * FROM doctors ORDER BY last_name ASC LIMIT 1000;

-- 1d. Выборка с JOIN (with join — Hash Join / Nested Loop expected without indexes)
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT)
SELECT mc.id,
       mc.diagnosis,
       COALESCE(d.first_name || ' ' || d.last_name, 'Не назначен') AS doctor_name,
       p.first_name || ' ' || p.last_name                          AS patient_name,
       i.name                                                        AS illness_name
FROM medical_cards mc
         LEFT JOIN doctors d ON mc.doctor_id = d.id
         LEFT JOIN patients p ON mc.patient_id = p.id
         LEFT JOIN illnesses i ON mc.illness_id = i.id
LIMIT 1000;

-- 1e. Выборка с агрегацией (aggregation — HashAggregate + Seq Scan expected without index)
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT)
SELECT specialization, COUNT(*)
FROM doctors
GROUP BY specialization
ORDER BY COUNT(*) DESC;

-- ============================================================
-- 2. Create indexes (or via POST http://localhost:8080/query-analysis/indexes)
-- ============================================================
CREATE INDEX IF NOT EXISTS idx_doctors_specialization ON doctors (specialization);
CREATE INDEX IF NOT EXISTS idx_doctors_last_name      ON doctors (last_name);
CREATE INDEX IF NOT EXISTS idx_medical_cards_doctor_id  ON medical_cards (doctor_id);
CREATE INDEX IF NOT EXISTS idx_medical_cards_patient_id ON medical_cards (patient_id);
CREATE INDEX IF NOT EXISTS idx_medical_cards_illness_id ON medical_cards (illness_id);

-- ============================================================
-- 3. AFTER optimization: same queries — now with indexes
-- ============================================================

-- 3a. Выборка по id (unchanged — was already fast via PK)
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT)
SELECT * FROM doctors WHERE id = (SELECT id FROM doctors LIMIT 1);

-- 3b. Выборка по полю — Index Scan expected
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT)
SELECT * FROM doctors WHERE specialization = 'Терапевт' LIMIT 100;

-- 3c. Выборка с сортировкой — Index Scan (no explicit sort needed)
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT)
SELECT * FROM doctors ORDER BY last_name ASC LIMIT 1000;

-- 3d. Выборка с JOIN — Index Scan on FK columns
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT)
SELECT mc.id,
       mc.diagnosis,
       COALESCE(d.first_name || ' ' || d.last_name, 'Не назначен') AS doctor_name,
       p.first_name || ' ' || p.last_name                          AS patient_name,
       i.name                                                        AS illness_name
FROM medical_cards mc
         LEFT JOIN doctors d ON mc.doctor_id = d.id
         LEFT JOIN patients p ON mc.patient_id = p.id
         LEFT JOIN illnesses i ON mc.illness_id = i.id
LIMIT 1000;

-- 3e. Выборка с агрегацией — may use index-only scan or bitmap scan
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT)
SELECT specialization, COUNT(*)
FROM doctors
GROUP BY specialization
ORDER BY COUNT(*) DESC;

-- ============================================================
-- 4. Drop indexes (or via DELETE http://localhost:8080/query-analysis/indexes)
-- ============================================================
DROP INDEX IF EXISTS idx_doctors_specialization;
DROP INDEX IF EXISTS idx_doctors_last_name;
DROP INDEX IF EXISTS idx_medical_cards_doctor_id;
DROP INDEX IF EXISTS idx_medical_cards_patient_id;
DROP INDEX IF EXISTS idx_medical_cards_illness_id;

-- ============================================================
-- 5. Count rows (verify data loaded)
-- ============================================================
SELECT 'doctors'      AS table_name, COUNT(*) FROM doctors
UNION ALL
SELECT 'patients',    COUNT(*) FROM patients
UNION ALL
SELECT 'illnesses',   COUNT(*) FROM illnesses
UNION ALL
SELECT 'medical_cards', COUNT(*) FROM medical_cards;
