package judoku.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import judoku.Debug;

public class TestSudokuSolver {
	@Before
	public void initializeDebug() {
		// show cell contents in Debug mode - include id and potential values
		Debug.CELL_DEBUG = false;
		// show grid in Debug mode - show lines, cols, blocks separate
		Debug.GRID_DEBUG = false;
		// show notifications in Debug mode
		Debug.NOTIFY_DEBUG = false;
		// show fixing of cells in Debug mode
		Debug.FIXED_DEBUG = false;
		// show solving algorithms in Debug mode
		Debug.SOLVING_DEBUG = false;
		// show deletion of values in Debug mode
		Debug.DELETE_DEBUG = false;
	}
	
	@Test
	public void testExample1() {
		String sampleGrid = 
			"..569...8" +
			"..4..2.1." +
			"6.3...9.2" +
			"..1...4.." +
			"8...5...." +
			"..7.2..5." +
			"37..84..." +
			"..297..6." +
			".9......." ;

		judoku.SudokuSolver suso = new judoku.SudokuSolver(sampleGrid);
		assertTrue (suso.solve());
		assertTrue (suso.isSolved());
		assertEquals (2, suso.getNumSolveSteps());
	}

	@Test
	public void testExample1_complete() {
		String sampleGrid = 
			"891274356" +
			"753968412" +
			"264135798" +
			"618543279" +
			"532791864" +
			"479682135" +
			"325819647" +
			"186457923" +
			"947326581";

		judoku.SudokuSolver suso = new judoku.SudokuSolver(sampleGrid);
		assertTrue (suso.isSolved());
		assertTrue (suso.solve());
		assertTrue (suso.isSolved());
	}

	@Test
	public void testExample2() {
		String sampleGrid = 
				"..3.....1" +
				".94.6.75." +
				"8.1....2." +
				"......269" +
				"5...37..." +
				"........." +
				"..5.931.." +
				"9....58.." +
				"4....8..6" ;

		judoku.SudokuSolver suso = new judoku.SudokuSolver(sampleGrid);
		assertTrue (suso.solve());
		assertTrue (suso.isSolved());
		assertEquals (3, suso.getNumSolveSteps());
	}

	@Test
	public void testExample3() {
		String sampleGrid = 
				"........." +
				"..37..54." +
				".......26" +
				"...4....." +
				"...935..." +
				"1.2.....4" +
				".6....8.1" +
				".2.5.839." +
				"7........" ;

		judoku.SudokuSolver suso = new judoku.SudokuSolver(sampleGrid);
		assertTrue (suso.solve());
		assertTrue (suso.isSolved());
		assertEquals (4, suso.getNumSolveSteps());
	}

	@Test
	public void testExample4() {
		String sampleGrid = 
				"...7..4.1" +
				"8...5...." +
				"95...18.." +
				"......5.8" +
				".3..6...4" +
				"1....5.3." +
				"5.8..7.69" +
				"..3...1.." +
				"..2..9..." ;

		judoku.SudokuSolver suso = new judoku.SudokuSolver(sampleGrid);
		assertTrue (suso.solve());
		assertTrue (suso.isSolved());
		assertEquals (4, suso.getNumSolveSteps());
	}

	@Test
	public void testExample5() {
		String sampleGrid = 
				".....8..." +
				".4......." +
				"9..1.5.2." +
				"2..9.41.." +
				"..85....." +
				".....2..6" +
				"6..81.93." +
				".9.3..8.." +
				"5..2...7." ;

		judoku.SudokuSolver suso = new judoku.SudokuSolver(sampleGrid);
		assertTrue (suso.solve());
		assertTrue (suso.isSolved());
		assertEquals (3, suso.getNumSolveSteps());
	}

	@Test
	public void testExample6() {
		String sampleGrid = 
				"....7.3.." +
				".5......." +
				".6.1.5..." +
				"..8....7." +
				"532....64" +
				"4...8..35" +
				".....9..." +
				".8..5.9.." +
				".4...6..1" ;

		judoku.SudokuSolver suso = new judoku.SudokuSolver(sampleGrid);
		assertTrue (suso.solve());
		assertTrue (suso.isSolved());
		assertEquals (6, suso.getNumSolveSteps());
	}

	@Test
	public void testBadExample1_cannotBeSolved() {
		String sampleGrid = 
				"........." +
				"..4..2.1." +
				"6.3...9.2" +
				"..1...4.." +
				"8...5...." +
				"..7.2..5." +
				"37..84..." +
				"..297..6." +
				".9......." ;

		assertFalse (new judoku.SudokuSolver(sampleGrid).solve());
	}

	@Test
	public void testBadExample2_cannotBeSolved() {
		String sampleGrid = 
				"........." +
				"........." +
				"........." +
				"........." +
				"........." +
				"........." +
				"........." +
				"........." +
				".........";

		assertFalse (new judoku.SudokuSolver(sampleGrid).solve());
	}
	
	@Test
	public void testBadExample3_wrongSetup() {
		String sampleGrid = 
				"........." +
				"........." +
				"........." +
				"........." +
				"........." +
				"........." +
				"........." +
				"........." +
				"........0";
		
		try {
			new judoku.SudokuSolver(sampleGrid).solve();
			fail("RuntimeException expected");
		}
		catch (RuntimeException rex) {
			assertTrue(rex.getMessage().contains("wrong setup"));
		}
	}
	
	@Test
	public void testBadExample4_tooShort() {
		String sampleGrid = 
				"........." +
				"........." +
				".........";
		
		try {
			new judoku.SudokuSolver(sampleGrid).solve();
			fail("RuntimeException expected");
		}
		catch (RuntimeException rex) {
			assertTrue(rex.getMessage().contains("9x9"));
		}
	}

	@Test
	public void testBadExample5_duplicateValues() {
		String sampleGrid = 
				"..569...8" +
				"..4..2.1." +
				"6.3...9.2" +
				"..1...4.." +
				"8...5...." +
				"..7.2..5." +
				"37..84..." +
				"..297..6." +
				".7......." ;

		try {
			assertTrue (new judoku.SudokuSolver(sampleGrid).solve());
			fail("RuntimeException expected");
		}
		catch (RuntimeException rex) {
			assertTrue(rex.getMessage().contains("duplicate"));
		}
	}
}
