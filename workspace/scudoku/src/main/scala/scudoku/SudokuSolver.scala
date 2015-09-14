package scudoku

import scudoku.data.Grid
import scudoku.solvers.TSolvingAlgorithm
import scudoku.solvers.OnlyOnePotentialValueLeftInCell
import scudoku.solvers.LastPotentialValueInBlockOrLineOrColumn
import scudoku.solvers.IfValueFixedToLineOrColumnInsideABlockThenDeleteFromRest
import scudoku.checkers.TConsistencyCheck
import scudoku.checkers.CheckCellsForFixDuplicateValues
import scudoku.checkers.CheckCellsForNoMorePotentialValuesAvailable

class SudokuSolver (solveThisGrid: String) {
  val grid: Grid = new Grid()
  
  { // Constructor
    grid.parseStringGrid(solveThisGrid)
    if (Debug.SOLVING_DEBUG) {
  	  Debug.debug("Grid after init (# fixed: " + grid.numOfCellsFixed + "): \n" + grid);
    }
  }

  var _numSolveSteps: Int = 0
  def numSolveSteps = _numSolveSteps
  
  def isSolved = grid.isSolved
  
  def solve: Boolean = {
    val solvers: List[TSolvingAlgorithm] = List(
        new OnlyOnePotentialValueLeftInCell(),
        new LastPotentialValueInBlockOrLineOrColumn(),
        new IfValueFixedToLineOrColumnInsideABlockThenDeleteFromRest())
    
    val checkers: List[TConsistencyCheck] = List(
        new CheckCellsForFixDuplicateValues(),
        new CheckCellsForNoMorePotentialValuesAvailable())
        
     solveLoop(grid, solvers, checkers)
     isSolved
  }

  def solveLoop(grid: Grid, solvers: List[TSolvingAlgorithm], checkers: List[TConsistencyCheck]) = {
    var oldFixedCounter = grid.numOfCellsFixed
    var gridHasChanged = true
    
    while (! isSolved && gridHasChanged) {
      gridHasChanged = false
      _numSolveSteps = _numSolveSteps + 1
      for (solver <- solvers) {        
        if (!isSolved) {          
          if (Debug.SOLVING_DEBUG) {
            Debug.debug("Iteration " + (_numSolveSteps) +
                ". Applying now algorithm " + solver.getClass().getName());
          }
          
          solver.tryToSolve(grid)

          // TODO: check not only for fixed but also for changes in potential values
          if (grid.numOfCellsFixed != oldFixedCounter) {
          	oldFixedCounter = grid.numOfCellsFixed
          	gridHasChanged = true
          }
        }

        if (Debug.SOLVING_DEBUG) {
          Debug.debug("Grid changed (# fixed: " + grid.numOfCellsFixed + "): \n" + grid);
        }
      }
      
      // apply all checkers one after the other
      for (checker <- checkers) {
        if (Debug.SOLVING_DEBUG) {
          Debug.debug("Applying now checker " + checker.getClass().getName());
        }
        if (! checker.checkConsistency(grid)) { // if not consistent, this throws a RunTimeException
        	throw new RuntimeException("consistency check failed!")
        }
      }
    }
  }
  
  def printResult = {
    if (isSolved) {
      println("Grid solved: \n" + grid)
      var cellDebug = Debug.CELL_DEBUG
      Debug.CELL_DEBUG = false
      println("Or short: \n" + grid)
      Debug.CELL_DEBUG = cellDebug
    }
    else {
        println("Grid not solved (# fixed: " + grid.numOfCellsFixed + "): \n" + grid);
    }    
  }
}

object SudokuSolver {
   def main(args: Array[String]): Unit = {
    // '.' is empty cell
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

    val sodukoSolver = new SudokuSolver(sampleGrid)
    
    sodukoSolver.solve
    sodukoSolver.printResult
   }
}