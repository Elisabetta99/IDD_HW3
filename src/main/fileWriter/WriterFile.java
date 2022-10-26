package fileWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class WriterFile {
	
	public void writeOnFileTime(long elapsedTime) {
		try {
			FileWriter myWriter = new FileWriter("Esperimenti.txt", true);
			myWriter.write("Tempo impiegato: " + elapsedTime/(60*1000F) + " minuti\n\n");
			myWriter.close();
			//System.out.println("Successfully wrote to the file.");
		}
		catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	
	
	public void writeOnFileQuery(int num) {
		try {
			FileWriter myWriter = new FileWriter("Esperimenti.txt", true);
			myWriter.write("Esperimento con query " + num + ", con k=2: \n");
			myWriter.close();
		}
		catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	
	
	public void writeOnFileMergedMap(Map<Integer,Integer> orderedSet2count) {
		try {
			FileWriter myWriter = new FileWriter("Esperimenti.txt", true);
			for (Integer i : orderedSet2count.keySet()) {
				myWriter.write(i + " -> " + orderedSet2count.get(i) + "\n");
			}
			myWriter.close();
		}
		catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
}
