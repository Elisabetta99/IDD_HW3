package main.strutturaTabelle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/*selezione dei campi della tabella da ignorare nel file json*/
@JsonIgnoreProperties({"_id", "className", "id", "beginIndex", "endIndex", "referenceContext", "type", "classe", "headersCleaned", "keyColumn"})

public class Tabelle {

	@JsonProperty("cells") //collezione non ordinata delle celle della tabella
	private Collection<Celle> collectionCells;

	@JsonProperty("maxDimensions") //massima dimensione della tabella
	private MaxDim maxDimension;

	private Map<Integer, List<Celle>> mappaColonne; //mappa delle colonne della tabella. chiave: numero della colonna, valore: lista delle celle della colonna

	public Tabelle() {
		this.mappaColonne = new HashMap<>();
	}

	//input: collezione di celle
	//output: mappa di colonne
	public void createCells() {
		List<Celle> temp = null;
		for(Celle c : this.collectionCells) {
			if(!(c.isHeader())) {
				temp = this.mappaColonne.get(c.getCoordinates().getColumn());
				if(temp == null) 
					temp = new ArrayList<Celle>();
				temp.add(c);
				this.mappaColonne.put(c.getCoordinates().getColumn(), temp);
			}
		}
	}

	public Collection<Celle> getCollectionCells() {
		return collectionCells;
	}

	public void setCollectionCells(Collection<Celle> collectionCells) {
		this.collectionCells = collectionCells;
	}

	public MaxDim getMaxDimension() {
		return maxDimension;
	}

	public void setMaxDimension(MaxDim maxDimension) {
		this.maxDimension = maxDimension;
	}

	public Map<Integer, List<Celle>> getMappaColonne() {
		return mappaColonne;
	}

	public void setMappaColonne(Map<Integer, List<Celle>> mappaColonne) {
		this.mappaColonne = mappaColonne;
	}

	@Override
	public String toString() {
		return "Table [collectionCells=" + collectionCells + ", maxDimension=" + maxDimension + ", mappaColonne="
				+ mappaColonne + "]";
	}

}

