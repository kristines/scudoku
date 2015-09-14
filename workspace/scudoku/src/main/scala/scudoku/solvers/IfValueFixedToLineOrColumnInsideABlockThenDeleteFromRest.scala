package scudoku.solvers

import scudoku.data.Grid
import scudoku.data.UnknownOrOneToNine
import scudoku.data.UnknownOrOneToNine._
import scudoku.data.Cell
import scudoku.data.Block
import scudoku.data.CellSet

class IfValueFixedToLineOrColumnInsideABlockThenDeleteFromRest extends TSolvingAlgorithm {
  override def tryToSolve(grid: Grid): Boolean = {
    // check all blocks
    grid.blocks.
      map(block => block.getCells).
      // check all cells inside the block
      foreach(cells => checkAllPotentialValuesInsideCells(cells))
     
    grid.isSolved
  }
  
  private def checkAllPotentialValuesInsideCells(cells: Traversable[Cell]) = {
    cells.
    foreach(cell => {
      checkAllPotentialValuesInsideTheCellsLineOrColumn(cell, (cell => cell.line))
      checkAllPotentialValuesInsideTheCellsLineOrColumn(cell, (cell => cell.column))
    })
  }
  
  private def checkAllPotentialValuesInsideTheCellsLineOrColumn(cell: Cell, mapCellToLineOrColumn: (Cell => CellSet)) = {
    // check all potential values inside the cell ... for line or column
    cell.potentialValues.
      filter(potentialValue => checkIfPotentialValueIsOnlyInsideTheCellsLineOrColumn(cell, potentialValue, mapCellToLineOrColumn)).
      foreach(potentialValue => deleteValueInRestOutsideBlock(cell, potentialValue, mapCellToLineOrColumn(cell).getCells))
  }
  
  private def checkIfPotentialValueIsOnlyInsideTheCellsLineOrColumn(cell: Cell, value: UnknownOrOneToNine, mapCellToLineOrColumn: (Cell => CellSet)): Boolean = {
    // check all cells in the block
    ! cell.block.getCells.
       // only look at cells which are inside the same block but are in another column
      filter(otherCellInBlock => (mapCellToLineOrColumn(cell) != mapCellToLineOrColumn(otherCellInBlock))).
      // if one of these cells contains the potential value, then we are done
      exists(otherCellInBlock => otherCellInBlock.potentialValues.contains(value))
  }
  
  // delete the potential value in all cells inside its line/column (but outside of its block)
  private def deleteValueInRestOutsideBlock(cell: Cell, potentialValue: UnknownOrOneToNine, restOfCells: Traversable[Cell]) = {
    restOfCells.
      filter(otherCell => !cell.block.contains(otherCell)).
      foreach(otherCell => otherCell.deletePotentialValue(potentialValue))
  }
}