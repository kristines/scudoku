package scudoku.checkers

/**
 * Java2Scala port from judoku.checkers.CheckCellsForFixDuplicateValues
 */

import scudoku.data.Grid
import scudoku.data.Cell
import scudoku.data.CellSet
import scudoku.Debug

class CheckCellsForFixDuplicateValues extends TConsistencyCheck {
  /**
   * allows to add a "debug" method to Traversable[Cell], see (*)
   */
  abstract class TraversableDebugger[A, +Repr] {
    def debug(a: A, debugFlag: Boolean = true): Repr
  }
  
  private implicit def traversableWrapper(as: Traversable[Cell]) = new TraversableDebugger[Cell, Traversable[Cell]] {
    def debug(cell: Cell, debugFlag: Boolean) = {
      if (debugFlag) Debug.debug("current cell in debug is: " + cell)

      Set[Cell](cell)
    }
  }

  // --------------------------------------------------------------------------------------------------------

  // check if there a two cells with the same fixed value (inside a block/line/column)
  override def checkConsistency(grid: Grid): Boolean = {
    // this is the very outer loop iterating over the cellsets of the grid
    // check all blocks, lines and cells
    ! grid.getAllCellSets().     // consistent, if there a NO duplicates
        map(cellSet => cellSet.getCells).
        // check if there are duplicates
        exists(cells => checkForDuplicates(cells, false))  // finding at least one duplicate is sufficient to determine that the grid is inconsistent
  }       

  private def checkForDuplicates(cells: Traversable[Cell], debugFlag: Boolean): Boolean = {
    // this is the outer loop iterating over the cells of the cellset
    // TODO 1st usage of 'cells' - is it possible to get access to this "variable" without "globalizing" it to another method?
    cells.    
    filter(cell => cell.isFix).
    exists(cell => {
      // TODO 2nd usage of 'cells' - (possible as globally available as method parameter). How can we write this into ONE method?
      cells.  

      // check for "id>" guarantees that otherCell!=cell - and is efficient as we do hence do not check b.isDuplicate(a) if we checked already a.isDuplicate(b)
      filter(otherCell => otherCell.id > cell.id).  

      // (*) create implicit debug method (see above), looks like as if we had this debug method implemented in Set[Cell]
      debug(cell, debugFlag).

      // variant 1: using foldLeft: not efficient as we check for *all* duplicates      
      // foldLeft(false)((duplicateFound: Boolean, otherCell: Cell) => duplicateFound || cell.isDuplicate(otherCell))
      
      // variant 2: using exist: efficient as in our example it is sufficient to find one duplicate to determine if the grid is inconsistent
      exists(otherCell => cell.isDuplicate(otherCell))
    })
  }
}