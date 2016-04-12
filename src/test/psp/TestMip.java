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
		
		SolverResult res = null;
		
		try{
		//Lance le solver. TODO : actionner cette action par un bouton de l'IHMs
		Instance instance = Parser.lireInstance("Data"+File.separator+"Instances"+File.separator+"instance10.txt",4);
		Mip mip = new Mip(instance);
		res = mip.solve();
		} catch(Exception exc){
			ihmManager.showMessageDialogError(exc);
		}
		
		//Update l'IHM une fois que les calculs sont termines pour afficher les resultats.
		if (res != null)
			ihmManager.UpdateIHMAfterSolve(res);
	}
}
