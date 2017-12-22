package item2;

public class Variable {
	private String base;
	private String offset;
	private String size;
	
	public Variable(String base, String offset, String size){
		this.base = base;
		this.offset = offset;
		this.size = size;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
}