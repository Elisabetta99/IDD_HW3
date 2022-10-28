package main.main;

import java.nio.file.Path;

import java.nio.file.Paths;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import main.strutturaTabelle.*;

public class ParserBeforeMain {
	
	public static void main(String args[]) throws Exception {
	    
	    /*Apertura path e directory*/
		Path path = Paths.get("lucene-index");
		Directory directory = FSDirectory.open(path);

		Parser parser = new Parser();
		parser.parserJsonTables(directory);
		
		System.out.println("Finito il parser.");
	}
}