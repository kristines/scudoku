package judoku.data;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import judoku.Debug;

public class Grid implements Observer {
	public static final int NINE = 9;
	
	// the lines of the grid
	private Line[]   lines   = new Line[NINE];
	// the columns of the grid
    private Column[] columns = new Column[NINE];
    // the blocks of the grid
    private Block[]  blocks  = new Block[NINE];
    // all cells in the grid
    private Cell[]   cells   = new Cell[NINE*NINE];

    // number of cells which are fix already
    private int numOfCellsFixed = 0;

    // helper map and cell id array
    private HashMap<Integer, Cell> cellsByID = new HashMap<Integer, Cell>();
    private int[][] ids =	// id's for the cell position, needed to construct the grid
	    { { 0, 1, 2,  9,10,11, 18,19,20},
	      { 3, 4, 5, 12,13,14, 21,22,23},
	      { 6, 7, 8, 15,16,17, 24,25,26},
	      {27,28,29, 36,37,38, 45,46,47},
	      {30,31,32, 39,40,41, 48,49,50},
	      {33,34,35, 42,43,44, 51,52,53},
	      {54,55,56, 63,64,65, 72,73,74},
	      {57,58,59, 66,67,68, 75,76,77},
	      {60,61,62, 69,70,71, 78,79,80} };

    public Grid() {
		// construct the lines, columns and blocks
        for (int l=0; l<lines.length;   l++)   lines[l] = new   Line(l);
        for (int c=0; c<columns.length; c++) columns[c] = new Column(c);
        for (int b=0; b<blocks.length;  b++)  blocks[b] = new  Block(b);

        // create cells and link them to the correct block, line and col and also keep them in the map
        for (int i=0; i<cells.length; i++) {
            Cell cell = new Cell(this, i);
            int id = cell.id;
            cells[i] = cell;

            // add cell to its block
            blocks[id/NINE].addCell(cell);  
            cell.setBlock(blocks[id/NINE]);
            
            // add cell to my map
            if (cellsByID.get(id) != null) throw new RuntimeException("cell duplicate found for id: " + id); // paranoia
            cellsByID.put(id, cells[i]);

            // set grid as observer to cell's changes
            cell.addObserver(this);
        }

        // finally set cell's line and column
        for (int idsi=0; idsi<ids.length; idsi++) {
            for (int idsj=0; idsj<ids[idsi].length; idsj++) {
                int id = ids[idsi][idsj];
                Cell cell = cellsByID.get(id);

                lines[idsi].addCell(cell);  	
                cell.setLine(lines[idsi]);
                columns [idsj].addCell(cell);  	
                cell.setColumn(columns [idsj]);
            }
        }
    }

    // parse a given "Stringified" grid
    public void parseStringGrid(String sGrid) {
        if (sGrid.length() != NINE*NINE) throw new RuntimeException("grid size is not 9x9");

        // iterate over the stringified grid and set all values
        int counter=0;
        for (int idsi=0; idsi<ids.length; idsi++) {
            for (int idsj=0; idsj<ids[idsi].length; idsj++) {
                int id = ids[idsi][idsj];
                Cell cell = cellsByID.get(id);
                cell.parseCharCell(sGrid.charAt(counter++));
            }
        }
    }

	// -----------------------------------------------------------------------------------------------

    public Block[] getBlocks() {
		return blocks;
	}
	public Column[] getColumns() {
		return columns;
	}
	public Line[] getLines() {
		return lines;
	}
    public Cell[] getCells() {
        return cells;
    }

	// -----------------------------------------------------------------------------------------------

    // grid is observer to *all* cells so we get the number of fixed cells for free
    @Override
    public void update (Observable obs, Object obj) {
        if (obs== null) return;
        Cell oAsCell = (Cell) obs;
        if (oAsCell.getValue() != null) {
            numOfCellsFixed++;
        }
    }

	// -----------------------------------------------------------------------------------------------

    // are we done?
    public boolean isSolved() {
        return (numOfCellsFixed == NINE*NINE);
    }

    // get the number of cells fixed
    public int numOfCellsFixed() {
        return this.numOfCellsFixed;
    }

	// -----------------------------------------------------------------------------------------------

    public String toString() {
        StringBuffer buf = new StringBuffer();

        int i=0;

        for (Line l: lines) {
            if (Debug.GRID_DEBUG) {
                buf.append("Line " + ((i<10)? " ": "")+ (++i)+ ": ");
            }

            buf.append(l.toString());
            buf.append("\n");
        }

        if (Debug.GRID_DEBUG) {
            i=0;
            for (Block b: blocks) {
                buf.append("Block " + ((i<10)? " ": "")+ (++i) + ":\n");
                buf.append(b.toString());
                buf.append("\n");
            }

            i=0;
            for (Column c: columns) {
                buf.append("Column " + ((i<10)? " ": "")+ (++i) + ":\n");
                buf.append(c.toString());
                buf.append("\n");
            }
        }
        return buf.toString();
   }

   public Object clone() {
	   Grid clone = new Grid();

	   // iterate over all cells of the original grid
	   for (Cell orgCell: this.getCells()) {
		   // find the corresponding cell in the clone grid
		   Cell cloneCell = clone.cellsByID.get(orgCell.id);

		   // if the original cell is fixed
		   if (orgCell.isFix()) {
			   // ... simply set the value
			   cloneCell.setValue(orgCell.getValue());
		   }
		   else {
			   // ... else iterate over all possible values and delete in the clone
			   // whatever cannot be found in the original cell
			   Set<OneToNine> orgPotentialValues = orgCell.getPotentialValues();
			   for (OneToNine o2n: OneToNine.values()) {
				   if (!orgPotentialValues.contains(o2n)) {
					   cloneCell.deletePotentialValue(o2n);
				   }
			   }
		   }
	   }

	   return clone;
   }
}