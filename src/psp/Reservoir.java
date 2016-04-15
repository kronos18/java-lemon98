package psp;

/**
 * Classe représentant un reservoir
 * @author guepetj
 *
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
	
	public void setH_0(double h0){
		this.h_0 = h0;
	}
	public void setLongueur(double longu){
		this.longueur = longu;
	}
	
	public void setLargeur(double larg){
		this.largeur = larg;
	}
	
	public void setHauteur(double haut){
		this.hauteur = haut;
	}
}
