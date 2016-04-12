package test.cplex;

import static org.junit.Assert.fail;

import java.io.File;

import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

import org.junit.Assert;

public class ExempleCplex {

	/**
	 * Exemple d'utilisation de Cplex, nous allons résoudre le programme
	 * linéaire suivant: 
	 * max z = 2x1 + x2 
	 * st 5x1 + 8x2 <= 80
	 *    8x1 + 4x2 <= 80
	 *    12x1 + 4x2 <= 120
	 *    x1, x2 >= 0
	 */
	public static void main(String[] args) throws IloException {
		// model
		IloCplex model = new IloCplex();
		model.setName("Exemple");
		// variables
		IloNumVar x1 = model.numVar(0.0, Double.MAX_VALUE, "x_1");
		IloNumVar x2 = model.numVar(0.0, Double.MAX_VALUE, "x_2");

		// fonction objectif
		IloNumExpr obj = model.sum(model.prod(2.0, x1), x2);
		model.addMaximize(obj, "z");

		// Contrainte 1
		IloNumExpr expr1 = model.sum(model.prod(5.0, x1), model.prod(8, x2));
		model.addLe(expr1, 80, "ctr_1");

		// Contrainte 2
		IloNumExpr expr2 = model.sum(model.prod(8.0, x1), model.prod(4, x2));
		model.addLe(expr2, 80, "ctr_2");

		// Contrainte 3
		IloNumExpr expr3 = model.sum(model.prod(12.0, x1), model.prod(4, x2));
		model.addLe(expr3, 120, "ctr_3");

		model.exportModel("Data"+File.separator+"lps"+File.separator+"exemple.lp");
		if (model.solve()) {
			System.out.println("\nSolution status = " + model.getStatus());
			System.out.println("Objective value : " + model.getObjValue());
			System.out.println("Solution : ");
			System.out.println("\tx_1 = "+ model.getValue(x1));
			System.out.println("\tx_2 = "+ model.getValue(x2));
			Assert.assertEquals(20.0, model.getObjValue(), 0);
		} else {
			fail("No feaisible solution has been found");
		}
	}
}
