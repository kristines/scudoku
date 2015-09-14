package scudoku.solvers

/**
 * Java2Scala port from judoku.solvers.ISolvingAlgorithm
 */

import scudoku.data.Grid

// TODO Naming Conventions, e.g. I Interface  - T Trait?
trait TSolvingAlgorithm {
  // returns true if solved
  def tryToSolve(grid: Grid): Boolean;
}