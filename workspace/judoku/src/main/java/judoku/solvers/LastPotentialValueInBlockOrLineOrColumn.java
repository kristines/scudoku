package judoku.solvers;

import java.util.List;

import judoku.data.Block;
import judoku.data.Cell;
import judoku.data.Column;
import judoku.data.Grid;
import judoku.data.Line;
import judoku.data.OneToNine;

public class LastPotentialValueInBlockOrLineOrColumn implements ISolvingAlgorithm {
	// Is there only one cell in a block/line/column where one value fits? Fix it.
	@Override
	public boolean tryToSolve(Grid grid) {
		// check all blocks
		for (Block b: grid.getBlocks()) {
			lastPotentialValueInListOfCells(b.getCells());
		}
		// check all lines
		for (Line l: grid.getLines()) {
			lastPotentialValueInListOfCells(l.getCells());
		}
		// check all columns
		for (Column col: grid.getColumns()) {
			lastPotentialValueInListOfCells(col.getCells());
		}

		return grid.isSolved();
	}

	private void lastPotentialValueInListOfCells(List<Cell> cells) {
		// check all values
		for (OneToNine value: OneToNine.values()) {
//			System.out.println("Value now: " + value);
			boolean foundTwiceOrMore = false;
			Cell cellWithValueFound = null;
			for (Cell c: cells) {
				// if value is found in c ...
				boolean found = c.getPotentialValues().contains(value);
				if (found) {
					// ... but not yet before, remember it
					if (cellWithValueFound == null) {
						cellWithValueFound = c;
//						System.out.println("Found pot. value : " + value + " in cell " + c.getId());
					}
					// ... else value has been found before, no not interesting
					else {
						foundTwiceOrMore = true;
//						System.out.println("Found pot. value : " + value + " twice (also in " + c.getId());
					}
				}
			}
			// if we have found a cell inside the block/line/column which is the only cell with this
			//    value as a potential value, then fix it
			if (!(foundTwiceOrMore) && (cellWithValueFound!=null)) {
				cellWithValueFound.setValue(value);
			}
		}
	}
}
