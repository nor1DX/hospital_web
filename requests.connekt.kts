
GET("http://localhost:8080/doctors") {

}
GET("http://localhost:8080/doctors/507f1f77bcf86cd799439011") {

}
POST("http://localhost:8080/doctors") {
    header("Content-Type", "application/json")
    body(
        """
        {
          "firstName": "Антон",
          "lastName": "Антонов",
          "specialization": "Невролог"
        }
        """.trimIndent()
    )

}
PUT("http://localhost:8080/doctors") {
    header("Content-Type", "application/json")
    body(
        """
        {
          "id": "507f1f77bcf86cd799439011",
          "firstName": "Иван",
          "lastName": "Петров",
          "specialization": "Терапевт и кардиолог"
        }
        """.trimIndent()
    )

}
DELETE("http://localhost:8080/doctors/507f1f77bcf86cd799439013") {

}
GET("http://localhost:8080/patients") {

}
GET("http://localhost:8080/patients/507f1f77bcf86cd799439031") {

}
POST("http://localhost:8080/patients") {
    header("Content-Type", "application/json")
    body(
        """
        {
          "firstName": "Анна",
          "lastName": "Морозова",
          "dateOfBirth": "2000-01-15"
        }
        """.trimIndent()
    )

}
DELETE("http://localhost:8080/patients/507f1f77bcf86cd799439033") {

}
GET("http://localhost:8080/illnesses") {

}
GET("http://localhost:8080/illnesses/507f1f77bcf86cd799439021") {

}
GET("http://localhost:8080/illnesses/search?text=дыхательных") {

}
POST("http://localhost:8080/illnesses") {
    header("Content-Type", "application/json")
    body(
        """
        {
          "name": "Диабет",
          "description": "Хроническое заболевание обмена веществ, характеризуется повышенным уровнем сахара в крови",
          "severity": "MODERATE"
        }
        """.trimIndent()
    )

}
DELETE("http://localhost:8080/illnesses/507f1f77bcf86cd799439023") {

}
GET("http://localhost:8080/medical-cards") {

}
GET("http://localhost:8080/medical-cards/507f1f77bcf86cd799439041") {

}
POST("http://localhost:8080/medical-cards") {
    header("Content-Type", "application/json")
    body(
        """
        {
          "patientId": "507f1f77bcf86cd799439033",
          "illnessId": "507f1f77bcf86cd799439022",
          "diagnosis": "Пневмония с осложнениями"
        }
        """.trimIndent()
    )

}
POST("http://localhost:8080/medical-cards/507f1f77bcf86cd799439041/doctors") {
    header("Content-Type", "application/json")
    body(
        """
        {
          "doctorId": "507f1f77bcf86cd799439012"
        }
        """.trimIndent()
    )

}
DELETE("http://localhost:8080/medical-cards/507f1f77bcf86cd799439041/doctors") {
    header("Content-Type", "application/json")
    body(
        """
        {
          "doctorId": "507f1f77bcf86cd799439012"
        }
        """.trimIndent()
    )

}
POST("http://localhost:8080/medical-cards/507f1f77bcf86cd799439041/illnesses") {
    header("Content-Type", "application/json")
    body(
        """
        {
          "illnessId": "507f1f77bcf86cd799439022"
        }
        """.trimIndent()
    )

}
POST("http://localhost:8080/medical-cards/507f1f77bcf86cd799439041/recover") {

}
