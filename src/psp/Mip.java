package psp;

import static org.junit.Assert.fail;

import ilog.concert.IloCopyManager;
import ilog.concert.IloCopyable;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarBound;
import ilog.concert.IloNumVarType;
import ilog.concert.IloCopyManager.Check;
import ilog.cplex.IloCplex;

public class Mip {
    private Instance instance;
    private IloCplex model;

    private boolean reservoir       = true;
    private boolean coutChangement  = false;
    private boolean refroidissement = false;
    private boolean regulation      = false;

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
    public void solve() throws IloException {
        System.out.println("\nStart solving...");
        if (model.solve()) {
            System.out.println("\nSolution status = " + model.getStatus());
            System.out.println("Objective value : " + this.getObjValue());
            System.out.println("Solution : ");
            System.out.println("\tcout.length = " + instance.getCout().length);
            for (int i = 0; i < instance.getCout().length; i++) {
                System.out.println("\t\tPt" + i + " = " + model.getValue(this.arrayPtt[i]));
                System.out.println("\t\tMt" + i + " = " + model.getValue(this.arrayMtt[i]));
                System.out.println("\t\tPp" + i + " = " + model.getValue(this.arrayPpt[i]));
                System.out.println("\t\tMp" + i + " = " + model.getValue(this.arrayMpt[i]));
                System.out.println("\t\tHp" + i + " = " + model.getValue(this.arrayHt[i]));
                System.out.println("-----------------------------------------------------");
            }
        }
        else {
            fail("No feaisible solution has been found");
        }
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

        this.arrayPtt = model.numVarArray(cout.length,
                                          0.0,
                                          Float.MAX_VALUE);
        this.arrayPpt = model.numVarArray(cout.length,
                                          Float.MIN_VALUE,
                                          0.0);
        this.arrayMpt = model.intVarArray(cout.length,
                                          0,
                                          1);
        this.arrayMtt = model.intVarArray(cout.length,
                                          0,
                                          1);

        // Question 3
		/*
		 * Ht : variable indiquant la hauteur de chute à la periode t.
		 */
        this.arrayHt = model.numVarArray(cout.length,
                                         -this.instance.getInf()
                                                       .getHauteur(),
                                         this.instance.getSup()
                                                      .getHauteur());

        // Question 4
    }

    /**
     * Function initialisant les contraintes
     */
    private void initConstraints() throws IloException {
        initConstraintesPuissance();
        if (reservoir) {
            initContraintesReservoir();
        }
        if (coutChangement) {
            initCoutChangementFonction();
        }
        if (refroidissement) {
            initConstraintsRefroidissmenet();
        }
        if (regulation) {
            initContraintesRegulation();
        }
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
        IloNumExpr   expr;

        // Mpt.Ppmax <= Ppt
        for (int i = 0; i < this.arrayPpt.length; i++) {
            expr = model.prod(this.arrayMpt[i],
                              tp.getP_P_max());
            model.addGe(this.arrayPpt[i],
                        expr,
                        "Mpt.Ppmax <= Ppt");
        }

        // Ppt <= Ppmin.Mpt
        for (int i = 0; i < this.arrayPpt.length; i++) {
            expr = model.prod(this.arrayMpt[i],
                              tp.getP_P_min());
            model.addLe(this.arrayPpt[i],
                        expr,
                        "Ppt <= Ppmin.Mpt");
        }

        // Mtt.Ptmin <= Ptt
        for (int i = 0; i < this.arrayPtt.length; i++) {
            expr = model.prod(this.arrayMtt[i],
                              tp.getP_T_min());
            model.addGe(this.arrayPtt[i],
                        expr,
                        "Mtt.Ptmin <= Ptt");
        }

        // Ptt <= Ptmax.Mtt
        for (int i = 0; i < this.arrayPtt.length; i++) {
            expr = model.prod(this.arrayMtt[i],
                              tp.getP_T_max());
            model.addLe(this.arrayPtt[i],
                        expr,
                        "Ptt <= Ptmax.Mtt");
        }

        // Mtt + Mpt <= 1
        for (int i = 0; i < this.arrayMtt.length; i++) {
            expr = model.sum(this.arrayMtt[i],
                             this.arrayMpt[i]);
            model.addLe(expr,
                        1,
                        "Mtt + Mpt <= 1");
        }
    }

    /**
     * Function initialisant les contraintes de reservoirs
     */
    private void initContraintesReservoir() throws IloException {
        // Question 3
		/*
		 * Ht – Ht+1 = (2.3600) / (L . l) . ((Ptt / αt) + (Ppt / αp)) H0 = 0 – H
		 * + dH
		 */

        TurbinePompe tp           = instance.getTP();
        Reservoir    reservoirInf = instance.getInf();
        Reservoir    reservoirSup = instance.getSup();
        IloNumExpr   expr1, expr2, expr3, expr4, expr5;
        Double       facteur      = ((2 * 3600) / (reservoirInf.getLargeur() * reservoirInf.getLongueur()));
        IloNumVar h_0 = model.numVar(reservoirInf.getH_0(),
                                     reservoirInf.getH_0());

        // this.arrayHt[i] = ;
        for (int i = 0; i < this.arrayHt.length; i++) {
            if (i == 0) {
                // H0 = 0 – H + dH
                arrayHt[i] = h_0;
                // expr1 = model.sum(-reservoirInf.getHauteur(),
                // model.sum(reservoirInf.getHauteur(), model.abs(dH)));
                // expr2 = model.diff(0, expr1);
                // model.addEq(this.arrayHt[i], expr2);
            }
            else {
                // Ht – Ht+1 = (2.3600) / (L . l) . ((Ptt / αt) + (Ppt / αp))
                expr1 = model.prod(this.arrayPpt[i - 1],
                                   1 / tp.getAlpha_P());
                expr2 = model.prod(this.arrayPtt[i - 1],
                                   1 / tp.getAlpha_T());
                expr3 = model.sum(expr1,
                                  expr2);
                expr4 = model.prod(facteur,
                                   expr3);

                expr5 = model.diff(this.arrayHt[i - 1],
                                   this.arrayHt[i]);
                model.addEq(expr5,
                            expr4,
                            "Ht-1 – Ht = (2.3600) / (L . l) . ((Ptt / αt) + (Ppt / αp))");
            }
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
		 * Max (∑ Ptt.CEt + ∑ Ppt.CEt)
		 */
        double[] cout = instance.getCout();

        // ∑ Ptt.CEt
        IloNumExpr[] arraytSumPtt = new IloNumExpr[cout.length];
        for (int i = 0; i < cout.length; i++) {
            arraytSumPtt[i] = model.prod(this.arrayPtt[i],
                                         cout[i]);
        }
        IloNumExpr tSumPtt = model.sum(arraytSumPtt);

        // ∑ Ppt.CEt
        IloNumExpr[] arraytSumPpt = new IloNumExpr[cout.length];
        for (int i = 0; i < cout.length; i++) {
            arraytSumPpt[i] = model.prod(this.arrayPpt[i],
                                         cout[i]);
        }
        IloNumExpr tSumPpt = model.sum(arraytSumPpt);

        // (∑ Ptt.CEt + ∑ Ppt.CEt)
        IloNumExpr obj = model.sum(tSumPtt,
                                   tSumPpt);

        // Max (∑ Ptt.CEt + ∑ Ppt.CEt)
        model.addMaximize(obj,
                          "obj");
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
