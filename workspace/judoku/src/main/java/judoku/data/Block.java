package judoku.data;

public class Block extends AbstractCellSet {
	public Block(int id) {
		super(id);
	}

	protected String getName() {
		return "block";
	}

	public String toString() {
        StringBuffer buf = new StringBuffer();
        int i=0;
    	for (Cell c: this.getCells()) {
    		buf.append(c);
    		i++;
    		if (i%3==0) buf.append("\n");
    	}

        return buf.toString();
   }
}
