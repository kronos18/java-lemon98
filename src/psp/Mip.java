package psp;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import psp.resultManagement.SolverResult;
import psp.resultManagement.TimeResult;

import static org.junit.Assert.fail;

public class Mip {
    private Instance instance;
    private IloCplex model;

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
        System.out.println("\nConfiguration : PPmax = " + instance.getTP(0)
                                                                  .getP_P_max() + ", PPmin = " + instance.getTP(0)
                                                                                                         .getP_P_min());
        System.out.println("\nConfiguration : PTmax = " + instance.getTP(0)
                                                                  .getP_T_max() + ", PTmin = " + instance.getTP(0)
                                                                                                         .getP_T_min());

        System.out.println("\nStart solving...");
        if (model.solve()) {
            res = new SolverResult(false, instance.getNbPSP());
            System.out.println("\nSolution status = " + model.getStatus());
            res.setSolutionStatus(model.getStatus()
                                       .toString());

            System.out.println("Objective value : " + this.getObjValue());
            res.setObjValue(this.getObjValue());

            System.out.println("Solution : ");
            System.out.println("\tcout.length = " + instance.getCout().length);
            
            for (int j = 0; j < instance.getNbPSP(); j++) {
				TurbinePompe tp = instance.getTP(j);

	            System.out.println("");
	            System.out.println("Turbine numero " + j + " :");
	            
	            for (int i = 0; i < instance.getCout().length; i++) {
	                if (instance.isContrainteReservoirActivated()) {
	                    if (instance.isContrainteCoutChangementActivated()) {
	                        res.addTimeResult(new TimeResult(i,
	                                                         model.getValue(tp.getArrayPtt()[i]),
	                                                         model.getValue(tp.getArrayMtt()[i]),
	                                                         model.getValue(tp.getArrayPpt()[i]),
	                                                         model.getValue(tp.getArrayMpt()[i]),
	                                                         model.getValue(this.arrayHt[i]),
	                                                         model.getValue(tp.getArrayBatt()[i]),
	                                                         model.getValue(tp.getArrayBtat()[i]),
	                                                         model.getValue(tp.getArrayBapt()[i]),
	                                                         model.getValue(tp.getArrayBpat()[i])));
	                    }
	                    else {
	                        res.addTimeResult(new TimeResult(i,
	                                                         model.getValue(tp.getArrayPtt()[i]),
	                                                         model.getValue(tp.getArrayMtt()[i]),
	                                                         model.getValue(tp.getArrayPpt()[i]),
	                                                         model.getValue(tp.getArrayMpt()[i]),
	                                                         model.getValue(this.arrayHt[i])));
	                    }
	                }
	                else {
	                    res.addTimeResult(new TimeResult(i,
	                                                     model.getValue(tp.getArrayPtt()[i]),
	                                                     model.getValue(tp.getArrayMtt()[i]),
	                                                     model.getValue(tp.getArrayPpt()[i]),
	                                                     model.getValue(tp.getArrayMpt()[i])));
	                }
	
	                System.out.println("\t\tPt" + i + " = " + model.getValue(tp.getArrayPtt()[i]));
	                System.out.println("\t\tMt" + i + " = " + model.getValue(tp.getArrayMtt()[i]));
	                System.out.println("\t\tPp" + i + " = " + model.getValue(tp.getArrayPpt()[i]));
	                System.out.println("\t\tMp" + i + " = " + model.getValue(tp.getArrayMpt()[i]));
	                if (instance.isContrainteReservoirActivated()) {
	                    System.out.println("\t\tMp" + i + " = " + model.getValue(this.arrayHt[i]));
	                }
	                System.out.println("----------------------------------------");
	            }
			}
        }
        else {
            res = new SolverResult(true, instance.getNbPSP());
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

        for (TurbinePompe tp : instance.getTPs()) {
			tp.initArrayOfVariableOfTheModel(model, cout);
		}

        // Question 3
		/*
		 * Ht : variable indiquant la hauteur de chute à la periode t.
		 */
        this.arrayHt = model.numVarArray(cout.length,
                                         -this.instance.getInf()
                                                       .getHauteur() + instance.getDelta_H(),
                                         this.instance.getSup()
                                                      .getHauteur() + instance.getDelta_H());
    }

    /**
     * Function initialisant les contraintes
     */
    private void initConstraints() throws IloException {
        initConstraintesPuissance();
        if (instance.isContrainteReservoirActivated()) {
            initContraintesReservoir();
        }
        if (instance.isContrainteCoutChangementActivated()) {
            initCoutChangementFonction();
        }
        if (instance.isContrainteRefroidissementActivated()) {
            initConstraintsRefroidissmenet();
        }
        if (instance.isContrainteRegulationActivated()) {
            initContraintesRegulation();
        }
    }

    /**
     * Fonction initialisant les contraintes de puissances des turbines pompes
     */
    private void initConstraintesPuissance() throws IloException {
		/*
		 * Mpt.Ppmax <= Ppt <= Ppmin.Mpt 
		 * Mtt.Ptmin <= Ptt <= Ptmax.Mtt 
		 * Mtt + Mpt <= 1 
		 * Mpt = {0, 1}  
		 * Mtt = {0, 1}
		 */

    	TurbinePompe tp;
        IloNumExpr   expr;

        for (int j = 0; j < instance.getNbPSP(); j++) {
			
        	tp = instance.getTP(j);
        	
	        for (int i = 0; i < tp.getArrayPpt().length; i++) {
	            // Mpt.Ppmax <= Ppt
	            expr = model.prod(tp.getArrayMpt()[i],
	                              tp.getP_P_max());
	            model.addGe(tp.getArrayPpt()[i],
	                        expr,
	                        "Mpt.Ppmax <= Ppt");
	
	            // Ppt <= Ppmin.Mpt
	            expr = model.prod(tp.getArrayMpt()[i],
	                              tp.getP_P_min());
	            model.addLe(tp.getArrayPpt()[i],
	                        expr,
	                        "Ppt <= Ppmin.Mpt");
	
	            // Mtt.Ptmin <= Ptt
	            expr = model.prod(tp.getArrayMtt()[i],
	                              tp.getP_T_min());
	            model.addGe(tp.getArrayPtt()[i],
	                        expr,
	                        "Mtt.Ptmin <= Ptt");
	
	            // Ptt <= Ptmax.Mtt
	            expr = model.prod(tp.getArrayMtt()[i],
	                              tp.getP_T_max());
	            model.addLe(tp.getArrayPtt()[i],
	                        expr,
	                        "Ptt <= Ptmax.Mtt");
	
	            // Mtt + Mpt <= 1
	            expr = model.sum(tp.getArrayMtt()[i],
	            		tp.getArrayMpt()[i]);
	            model.addLe(expr,
	                        1,
	                        "Mtt + Mpt <= 1");
	        }
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

        //TurbinePompe tp           = instance.getTP();
        Reservoir    reservoirInf = instance.getInf();
        Reservoir    reservoirSup = instance.getSup();
        IloNumExpr   exprRight1, exprRight2, exprRight3, exprLeft;
        Double       facteur      = ((2 * 3600) / (reservoirInf.getLargeur() * reservoirInf.getLongueur()));

        // H0 = 0 - H + dH
        model.addEq(this.arrayHt[0],
                    reservoirSup.getH_0() - reservoirInf.getH_0() + instance.getDelta_H());        
        
        for (int i = 1; i < this.arrayHt.length; i++) {
            // Ht-1 - Ht = (2.3600) / (L . l) . ((Ptt / Î±t) + (Ppt / Î±p))
            // soit : Ht-1 - Ht = ((2.3600) / (L . l) . (Ptt / Î±t)) + ((2.3600) / (L . l) . (Ppt / Î±p))
        	
        	IloNumExpr sumPptOfAllTurbinePompe = model.numVar(0, 0);
        	IloNumExpr sumPttOfAllTurbinePompe = model.numVar(0, 0);
        	for(TurbinePompe tp : instance.getTPs()){
        		sumPptOfAllTurbinePompe = model.sum(sumPptOfAllTurbinePompe, tp.getArrayPpt()[i-1]);
        		sumPttOfAllTurbinePompe = model.sum(sumPttOfAllTurbinePompe, tp.getArrayPtt()[i-1]);
        	}
        	
            exprRight1 = model.prod(sumPptOfAllTurbinePompe,
                                    facteur / instance.getTP(0).getAlpha_P());
            exprRight2 = model.prod(sumPttOfAllTurbinePompe,
                                    facteur / instance.getTP(0).getAlpha_T());
            exprRight3 = model.sum(exprRight1,
                                   exprRight2);
            exprLeft = model.diff(this.arrayHt[i-1],
                                  this.arrayHt[i]);
            model.addEq(exprLeft,
                        exprRight3,
                        "Ht-1 - Ht = (2.3600) / (L . l) . ((Ptt / Î±t) + (Ppt / Î±p))");
        }
    }

    /**
     * Fonction initialisant les couts de changement de fonctionnement
     */
    private void initCoutChangementFonction() throws IloException {
        // Question 4
		/*
		 * Bpa(t+1) >= Mpt - Mpt+1
		 * Bta(t+1) >= Mtt - Mtt+1
		 * Bat(t+1) >= Mtt+1 - Mtt
		 * Bap(t+1) >= Mpt+1 - Mpt
		 * */
        IloNumExpr expr1, expr2, expr3, expr4;
        
        for (int j = 0; j < instance.getNbPSP(); j++) {
				
        	TurbinePompe tp = instance.getTP(j);
        	
	        for (int i = 0; i < tp.getArrayBatt().length - 1; i++) {
	            //Bpa(t+1) >= Mpt - Mpt+1
	            expr1 = model.diff(tp.getArrayMpt()[i],
	            		tp.getArrayMpt()[i + 1]);
	            model.addGe(tp.getArrayBpat()[i + 1],
	                        expr1,
	                        "Bpa(t+1) >= Mpt - Mpt+1");
	
	            //Bta(t+1) >= Mtt - Mtt+1
	            expr2 = model.diff(tp.getArrayMtt()[i],
	            		tp.getArrayMtt()[i + 1]);
	            model.addGe(tp.getArrayBtat()[i + 1],
	                        expr2,
	                        "Bta(t+1) >= Mtt - Mtt+1");
	
	            //Bat(t+1) >= Mtt+1 - Mtt
	            expr3 = model.diff(tp.getArrayMtt()[i + 1],
	            		tp.getArrayMtt()[i]);
	            model.addGe(tp.getArrayBatt()[i + 1],
	                        expr3,
	                        "Bat(t+1) >= Mtt+1 - Mtt");
	
	            //Bap(t+1) >= Mpt+1 - Mpt
	            expr4 = model.diff(tp.getArrayMpt()[i + 1],
	            		tp.getArrayMpt()[i]);
	            model.addGe(tp.getArrayBapt()[i + 1],
	                        expr4,
	                        "Bap(t+1) >= Mpt+1 - Mpt");
	        }
		}
        
    }

    /**
     * Fonction initialisant les contraintes de refroidissement
     */
    private void initConstraintsRefroidissmenet() throws IloException {
    	//Question 5
		/*
		 * Pour tout t, SOMME(Mtt + Mpt) <= 12, avec t allant de t � t - 13. (une machine ne doit pas fonctionner plus de 12h a la suite)
		 */

    	int iNbHoursFonctionnementMax = instance.getNbHoursRefroidissement();
    	int iNbHoursToVerify = iNbHoursFonctionnementMax + 1;
        IloNumExpr expr, expr1;

    	if (instance.getCout().length < iNbHoursToVerify)
    		return;
    	
    	for (int k = 0; k < instance.getNbPSP(); k++) {
				
    		TurbinePompe tp = instance.getTP(k);
    		
	        for (int i = iNbHoursToVerify; i < instance.getCout().length; i++) {
	        	expr = model.numVar(0, 0);
	        	
	            for (int j = i; j >= i - iNbHoursToVerify; j--) {
		            //Mtt + Mpt
		            expr1 = model.sum(tp.getArrayMtt()[j], tp.getArrayMpt()[j]);
		
		            expr = model.sum(expr, expr1);
	            }
	            
	            model.addLe(expr, iNbHoursFonctionnementMax);
	        }
		}
    }

    /**
     * Fonction initialisant les contraintes liees a la regulation
     */
    private void initContraintesRegulation() throws IloException {
        //Question 6
    	/*
    	 * Pour garantir la regulation de puissance, il faut que la psp soit en mode turbine afin de pouvoir augmenter le debit et donc la puissance, cela sans changer le mode de fonctionnement.
    	 * Il faut aussi beaucoup d'eau dans le reservoir.
    	 */
    }

    /**
     * Fonction initialisant la fonction objectif
     */
    private void initObjective() throws IloException {
		/*
		 * On additionne les cout due aux changements, car ces couts sont negatifs dans le fichiers instance10.txt...
		 * Max (SOMME( CEt(Ptt + Ppt) + Cat * Bat + Cta * Bta + Cap * Bap + Cpa * Bpa))
		 */

        IloNumExpr obj = model.numVar(0,
                                      0);
        IloNumExpr   expr1, expr2, expr3, expr4, expr5, expr6, expr7, expr8, expr9, expr10;
        //TurbinePompe tp = instance.getTP(0);

        for (int i = 0; i < instance.getCout().length; i++) {
        	
        	for (int k = 0; k < instance.getNbPSP(); k++) {
				
        		TurbinePompe tp = instance.getTP(k);
	        		
	            //Ptt + Ppt
	            expr1 = model.sum(tp.getArrayPtt()[i],
	            		tp.getArrayPpt()[i]);
	
	            //CEt(Ptt + Ppt)
	            expr2 = model.prod(instance.getCout()[i],
	                               expr1);
	
	            //Cat * Bat
	            expr3 = model.prod(tp.getC_AT(),
	            		tp.getArrayBatt()[i]);
	
	            //Cta * Bta
	            expr4 = model.prod(tp.getC_TA(),
	            		tp.getArrayBtat()[i]);
	
	            //Cap * Bap
	            expr5 = model.prod(tp.getC_AP(),
	            		tp.getArrayBapt()[i]);
	
	            //Cpa * Bpa
	            expr6 = model.prod(tp.getC_PA(),
	            		tp.getArrayBpat()[i]);
	
	            //Cat * Bat + Cta * Bta
	            expr7 = model.sum(expr3,
	                              expr4);
	
	            //Cat * Bat + Cta * Bta + Cap * Bap
	            expr8 = model.sum(expr7,
	                              expr5);
	
	            //Cat * Bat + Cta * Bta + Cap * Bap + Cpa * Bpa
	            expr9 = model.sum(expr8,
	                              expr6);
	
	            //CEt(Ptt + Ppt) + Cat * Bat + Cta * Bta + Cap * Bap + Cpa * Bpa
	            expr10 = model.sum(expr2,
	                               expr9);
	
	            //SOMME( CEt(Ptt + Ppt) + Cat * Bat + Cta * Bta + Cap * Bap + Cpa * Bpa)
	            obj = model.sum(obj,
	                            expr10);
        		}
	        }

        // Max (SOMME( CEt(Ptt + Ppt) + Cat * Bat + Cta * Bta + Cap * Bap + Cpa * Bpa))
        model.addMaximize(obj,
                          "Objective");
    }
}
