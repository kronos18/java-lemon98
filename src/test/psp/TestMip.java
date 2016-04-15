package test.psp;

import ilog.concert.IloException;

import java.io.File;
import java.io.IOException;

import psp.Instance;
import psp.Mip;
import psp.Parser;
import psp.resultManagement.SolverResult;


public class TestMip {
	
	
	public static void main(String [] args) throws IOException, IloException {

		//Initialise l'IHM
		IHM ihmManager = new IHM();
		
		String sPath = "";
		
		try{
			sPath = "Data"+File.separator+"Instances"+File.separator+"instance10.txt";
			Instance instance = Parser.lireInstance(sPath, 4);
		
			ihmManager.updateIHMWithValuesInFile(instance);		
		} catch(Exception exc){
			ihmManager.showMessageDialogError(exc, "Erreur lors de la lecture du fichier " + sPath);
		}
	}
}
