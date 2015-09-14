package scudoku.solvers

/**
 * Java2Scala port from judoku.solvers.LastPotentialValueInBlockOrLineOrColumn
 */

import scudoku.data._
import scudoku.data.UnknownOrOneToNine._

class LastPotentialValueInBlockOrLineOrColumn extends TSolvingAlgorithm {
  // Is there only one cell in a block/line/column where one value fits? Fix it.
  override def tryToSolve(grid: Grid): Boolean = {
    // check all blocks, lines and cells
    grid.getAllCellSets().
      map(cellSet => cellSet.getCells).
      foreach (cells => lastPotentialValueInListOfCells(cells))
    
    grid.isSolved
  }
  
  private def lastPotentialValueInListOfCells(cells: Traversable[Cell]) = {
    // check all values
    UnknownOrOneToNine.values.
      // UNKNOWN is not needed to be checked
      filter (value => value!=UNKNOWN).
      foreach(value => {
        // find all cells with this value inside potential values
        val cellsWithPotentialValue = getAllCellsWithPotentialValue(cells, value)
        // only one cell? then fix it
        if (cellsWithPotentialValue.size == 1) {
           cellsWithPotentialValue.head.value_=(value) 
        }
      })
  }
  
  // get all cells which have value inside there potential values
  private def getAllCellsWithPotentialValue(cells: Traversable[Cell], value: UnknownOrOneToNine): Traversable[Cell] = {
    cells.filter(cell => cell.potentialValues.contains(value))
  }
}