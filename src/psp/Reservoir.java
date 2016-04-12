package psp;

/**
 * Classe repr?sentant un reservoir
 *
 * @author guepetj
 */
public class Reservoir {
    private double longueur, largeur, hauteur, h_0;

    public Reservoir(double longueur, double largeur, double hauteur, double h_0) {
        this.longueur = longueur;
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.h_0 = h_0;
    }

    public double getLongueur() {
        return longueur;
    }

    public double getLargeur() {
        return largeur;
    }

    public double getHauteur() {
        return hauteur;
    }

    public double getH_0() {
        return h_0;
    }
}
