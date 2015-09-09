package judoku.checkers;

import judoku.data.Block;
import judoku.data.Cell;
import judoku.data.Grid;

public class CheckCellsForNoMorePotentialValuesAvailable implements IConsistencyCheck {
	@Override
	// check if there is a cell which is not fix but has no more potential values
	public boolean checkConsistency(Grid grid) {
		for (Block b: grid.getBlocks()) {
			for (Cell c: b.getCells()) {
				if (!(c.isFix()) && c.getPotentialValues().isEmpty()) {
					throw new RuntimeException("Cell c " + c.id + " is not fixed but has no more potential values");
				}
			}
		}
		return true;
	}
}
