package test.Transport;

import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Assert;

import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class Oleoduc {
	public static void main(String[] args) throws IloException {
		// model
		IloCplex model = new IloCplex();
		model.setName("Transport");
		
		// variables
		/*
		 * X1a, X1b, X2a, X2b, Xba : nombre de barils transportes par jour par un oleoduc 
		 * Y1a, Y1b, Y2a, Y2b, Yba : variable binaire qui indique si on utilise l’oleoduc (=1) ou pas (=0)
		 */
		IloNumVar X1a = model.numVar(0.0, Double.MAX_VALUE, "X1a");
		IloNumVar X1b = model.numVar(0.0, Double.MAX_VALUE, "X1b");
		IloNumVar X2a = model.numVar(0.0, Double.MAX_VALUE, "X2a");
		IloNumVar X2b = model.numVar(0.0, Double.MAX_VALUE, "X2b");
		IloNumVar Xba = model.numVar(0.0, Double.MAX_VALUE, "Xba");
		IloNumVar Y1a = model.intVar(0, 1, "Y1a");
		IloNumVar Y1b = model.intVar(0, 1, "Y1b");
		IloNumVar Y2a = model.intVar(0, 1, "Y2a");
		IloNumVar Y2b = model.intVar(0, 1, "Y2b");
		IloNumVar Yba = model.intVar(0, 1, "Yba");

		// fonction objectif
		/*
		 * Min(4.X1a + X1b + 2.X2a + 3.X2b + 130000.Y1a + 90000.Y1b + 80000.Y2a + 140000.Y2b + 150000.Yba)
		 */
		IloNumExpr[] tSum = {model.prod(4.0, X1a), model.prod(1.0, X1b), model.prod(2.0, X2a), model.prod(3.0, X2b), model.prod(130000.0, Y1a), model.prod(90000.0, Y1b), model.prod(80000.0, Y2a), model.prod(140000.0, Y2b), model.prod(150000.0, Yba)};
		IloNumExpr obj = model.sum(tSum);
		model.addMinimize(obj, "obj");

		// Contrainte 1
		/*
		 * X1a <= 70000.Y1a
		 */
		IloNumExpr expr1 = model.prod(70000.0, Y1a);
		model.addLe(X1a, expr1, "X1a <= 70000.Y1a");

		// Contrainte 2
		/*
		 * X2a <= 30000.Y2a
		 */
		IloNumExpr expr2 = model.prod(30000.0, Y2a);
		model.addLe(X2a, expr2, "X2a <= 30000.Y2a");

		// Contrainte 3
		/*
		 * X1b <= 40000.Y1b
		 */
		IloNumExpr expr3 = model.prod(40000.0, Y1b);
		model.addLe(X1b, expr3, "X1b <= 40000.Y1b");

		//Contrainte 4
		/*
		 * X2b <= 80000.Y2b
		 */
		IloNumExpr expr4 = model.prod(80000.0, Y2b);
		model.addLe(X2b, expr4, "X2b <= 80000.Y2b");
		
		//Contrainte 5
		/*
		 * Xba <= 100000.Yba
		 */
		IloNumExpr expr5 = model.prod(100000.0, Yba);
		model.addLe(Xba, expr5, "Xba <= 100000.Yba");
		
		//Contrainte 6
		/*
		 * X1a + X1b = 75000
		 */
		IloNumExpr expr6 = model.sum(X1a, X1b);
		model.addEq(expr6, 75000.0, "X1a + X1b = 75000");
		
		//Contrainte 7
		/*
		 * X2a + X2b = 75000
		 */
		IloNumExpr expr7 = model.sum(X2a, X2b);
		model.addEq(expr7, 75000.0, "X2a + X2b = 75000");
		
		//Contrainte 8
		/*
		 * X1a + X2a + Xba <= 150000
		 */
		IloNumExpr[] tSum8 = {X1a, X2a, Xba};
		IloNumExpr expr8 = model.sum(tSum8);
		model.addLe(expr8, 150000.0, "X1a + X2a + Xba <= 150000");
		
		//Contrainte 9
		/*
		 * X1b + X2b – Xba <= 50000
		 */
		IloNumExpr expr9 = model.diff(model.sum(X1b, X2b), Xba);
		model.addLe(expr9, 50000.0, "X1b + X2b - Xba <= 50000");		
		
		//Exporte et calcule
		model.exportModel("Data" + File.separator + "lps" + File.separator + "transport.lp");
		if (model.solve()) {
			System.out.println("\nSolution status = " + model.getStatus());
			System.out.println("Objective value : " + model.getObjValue());
			System.out.println("Solution : ");
			System.out.println("\tX1a = " + model.getValue(X1a));
			System.out.println("\tX1b = " + model.getValue(X1b));
			System.out.println("\tX2a = " + model.getValue(X2a));
			System.out.println("\tX2b = " + model.getValue(X2b));
			System.out.println("\tXba = " + model.getValue(Xba));
			System.out.println("\tY1a = " + model.getValue(Y1a));
			System.out.println("\tY1b = " + model.getValue(Y1b));
			System.out.println("\tY2a = " + model.getValue(Y2a));
			System.out.println("\tY2b = " + model.getValue(Y2b));
			System.out.println("\tYba = " + model.getValue(Yba));
		} else {
			fail("No feaisible solution has been found");
		}
	}
}
