package hu.bme.mit.temalabor.choco;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Daniel on 2016. 10. 20..
 */
public class FillAPix {
	private int height;
	private int width;
	private IntVar[][] outputMatrix; // 0 vagy 1 értéket vehet csak fel
	private Integer[][] inputMatrix;
	private Model model;

	public FillAPix(int[] dimensions) {
		model = new Model();
		height = dimensions[0];
		width = dimensions[1];
		inputMatrix = new Integer[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				inputMatrix[i][j] = -1;
			}
		}
		outputMatrix = model.intVarMatrix("fill-a-pix-game", height, width, 0, 1);
	}

	public void addClue(int value, int x, int y) {
		inputMatrix[x][y] = value;
	}

	public void calculateConstraints() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (inputMatrix[i][j] != -1) {
					model.sum(getSurroundingOutputVars(i, j), "=", inputMatrix[i][j]).post();
				}
			}
		}
	}

	private IntVar[] getSurroundingOutputVars(int i, int j) {
		List<IntVar> outputVarsList = new ArrayList<>();

		for (int i1 = i - 1; i1 <= i + 1; i1++) {
			for (int j1 = j - 1; j1 <= j + 1; j1++) {
				if (i1 >= 0 && i1 < height && j1 >= 0 && j1 < width) {
					outputVarsList.add(outputMatrix[i1][j1]);
				}
			}
		}
		return outputVarsList.toArray(new IntVar[outputVarsList.size()]);
	}

	public void printSolution() {
		Solver solver = model.getSolver();
		int solutionNo = 0;
		while (solver.solve()) {
			++solutionNo;
		}
		if (solutionNo == 0) {
			System.out.println("Nincs megoldás!\n");
		} else {
			System.out.println("Összesen " + solver.getSolutionCount() + " megoldása van a játéknak.\n");
		}
		solver.reset();
		for (int solutionIter = 1; solutionIter <= solutionNo; ++solutionIter) {
			solver.solve();
			System.out.println(solutionIter + ". megoldás:");
			for (int i = 0; i < outputMatrix.length; i++) {
				for (int i1 = 0; i1 < outputMatrix[0].length - 1; i1++) {
					System.out.print(outputMatrix[i][i1].getValue() + ",");
				}
				System.out.println(outputMatrix[i][width - 1].getValue());
			}
		}
	}
}
