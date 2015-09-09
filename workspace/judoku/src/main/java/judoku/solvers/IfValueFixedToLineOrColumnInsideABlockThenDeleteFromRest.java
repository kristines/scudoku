package judoku.solvers;

import java.util.List;

import judoku.data.Block;
import judoku.data.Cell;
import judoku.data.Grid;
import judoku.data.OneToNine;

public class IfValueFixedToLineOrColumnInsideABlockThenDeleteFromRest implements ISolvingAlgorithm {
	// If potential values are only possible on one line/column of a block (as others are fixed, for example)
	//     then delete this potential value from the rest
	@Override
	public boolean tryToSolve(Grid grid) {
		// check all blocks
		for (Block block: grid.getBlocks()) {
			// check all cells inside the block
			for (Cell cell: block.getCells()) {
				// check all potential values inside the cell
				for (OneToNine potentialValue: cell.getPotentialValues()) {
					if (checkIfValueIsOnlyInsideTheCellsLine(block, cell, potentialValue)) {
						deleteValueInRestOutsideBlock(block, cell, potentialValue, cell.getLine().getCells());
					}
					if (checkIfValueIsOnlyInsideTheCellsColumn(block, cell, potentialValue)) {
						deleteValueInRestOutsideBlock(block, cell, potentialValue, cell.getColumn().getCells());
					}
				}
			}
		}

		return grid.isSolved();
	}

	// delete the potential value in all cells inside its line/column (but outside of its block)
	private void deleteValueInRestOutsideBlock(Block block, Cell cell, OneToNine potentialValue, List<Cell> restOfCells) {
		Block theCellsBlock = cell.getBlock();
		if (theCellsBlock != block) throw new RuntimeException("should never happen"); // paranoia

		for (Cell otherCell: restOfCells) {
			if (!theCellsBlock.contains(otherCell)) {
				otherCell.deleteValue(potentialValue);
			}
		}
	}

	private boolean checkIfValueIsOnlyInsideTheCellsColumn(Block block, Cell cell, OneToNine value) {
		// check all cells in the block
		for (Cell otherCellInBlock: block.getCells()) {
			// only look at cells which are inside the same block but are in another line
			if (cell.getColumn() != otherCellInBlock.getColumn()) {
				// if one of these cells contains the potential value , then we are done
				if (otherCellInBlock.getPotentialValues().contains(value)) {
					return false;
				}
			}
		}

		return true;
	}

	private boolean checkIfValueIsOnlyInsideTheCellsLine(Block block, Cell cell, OneToNine value) {
		// check all cells in the block
		for (Cell otherCellInBlock: block.getCells()) {
			// only look at cells which are inside the same block but are in another line
			if (cell.getLine() != otherCellInBlock.getLine()) {
				// if one of these cells contains the potential value , then we are done
				if (otherCellInBlock.getPotentialValues().contains(value)) {
					return false;
				}
			}
		}

		return true;
	}
}
