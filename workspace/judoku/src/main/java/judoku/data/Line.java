package judoku.data;

public class Line extends AbstractCellSet {
	public Line(int id) {
		super(id);
	}

	protected String getName() {
		return "line";
	}
	
	public String toString() {
        StringBuffer buf = new StringBuffer();
    	for (Cell c: this.getCells()) {
    		buf.append(c);
    	}

        return buf.toString();
   }
}
