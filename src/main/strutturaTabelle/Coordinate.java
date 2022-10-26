package main.strutturaTabelle;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Coordinate {

	@JsonProperty("row")
	private int row;
	
	@JsonProperty("column")
	private int column;
	
	public Coordinate() {	
	}
	
	public Coordinate(int row, int column) {
		this.row = row;
		this.column = column;		
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	@Override
	public String toString() {
		return "Coordinates [row=" + row + ", column=" + column + "]";
	}
}
