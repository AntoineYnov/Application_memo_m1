package com.ynov.applicationmemo.entity;

public class Memo {
    // Attributs :
    public String intitule;

    /**
     * Constructeur.
     * @param intitule Intitulé du mémo
     */
    public Memo(String intitule) {
        this.intitule = intitule;
    }

    /**
     * Getter intitulé.
     * @return Intitulé du mémo
     */
    public String getIntitule()
    {
        return intitule;
    }
}