/*
 * generated by Xtext 2.10.0
 */
package hu.bme.mit.temalabor.validation

import hu.bme.mit.temalabor.myDsl.Domainmodel
import hu.bme.mit.temalabor.myDsl.MyDslPackage.Literals
import java.util.ArrayList
import org.eclipse.xtext.validation.Check
import hu.bme.mit.temalabor.myDsl.Mapdim

/**
 * This class contains custom validation rules. 
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
class MyDslValidator extends AbstractMyDslValidator {

	public static val NOT_SQUARE_MATRIX = 'SQUARE_MATRIX'
	public static val CLUE_ALREADY_DEFINED = 'CLUE_ALREADY_DEFINED'
	public static val CLUE_NOT_INSIDE_MATRIX_X_HIGH = 'CLUE_NOT_INSIDE_MATRIX_X_HIGH'
	public static val CLUE_NOT_INSIDE_MATRIX_X_LOW = 'CLUE_NOT_INSIDE_MATRIX_X_LOW'
	public static val CLUE_NOT_INSIDE_MATRIX_Y_HIGH = 'CLUE_NOT_INSIDE_MATRIX_Y_HIGH'
	public static val CLUE_NOT_INSIDE_MATRIX_Y_LOW = 'CLUE_NOT_INSIDE_MATRIX_Y_LOW'
	public static val INVALID_CLUE_VALUE_HIGH = 'INVALID_CLUE_VALUE_HIGH'
	public static val INVALID_CLUE_LOW = 'INVALID_CLUE_LOW'

	@Check(FAST)
	def checkMapSize(Mapdim mapdim) {
		if (mapdim.sizex != mapdim.sizey) {
			error('Map dimension should be a square matrix!', Literals.MAPDIM__SIZEY, NOT_SQUARE_MATRIX,
				mapdim.sizex.toString, mapdim.sizey.toString)
		}
	}

	@Check(FAST)
	def validate(Domainmodel domainmodel) {
//        if(domainmodel.getMapdim.sizex != domainmodel.getMapdim.sizey) {
//            error('Map dimension should be a square matrix!', Literals.DOMAINMODEL__MAPDIM, NOT_SQUARE_MATRIX, )
//        }
		val xCoordinates = new ArrayList<Integer>
		val yCoordinates = new ArrayList<Integer>

		for (i : 0 ..< domainmodel.clues.size) {
			val clue = domainmodel.clues.get(i)
			// CLUE INSIDE MATRIX
			if (domainmodel.getMapdim.sizex <= clue.posx) {
				error('Clue position must be inside matrix!', Literals.DOMAINMODEL__CLUES, i,
					CLUE_NOT_INSIDE_MATRIX_X_HIGH)
			}
			if (clue.posx < 0) {
				error('Clue position must be inside matrix!', Literals.DOMAINMODEL__CLUES, i,
					CLUE_NOT_INSIDE_MATRIX_X_LOW)
			}
			if (domainmodel.getMapdim.sizey <= clue.posy) {
				error('Clue position must be inside matrix!', Literals.DOMAINMODEL__CLUES, i,
					CLUE_NOT_INSIDE_MATRIX_Y_HIGH)
			}
			if (clue.posy < 0) {
				error('Clue position must be inside matrix!', Literals.DOMAINMODEL__CLUES, i,
					CLUE_NOT_INSIDE_MATRIX_Y_LOW)
			}

			// VALUE BETWEEN 0 AND 9
			if (clue.value > 9) {
				error('Clue value can only be between 0 and 9', Literals.DOMAINMODEL__CLUES, i, INVALID_CLUE_VALUE_HIGH)
			}
			if (clue.value < 0) {
				error('Clue value can only be between 0 and 9', Literals.DOMAINMODEL__CLUES, i, INVALID_CLUE_LOW)
			}

			// ONLY ONE CLUE AT A PLACE
			for (j : 0 ..< xCoordinates.size) {
				if (clue.posx == xCoordinates.get(j) && clue.posy == yCoordinates.get(j)) {
					error('Clue at this position already defined', Literals.DOMAINMODEL__CLUES, i, CLUE_ALREADY_DEFINED)
				}
			}
			xCoordinates.add(clue.getPosx)
			yCoordinates.add(clue.getPosy)
		}
	}
}
