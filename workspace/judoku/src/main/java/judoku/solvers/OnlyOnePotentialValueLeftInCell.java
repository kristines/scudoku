package judoku.solvers;

import java.util.Set;

import judoku.data.Cell;
import judoku.data.Grid;
import judoku.data.OneToNine;

public class OnlyOnePotentialValueLeftInCell implements ISolvingAlgorithm {
	// Does a cell have only one potential value left? Fix it then.
	@Override
	public boolean tryToSolve(Grid grid) {
		for (Cell c: grid.getCells()) {
			if (!c.isFix()) {
				Set<OneToNine> valuesLeft = c.getPotentialValues();

				if (valuesLeft.size() == 1) {
					c.setValue((OneToNine)valuesLeft.toArray()[0]);
				}
				
		// covered by CheckCellsForNoMorePotentialValuesAvailable already?
				else if (valuesLeft.isEmpty()) {
					System.out.println("Error. Grid is: \n" + grid);
					throw new RuntimeException("No values left for cell " + c.id + ". No solution for this grid possible");
				}
		// -- end of ... covered by CheckCellsForNoMorePotentialValuesAvailable already?

			}
		}

		return grid.isSolved();
	}
}
