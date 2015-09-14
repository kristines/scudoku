package scudoku.solvers

/**
 * Java2Scala port from judoku.solvers.OnlyOnePotentialValueLeftInCell
 */

import scudoku.data.Grid
import scudoku.data.Cell

class OnlyOnePotentialValueLeftInCell extends TSolvingAlgorithm {
  // Does a cell have only one potential value left? Fix it then.
  override def tryToSolve(grid: Grid): Boolean = {
    grid.blocks.
      map(block => block.getCells).
      foreach(cells => {
        cells.
        filter(cell => !cell.isFix).
        filter(cell => cell.potentialValues.size == 1).
        map(cell => cell.value_=(cell.potentialValues.head))
      })

    grid.isSolved
  }
}