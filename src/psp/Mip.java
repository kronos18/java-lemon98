﻿package psp;

import static org.junit.Assert.fail;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import psp.resultManagement.SolverResult;
import psp.resultManagement.TimeResult;

public class Mip {
	private Instance instance;
	private IloCplex model;

	private boolean reservoir = true;
	private boolean coutChangement = false;
	private boolean refroidissement = false;
	private boolean regulation = false;

	private IloNumVar[] arrayPtt;
	private IloNumVar[] arrayPpt;
	private IloIntVar[] arrayMpt;
	private IloIntVar[] arrayMtt;
	private IloNumVar[] arrayHt;

	/**
	 * Constructeur d'un MIP pour résoudre l'instance
	 */
	public Mip(Instance instance) throws IloException {
		this.instance = instance;
		initModel();
	}

	/**
	 * Fonction résolvant l'instance.
	 */
	public SolverResult solve() throws IloException {
		SolverResult res = null;
		System.out.println("\nConfiguration : PPmax = " + instance.getTP().getP_P_max() + ", PPmin = " + instance.getTP().getP_P_min());
		System.out.println("\nConfiguration : PTmax = " + instance.getTP().getP_T_max() + ", PTmin = " + instance.getTP().getP_T_min());
		
		System.out.println("\nStart solving...");
		if (model.solve()) {
			res = new SolverResult(false);
			System.out.println("\nSolution status = " + model.getStatus());
			res.setSolutionStatus(model.getStatus().toString());
			
			System.out.println("Objective value : " + this.getObjValue());
			res.setObjValue(this.getObjValue());
			
			System.out.println("Solution : ");
			System.out.println("\tcout.length = " + instance.getCout().length);
			for (int i = 0; i < instance.getCout().length; i++) {
				if (reservoir)
					res.addTimeResult(new TimeResult(i, model.getValue(this.arrayPtt[i]), model.getValue(this.arrayMtt[i]), model.getValue(this.arrayPpt[i]), model.getValue(this.arrayMpt[i]), model.getValue(this.arrayHt[i])));
				else
					res.addTimeResult(new TimeResult(i, model.getValue(this.arrayPtt[i]), model.getValue(this.arrayMtt[i]), model.getValue(this.arrayPpt[i]), model.getValue(this.arrayMpt[i])));
				
				System.out.println("\t\tPt" + i + " = " + model.getValue(this.arrayPtt[i]));
				System.out.println("\t\tMt" + i + " = " + model.getValue(this.arrayMtt[i]));
				System.out.println("\t\tPp" + i + " = " + model.getValue(this.arrayPpt[i]));
				System.out.println("\t\tMp" + i + " = " + model.getValue(this.arrayMpt[i]));
				if (reservoir)
					System.out.println("\t\tMp" + i + " = " + model.getValue(this.arrayHt[i]));
				System.out.println("----------------------------------------");
			}
		} else {
			res = new SolverResult(true);
			fail("No feaisible solution has been found");
		}
		
		return res;
	}

	/**
	 * Fonction retournant la valeur de l'objectif. Requiert qu'une solution ait
	 * été trouvée
	 */
	public double getObjValue() throws IloException {
		return model.getObjValue();
	}

	/**
	 * Fonction liberant la memoire utilisee par le model
	 */
	public void clear() {
		model.end();
	}

	/**
	 * Fonction initialisant le model Cplex
	 */
	private void initModel() throws IloException {
		model = new IloCplex();
		initVariables();
		initConstraints();
		initObjective();
	}

	/**
	 * Function initialisant les variables
	 */
	private void initVariables() throws IloException {
		// variables
		/*
		 * CEt : cout de l’électricité à la période t. t = 1..168 Ppmax, Ppmin :
		 * puissances minimales et maximales de la PSP en mode pompe Ptmin,
		 * Ptmax : puissances minimales et maximales de la PSP en mode turbine
		 * Mpt : variable binaire qui indique si le mode pompe de la PSP est
		 * actif (=1) ou inactif (=0) Mtt : variable binaire qui indique si le
		 * mode turbine de la PSP est actif (=1) ou inactif (=0) Ptt : puissance
		 * produite à la période t par le mode turbine Ppt : puissance consommée
		 * à la période t par le mode pompe
		 */
		double[] cout = instance.getCout();

		this.arrayPtt = model.numVarArray(cout.length, 0.0, Double.MAX_VALUE);
		this.arrayPpt = model.numVarArray(cout.length, -Double.MAX_VALUE, 0.0);
		this.arrayMpt = model.intVarArray(cout.length, 0, 1);
		this.arrayMtt = model.intVarArray(cout.length, 0, 1);

		// Question 3
		/*
		 * Ht : variable indiquant la hauteur de chute à la periode t.
		 */
		this.arrayHt = model.numVarArray(cout.length, -this.instance.getInf().getHauteur() + instance.getDelta_H(), this.instance.getSup().getHauteur() + instance.getDelta_H());
		// Question 4
		
		
	}

	/**
	 * Function initialisant les contraintes
	 */
	private void initConstraints() throws IloException {
		initConstraintesPuissance();
		if (reservoir)
			initContraintesReservoir();
		if (coutChangement)
			initCoutChangementFonction();
		if (refroidissement)
			initConstraintsRefroidissmenet();
		if (regulation)
			initContraintesRegulation();
	}

	/**
	 * Fonction initialisant les contraintes de puissances des turbines pompes
	 */
	private void initConstraintesPuissance() throws IloException {
		/*
		 * Mpt.Ppmax <= Ppt <= Ppmin.Mpt Mtt.Ptmin <= Ptt <= Ptmax.Mtt Mtt + Mpt
		 * <= 1 Mpt = {0, 1} Mtt = {0, 1}
		 */
		TurbinePompe tp = instance.getTP();
		IloNumExpr expr;

		for (int i = 0; i < this.arrayPpt.length; i++) {
			// Mpt.Ppmax <= Ppt
			expr = model.prod(this.arrayMpt[i], tp.getP_P_max());
			model.addGe(this.arrayPpt[i], expr, "Mpt.Ppmax <= Ppt");		

			// Ppt <= Ppmin.Mpt
			expr = model.prod(this.arrayMpt[i], tp.getP_P_min());
			model.addLe(this.arrayPpt[i], expr, "Ppt <= Ppmin.Mpt");		

			// Mtt.Ptmin <= Ptt
			expr = model.prod(this.arrayMtt[i], tp.getP_T_min());
			model.addGe(this.arrayPtt[i], expr, "Mtt.Ptmin <= Ptt");
			
			// Ptt <= Ptmax.Mtt
			expr = model.prod(this.arrayMtt[i], tp.getP_T_max());
			model.addLe(this.arrayPtt[i], expr, "Ptt <= Ptmax.Mtt");
		
			// Mtt + Mpt <= 1
			expr = model.sum(this.arrayMtt[i], this.arrayMpt[i]);
			model.addLe(expr, 1, "Mtt + Mpt <= 1");
		}
	}

	/**
	 * Function initialisant les contraintes de reservoirs
	 */
	private void initContraintesReservoir() throws IloException {
		// Question 3
		/*
		 * Ht – Ht+1 = (2.3600) / (L . l) . ((Ptt / αt) + (Ppt / αp))
		 * H0 = 0 – H + dH
		 */

		TurbinePompe tp = instance.getTP();
		Reservoir reservoirInf = instance.getInf();
		Reservoir reservoirSup = instance.getSup();
		IloNumExpr exprRight1, exprRight2, exprRight3, exprLeft;
		Double facteur = ((2 * 3600) / (reservoirInf.getLargeur() * reservoirInf.getLongueur()));

		// H0 = 0 - H + dH
		model.addEq(this.arrayHt[0], reservoirSup.getH_0() - reservoirInf.getH_0() + instance.getDelta_H());
		for (int i = 0; i < this.arrayHt.length-1; i++) {
			// Ht - Ht+1 = (2.3600) / (L . l) . ((Ptt / Î±t) + (Ppt / Î±p))			
			// soit : Ht - Ht+1 = ((2.3600) / (L . l) . (Ptt / Î±t)) + ((2.3600) / (L . l) . (Ppt / Î±p))
			exprRight1 = model.prod(this.arrayPpt[i], facteur / tp.getAlpha_P());
			exprRight2 = model.prod(this.arrayPtt[i], facteur / tp.getAlpha_T());
			exprRight3 = model.sum(exprRight1, exprRight2);
			exprLeft = model.diff(this.arrayHt[i], this.arrayHt[i+1]);
			model.addEq(exprLeft, exprRight3, "Ht - Ht+1 = (2.3600) / (L . l) . ((Ptt / Î±t) + (Ppt / Î±p))");
		}
	}

	/**
	 * Fonction initialisant les couts de changement de fonctionnement
	 */
	private void initCoutChangementFonction() throws IloException {
		// Question 4
		System.out.println("Couts de changement de fonctionnement non implementees");
		System.exit(1);
	}

	/**
	 * Fonction initialisant les contraintes de refroidissement
	 */
	private void initConstraintsRefroidissmenet() throws IloException {
		// TODO à vous de jouer
		System.out.println("Contraintes de refroidissement non implementees");
		System.exit(1);
	}

	/**
	 * Fonction initialisant les contraintes liees a la regulation
	 */
	private void initContraintesRegulation() throws IloException {
		// TODO à vous de jouer
		System.out.println("Regulation non implementee");
		System.exit(1);
	}

	/**
	 * Fonction initialisant la fonction objectif
	 */
	private void initObjective() throws IloException {
		/*
		 * Max (SOMME( CEt(Ptt + Ppt)))
		 */
		
		IloNumExpr obj = model.numVar(0, 0);
		IloNumExpr expr1, expr2;

		for(int i = 0; i < instance.getCout().length; i++){
			//Ptt + Ppt
			expr1 = model.sum(this.arrayPtt[i], this.arrayPpt[i]);
			
			//CEt(Ptt + Ppt)
			expr2 = model.prod(instance.getCout()[i], expr1);
			
			//SOMME( CEt(Ptt + Ppt))
			obj = model.sum(obj, expr2);
		}
		
		// Max (SOMME( CEt(Ptt + Ppt)))
		model.addMaximize(obj, "Objective");
	}

	public boolean isCoutChangement() {
		return coutChangement;
	}

	public void setCoutChangement(boolean coutChangement) {
		this.coutChangement = coutChangement;
	}

	public boolean isRefroidissement() {
		return refroidissement;
	}

	public void setRefroidissement(boolean refroidissement) {
		this.refroidissement = refroidissement;
	}

	public boolean isRegulation() {
		return regulation;
	}

	public void setRegulation(boolean regulation) {
		this.regulation = regulation;
	}
}
