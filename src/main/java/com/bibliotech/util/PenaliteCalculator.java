package com.bibliotech.util;

import com.bibliotech.model.StatutEmprunt;

public class PenaliteCalculator {

    public static double calculerPenalite(StatutEmprunt statut) {
        return switch (statut) {
            case StatutEmprunt.EnCours e -> 0.00;
            case StatutEmprunt.Rendu r ->
                    r.enRetardLorsDuRetour() ? 0.50 : 0.00;
            case StatutEmprunt.EnRetard e ->
                    Math.min(e.joursDeRetard(), 30) * 0.50;
        };
    }
}