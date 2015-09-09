package judoku.checkers;

import java.util.List;

import judoku.data.Block;
import judoku.data.Cell;
import judoku.data.Column;
import judoku.data.Grid;
import judoku.data.Line;

public class CheckCellsForDuplicateFixedValuesInBlockOrLineOrColumn implements IConsistencyCheck {
	@Override
	public boolean checkConsistency(Grid grid){
		// check all blocks
		for (Block b: grid.getBlocks()) {
			checkForDuplicates(b.getCells());
		}
		// check all lines
		for (Line l: grid.getLines()) {
			checkForDuplicates(l.getCells());
		}
		// check all cells of all blocks
		for (Column col: grid.getColumns()) {
			checkForDuplicates(col.getCells());
		}

		return true;
	}

	// check if there a two cells with the same fixed value (inside a block/line/column)
	public boolean checkForDuplicates(List<Cell> cells) {
		for (Cell c: cells) {
			if (c.isFix()) {
				for (Cell otherCell: cells) {
					if ((c!=otherCell) && otherCell.isFix()) {
						if (c.getValue() == otherCell.getValue())
							throw new RuntimeException("Fixed value duplicate in cells " + c.id + " and " + otherCell.id);
					}
				}
			}
		}
		return true;
	}
}
