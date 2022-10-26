package main.strutturaTabelle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({"className", "Rows", "innerHTML", "type"})

public class Celle {

	@JsonProperty("isHeader")
	private boolean isHeader;
	
	@JsonProperty("Coordinates")
	private Coordinate coordinates;
	
	@JsonProperty("cleanedText")
	private String cleanedText;
	
	public Celle() {
	}
	
	public Celle(boolean isHeader, Coordinate coordinates, String text) {
		super();
		this.isHeader = isHeader;
		this.coordinates = coordinates;
		this.cleanedText = text;
	}

	public boolean isHeader() {
		return isHeader;
	}

	public void setHeader(boolean isHeader) {
		this.isHeader = isHeader;
	}

	public Coordinate getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Coordinate coordinates) {
		this.coordinates = coordinates;
	}

	public String getCleanedText() {
		return cleanedText;
	}

	public void setCleanedText(String cleanedText) {
		this.cleanedText = cleanedText;
	}

	@Override
	public String toString() {
		return "Cell [isHeader=" + isHeader + ", coordinates=" + coordinates + ", cleanedText=" + cleanedText + "] " + "\n";
	}
}
