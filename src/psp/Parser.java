package psp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Parser {

	/**
	 * Fonction construisant une instance à partir d'un fichier d'instance.
	 * Requiert que le fichier soit bien formatté.
	 */
	public static Instance lireInstance(String file, int nbTP) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		TurbinePompe[] tps = lireTurbinePompe(br, nbTP);
		Reservoir[] reservoir = lireReservoir(br);
		double delta_H = lireDouble(prochaineLigne(br));
		double[] cost = lireTableauDouble(br);
		double[] regulationPrice = lireTableauDouble(br);
		br.close();
		isr.close();
		fis.close();
		return new Instance(tps, reservoir[0], reservoir[1], cost, regulationPrice, delta_H);
	}

	/**
	 * Fonction construisant nbTP turbine pompe identique à partir du fichier.
	 */
	private static TurbinePompe[] lireTurbinePompe(BufferedReader br, int nbTP) throws IOException {
		double p_T_min = lireDouble(prochaineLigne(br));
		double p_T_max = lireDouble(prochaineLigne(br));
		double p_P_min = lireDouble(prochaineLigne(br));
		double p_P_max = lireDouble(prochaineLigne(br));
		double alpha_T = lireDouble(prochaineLigne(br));
		double alpha_P = lireDouble(prochaineLigne(br));
		double c_AT = lireDouble(prochaineLigne(br));
		double c_TA = lireDouble(prochaineLigne(br));
		double c_AP = lireDouble(prochaineLigne(br));
		double c_PA = lireDouble(prochaineLigne(br));
		TurbinePompe[] tps = new TurbinePompe[nbTP];
		for (int i=0; i<nbTP; i++) {
			tps[i] = new TurbinePompe(p_P_min, p_P_max, p_T_min, p_T_max, c_AP, c_PA, c_AT, c_TA, alpha_P, alpha_T); 
		}
		return tps;
	}

	/**
	 * Fonction construisant les reservoir à partir du fichier.
	 * @return le tableau est du taille 2. Le premier élément est le réservoir supérieur, le second le réservoir inférieur.
	 * @throws IOException 
	 */
	private static Reservoir[] lireReservoir(BufferedReader br) throws IOException {
		double hauteur = lireDouble(prochaineLigne(br));
		double longueur = lireDouble(prochaineLigne(br));
		double largeur = lireDouble(prochaineLigne(br));
		double h_0_sup = lireDouble(prochaineLigne(br));
		double h_0_inf= lireDouble(prochaineLigne(br));
		Reservoir sup = new Reservoir(longueur, largeur, hauteur, h_0_sup);
		Reservoir inf = new Reservoir(longueur, largeur, hauteur, h_0_inf);
		return new Reservoir[]{sup, inf};
	}

	/**
	 * Fonction lisant une tableau de double au format "nom_du_tableau : [val_0, .., val_n];
	 * @throws IOException 
	 */
	private static double[] lireTableauDouble(BufferedReader br) throws IOException {
		StringTokenizer tokenizer = new StringTokenizer(prochaineLigne(br), ":;");
		tokenizer.nextToken(); // passe le nom du tableau
		StringTokenizer tokenizer2 = new StringTokenizer(tokenizer.nextToken(),"[], ");
		// lit le tableau, on a besoin d'une liste intermediaire
		ArrayList<Double> liste = new ArrayList<Double>();
		while (tokenizer2.hasMoreTokens()) {
			liste.add(new Double(tokenizer2.nextToken()));
		}
		
		// convertit la liste en tableau
		double[] tab = new double[liste.size()];
		for (int i=0; i<tab.length; i++) {
			tab[i] = liste.get(i);
		}
		return tab;
	}
	
	/**
	 * Fonction lisant un double à partir du format "nom_de_variable : valeur;
	 */
	private static double lireDouble(String ligne) {
		StringTokenizer tokenizer = new StringTokenizer(ligne,": ;");
		tokenizer.nextToken();// passe le nom de la variable
		return new Double(tokenizer.nextToken());
	}

	/**
	 * Fonction retournant la prochaine ligne non vide et ne commençant pas par "//"
	 */
	private static String prochaineLigne(BufferedReader br) throws IOException {
		String ligne = br.readLine();
		while (ligne != null && (ligne.equals("") || ligne.startsWith("//"))) {
			ligne = br.readLine();
		}
		return ligne;
	}
	
}
