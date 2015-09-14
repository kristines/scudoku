package scudoku.data

/**
 * Java2Scala port from judoku.data.Block
 */

class Block extends CellSet("block") {
  override def toString = {
      val buf: StringBuffer = new StringBuffer();
      var i: Int = 0
      for (c <- getCells) {
        buf.append(c)
        i = i+1
        if (i%3==0) buf.append("\n")
      }

      buf.toString
  }
}