package scudoku

object Debug {
  // show cell contents in Debug mode - include id and potential values
  var CELL_DEBUG = true
  // show grid in Debug mode - show lines, cols, blocks separate
  var GRID_DEBUG = false
  // show notifications in Debug mode
  var NOTIFY_DEBUG = false
  // show fixing of cells in Debug mode
  var FIXED_DEBUG = true
  // show solving algorithms in Debug mode
  var SOLVING_DEBUG = true
  // show deletion of values in Debug mode
  var DELETE_DEBUG = false  
  
  def debug(msg: String) = {
    println(msg)
  }
}