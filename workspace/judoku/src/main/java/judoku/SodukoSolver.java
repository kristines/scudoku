package judoku;

import java.util.ArrayList;
import java.util.List;

import judoku.checkers.*;
import judoku.data.Grid;
import judoku.solvers.*;

public class SodukoSolver {
	private String gridAsString = null;
	private Grid grid = null;
	private int numSolveSteps = 0;
	
	public SodukoSolver(String solveThisGrid) {
		this.gridAsString = solveThisGrid;
		this.grid = new Grid();

		parseStringGrid(gridAsString);
	}

	// returns true if solved
	public boolean solve() {
        List<ISolvingAlgorithm> solvers  = collectSolvingAlgorithms();
        List<IConsistencyCheck> checkers = collectCheckingAlgorithms();
        solveLoop(grid, solvers, checkers);
        
        return grid.isSolved();
	}

    public boolean isSolved() {
        return (grid.isSolved());
    }
    
    public int getNumSolveSteps() {
    	return numSolveSteps;
    }

	// initialize the grid with the Stringified grid
	private void parseStringGrid(String sampleGrid) {
		// initialize the grid
		grid.parseStringGrid(sampleGrid);

		if (Debug.SOLVING_DEBUG) {
			System.out.println("Grid after init (# fixed: " + grid.numOfCellsFixed() + "): \n" + grid);
		}
	}

	// initialize the list of solving algorithms
	private List<ISolvingAlgorithm> collectSolvingAlgorithms() {
		List<ISolvingAlgorithm> solvers = new ArrayList<ISolvingAlgorithm>();
		solvers.add(new OnlyOnePotentialValueLeftInCell());
		solvers.add(new LastPotentialValueInBlockOrLineOrColumn());
		solvers.add(new IfValueFixedToLineOrColumnInsideABlockThenDeleteFromRest());
		return solvers;
	}

	// initialize the list of consistency checking algorithms
	private List<IConsistencyCheck> collectCheckingAlgorithms() {
		List<IConsistencyCheck> checkers = new ArrayList<IConsistencyCheck>();
		checkers.add(new CheckCellsForDuplicateFixedValuesInBlockOrLineOrColumn());
		checkers.add(new CheckCellsForNoMorePotentialValuesAvailable());
		return checkers;
	}

	// solving and checking loop
	private void solveLoop(Grid grid, List<ISolvingAlgorithm> solvers, List<IConsistencyCheck> checkers) {
		// while not solved, apply all solvers and checkers
		int oldFixedCounter = grid.numOfCellsFixed();
		boolean gridHasChanged = true;
		
		// while not done and we still see progress...
		while (!grid.isSolved() && gridHasChanged) {
			gridHasChanged = false;
			numSolveSteps++;
			
			// apply solvers one after the other
			for (ISolvingAlgorithm solver: solvers) {
				if (Debug.SOLVING_DEBUG) {
					System.out.println("Iteration " + (numSolveSteps) +
							". Applying now algorithm " + solver.getClass().getName());
				}

				// do it
				boolean solved = solver.tryToSolve(grid);
				if (solved) break; // we are done - so no need to solve anything more

				// any changes?
				if (grid.numOfCellsFixed() != oldFixedCounter) {
					oldFixedCounter = grid.numOfCellsFixed();
					gridHasChanged = true;
					if (Debug.SOLVING_DEBUG) {
				        System.out.println("Grid changed (# fixed: " + grid.numOfCellsFixed() + "): \n" + grid);
					}
				}
			}

			// apply all checkers one after the other
			for (IConsistencyCheck checker: checkers) {
				if (Debug.SOLVING_DEBUG) {
					System.out.println("Applying now checker " + checker.getClass().getName());
				}
				checker.checkConsistency(grid);	// if not consistent, this throws a RunTimeException 
			}
		}
	}

	// and the result finally is ...
	private void printResult() {
		if (grid.isSolved()) {
			System.out.println("Grid solved: \n" + grid);
			boolean cellDebug = Debug.CELL_DEBUG;
			Debug.CELL_DEBUG = false;
			System.out.println("Or short: \n" + grid);
			Debug.CELL_DEBUG = cellDebug;
		}
		else {
		    System.out.println("Grid not solved (# fixed: " + grid.numOfCellsFixed() + "): \n" + grid);
		}
	}

	// ----------------------------------------------------------------------------------------
	
	public static void main(String[] args) throws Throwable {
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

		SodukoSolver sodukoSolver = new SodukoSolver(sampleGrid);
		
		sodukoSolver.solve();
		sodukoSolver.printResult();
	}
}
