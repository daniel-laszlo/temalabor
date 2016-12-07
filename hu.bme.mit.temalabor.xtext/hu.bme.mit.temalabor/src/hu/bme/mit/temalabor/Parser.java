package hu.bme.mit.temalabor;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Injector;

import hu.bme.mit.temalabor.MyDslStandaloneSetup;
import hu.bme.mit.temalabor.myDsl.Clue;
import hu.bme.mit.temalabor.myDsl.Domainmodel;
import hu.bme.mit.temalabor.myDsl.Mapdim;

public class Parser {
	
	public static void main(String[] args) {
		if (args.length > 0) {
			String fileName = args[0];
			try {
				Domainmodel domainmodel = parse(fileName);
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
				System.out.println("Nem létezik a " + fileName + " nevû fájl!");
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
	
	public static Domainmodel parse(String filePath) throws IOException {
//		new org.eclipse.emf.mwe.utils.StandaloneSetup().setPlatformUri("../");
		Injector injector = new MyDslStandaloneSetup().createInjectorAndDoEMFRegistration();
		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		Resource resource = resourceSet.createResource(URI.createURI(filePath));
		resource.load(resourceSet.getLoadOptions());

		Domainmodel domainModel = (Domainmodel) resource.getContents().get(0);

		return domainModel;
	}
}
