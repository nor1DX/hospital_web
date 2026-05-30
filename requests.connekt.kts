// Создать врача
POST("http://localhost:8080/doctors") {
    header("Content-Type", "application/json")
    body(
        """
        {
          "firstName": "Иван",
          "lastName": "Петров",
          "specialization": "Терапевт"
        }
        """.trimIndent()
    )
}

// Создать врача
POST("http://localhost:8080/doctors") {
    header("Content-Type", "application/json")
    body(
        """
        {
          "firstName": "Мария",
          "lastName": "Сидорова",
          "specialization": "Кардиолог"
        }
        """.trimIndent()
    )
}

// Создать пациента
POST("http://localhost:8080/patients") {
    header("Content-Type", "application/json")
    body(
        """
        {
          "firstName": "Дмитрий",
          "lastName": "Смирнов",
          "dateOfBirth": "1985-03-15"
        }
        """.trimIndent()
    )
}

// Создать болезнь
POST("http://localhost:8080/illnesses") {
    header("Content-Type", "application/json")
    body(
        """
        {
          "name": "ОРВИ",
          "description": "Острая респираторная вирусная инфекция верхних дыхательных путей",
          "severity": "MILD"
        }
        """.trimIndent()
    )
}

// Создать медкарту
POST("http://localhost:8080/medical-cards") {
    header("Content-Type", "application/json")
    body(
        """
        {
          "patientId": "ВСТАВИТЬ_ID_ПАЦИЕНТА",
          "illnessId": "ВСТАВИТЬ_ID_БОЛЕЗНИ",
          "diagnosis": "ОРВИ средней тяжести"
        }
        """.trimIndent()
    )
}

// Все

GET("http://localhost:8080/doctors") {}

GET("http://localhost:8080/patients") {}

GET("http://localhost:8080/illnesses") {}

GET("http://localhost:8080/medical-cards") {}


// Поиск болезней по тексту описания
GET("http://localhost:8080/illnesses/search?text=дыхательных") {}

// Врач по id
GET("http://localhost:8080/doctors/ВСТАВИТЬ_ID") {}

// Обновить специализацию врача
PUT("http://localhost:8080/doctors") {
    header("Content-Type", "application/json")
    body(
        """
        {
          "id": "ВСТАВИТЬ_ID",
          "firstName": "Иван",
          "lastName": "Петров",
          "specialization": "Терапевт и кардиолог"
        }
        """.trimIndent()
    )
}

// Назначить врача на медкарту
POST("http://localhost:8080/medical-cards/ВСТАВИТЬ_ID_КАРТЫ/doctors") {
    header("Content-Type", "application/json")
    body(
        """
        {
          "doctorId": "ВСТАВИТЬ_ID_ВРАЧА"
        }
        """.trimIndent()
    )
}

// Пациент выздоровел (карта закрывается, expireAt = now)
POST("http://localhost:8080/medical-cards/ВСТАВИТЬ_ID_КАРТЫ/recover") {}

// Удалить врача
DELETE("http://localhost:8080/doctors/ВСТАВИТЬ_ID") {}

// Загрузить 10000 врачей
POST("http://localhost:8080/doctors/bulk") {
    header("Content-Type", "application/json")
    body(
        """
        {
          "namePrefix": "bulk-doctor",
          "startIndex": 0,
          "count": 10000
        }
        """.trimIndent()
    )
}

// Загрузить 10000 пациентов
POST("http://localhost:8080/patients/bulk") {
    header("Content-Type", "application/json")
    body(
        """
        {
          "namePrefix": "bulk-patient",
          "startIndex": 0,
          "count": 10000
        }
        """.trimIndent()
    )
}

// Загрузить 10000 болезней
POST("http://localhost:8080/illnesses/bulk") {
    header("Content-Type", "application/json")
    body(
        """
        {
          "namePrefix": "bulk-illness",
          "startIndex": 0,
          "count": 10000
        }
        """.trimIndent()
    )
}

// Проверить после bulk загрузки
GET("http://localhost:8080/doctors") {}

// Выборка по id (PRIMARY KEY)
GET("http://localhost:8080/doctors/ВСТАВИТЬ_ID") {}

// Выборка по полю врачи по специализации
GET("http://localhost:8080/doctors?specialization=Терапевт") {}

// Выборка с сортировкой врачи по фамилии А-Я
GET("http://localhost:8080/doctors?sort=lastName") {}

// Выборка с JOIN медкарты + врач + пациент + болезнь
GET("http://localhost:8080/medical-cards/details") {}

// Выборка с агрегацией кол-во врачей по специализации
GET("http://localhost:8080/doctors/stats") {}
