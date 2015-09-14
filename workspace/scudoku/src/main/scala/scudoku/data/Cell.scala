package scudoku.data

/**
 * Java2Scala port from judoku.data.Cell
 */

import UnknownOrOneToNine._
import java.util.Observable
import scudoku.Debug

// TODO implement Observable as in https://stackoverflow.com/questions/13435151/implementing-observer-pattern
class Cell (val grid: Grid, val block: Block, val line: Line, val column: Column, val id: Int) extends Observable { 
  //TODO can we implement as a case class? (we get equals/hashCode/toString for free)
    
  private var _value: UnknownOrOneToNine = UNKNOWN
  
  // get the cell's value (if fixed)
  def value = _value 

  // TODO override did not work - had to rename cellValue to _cellValue 
  // fix value and then notify all observers (ie line, block, column of the cell)
  def value_=(newValue: UnknownOrOneToNine): Unit = {  
    _value = newValue
    for (potentialValue <- potentialValues) potentialValues-=(potentialValue)
    
    if (Debug.FIXED_DEBUG) {
      Debug.debug("Fix cell " + id + " to value " + value);
    }
    
    // Observable
    setChanged
    notifyObservers
  }
  
  // list of potential values (never null, but might be empty)
  val potentialValues = scala.collection.mutable.Set[UnknownOrOneToNine](ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE)
    
  // delete a value from the list of potential values
  def deletePotentialValue(valueToDelete: UnknownOrOneToNine) {
    potentialValues-=(valueToDelete)
    
    if (Debug.DELETE_DEBUG) {
       Debug.debug("Delete value "+ valueToDelete + " from cell " + id); 
    }
  }
  
  // is this cell fixed already?
  def isFix: Boolean = _value != UNKNOWN 
  
  // checks if cell and otherCell have the same fix value (and are different objects)
  def isDuplicate(otherCell: Cell): Boolean = {
    this != otherCell && 
      isFix && otherCell.isFix && 
      value==otherCell.value
  }
    
  // --------------------------------------------------------

  override def equals(o: Any): Boolean = {
    o match {
      case o: Cell => this.id == o.id
      case _ => false
    }
  }
  override def hashCode: Int = id
  
  // --------------------------------------------------------
  
  override def toString: String = {
    val buf: java.lang.StringBuffer = new StringBuffer()
    
    if (! Debug.CELL_DEBUG) {
      if (isFix) {
        buf.append(value.toString());
      }
      else {
        buf.append(".");
      }
     }
     else {
       if (id<10) buf.append(" ")
       buf.append(id)
       if (isFix) {
          buf.append("[");
          buf.append("________");
          buf.append(_value);
          buf.append("] ");
        }
        else {
          buf.append("{");
          val numBlanks = 9 - potentialValues.size
          for (i <- 0 to numBlanks-1) buf.append(".");
          for (o2n <- potentialValues) {
            buf.append(o2n);
          }
          buf.append("} ");
        }    
    }
    buf.toString()
  }
}