package scudoku.data

/**
 * Java2Scala port from judoku.data.Column
 */

class Column extends CellSet("column") {
  override def toString = {
      val buf: StringBuffer = new StringBuffer();
      for (c <- getCells) {
        buf.append(c)
        buf.append("\n")
      }

      buf.toString
  }
}