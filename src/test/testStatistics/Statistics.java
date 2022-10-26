package test.testStatistics;


import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import main.strutturaTabelle.*;
import main.mergeList.InvertedIndex;

public class Statistics {
	
	private int totTables = 0;
	private int numColumns = 0;
	private int totColumns = 0;
	private int avgColumns = 0;
	
	private int numRows = 0;
	private int totRows = 0;
	private int avgRows = 0;
	
	private int numNullValues = 0;
	private int totNullValues = 0;
	private int avgNullValues = 0;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	private Path path = Paths.get("lucene-index");
	
	
	private Map<Integer,Integer> distrNumColumns = new HashMap<Integer,Integer>(); 	//chiave: numero di colonne
																					//valore: quante tabelle hanno quel numero di colonne
	private Map<Integer,Integer> distrNumRows = new HashMap<Integer,Integer>();		//chiave: numero di righe
																					//valore: quante tabelle hanno quel numero di righe
	private Map<Integer,Integer> distrDistValues4Columns = new HashMap<Integer,Integer>();  //chiave: numero valori distinti
																							//valore: quante colonne hanno quel numero di valori distinti
	private Map<Integer,Integer> distrDistValues4Tables = new HashMap<Integer,Integer>();	//chiave: numero valori distinti
																							//valore: quante tabelle hanno quel numero di valori distinti
	
	@Before
	public void setUp() throws IOException {
		Directory directory = FSDirectory.open(path);
		InvertedIndex invertedIndex = new InvertedIndex(directory);
		invertedIndex.getWriter().deleteAll();
	}
	
	
	@Test
	public void parserJsonTablesStatistics() throws IOException{
		
		FileInputStream fis = new FileInputStream("tables.txt");       
		Scanner sc = new Scanner(fis);    //file to be scanned  

		//returns true if there is another line to read  
		while(sc.hasNextLine()) {  
			totTables = totTables + 1;  					//numero di tabelle totali
			
			String line = sc.nextLine();

			// Deserialization into the `Table` class
			Tabelle table = objectMapper.readValue(line, Tabelle.class);
			table.createCells();
			
			//COLONNE
			numColumns = table.getMappaColonne().size();				//numero di colonne di una tabella
			totColumns = totColumns + numColumns;						//numero di colonne totali
			
			if(distrNumColumns.containsKey((Integer)numColumns)) {		//distribuzione del numero di colonne 
				distrNumColumns.put((Integer)numColumns, distrNumColumns.get((Integer)numColumns) + 1);
			}
			else {
				distrNumColumns.put((Integer)numColumns, 1);
			}
			
			
			//RIGHE
			numRows = table.getMappaColonne().get(0).size();		//conto il numero di righe di una tabella
			totRows = totRows + numRows;							//conto il numero di righe totali
			
			if(distrNumRows.containsKey((Integer)numRows)) {		//distribuzione del numero di righe 
				distrNumRows.put((Integer)numRows, distrNumRows.get((Integer)numRows) + 1);
			}
			else {
				distrNumRows.put((Integer)numRows, 1);
			}
			
			
			//VALORI NULLI E DISTINTI
			Set<String> distValues4TableSet = new HashSet<>();
			numNullValues = 0;
			for(int i : table.getMappaColonne().keySet()) {
				Set<String> distValues4ColumnSet = new HashSet<>(); 
				List<Celle> column = table.getMappaColonne().get(i);
				for(Celle c : column) {
					if(c.getCleanedText().equals("")) {
						numNullValues = numNullValues + 1;		//numero di valori nulli in ogni tabella
					}
					else {										//valori distinti
						distValues4ColumnSet.add(c.getCleanedText());
						distValues4TableSet.add(c.getCleanedText());
					}
				}
	
				int num = distValues4ColumnSet.size();  						//chiave di distrDistValues4Columns
				if(distrDistValues4Columns.containsKey((Integer)num)) {			//distribuzione del numero di valori distinti per colonna 
					distrDistValues4Columns.put((Integer)num, distrDistValues4Columns.get((Integer)num) + 1);
				}
				else {
					distrDistValues4Columns.put((Integer)num, 1);
				} 
			}
			
			totNullValues = totNullValues + numNullValues;		//numero di valori nulli in tutte le tabelle
			
			int num1 = distValues4TableSet.size();  						//chiave di distrDistValues4Tables
			if(distrDistValues4Tables.containsKey((Integer)num1)) {		//distribuzione del numero di valori distinti per colonna 
				distrDistValues4Tables.put((Integer)num1, distrDistValues4Tables.get((Integer)num1) + 1);
			}
			else {
				distrDistValues4Tables.put((Integer)num1, 1);
			} 
		}  
		
		sc.close(); 
		
		
		//medie
		avgColumns = totColumns/totTables;  		//numero medio di colonne
		avgRows = totRows/totTables;				//numero medio di righe
		avgNullValues = totNullValues/totTables;	//numero medio di valori nulli
	
		/*for (Integer i : distrDistValues4Columns.keySet()) {
			System.out.println(i + " -> " + distrDistValues4Columns.get(i));
		}
		System.out.println("size: " + distrDistValues4Columns.size() + "\n");
		
		
		for (Integer i : distrDistValues4Tables.keySet()) {
			System.out.println(i + " -> " + distrDistValues4Tables.get(i));
		}
		System.out.println("size: " + distrDistValues4Tables.size() + "\n");
		*/
		writeOnFile();
	}
	
	public void writeOnFile() {
		try {
			FileWriter myWriter = new FileWriter("statistics.txt");
			myWriter.write("Numero di tabelle: " + totTables + "\n");
			myWriter.write("Numero totale di colonne: " + totColumns + "\n");
			myWriter.write("Numero medio di colonne: " + avgColumns + "\n\n");
			myWriter.write("Numero totale di righe: " + totRows + "\n");
			myWriter.write("Numero medio di righe: " + avgRows + "\n\n");
			myWriter.write("Numero totale di valori nulli per tabella: " + totNullValues + "\n");
			myWriter.write("Numero medio di valori nulli per tabella: " + avgNullValues + "\n\n");
			
			//per scrivere mappe su file
			myWriter.write("Distribuzione numero di colonne (quante tabelle hanno 1, 2, 3, 4, etc. colonne) " + "\n");
			myWriter.write("[Chiave: numero di colonne, Valore: quante tabelle hanno quel numero di colonne] " + "\n");
			for (Integer i : distrNumColumns.keySet()) {
				myWriter.write(i + " -> " + distrNumColumns.get(i) + "\n");
			}
			myWriter.write("\n");
			
			myWriter.write("Distribuzione numero di righe (quante tabelle hanno 1, 2, 3, 4, etc. righe) " + "\n");
			myWriter.write("[Chiave: numero di righe, Valore: quante tabelle hanno quel numero di righe] " + "\n");
			for (Integer i : distrNumRows.keySet()) {
				myWriter.write(i + " -> " + distrNumRows.get(i) + "\n");
			}
			myWriter.write("\n");
			
			myWriter.write("Distribuzione di valori distinti (quante colonne hanno 1, 2, 3, 4, etc. valori distinti) " + "\n");
			myWriter.write("[Chiave: numero di valori distinti, Valore: quante colonne hanno quel numero di valori distinti su tutte le tabelle]" + "\n");
			for (Integer i : distrDistValues4Columns.keySet()) {
				myWriter.write(i + " -> " + distrDistValues4Columns.get(i) + "\n");
			}
			myWriter.write("\n");
			
			myWriter.write("Distribuzione di valori distinti (quante tabelle hanno 1, 2, 3, 4, etc. valori distinti) " + "\n");
			myWriter.write("[Chiave: numero di valori distinti, Valore: quante tabelle hanno quel numero di valori distinti]" + "\n");
			for (Integer i : distrDistValues4Tables.keySet()) {
				myWriter.write(i + " -> " + distrDistValues4Tables.get(i) + "\n");
			}
			myWriter.close();
			//System.out.println("Successfully wrote to the file.");
		}
		catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
}
