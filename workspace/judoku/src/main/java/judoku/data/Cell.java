package judoku.data;

import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

import judoku.Debug;

public class Cell extends Observable {
	private OneToNine value = null;
	private Set<OneToNine> potentialValues = null;
	public final int id;

	// reference to the cell's line
	private Line line;
	// reference to the cell's block
	private Block block;
	// reference to the cell's column
	private Column column;

	public Cell(Grid grid, int id) {
		this.id=id;

		// initialize the cell with a list of potential values from 1..9
		potentialValues = new HashSet<OneToNine>();
		for (OneToNine o2n: OneToNine.values()) {
			potentialValues.add(o2n);
		}
	}

	// initialize the cells
	void parseCharCell(char c) {
		setValue(OneToNine.fromChar(c));
	}

	// -----------------------------------------------------------------------------------------------

	public Block getBlock() {
		return block;
	}
	public void setBlock(Block block) {
		this.block = block;
	}

	public Column getColumn() {
		return column;
	}
	public void setColumn(Column column) {
		this.column = column;
	}

	public Line getLine() {
		return line;
	}
	public void setLine(Line line) {
		this.line = line;
	}

	// -----------------------------------------------------------------------------------------------

	// get the cells value (if fixed)
	public OneToNine getValue() {
		return value;
	}

	// get the list of potential values (never null, but might be empty)
	public Set<OneToNine> getPotentialValues() {
		return potentialValues;
	}

	// set the list of potential values (never null, but might be empty)
	public void setPotentialValues(Set<OneToNine> potentialValues) {
		this.potentialValues = potentialValues;
	}

	// fix value and then notify all observers (ie line, block, column of the cell)
	public void setValue(OneToNine value) {
		if (value == null) return;
		
		if (Debug.FIXED_DEBUG) {
			System.out.println("Fix cell " + id + " to value "+ value);
		}
		this.value = value;
		potentialValues.clear();	// better than null to prevent NPEs elsewhere
		setChanged();
		notifyObservers();
	}

	// delete a value from the list of potential values
	public void deleteValue(OneToNine deleteValue) {
		if (isFix()) return;
		if (Debug.DELETE_DEBUG) {
			System.out.println("Delete value "+ deleteValue + " from cell " + id);
		}
		this.getPotentialValues().remove(deleteValue);
	}

	// -----------------------------------------------------------------------------------------------

	// is this cell fixed already?
	public boolean isFix() {
		return value != null;
	}

	// -----------------------------------------------------------------------------------------------
	
	public String toString() {
		StringBuffer buf = new StringBuffer();

		if (!Debug.CELL_DEBUG) {
			if (isFix()) {
				buf.append(value.toString());
			}
			else {
				buf.append(".");
			}
		}
		else {
			if (id<10) buf.append(" ");
			buf.append(" " + id);
			if (isFix()) {
				buf.append("[");
				buf.append("........");
				buf.append(value.toString());
				buf.append("]");
			}
			else {
				buf.append("{");
				int numBlanks = 9 - (potentialValues.size());
				for (int i=0; i<numBlanks; i++) buf.append(".");
				for (OneToNine o2n: potentialValues) {
					buf.append(o2n.toString());
				}
				buf.append("}");
			}
		}

		return buf.toString();
	}

	public boolean equals(Object o) {
		if (o==null) return false;
		if (o instanceof Cell) {
			Cell oAsCell = (Cell) o;
			return (this.id == oAsCell.id);
		}
		return false;
	}

	public int hashCode () {
		return id;
	}
}
