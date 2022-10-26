package strutturaTabelle;

import java.io.FileInputStream;
import java.util.Scanner;

import org.apache.lucene.store.Directory;

import com.fasterxml.jackson.databind.ObjectMapper;

import mergeList.InvertedIndex;

public class Parser {

	ObjectMapper objectMapper;
	
	public Parser() {
		super();
		this.objectMapper = new ObjectMapper();
	}
	
	public void parserJsonTables(Directory directory) throws Exception{

		InvertedIndex invertedIndex = new InvertedIndex(directory);
		invertedIndex.getWriter().deleteAll();

		FileInputStream fis = new FileInputStream("tables.txt");       
		Scanner sc = new Scanner(fis);    //file to be scanned  

		//returns true if there is another line to read  
		while(sc.hasNextLine()) {  
			String line = sc.nextLine();

			// Deserialization into the `Table` class
			Tabelle table = objectMapper.readValue(line, Tabelle.class);
			table.createCells();
			invertedIndex.indexing(table);
		}  
		sc.close(); 
	}

	
	public Tabelle parserJsonQuery() throws Exception {

		FileInputStream fis = new FileInputStream("tabellaPerQuery.txt");       
		Scanner sc = new Scanner(fis);    //file to be scanned  
		String line = sc.nextLine();
		Tabelle table = objectMapper.readValue(line, Tabelle.class);
		table.createCells();
		sc.close();
		
		return table;
		
		
		/* per leggere più tabelle
		//returns true if there is another line to read  
		while(sc.hasNextLine()) {  
			String line = sc.nextLine();

			// Deserialization into the `Table` class
			Table table = objectMapper.readValue(line, Table.class);
			table.createCells();
		}  
		sc.close(); */
	}
}
