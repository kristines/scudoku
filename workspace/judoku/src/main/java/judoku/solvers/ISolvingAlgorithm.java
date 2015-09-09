package judoku.solvers;

import judoku.data.Grid;

public interface ISolvingAlgorithm {
	// returns true if solved
	public boolean tryToSolve(Grid grid);
}
