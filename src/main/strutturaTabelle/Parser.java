package main.strutturaTabelle;

import java.io.FileInputStream;
import java.util.Scanner;

import org.apache.lucene.store.Directory;

import com.fasterxml.jackson.databind.ObjectMapper;

import main.mergeList.InvertedIndex;

public class Parser {

	ObjectMapper objectMapper;
	
	public Parser() {
		super();
		this.objectMapper = new ObjectMapper();
	}
	
	/*Effettua il parser e crea l'indice per ogni tabella*/
	public void parserJsonTables(Directory directory) throws Exception{

		InvertedIndex invertedIndex = new InvertedIndex(directory);
		invertedIndex.getWriter().deleteAll();

		//FileInputStream fis = new FileInputStream("tabelleProva.txt");     
		FileInputStream fis = new FileInputStream("tables.txt");
		
		Scanner sc = new Scanner(fis);    //file to be scanned  

		while(sc.hasNextLine()) {  
			String line = sc.nextLine();
			Tabelle table = objectMapper.readValue(line, Tabelle.class);
			table.createCells();
			invertedIndex.indexing(table);
		}  
		sc.close(); 
	}

	public Tabelle parserJsonQuery() throws Exception {

		FileInputStream fis = new FileInputStream("tabellaPerQuery.txt");       
		Scanner sc = new Scanner(fis);    //file da scansionare  
		String line = sc.nextLine();
		Tabelle table = objectMapper.readValue(line, Tabelle.class);
		table.createCells();
		sc.close();
		
		return table;
	}
}
