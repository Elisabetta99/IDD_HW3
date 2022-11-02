package main.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Scanner;

import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import main.fileWriter.WriterFile;
import main.strutturaTabelle.Parser;
import main.strutturaTabelle.Tabelle;
import main.mergeList.MergeList;
import main.statistics.Statistics;

public class Main {

	private static final int QUERY_NUMBER = 4; //QUERY_NUMBER: numero della colonna della query
	
	public static void main(String args[]) throws Exception {
		
		/*Apertura path e directory*/
		long start = System.currentTimeMillis();
		Path path = Paths.get("lucene-index");
		Directory directory = FSDirectory.open(path);
		WriterFile writerFile = new WriterFile();
		
		IndexWriterConfig config = new IndexWriterConfig();
	    config.setCodec(new SimpleTextCodec());
		IndexWriter writer = new IndexWriter(directory, config);
		
		/*Lettura file*/
		FileReader fileR = new FileReader("config.txt");
        BufferedReader bufferR = new BufferedReader(fileR);
        
        /*Parser*/
        LinkedList<String> lista = new LinkedList<String>();
        String s = bufferR.readLine();
        while (s != null) {
            lista.add(s);
            s = bufferR.readLine();
        } 
        fileR.close();
        bufferR.close();

        String confPath = lista.poll(); //Recupera e rimuove il primo elemento della lista
        
        System.out.println("Inizio parser.");
		Parser parser = new Parser(confPath, writer);
		parser.parse();
		System.out.println("Fine parser.");
		
		/*Stampa statistiche*/
		Scanner scanner = new Scanner(System.in);
		System.out.println("\nElaborare le statistiche? [y/n]");
		String sc = scanner.next();

        if(sc.equals("y") ) {
            Statistics stats = new Statistics(confPath);
            stats.calculateStats();
        } 
        scanner.close();
        
        writer.commit();
        writer.close();
		
        Tabelle table = parser.parserJsonQuery();
        MergeList mergeList = new MergeList();
        
		System.out.println("Esperimento con query: " + QUERY_NUMBER);			//chiave: documento, valore: numero di volte che matcha con la query
		writerFile.writeOnFileQuery(QUERY_NUMBER);
		
		mergeList.mergeList(table.getMappaColonne().get(QUERY_NUMBER), directory);

		directory.close();
		
		long elapsedTime = System.currentTimeMillis() - start; //nanoTime() - start;
		System.out.println("\nTempo impiegato per l'elaborazione: " + elapsedTime/(60*1000F) + " ms\n");
		
		writerFile.writeOnFileTime(elapsedTime);
		
		System.out.println("\nFine.");
	}	
}
