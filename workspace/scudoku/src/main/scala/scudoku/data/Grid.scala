package scudoku.data

/**
 * Java2Scala port from judoku.data.Grid
 */

import java.util.Observer
import java.util.Observable
import UnknownOrOneToNine._
import scudoku.Debug

class Grid extends Observer {
  private val NINE = 9
  
  // the lines of the grid
  val lines: Array[Line]     = new Array[Line](NINE)
  // the columns of the grid
  val columns: Array[Column] = new Array[Column](NINE)
  // the blocks of the grid
  val blocks: Array[Block]   = new Array[Block](NINE)
  // all cells of the grid
  val cells: Array[Cell]     = new Array[Cell](NINE*NINE)
  
  // number of cells which are fix already
  private var _numOfCellsFixed: Int = 0
  
  // helper cell id array
  private val ids = Array(
        Array( 0, 1, 2,  9,10,11, 18,19,20),
        Array( 3, 4, 5, 12,13,14, 21,22,23),
        Array( 6, 7, 8, 15,16,17, 24,25,26),
        Array(27,28,29, 36,37,38, 45,46,47),
        Array(30,31,32, 39,40,41, 48,49,50),
        Array(33,34,35, 42,43,44, 51,52,53),
        Array(54,55,56, 63,64,65, 72,73,74),
        Array(57,58,59, 66,67,68, 75,76,77),
        Array(60,61,62, 69,70,71, 78,79,80)
  )
  
  private val id2LineAndColumnMap = scala.collection.mutable.Map[Int, (Line, Column)]()
  
  // Constructor
  {
      // construct the lines, columns and blocks
      for (l <- 0 to lines.length-1)   lines(l)   = new Line()
      for (c <- 0 to columns.length-1) columns(c) = new Column()
      for (b <- 0 to blocks.length-1)  blocks(b)  = new Block()
      
      // create cells and link them to the correct block, line and col and also keep them in the map
      for (idsi <- 0 to ids.length-1) {
        for (idsj <- 0 to ids(idsi).length-1) {
          val id: Int = ids(idsi)(idsj)
          
          val myline: Line = lines(idsi)
          val mycolumn: Column = columns(idsj)
          
          id2LineAndColumnMap += (id -> (myline,mycolumn))
        }
      }
      
      for (id <- 0 to cells.length-1) {
        val block: Block = blocks(id/NINE)
        val line: Line = id2LineAndColumnMap(id)._1
        val column: Column = id2LineAndColumnMap(id)._2

        val cell: Cell = new Cell(this, block, line, column, id)
        cells(id) = cell
        
        block.addCell(cell)
        line.addCell(cell)
        column.addCell(cell)
        
        // set grid as observer to cell's changes
        cell.addObserver(this)
      }
  }
  
  // parse a given "Stringified" grid
  def parseStringGrid(sGrid: String) = {
    if (sGrid.length != NINE*NINE) throw new RuntimeException("grid size is not 9x9");
    var stringIndex: Int = 0
    for (idsi <- 0 to ids.length-1) {
      for (idsj <- 0 to ids(idsi).length-1) {
        val id: Int = ids(idsi)(idsj)
        val block: Block = blocks(id/NINE)
        
        val cell: Cell = block.getCell(id)

        val value = UnknownOrOneToNine.fromChar(sGrid.charAt(stringIndex))
        if (value != UnknownOrOneToNine.UNKNOWN) cell.value_=(value)

        stringIndex = stringIndex+1
      }
    }
  }

  // grid is observer to *all* cells so we get the number of fixed cells for free
  override def update(o: Observable, arg: Any) = {
    o match {
      case c: Cell if c.value != UNKNOWN => {
        _numOfCellsFixed = _numOfCellsFixed+1
      }
    }
  }
  
  // are we done?
  def isSolved = _numOfCellsFixed == NINE*NINE
  
  // get the number of cells fixed
  def numOfCellsFixed = _numOfCellsFixed

  def getAllCellSets(): List[CellSet] = {
    blocks.toList ::: lines.toList ::: columns.toList 
  }
  
  // ---------------------------------------------------------------------------

  override def toString: String = {
    val buf = new StringBuffer()
    
    var i: Int = 0
    for (l <- lines) {
      if (Debug.GRID_DEBUG) {
          buf.append("Line " + (if (i<10) " " else "") + i + ": ");
      }
      
      buf.append(l.toString());
      buf.append("\n");
      i = i+1
    }

    if (Debug.GRID_DEBUG) {
        i=0;
        for (b <- blocks) {
            buf.append("Block " + (if (i<10) " " else "") + i + ":\n");
            buf.append(b.toString());
            buf.append("\n");
            i = i+1
        }

        i=0;
        for (c <- columns) {
            buf.append("Column " + (if (i<10) " " else "") + i + ":\n");
            buf.append(c.toString());
            buf.append("\n");
            i = i+1
        }
    }
    
    buf.toString()
  }
}