package judoku.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import judoku.Debug;

public abstract class AbstractCellSet implements Observer {
	protected List<Cell> cells = new ArrayList<Cell>();
	protected final int id;

	protected AbstractCellSet(int id) {
		this.id=id;
	}

	protected abstract String getName();
	
	// observer to cell fix
//ported	
	@Override
	public void update (Observable obs, Object obj) {
		if (obs== null) return;
		Cell oAsCell = (Cell) obs;
		OneToNine valueToDelete = oAsCell.getValue();
		deletePotentialValue(valueToDelete);
	}

	// delete a value from all potential values in all cells of the cell set
//ported	
	private void deletePotentialValue(OneToNine valueToDelete) {
		for (Cell cell: cells) {
			cell.deletePotentialValue(valueToDelete);

			if (Debug.NOTIFY_DEBUG) {
				StringBuffer buf = new StringBuffer();
				buf.append("Notify by cell, id=" + cell.id + " for " + getName() + ", id=" + id);
				System.out.println(buf.toString());
			}
		}
	}

	// add a cell to the set
//ported	
	public void addCell(Cell cell) {
		cells.add(cell);
		cell.addObserver(this);
	}

	// does this cell set contain the cell c?
//ported	
	public boolean contains(Cell c) {
		return getCells().contains(c);
	}

	// get all cells in this set
//ported	
	public List<Cell> getCells() {
		return cells;
	}
}
