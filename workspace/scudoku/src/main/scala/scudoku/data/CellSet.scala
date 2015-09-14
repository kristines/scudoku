package scudoku.data

/**
 * Java2Scala port from judoku.data.AbstractCellSet
 */

import java.util.Observer
import java.util.Observable
import UnknownOrOneToNine._
import scudoku.Debug

// https://stackoverflow.com/questions/1991042/what-is-the-advantage-of-using-abstract-classes-instead-of-traits
abstract class CellSet(val name: String) extends Observer {
  private val cells = scala.collection.mutable.MutableList[Cell]()    // is a list to preserve the order (in debug outputs)
  
  override def update(o: Observable, arg: Any) = {
    o match {
      case o:Cell => deletePotentialValue(o.value)
    }
  }
  
  def deletePotentialValue(valueToDelete: UnknownOrOneToNine) = {
    for (cell <- cells){
      cell.deletePotentialValue(valueToDelete)
      
      if (Debug.NOTIFY_DEBUG) {
        val buf = new StringBuffer();
        buf.append("Notify by cell, id=" + cell.id + " for " + name);
        Debug.debug(buf.toString());
      }
    }
  }
  
  def addCell(cell: Cell) = {
    cells+=(cell)
    cell.addObserver(this)
  }
  
  def contains(cell: Cell): Boolean = cells.contains(cell)  

  def getCells: Traversable[Cell] = cells
  
  def getCell(id: Int): Cell = {
    for (cell <- cells) {
      if (cell.id == id) return cell
    }
    
    throw new RuntimeException("wrong id " + id + " searched") 
  }
}