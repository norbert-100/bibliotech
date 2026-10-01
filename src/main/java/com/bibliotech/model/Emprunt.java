package com.bibliotech.model;

import java.time.LocalDate;

public record Emprunt(
        long id,
        long livreId,
        long etudiantId,
        LocalDate dateEmprunt,
        LocalDate dateRetourPrevue,
        LocalDate dateRetourEffective,
        StatutEmprunt statut
) {

    public Emprunt {
        if (dateEmprunt == null) {
            throw new IllegalArgumentException("La date d'emprunt ne doit pas être nulle");
        }

        if (dateRetourPrevue == null) {
            throw new IllegalArgumentException("La date de retour prévue ne doit pas être nulle");
        }

        if (statut == null) {
            throw new IllegalArgumentException("Le statut ne doit pas être nul");
        }
    }
}