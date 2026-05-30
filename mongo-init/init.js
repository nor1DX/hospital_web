db = db.getSiblingDB('hospital');

db.createCollection('doctors');
db.createCollection('patients');
db.createCollection('illnesses');
db.createCollection('medical_cards');

// Full-text index for searching illness descriptions
db.illnesses.createIndex(
    { description: "text" },
    { name: "illness_description_text" }
);

// TTL index: MongoDB automatically removes recovered patient records when expireAt is in the past
db.medical_cards.createIndex(
    { expireAt: 1 },
    { expireAfterSeconds: 0, name: "medical_cards_ttl", sparse: true }
);

// Initial doctors
db.doctors.insertMany([
    {
        _id: ObjectId("507f1f77bcf86cd799439011"),
        firstName: "Иван",
        lastName: "Петров",
        specialization: "Терапевт"
    },
    {
        _id: ObjectId("507f1f77bcf86cd799439012"),
        firstName: "Мария",
        lastName: "Сидорова",
        specialization: "Кардиолог"
    },
    {
        _id: ObjectId("507f1f77bcf86cd799439013"),
        firstName: "Алексей",
        lastName: "Козлов",
        specialization: "Хирург"
    }
]);

// Initial illnesses (description field is indexed for full-text search)
db.illnesses.insertMany([
    {
        _id: ObjectId("507f1f77bcf86cd799439021"),
        name: "ОРВИ",
        description: "Острая респираторная вирусная инфекция. Характеризуется воспалением верхних дыхательных путей, насморком и кашлем.",
        severity: "MILD"
    },
    {
        _id: ObjectId("507f1f77bcf86cd799439022"),
        name: "Пневмония",
        description: "Воспаление лёгких. Серьёзное инфекционное заболевание нижних дыхательных путей, требующее госпитализации.",
        severity: "SEVERE"
    },
    {
        _id: ObjectId("507f1f77bcf86cd799439023"),
        name: "Гипертония",
        description: "Повышенное артериальное давление. Хроническое сердечно-сосудистое заболевание, требующее постоянного контроля.",
        severity: "MODERATE"
    }
]);

// Initial patients
db.patients.insertMany([
    {
        _id: ObjectId("507f1f77bcf86cd799439031"),
        firstName: "Дмитрий",
        lastName: "Смирнов",
        dateOfBirth: "1985-03-15",
        medicalCardId: null
    },
    {
        _id: ObjectId("507f1f77bcf86cd799439032"),
        firstName: "Елена",
        lastName: "Новикова",
        dateOfBirth: "1990-07-22",
        medicalCardId: null
    },
    {
        _id: ObjectId("507f1f77bcf86cd799439033"),
        firstName: "Сергей",
        lastName: "Федоров",
        dateOfBirth: "1978-11-08",
        medicalCardId: null
    }
]);

// Initial medical cards (expireAt is null = patient is still sick, TTL index ignores null values)
db.medical_cards.insertMany([
    {
        _id: ObjectId("507f1f77bcf86cd799439041"),
        patientId: "507f1f77bcf86cd799439031",
        doctorId: "507f1f77bcf86cd799439011",
        illnessId: "507f1f77bcf86cd799439021",
        diagnosis: "ОРВИ средней тяжести",
        treatmentStart: new Date("2026-05-25"),
        expireAt: null
    },
    {
        _id: ObjectId("507f1f77bcf86cd799439042"),
        patientId: "507f1f77bcf86cd799439032",
        doctorId: "507f1f77bcf86cd799439012",
        illnessId: "507f1f77bcf86cd799439023",
        diagnosis: "Гипертония I степени",
        treatmentStart: new Date("2026-05-20"),
        expireAt: null
    }
]);

// Link patients to their medical cards
db.patients.updateOne(
    { _id: ObjectId("507f1f77bcf86cd799439031") },
    { $set: { medicalCardId: "507f1f77bcf86cd799439041" } }
);
db.patients.updateOne(
    { _id: ObjectId("507f1f77bcf86cd799439032") },
    { $set: { medicalCardId: "507f1f77bcf86cd799439042" } }
);

print("Hospital database initialized successfully!");
print("Collections: doctors, patients, illnesses, medical_cards");
print("Indexes: text index on illnesses.description, TTL index on medical_cards.expireAt");
