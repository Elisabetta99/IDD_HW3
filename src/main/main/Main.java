package main;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import fileWriter.WriterFile;
import jsonparser.Parser;
import jsonparser.Table;
import mergeList.MergeList;

public class Main {

	private static final int QUERY_NUMBER = 4;
	
	public static void main(String args[]) throws Exception {
		
		WriterFile writerFile = new WriterFile();
		Path path = Paths.get("lucene-index");
		Directory directory = FSDirectory.open(path);
		
		Parser parser = new Parser();
		Table table = parser.parserJsonQuery();
		
		MergeList mergeList = new MergeList();
		long start = System.currentTimeMillis();
		System.out.println("Esperimento con query: " + QUERY_NUMBER);			//chiave: documento
																				//valore: numero di volte che matcha con la query
		writerFile.writeOnFileQuery(QUERY_NUMBER);
		mergeList.mergeList(table.getMappaColonne().get(QUERY_NUMBER), directory);
		
		long elapsedTime = System.currentTimeMillis() - start; //nanoTime() - start;
		System.out.println("Tempo impiegato: " + elapsedTime/(60*1000F) + " minuti\n");
		
		writerFile.writeOnFileTime(elapsedTime);
		
		System.out.println("Fine");
	}	
}
