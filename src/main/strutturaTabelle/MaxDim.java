package main.strutturaTabelle;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MaxDim {

	@JsonProperty("row")
	private int row;
	
	@JsonProperty("column")
	private int column;

	public MaxDim() {
		
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	@Override
	public String toString() {
		return "MaxDimension [row=" + row + ", column=" + column + "]";
	}
}
