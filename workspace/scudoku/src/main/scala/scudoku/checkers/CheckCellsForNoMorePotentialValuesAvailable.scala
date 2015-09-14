package scudoku.checkers

/**
 * Java2Scala port from judoku.checkers.CheckCellsForNoMorePotentialValuesAvailable
 */

import scudoku.data._

class CheckCellsForNoMorePotentialValuesAvailable extends TConsistencyCheck {
  // check if there is a cell which is not fix but has no more potential values
  override def checkConsistency(grid: Grid): Boolean = {
    ! grid.blocks.
      map((block: Block) => block.getCells). 
      exists(cells => 
        cells.exists(cell => (!cell.isFix && cell.potentialValues.isEmpty)))
  }
}