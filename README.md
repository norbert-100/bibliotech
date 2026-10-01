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

# Arborescence principale
```
bibliotech/
│
├── sql/
│   └── schema.sql
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── bibliotech/
│       │           ├── LivreServlet.java
│       │           ├── EmpruntServlet.java
│       │           │
│       │           ├── dao/
│       │           │   ├── GenericDAO.java
│       │           │   ├── LivreDAO.java
│       │           │   ├── EmpruntDAO.java
│       │           │   └── LivreIndisponibleException.java
│       │           │
│       │           ├── model/
│       │           │   ├── Livre.java
│       │           │   ├── Etudiant.java
│       │           │   ├── Emprunt.java
│       │           │   └── StatutEmprunt.java
│       │           │
│       │           └── util/
│       │               └── PenaliteCalculator.java
│       │
│       └── webapp/
│           └── WEB-INF/
│               ├── web.xml
│               └── livres/
│                   ├── list.jsp
│                   ├── form.jsp
│                   └── detail.jsp
│
├── pom.xml
└── README.md
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
## A.3 — Pattern matching et calcul de pénalité

La classe `PenaliteCalculator` a été créée dans :

`src/main/java/com/bibliotech/util/PenaliteCalculator.java`

Elle contient la méthode :

`calculerPenalite(StatutEmprunt statut)`

Cette méthode utilise un `switch` avec le pattern matching sur les différents types de `StatutEmprunt`.

Les règles de calcul sont les suivantes :

* `EnCours` : pénalité de `0.00 €` ;
* `Rendu` sans retard : pénalité de `0.00 €` ;
* `Rendu` avec retard : `0.50 €` par jour de retard enregistré ;
* `EnRetard` : `0.50 €` par jour de retard, avec un maximum de 30 jours.

Le calcul de la pénalité pour `EnRetard` est donc limité à 30 jours.

Le `switch` utilise directement les Records de `StatutEmprunt` avec le pattern matching, sans utiliser de `if/else` avec `instanceof`.

### Q A.3 — Pourquoi le compilateur garantit-il que le `switch` est exhaustif sans `default` ?

`StatutEmprunt` est une interface `sealed`. Le compilateur connaît donc les trois implémentations autorisées :

* `EnCours`
* `Rendu`
* `EnRetard`

Le `switch` traite ces trois possibilités. Le compilateur peut donc vérifier que tous les cas sont couverts, sans avoir besoin d'un `default`.

Si une nouvelle implémentation `Perdu` était ajoutée à `StatutEmprunt`, le `switch` ne couvrirait plus tous les cas. Le compilateur signalerait alors que le `switch` n'est plus exhaustif et qu'il faut traiter le nouveau cas.
# Partie B — Couche Web Servlet + MVC

Cette partie met en œuvre le modèle MVC avec les Servlets, les JSP et JSTL.

L'objectif est de séparer les responsabilités :

* les Servlets gèrent les requêtes HTTP ;
* les DAO gèrent l'accès aux données ;
* les JSP affichent les données ;
* aucune logique Java n'est écrite directement dans les JSP.

---

## B.1 — Servlets et routing par méthode HTTP

Deux Servlets sont présents dans l'application :

* `LivreServlet`
* `EmpruntServlet`

### `LivreServlet`

Le Servlet est situé dans :

```text
src/main/java/com/bibliotech/LivreServlet.java
```

Il est associé à :

```java
@WebServlet("/livres/*")
```

Il gère les opérations demandées sur les livres.

### Routes utilisées

| Méthode | URL                   | Action                                 |
| ------- | --------------------- | -------------------------------------- |
| GET     | `/livres`             | Afficher la liste des livres           |
| GET     | `/livres/{id}`        | Afficher le détail d'un livre          |
| GET     | `/livres/nouveau`     | Afficher le formulaire de création     |
| POST    | `/livres`             | Créer un livre                         |
| GET     | `/livres/{id}/edit`   | Afficher le formulaire de modification |
| POST    | `/livres/{id}`        | Modifier un livre                      |
| POST    | `/livres/{id}/delete` | Supprimer un livre                     |

Le Servlet utilise `request.getPathInfo()` pour déterminer la partie de l'URL demandée et effectuer l'action correspondante.

### Liste des livres

Avec :

```text
GET /livres
```

le Servlet récupère les livres avec `LivreDAO.findAll()` puis transmet la liste à la JSP avec :

```java
request.setAttribute("livres", livreDAO.findAll());
```

La JSP `list.jsp` affiche ensuite les livres.

### Détail d'un livre

Avec :

```text
GET /livres/{id}
```

le Servlet récupère l'identifiant et recherche le livre avec :

```java
livreDAO.findById(id)
```

Le livre est ensuite transmis à `detail.jsp`.

### Création

Avec :

```text
GET /livres/nouveau
```

le formulaire de création est affiché.

Lorsque l'utilisateur valide le formulaire :

```text
POST /livres
```

le Servlet récupère les valeurs, crée un `Livre` et appelle :

```java
livreDAO.save(livre);
```

### Modification

Le formulaire de modification est accessible avec :

```text
GET /livres/{id}/edit
```

Les informations existantes du livre sont chargées dans le formulaire.

Après validation :

```text
POST /livres/{id}
```

le Servlet crée le nouveau `Livre` avec l'identifiant correspondant et appelle :

```java
livreDAO.update(id, livre);
```

### Suppression

La suppression utilise :

```text
POST /livres/{id}/delete
```

Le Servlet appelle :

```java
livreDAO.delete(id);
```

Puis redirige vers la liste.

### `EmpruntServlet`

Le Servlet est situé dans :

```text
src/main/java/com/bibliotech/EmpruntServlet.java
```

Il est associé à :

```java
@WebServlet("/emprunts/*")
```

Il est prévu pour gérer les opérations liées aux emprunts, notamment la consultation des emprunts et le retour d'un livre.

---

### Q B.1 — Pourquoi le HTML standard ne supporte que GET et POST côté formulaire ?

Les formulaires HTML standards permettent principalement d'utiliser les méthodes `GET` et `POST`.

Les méthodes comme `PUT`, `PATCH` ou `DELETE` ne sont pas directement disponibles avec l'attribut `method` d'un formulaire HTML classique.

Pour utiliser ces méthodes, il faudrait par exemple utiliser JavaScript ou une API spécifique.

Dans BiblioTech, les requêtes `GET` sont utilisées pour consulter les ressources et les requêtes `POST` pour les opérations qui modifient les données.

---

## B.2 — JSP avec JSTL

Les vues des livres sont situées dans :

```text
src/main/webapp/WEB-INF/livres/
```

Trois JSP ont été créées :

```text
list.jsp
form.jsp
detail.jsp
```

### `list.jsp`

Cette JSP affiche la liste des livres sous forme de tableau.

Les colonnes affichées sont :

* Titre ;
* Auteur ;
* Année ;
* Disponibles ;
* Actions.

Les actions disponibles sont :

* Détail ;
* Modifier ;
* Supprimer.

Un bouton **Ajouter un livre** permet également d'accéder au formulaire de création.

### `form.jsp`

La même JSP est utilisée pour :

* la création d'un livre ;
* la modification d'un livre.

Le formulaire adapte son action selon qu'il s'agit d'une création ou d'une modification.

### `detail.jsp`

Cette JSP affiche les informations détaillées d'un livre :

* titre ;
* auteur ;
* année de publication ;
* nombre total d'exemplaires ;
* nombre d'exemplaires disponibles.

Un bouton permet de revenir à la liste.

### Utilisation de JSTL

Les JSP utilisent JSTL :

```jsp
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
```

La JSP `form.jsp` utilise également les fonctions JSTL :

```jsp
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
```

Aucun scriptlet Java `<% %>` n'est utilisé dans les JSP.

Les valeurs dynamiques sont affichées avec `c:out`.

Par exemple :

```jsp
<c:out value="${livre.titre()}" />
```

Comme `Livre` est un Record Java, les accesseurs utilisés dans les JSP sont :

```text
livre.titre()
livre.auteur()
livre.anneePublication()
livre.exemplairesDisponibles()
```

### Échappement des valeurs

Dans `form.jsp`, les valeurs saisies précédemment sont également échappées avec :

```jsp
${fn:escapeXml(titre)}
```

Cela permet d'éviter d'insérer directement une valeur utilisateur non échappée dans le HTML.

---
### `WEB-INF/emprunts/list.jsp`

Cette JSP affiche les emprunts en cours sous forme de tableau.

Les informations affichées sont :

* livre ;
* étudiant ;
* date d'emprunt ;
* date de retour prévue ;
* statut ;
* action de retour.

Seuls les emprunts dont `date_retour_effective` est nulle sont affichés.

Un bouton **Retour** permet d'enregistrer le retour du livre.

### Q B.2 — Quelle est la différence entre `${livre.titre}` et `<c:out value="${livre.titre}" />` ?

`${livre.titre}` est une expression EL qui permet d'insérer directement une valeur dans la page.

Avec :

```jsp
${livre.titre}
```

la valeur est insérée directement dans le contenu HTML.

Avec :

```jsp
<c:out value="${livre.titre}" />
```

JSTL affiche la valeur en effectuant par défaut un échappement XML/HTML.

Par exemple, si une valeur contient :

```html
<script>alert('XSS')</script>
```

`c:out` transforme les caractères HTML afin que le contenu soit affiché comme du texte et non interprété comme du code HTML.

Dans BiblioTech, les valeurs dynamiques affichées dans les JSP utilisent donc `c:out`.

---

## B.3 — Pattern Post-Redirect-Get

Après une création, une modification ou une suppression réussie, le Servlet utilise :

```java
response.sendRedirect(request.getContextPath() + "/livres");
```

Le navigateur reçoit alors une réponse de redirection HTTP et effectue une nouvelle requête `GET` vers :

```text
/livres
```

Le formulaire n'est donc pas renvoyé lors d'un rafraîchissement de la page.

Le pattern utilisé est :

```text
POST
  ↓
Traitement de l'opération
  ↓
HTTP 302
  ↓
GET /livres
```

Ce mécanisme a été testé après les opérations de création, modification et suppression.

---

### Q B.3 — Que se passe-t-il si le PRG est oublié après un INSERT ?

Si le Servlet fait directement un `forward` après l'INSERT, le navigateur reste sur une requête `POST`.

Si l'utilisateur actualise la page avec F5, le navigateur peut renvoyer la même requête `POST`.

L'INSERT peut alors être exécuté une nouvelle fois et créer un doublon dans la base de données.

Le pattern Post-Redirect-Get évite ce problème en redirigeant le navigateur vers `/livres` après le traitement réussi.

---

## B.4 — Validation et messages d'erreur

La validation des formulaires est réalisée dans `LivreServlet`.

Avant de créer ou modifier un livre, les valeurs reçues sont vérifiées.

Les champs contrôlés sont :

* titre ;
* auteur ;
* année de publication ;
* exemplaires total ;
* exemplaires disponibles.

### Champs obligatoires

Le Servlet vérifie que les champs ne sont pas vides.

Par exemple :

```java
if (titre == null || titre.isEmpty()) {
    errors.put("titre", "Le titre ne doit pas être vide");
}
```

### Validation métier

Le Record `Livre` réalise également les validations métier.

Par exemple, une année invalide provoque :

```text
Année de publication invalide
```

Le nombre d'exemplaires disponibles ne peut pas dépasser le nombre total.

Dans ce cas, le message est :

```text
Le nombre d'exemplaires disponibles ne peut pas dépasser le total
```

Le Servlet attrape l'exception :

```java
catch (IllegalArgumentException e)
```

puis transmet le message à la JSP avec l'attribut :

```text
errors
```

### Conservation des valeurs

En cas d'erreur, le formulaire est réaffiché avec les valeurs précédemment saisies.

Les messages d'erreur sont affichés sous les champs concernés.

### Tests réalisés

Les tests suivants ont été réalisés :

* formulaire complètement vide ;
* année de publication invalide ;
* exemplaires disponibles supérieurs au total ;
* conservation des valeurs saisies ;
* affichage des erreurs sous les champs ;
* création valide ;
* modification valide ;
* suppression ;
* redirection après opération réussie.

---

Partie C — JDBC + Generic DAO (35 pts)
C.1 — GenericDAO (5 pts)
Réalisation

L'objectif de cette partie est de créer une interface générique permettant de définir les opérations communes aux différents DAO de l'application.

J'ai créé l'interface GenericDAO<T, ID> dans le package :

src/main/java/com/bibliotech/dao/

Le fichier GenericDAO.java contient :

package com.bibliotech.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDAO<T, ID> {

    List<T> findAll();

    Optional<T> findById(ID id);

    void save(T entity);

    void update(ID id, T entity);

    void delete(ID id);
}
Explication

L'interface utilise deux paramètres génériques :

T représente le type de l'entité manipulée.
ID représente le type de son identifiant.

Cela permet de réutiliser la même interface pour plusieurs entités.

Par exemple :

LivreDAO implements GenericDAO<Livre, Long>

et :

EmpruntDAO implements GenericDAO<Emprunt, Long>

Les méthodes communes sont donc définies une seule fois dans GenericDAO.

Question C.1

Pourquoi créer une interface paramétrée GenericDAO<T, ID> dès le premier DAO plutôt qu'une classe concrète ? Donnez deux raisons.

Réponse C.1

Premièrement, une interface générique permet de réutiliser le même contrat pour plusieurs entités, comme Livre et Emprunt, sans recopier la définition des méthodes communes.

Deuxièmement, les paramètres T et ID permettent d'avoir un code type-safe : chaque DAO travaille avec le bon type d'entité et le bon type d'identifiant, tout en gardant une architecture commune.

C.2 — LivreDAO / EmpruntDAO (15 pts)
Réalisation

Après avoir créé GenericDAO, j'ai créé les DAO correspondant aux entités utilisées dans l'application.

Les fichiers sont placés dans :

src/main/java/com/bibliotech/dao/
LivreDAO

LivreDAO implémente :

GenericDAO<Livre, Long>

Il contient les opérations demandées :

findAll() : récupérer tous les livres.
findById(Long id) : récupérer un livre par son identifiant.
save(Livre livre) : ajouter un livre.
update(Long id, Livre livre) : modifier un livre.
delete(Long id) : supprimer un livre.
EmpruntDAO

EmpruntDAO implémente :

GenericDAO<Emprunt, Long>

Il contient également les opérations :

findAll()
findById(Long id)
save(Emprunt emprunt)
update(Long id, Emprunt emprunt)
delete(Long id)

J'ai également ajouté la méthode demandée par le TP :

findEnRetard()

Cette méthode permet de rechercher les emprunts dont la date de retour prévue est dépassée et dont le retour effectif est encore null.

Utilisation de PreparedStatement

Toutes les requêtes SQL des DAO utilisent PreparedStatement.

Par exemple :

String sql = "SELECT * FROM livre WHERE id = ?";

try (Connection connection = DatabaseConnection.get();
     PreparedStatement statement = connection.prepareStatement(sql)) {

    statement.setLong(1, id);

    try (ResultSet resultSet = statement.executeQuery()) {
        // lecture du résultat
    }
}

Les paramètres sont donc transmis avec setLong(), setString(), etc., et non avec une concaténation de chaînes.

Fermeture des ressources

Les connexions JDBC, les PreparedStatement et les ResultSet sont gérés avec :

try-with-resources

Cela permet de fermer automatiquement les ressources après leur utilisation.

Question C.2

Pourquoi PreparedStatement est-il systématiquement préféré à Statement ? Donnez deux avantages principaux.

Réponse C.2

PreparedStatement permet premièrement de séparer la requête SQL des valeurs fournies par l'utilisateur, ce qui permet notamment de limiter les risques d'injection SQL.

Deuxièmement, il permet de paramétrer proprement les requêtes avec des ? et des méthodes comme setString() ou setLong(), ce qui rend le code plus propre et plus sûr.

C.3 — Transaction pour enregistrer un emprunt (15 pts)
Réalisation

La méthode demandée par le TP est :

public void enregistrerEmprunt(
    long livreId,
    long etudiantId,
    int dureeJours
);

Cette méthode se trouve dans EmpruntDAO.

L'objectif est de réaliser plusieurs opérations dans une seule transaction.

Étape 1 — Désactiver l'auto-commit

La transaction commence avec :

connection.setAutoCommit(false);

Cela signifie que les opérations SQL ne sont pas validées automatiquement une par une.

Étape 2 — Vérifier le livre

Le livre est recherché à partir de son identifiant.

Il faut vérifier :

que le livre existe ;
que le nombre d'exemplaires disponibles est supérieur à zéro.

Si le livre n'existe pas, l'emprunt ne peut pas être enregistré.

Étape 3 — Vérifier le stock

Si :

exemplaires_disponibles == 0

le prêt est impossible.

Une exception dédiée est utilisée :

LivreIndisponibleException
Étape 4 — Diminuer le stock

Lorsqu'un exemplaire est disponible, le nombre d'exemplaires disponibles est diminué de 1.

Étape 5 — Créer l'emprunt

Un nouvel emprunt est ensuite enregistré avec :

l'identifiant du livre ;
l'identifiant de l'étudiant ;
la date d'emprunt ;
la date de retour prévue ;
le statut EnCours.

La date de retour prévue est calculée à partir de dureeJours.

Étape 6 — Valider la transaction

Si toutes les opérations se terminent correctement :

connection.commit();

La transaction est alors validée.

Étape 7 — Annuler en cas d'erreur

Si une erreur survient pendant une des opérations :

connection.rollback();

est exécuté.

Cela permet d'annuler les modifications déjà effectuées dans la transaction.

Principe de la transaction

Le fonctionnement est donc :

Début transaction
       ↓
Vérification du livre
       ↓
Vérification du stock
       ↓
Diminution du stock
       ↓
Insertion de l'emprunt
       ↓
     commit()

En cas d'erreur :

Erreur
  ↓
rollback()
  ↓
Annulation des modifications
Question C.3

Que se passe-t-il sans setAutoCommit(false) si une exception survient entre la mise à jour du stock et l'insertion de l'emprunt ?

Réponse C.3

Sans setAutoCommit(false), chaque requête SQL peut être validée automatiquement.

Si la diminution du stock réussit mais qu'une exception survient avant l'insertion de l'emprunt, la diminution du stock peut rester enregistrée alors que l'emprunt n'a pas été créé.

La transaction permet donc de garantir que les opérations sont réalisées ensemble : soit toutes réussissent avec commit(), soit elles sont annulées avec rollback().

Tests de la Partie C
Test de GenericDAO

La présence de l'interface GenericDAO<T, ID> a été vérifiée avec :

LivreDAO implements GenericDAO<Livre, Long>
EmpruntDAO implements GenericDAO<Emprunt, Long>

Les deux DAO utilisent donc le même contrat générique.

Test de LivreDAO

Les opérations suivantes ont été implémentées :

récupération de tous les livres ;
recherche d'un livre par son ID ;
ajout d'un livre ;
modification d'un livre ;
suppression d'un livre.
Test de EmpruntDAO

Les opérations suivantes ont été implémentées :

récupération des emprunts ;
recherche par ID ;
ajout ;
modification ;
suppression ;
recherche des emprunts en retard.
Test Maven

La compilation et la génération du WAR ont été vérifiées avec :

mvn clean package

Résultat :

BUILD SUCCESS

Le fichier WAR généré est :

target/bibliotech.war


