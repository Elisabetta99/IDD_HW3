package main.mergeList;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

import main.fileWriter.WriterFile;
import main.strutturaTabelle.Celle;

public class MergeList {

	public void mergeList(List<Celle> queryColumn, Directory directory) throws Exception {

		WriterFile writerFile = new WriterFile();
		IndexReader reader = DirectoryReader.open(directory); //obtain read access to the inverted indexes
		IndexSearcher searcher = new IndexSearcher(reader); 
		
		Map<Integer,Integer> set2count = new TreeMap<Integer,Integer>();		//chiave: documento																	//valore: numero di volte che matcha con la query

		for(Celle cell : queryColumn) {
			String text = cell.getCleanedText().toLowerCase();
			Query query = new TermQuery(new Term("cella", text));
			TopDocs hits = searcher.search(query, reader.numDocs()); //search for all documents that match the query

			for (int i = 0; i < hits.scoreDocs.length; i++) { 
				ScoreDoc scoreDoc = hits.scoreDocs[i]; 								//hits.scoreDocs[i] indica il documento 
																					//hits.scoreDocs documenti che matchano con la query 
				//Document doc = searcher.doc(scoreDoc.doc); //fetch returned document		//scoreDoc.doc è la posizione del documento
																							//doc è il documento
				if(set2count.containsKey(scoreDoc.doc)) {
					int count = set2count.get(scoreDoc.doc) + 1;
					set2count.put(scoreDoc.doc, count);
				}
				else { 
					set2count.put(scoreDoc.doc, 1);
				}
			}
		}		

		
		Map<Integer,Integer> orderedSet2count = sortByValue(set2count);
		
		for (Integer i : orderedSet2count.keySet()) {
			System.out.println(i + " -> " + orderedSet2count.get(i));
		}
		System.out.println("size: " + orderedSet2count.size() + "\n");
		
		writerFile.writeOnFileMergedMap(orderedSet2count);
		
	}
	
	public Map<Integer,Integer> sortByValue(Map<Integer,Integer> set2count) {
		// Create a list from elements of HashMap
		List<Map.Entry<Integer, Integer> > list = new LinkedList<Map.Entry<Integer, Integer> >(set2count.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<Integer, Integer> >() {
			public int compare(Map.Entry<Integer, Integer> o1,
					Map.Entry<Integer, Integer> o2)
			{
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		//select top k value
		list = list.subList(0, 2);
		
		// put data from sorted list to hashmap
		HashMap<Integer, Integer> orderedSet2count = new LinkedHashMap<Integer, Integer>();
		for (Map.Entry<Integer, Integer> element : list) {
			orderedSet2count.put(element.getKey(), element.getValue());
		}
		return orderedSet2count;
	}
}
