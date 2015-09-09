package judoku;

import java.util.ArrayList;
import java.util.List;

import judoku.checkers.*;
import judoku.data.Grid;
import judoku.solvers.*;

public class SodukoReverser {
	public static void main(String[] args) throws Throwable {
		try {
			// '.' is empty cell
	        String sampleGrid =
	        	"....7.3.." +
	        	".5......." +
	        	".6.1.5..." +
	        	"..8....7." +
	        	"532....64" +
	        	"4...8..35" +
	        	".....9..." +
	        	".8..5.9.." +
	        	".4...6..1" ;

	        Grid grid = initialize(sampleGrid);
	        List<ISolvingAlgorithm> solvers = collectSolvingAlgorithms();
	        List<IConsistencyCheck> checkers = collectCheckingAlgorithms();
	        solve(grid, solvers, checkers);
	        printResult(grid);

	        System.out.println("\nNow trying to reverse the solution");
	        reverse(grid, solvers, checkers);
		}
		catch (Throwable t) {
			System.err.println("Caught an Exception " + t + ". StackTrace:");
			t.printStackTrace(System.err);
			throw t;
		}
	}

	private static void reverse (Grid solvedGrid, List<ISolvingAlgorithm> solvers, List<IConsistencyCheck> checkers) {
		// set 1,2,... 81 cells as empty
		for (int numEmptyCells=1; numEmptyCells<Grid.NINE*Grid.NINE; numEmptyCells++) {
			// find all combinations of <numEmptyCells> cell ids to set to empty
//TODO
			{
				// clone the solved grid
				Grid cloneGrid = (Grid)solvedGrid.clone();

				// set all cells to empty
//TODO

				// solve the cloned grid
				solve(cloneGrid, solvers, checkers);
//		        printResult(grid);
			}
		}
	}



	// initialize the grid with the chars
	private static Grid initialize(String sampleGrid) {
		// initialize the grid
		Grid grid = new Grid();
		grid.parseStringGrid(sampleGrid);

		System.out.println("Grid after init (# fixed: " + grid.numOfCellsFixed() + "): \n" + grid);
		return grid;
	}

	// initialize the list of solving algorithms
	private static List<ISolvingAlgorithm> collectSolvingAlgorithms() {
		List<ISolvingAlgorithm> solvers = new ArrayList<ISolvingAlgorithm>();
		solvers.add(new OnlyOnePotentialValueLeftInCell());
		solvers.add(new LastPotentialValueInBlockOrLineOrColumn());
		solvers.add(new IfValueFixedToLineOrColumnInsideABlockThenDeleteFromRest());
		return solvers;
	}

	// initialize the list of consistency checking algorithms
	private static List<IConsistencyCheck> collectCheckingAlgorithms() {
		List<IConsistencyCheck> checkers = new ArrayList<IConsistencyCheck>();
		checkers.add(new CheckCellsForDuplicateFixedValuesInBlockOrLineOrColumn());
		checkers.add(new CheckCellsForNoMorePotentialValuesAvailable());
		return checkers;
	}

	// solving and checking loop
	private static void solve(Grid grid, List<ISolvingAlgorithm> solvers, List<IConsistencyCheck> checkers) {
		// while not solve, apply all solvers and checkers
		int solveStep = 0;
		int oldFixedCounter = grid.numOfCellsFixed();
		boolean gridHasChanged = true;
		while (!grid.isSolved() && gridHasChanged) {
			gridHasChanged = false;

			// apply solvers one after the other
			for (ISolvingAlgorithm solver: solvers) {
				if (Debug.SOLVING_DEBUG) {
					System.out.println("Iteration " + (++solveStep) +
							". Applying now algorithm " + solver.getClass().getName());
				}

				// do it
				boolean solved = solver.tryToSolve(grid);
				if (solved) break; // we are done - so no need to solve anything else ;-)

				// any changes?
				if (grid.numOfCellsFixed() != oldFixedCounter) {
					oldFixedCounter = grid.numOfCellsFixed();
					gridHasChanged = true;
			        System.out.println("Grid changed (# fixed: " + grid.numOfCellsFixed() + "): \n" + grid);
				}
			}

			// apply all checkers one after the other
			for (IConsistencyCheck checker: checkers) {
				if (Debug.SOLVING_DEBUG) {
					System.out.println("Applying now checker " + checker.getClass().getName());
				}
				checker.checkConsistency(grid);
			}
		}
	}

	// and the result finally is ...
	private static void printResult(Grid grid) {
		if (grid.isSolved()) {
			System.out.println("Grid solved: \n" + grid);
			Debug.CELL_DEBUG = false;
			System.out.println("Or short: \n" + grid);
		}
		else {
		    System.out.println("Grid not solved (# fixed: " + grid.numOfCellsFixed() + "): \n" + grid);
		}
	}
}
