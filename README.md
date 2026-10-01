# BiblioTech

Projet réalisé dans le cadre du TP 01 BiblioTech.

## 🛠️ Stack technique

* Java 21
* Jakarta EE 10
* Tomcat 10.1.60
* Maven 3.9.16
* PostgreSQL 17.11
* PostgreSQL JDBC 42.7.13
* Eclipse IDE
* pgAdmin 4

---
##🧱 Modèle de données — schéma cible

<img width="472" height="454" alt="image" src="https://github.com/user-attachments/assets/5132088e-66de-4ff4-bb14-0f48ab609436" />

---
## 📐 Architecture cible

<img width="858" height="290" alt="image" src="https://github.com/user-attachments/assets/b8b0c2bf-0f27-4f96-9ccc-c18fce4c91a1" />

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
on a tapé sur le navigateur ce http://localhost:8080/bibliotech/health
Lorsque la connexion fonctionne, le résultat obtenu est de la forme :

```text
OK · PostgreSQL 17.11 on x86_64-windows, compiled by msvc-19.44.35228, 64-bit
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

# Partie A — Fondations Java moderne

## A.1 — Modèles de données avec Records

Trois Records ont été créés dans le package :

`src/main/java/com/bibliotech/model/`

### `Livre`

Le Record `Livre` contient les informations suivantes :

* `id`
* `titre`
* `auteur`
* `anneePublication`
* `exemplairesTotal`
* `exemplairesDisponibles`

Un constructeur compact vérifie les contraintes demandées :

* le titre ne doit pas être nul ou vide ;
* l'auteur ne doit pas être nul ou vide ;
* l'année de publication doit être comprise entre 1500 et l'année actuelle ;
* le nombre d'exemplaires disponibles ne doit pas dépasser le nombre total.

### `Etudiant`

Le Record `Etudiant` contient :

* `id`
* `numeroEtudiant`
* `nom`
* `prenom`
* `email`

Les validations suivantes sont effectuées :

* le numéro étudiant ne doit pas être nul ou vide ;
* le numéro étudiant doit respecter le format `[A-Z]{2}\d{6}` ;
* le nom ne doit pas être nul ou vide ;
* le prénom ne doit pas être nul ou vide ;
* l'email ne doit pas être nul ou vide ;
* l'email doit contenir `@`.

### `Emprunt`

Le Record `Emprunt` contient :

* `id`
* `livreId`
* `etudiantId`
* `dateEmprunt`
* `dateRetourPrevue`
* `dateRetourEffective`
* `statut`

Les validations suivantes sont effectuées :

* `dateEmprunt` ne doit pas être nulle ;
* `dateRetourPrevue` ne doit pas être nulle ;
* `statut` ne doit pas être nul.

### Q A.1 — Pourquoi un Record est-il immutable par défaut ?

Un Record possède des composants qui sont `final`. Ils ne peuvent donc pas être modifiés après la création du Record.

Dans une application web, plusieurs threads peuvent exécuter le même Servlet en même temps. Des objets immuables peuvent être partagés entre plusieurs threads sans modifier leur état, ce qui facilite la gestion de la concurrence.

---

## A.2 — Sealed interface

L'interface `StatutEmprunt` a été créée avec le mot-clé `sealed`.

Elle autorise uniquement trois implémentations :

* `EnCours`
* `Rendu`
* `EnRetard`

Ces trois implémentations sont des Records imbriqués dans `StatutEmprunt`.

### Q A.2 — Quelle différence entre une interface classique et une interface `sealed` ?

Une interface classique peut être implémentée par n'importe quelle classe ou record.

Une interface `sealed` limite les classes ou records qui peuvent l'implémenter grâce au mot-clé `permits`.

Dans notre cas, `StatutEmprunt` autorise uniquement :

* `EnCours`
* `Rendu`
* `EnRetard`

Le compilateur connaît donc toutes les possibilités. Dans un `switch`, il peut vérifier que tous les cas possibles sont traités.

