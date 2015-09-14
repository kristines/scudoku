# Sudoku in Scala

## Contents 
1) Java version is in (Eclipse) project ```judoku```.
2) Scala port is in (Eclipse) project ```scudoku```. Port is 90% identical to the Java version except for some optimizations and minor refactorings. Algorithms are Scala-style!

## Main class
Main class is ```scudoku.SudokuSolver``` which calls various solving algorithms one after another until grid does not change anymore.
Besides the SudokuSolver also calls the consistency checks to find out if we ran into an inconsistent situation.

## Data structures
Data structures are in judoku|scudoku.data:
 * ```scudoku.data.Grid``` is the sudoku grid, containing 81 = 9x9 cells
 * ```scudoku.data.Line``` is one of the 9 lines (= rows) in the grid
 * ```scudoku.data.Column``` is one of the 9 columns in the grid
 * ```scudoku.data.Block``` is one of the 9 (3x3) blocks in the grid

Each of the 81 ```scudoku.data.Cell```s has a value (in case its vallue is fix) or a set of potential values which are eliminated one by one.
Changes to a ```scudoku.data.Cell``` are observed by the ```scudoku.data.Grid``` (and other observers) to react on the change.

The enumeration ```scudoku.data.UnknownOneToNine``` is an enumeration of 1,2,...,9 and 'Unknown' for the list of potential values of a cell.

## Solvers 
Solvers are different algorithms which try to either fix a value in a cell by deleting potential values.
 * ```scudoku.solvers.OnlyOnePotentialValueLeftInCell```: If no more potential values in a cell => Fix its value
 * ```scudoku.solvers.LastPotentialValueInBlockOrLineOrColumn```: If only one cell has a potential value (in a block/line/column) => Fix its value
 * ```scudoku.solvers.IfValueFixedToLineOrColumnInsideABlockThenDeleteFromRest```: If we know that one line/column of a block can contain an potential values => Delete it from the rest of the line/column in the other blocks

## Checkers
Checkers do consistency checks on the grid.
 * ```scudoku.checkers.CheckCellsForFixDuplicateValues```: If two or more cells (in a line/column/block) have the same fix value, then we are doomed.
 * ```scudoku.checkers.CheckCellsForNoMorePotentialValuesAvailable```: Any cell which is not fix but does not have any more potential values? Then we are also doomed.
 
###### (w) by [MartinLehmann1971](https://github.com/MartinLehmann1971) and [kristines](https://github.com/kristines) in September 2015