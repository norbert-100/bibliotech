package com.bibliotech.model;

public record Livre(
        long id,
        String titre,
        String auteur,
        int anneePublication,
        int exemplairesTotal,
        int exemplairesDisponibles
) {

    public Livre {
        if (titre == null || titre.isEmpty()) {
            throw new IllegalArgumentException("Le titre ne doit pas être vide");
        }

        if (auteur == null || auteur.isEmpty()) {
            throw new IllegalArgumentException("L'auteur ne doit pas être vide");
        }

        if (anneePublication < 1500 || anneePublication > java.time.Year.now().getValue()) {
            throw new IllegalArgumentException("Année de publication invalide");
        }

        if (exemplairesDisponibles > exemplairesTotal) {
            throw new IllegalArgumentException(
                    "Le nombre d'exemplaires disponibles ne peut pas dépasser le total"
            );
        }
    }
}