package scudoku.checkers

/**
 * Java2Scala port from judoku.checkers.IConsistencyCheck
 */

import scudoku.data.Grid

// TODO Naming Conventions, e.g. I Interface  - T Trait?
trait TConsistencyCheck {
  def checkConsistency(grid: Grid): Boolean
}