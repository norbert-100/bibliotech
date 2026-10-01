# BiblioTech

Projet réalisé dans le cadre du TP 01 BiblioTech.

## Stack technique

* Java 21
* Jakarta EE 10
* Tomcat 10.1.60
* Maven 3.9.16
* PostgreSQL 17.11
* PostgreSQL JDBC 42.7.13
* Eclipse IDE
* pgAdmin 4

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
            ├── WEB-INF/
            │   └── web.xml
            └── index.jsp
```

---

# Partie 0 — Mise en place

## 0.1 — Maven WAR et Tomcat

### Réalisation

Le projet Maven a été créé avec les paramètres suivants :

* GroupId : `com.bibliotech`
* ArtifactId : `bibliotech`
* Version : `0.0.1-SNAPSHOT`
* Packaging : `war`

Le projet utilise Java 21.

La dépendance Jakarta EE 10 a été ajoutée dans le fichier `pom.xml` avec le scope `provided`.

Le servlet `HealthServlet` a été créé dans :

`src/main/java/com/bibliotech/HealthServlet.java`

Il est accessible avec l'URL :

`http://localhost:8080/bibliotech/health`

Le test retourne :

```text
OK
```

L'application est déployée sur Tomcat 10.1.60.

### Q0.1 — Pourquoi la dépendance `jakarta.jakartaee-web-api` est-elle déclarée en `provided` plutôt qu'en `compile` ?

La dépendance est en `provided` car Tomcat fournit déjà les API Jakarta EE nécessaires à l'exécution de l'application.

Avec `compile`, les API seraient également ajoutées dans le fichier WAR. Cela peut provoquer des conflits avec les API déjà fournies par Tomcat.

---

## 0.2 — Connexion JDBC à PostgreSQL

### Réalisation

Le driver JDBC PostgreSQL a été ajouté dans le fichier `pom.xml` :

* GroupId : `org.postgresql`
* ArtifactId : `postgresql`
* Version : `42.7.13`

La classe `DatabaseConnection` a été créée dans :

`src/main/java/com/bibliotech/dao/DatabaseConnection.java`

Elle permet d'établir une connexion avec PostgreSQL.

La connexion utilise les variables d'environnement :

* `DB_URL`
* `DB_USER`
* `DB_PASSWORD`

Le mot de passe n'est donc pas écrit directement dans le code.

Le `HealthServlet` utilise la connexion PostgreSQL et exécute la requête :

```sql
SELECT version()
```

Lorsque la connexion fonctionne, le résultat obtenu est de la forme :

```text
OK · PostgreSQL 17.11 ...
```

En cas d'erreur de connexion, le servlet retourne :

```text
KO
```

avec le code HTTP `500`.

### Q0.2 — Pourquoi ne faut-il jamais mettre le mot de passe PostgreSQL en dur dans le code ?

Le mot de passe ne doit pas être écrit directement dans le code car le projet peut être partagé, notamment sur GitHub.

Une personne qui consulte le code pourrait récupérer le mot de passe et accéder à la base de données.

Les informations sensibles doivent donc être externalisées.

### Deux autres méthodes pour externaliser les secrets

1. Utiliser un fichier de configuration externe qui n'est pas envoyé sur GitHub.
2. Utiliser un gestionnaire de secrets.
