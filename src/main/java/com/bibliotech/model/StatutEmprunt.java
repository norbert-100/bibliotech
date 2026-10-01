package com.bibliotech.model;

import java.time.LocalDate;

public sealed interface StatutEmprunt
        permits StatutEmprunt.EnCours,
                StatutEmprunt.Rendu,
                StatutEmprunt.EnRetard {

    record EnCours(LocalDate dateLimite) implements StatutEmprunt {
    }

    record Rendu(LocalDate dateRetour, boolean enRetardLorsDuRetour)
            implements StatutEmprunt {
    }

    record EnRetard(LocalDate dateLimite, long joursDeRetard)
            implements StatutEmprunt {
    }
}