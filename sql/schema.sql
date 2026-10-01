CREATE TABLE livre (
    id BIGSERIAL PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    auteur VARCHAR(255) NOT NULL,
    annee_publication INTEGER NOT NULL,
    exemplaires_total INTEGER NOT NULL,
    exemplaires_disponibles INTEGER NOT NULL
);
CREATE TABLE etudiant (
    id BIGSERIAL PRIMARY KEY,
    numero_etudiant VARCHAR(8) NOT NULL UNIQUE,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);
CREATE TABLE emprunt (
    id BIGSERIAL PRIMARY KEY,
    livre_id BIGINT NOT NULL REFERENCES livre(id),
    etudiant_id BIGINT NOT NULL REFERENCES etudiant(id),
    date_emprunt DATE NOT NULL,
    date_retour_prevue DATE NOT NULL,
    date_retour_effective DATE,
    statut VARCHAR(50) NOT NULL
);

INSERT INTO livre (
    titre,
    auteur,
    annee_publication,
    exemplaires_total,
    exemplaires_disponibles
) VALUES
('Effective Java', 'Norbert DJIGUEMDE', 2018, 3, 3),
('Clean Code', 'Robert C. Martin', 2008, 4, 4),
('Design Patterns', 'Erich Gamma', 1994, 2, 2),
('Java: The Complete Reference', 'Herbert Schildt', 2020, 3, 3),
('Head First Java', 'Kathy Sierra', 2005, 5, 5);