package test.psp;

import java.io.File;
import java.io.IOException;

import ilog.concert.IloException;
import psp.Instance;
import psp.Parser;

public class TestMip {

	private static final String FILE_NAME = "instance0.txt";

	public static void main(String[] args) throws IOException, IloException {

		// Initialise l'IHM
		IHM ihmManager = new IHM();

		String sPath = "";

		try {
			sPath = "Data" + File.separator + "Instances" + File.separator + FILE_NAME;
			Instance instance = Parser.lireInstance(sPath);

			ihmManager.updateIHMWithValuesInFile(instance);
		} catch (Exception exc) {
			ihmManager.showMessageDialogError(exc, "Erreur lors de la lecture du fichier " + sPath);
		}
	}
}
