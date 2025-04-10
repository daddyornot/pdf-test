package com.daddyornot.testpdf.service;

public class Etat {
    private String nom;
    private String valeur;

    // Constructeur
    public Etat(String nom, String valeur) {
        this.nom = nom;
        this.valeur = valeur;
    }

    // Getters
    public String getNom() {
        return nom;
    }

    public String getValeur() {
        return valeur;
    }

    // Optionnel : toString pour faciliter l'affichage
    @Override
    public String toString() {
        return "Etat{" +
                "nom='" + nom + '\'' +
                ", valeur='" + valeur + '\'' +
                '}';
    }
}
