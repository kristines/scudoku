package scudoku.test

import org.scalatest.junit.JUnitSuite
import org.junit.Assert._
import org.junit.Test
import org.junit.Before
import scudoku.Debug

class TestSudokuSolverAsScalaTest extends JUnitSuite{
    @Before 
    def initializeDebug(){
      // show cell contents in Debug mode - include id and potential values
      Debug.CELL_DEBUG = false
      // show grid in Debug mode - show lines, cols, blocks separate
      Debug.GRID_DEBUG = false
      // show notifications in Debug mode
      Debug.NOTIFY_DEBUG = false
      // show fixing of cells in Debug mode
      Debug.FIXED_DEBUG = false
      // show solving algorithms in Debug mode
      Debug.SOLVING_DEBUG = false
      // show deletion of values in Debug mode
      Debug.DELETE_DEBUG = false      
    }
    
    @Test
    def testExample1() {
      val sampleGrid = 
        "..569...8" +
        "..4..2.1." +
        "6.3...9.2" +
        "..1...4.." +
        "8...5...." +
        "..7.2..5." +
        "37..84..." +
        "..297..6." +
        ".9......." ;
  
      val suso = new scudoku.SudokuSolver(sampleGrid);
      
      assertTrue (suso.solve)
      assertTrue (suso.isSolved)
      assertEquals (3, suso.numSolveSteps)   // funny enough but Java needs 2 solve steps while Scala needs 3!
    }

    @Test
    def testExample1_complete() {
      val sampleGrid = 
        "891274356" +
        "753968412" +
        "264135798" +
        "618543279" +
        "532791864" +
        "479682135" +
        "325819647" +
        "186457923" +
        "947326581";
  
      val suso = new scudoku.SudokuSolver(sampleGrid);
      assertTrue (suso.isSolved);
      assertTrue (suso.solve);
      assertTrue (suso.isSolved);
    }
  
    @Test
    def testExample2() {
      val sampleGrid = 
          "..3.....1" +
          ".94.6.75." +
          "8.1....2." +
          "......269" +
          "5...37..." +
          "........." +
          "..5.931.." +
          "9....58.." +
          "4....8..6" ;
  
      val suso = new scudoku.SudokuSolver(sampleGrid);
      assertTrue (suso.solve);
      assertTrue (suso.isSolved);
      assertEquals (3, suso.numSolveSteps);
    }
  
    @Test
    def testExample3() {
      val sampleGrid = 
          "........." +
          "..37..54." +
          ".......26" +
          "...4....." +
          "...935..." +
          "1.2.....4" +
          ".6....8.1" +
          ".2.5.839." +
          "7........" ;
  
      val suso = new scudoku.SudokuSolver(sampleGrid);
      assertTrue (suso.solve);
      assertTrue (suso.isSolved);
      assertEquals (4, suso.numSolveSteps);
    }
  
    @Test
    def testExample4() {
      val sampleGrid = 
          "...7..4.1" +
          "8...5...." +
          "95...18.." +
          "......5.8" +
          ".3..6...4" +
          "1....5.3." +
          "5.8..7.69" +
          "..3...1.." +
          "..2..9..." ;
  
      val suso = new scudoku.SudokuSolver(sampleGrid);
      assertTrue (suso.solve);
      assertTrue (suso.isSolved);
      assertEquals (4, suso.numSolveSteps);
    }
  
    @Test
    def testExample5() {
      val sampleGrid = 
          ".....8..." +
          ".4......." +
          "9..1.5.2." +
          "2..9.41.." +
          "..85....." +
          ".....2..6" +
          "6..81.93." +
          ".9.3..8.." +
          "5..2...7." ;
  
      val suso = new scudoku.SudokuSolver(sampleGrid);
      assertTrue (suso.solve);
      assertTrue (suso.isSolved);
      assertEquals (3, suso.numSolveSteps);
    }
  
    @Test
    def testExample6() {
      val sampleGrid = 
          "....7.3.." +
          ".5......." +
          ".6.1.5..." +
          "..8....7." +
          "532....64" +
          "4...8..35" +
          ".....9..." +
          ".8..5.9.." +
          ".4...6..1" ;
  
      val suso = new scudoku.SudokuSolver(sampleGrid);
      assertTrue (suso.solve);
      assertTrue (suso.isSolved);
      assertEquals (6, suso.numSolveSteps);
    }
  
    @Test
    def testBadExample1_cannotBeSolved() {
      val sampleGrid = 
          "........." +
          "..4..2.1." +
          "6.3...9.2" +
          "..1...4.." +
          "8...5...." +
          "..7.2..5." +
          "37..84..." +
          "..297..6." +
          ".9......." ;
  
      assertFalse (new scudoku.SudokuSolver(sampleGrid).solve);
    }
  
    @Test
    def testBadExample2_cannotBeSolved() {
      val sampleGrid = 
          "........." +
          "........." +
          "........." +
          "........." +
          "........." +
          "........." +
          "........." +
          "........." +
          ".........";
  
      assertFalse (new scudoku.SudokuSolver(sampleGrid).solve);
    }
    
    @Test
    def testBadExample3_wrongSetup() {
      val sampleGrid = 
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
        new scudoku.SudokuSolver(sampleGrid).solve;
        fail("RuntimeException expected");
      }
      catch   {
        case rex: RuntimeException => assertTrue(rex.getMessage().contains("wrong setup"));
        case _: Throwable => fail("Wrong Throwable!")
      }
    }
    
    @Test
    def testBadExample4_tooShort() {
      val sampleGrid = 
          "........." +
          "........." +
          ".........";
      
      try {
        new scudoku.SudokuSolver(sampleGrid).solve;
        fail("RuntimeException expected");
      }
      catch  {
        case rex: RuntimeException => assertTrue(rex.getMessage().contains("9x9"))
        case _: Throwable => fail("Wrong Throwable!")
      }
    }
  
    @Test
    def testBadExample5_duplicateValues() {
      val sampleGrid = 
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
        assertTrue (new scudoku.SudokuSolver(sampleGrid).solve);
        fail("RuntimeException expected");
      }
      catch  {
        case rex: RuntimeException => assertTrue(rex.getMessage().contains("consistency check failed"));
        case _: Throwable => fail("Wrong Throwable!")
      }
    }
}