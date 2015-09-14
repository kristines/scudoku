package scudoku.data

/**
 * Java2Scala port from judoku.data.Line
 */

//TODO rename to Row
class Line extends CellSet("line") {
  override def toString = {
      val buf: StringBuffer = new StringBuffer();
      for (c <- getCells)  buf.append(c)
      buf.toString
  }
}