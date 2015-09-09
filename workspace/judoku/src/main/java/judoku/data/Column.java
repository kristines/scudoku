package judoku.data;

public class Column extends AbstractCellSet {
	public Column(int id) {
		super(id);
	}

	protected String getName() {
		return "column";
	}

	public String toString() {
        StringBuffer buf = new StringBuffer();
    	for (Cell c: this.getCells()) {
    		buf.append(c);
    		buf.append("\n");
    	}

        return buf.toString();
   }
}
