package hu.bme.mit.temalabor.choco;

import parser.Parser;
import hu.bme.mit.temalabor.myDsl.Clue;
import hu.bme.mit.temalabor.myDsl.Domainmodel;
import hu.bme.mit.temalabor.myDsl.Mapdim;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		if (args.length > 0) {
			String fileName = args[0];
			try {
				Domainmodel domainmodel = Parser.parse(fileName);
				Mapdim mapdim = domainmodel.getMapdim();

				int[] dimensions = new int[2];
				dimensions[0] = mapdim.getSizex();
				dimensions[1] = mapdim.getSizey();
				FillAPix fillAPixGame = new FillAPix(dimensions);

				org.eclipse.emf.common.util.EList<Clue> clueList = domainmodel.getClues();

				for (Clue clue : clueList) {
					fillAPixGame.addClue(clue.getValue(), clue.getPosx(), clue.getPosy());
				}

				fillAPixGame.calculateConstraints();
				fillAPixGame.printSolution();
			} catch (IOException e) {
				System.out.println("Nem létezik a " + fileName + " nevű fájl!");
			}

		} else {
			Scanner scanner = new Scanner(System.in);
			int[] dimensions = Arrays.stream(scanner.nextLine().split(",")).map(Integer::valueOf).mapToInt(i -> i).toArray();
			FillAPix fillAPixGame = new FillAPix(dimensions);

			for (int i = 0; i < dimensions[1]; i++) {
				int aRowOfClues[] = Arrays.stream(scanner.nextLine().split(",")).map(Integer::valueOf).mapToInt(j -> j).toArray();
				for (int i1 = 0; i1 < aRowOfClues.length; i1++) {
					fillAPixGame.addClue(aRowOfClues[i1], i, i1);
				}
			}
			fillAPixGame.calculateConstraints();
			fillAPixGame.printSolution();
		}
	}
}
