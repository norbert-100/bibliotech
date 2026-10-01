package com.bibliotech.model;

public record Etudiant(
        long id,
        String numeroEtudiant,
        String nom,
        String prenom,
        String email
) {

    public Etudiant {
        if (numeroEtudiant == null || numeroEtudiant.isEmpty()) {
            throw new IllegalArgumentException("Le numéro étudiant ne doit pas être vide");
        }

        if (!numeroEtudiant.matches("[A-Z]{2}\\d{6}")) {
            throw new IllegalArgumentException("Numéro étudiant invalide");
        }

        if (nom == null || nom.isEmpty()) {
            throw new IllegalArgumentException("Le nom ne doit pas être vide");
        }

        if (prenom == null || prenom.isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne doit pas être vide");
        }

        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("L'email ne doit pas être vide");
        }

        if (!email.contains("@")) {
            throw new IllegalArgumentException("Email invalide");
        }
    }
}