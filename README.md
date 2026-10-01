# BiblioTech

Projet réalisé dans le cadre du TP 01 BiblioTech.

## Stack technique

- Java 21
- Jakarta EE 10
- Tomcat 10.1.60
- Maven 3.9.16
- PostgreSQL 17.11
- PostgreSQL JDBC 42.7.13
- Eclipse IDE
- pgAdmin 4

---

# Structure du projet

```text
bibliotech/
├── .gitignore
├── README.md
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── bibliotech/
        │           ├── HealthServlet.java
        │           └── dao/
        │               └── DatabaseConnection.java
        │
        └── webapp/
            └── WEB-INF/
                └── web.xml