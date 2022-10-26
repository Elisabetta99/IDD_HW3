package main.fileWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class WriterFile {
	
	public void writeOnFileTime(long elapsedTime) {
		try {
			FileWriter myWriter = new FileWriter("provaTemp.txt", true);
			myWriter.write("Tempo impiegato: " + elapsedTime/(60*1000F) + " minuti\n\n");
			myWriter.close();
		}
		catch (IOException e) {
			System.out.println("Errore!");
			e.printStackTrace();
		}
	}
	
	
	public void writeOnFileQuery(int numRows) {
		try {
			FileWriter myWriter = new FileWriter("prova.txt", true);
			myWriter.write("Prova: " + " query con " + numRows + ", e k=2: \n");
			myWriter.close();
		}
		catch (IOException e) {
			System.out.println("Errore!");
			e.printStackTrace();
		}
	}
	
	
	public void writeOnFileMergedMap(Map<Integer,Integer> orderedSet2count) {
		try {
			FileWriter myWriter = new FileWriter("prova.txt", true);
			for (Integer i : orderedSet2count.keySet()) {
				myWriter.write(i + " = " + orderedSet2count.get(i) + "\n");
			}
			myWriter.close(); 
		}
		catch (IOException e) {
			System.out.println("Errore!");
			e.printStackTrace();
		}
	}
}
