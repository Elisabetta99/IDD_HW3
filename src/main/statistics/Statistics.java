package main.statistics;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import java.util.function.BiConsumer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class Statistics extends Thread {

    private String inputFile;
    
    private NumberOfRowsAndColumns rowsAndColumns;
    private NumberOfNullValues nullValues;
    private DistributionOfRowsAndColumns distribution;
    private NumberOfDistinctValuesForColumn distinctValues;

    public Statistics(String inputFile) throws IOException {
        this.inputFile = inputFile;
        this.rowsAndColumns = new NumberOfRowsAndColumns(inputFile);
        this.nullValues = new NumberOfNullValues(inputFile);
        this.distribution = new DistributionOfRowsAndColumns(inputFile);
        this.distinctValues = new NumberOfDistinctValuesForColumn(inputFile);
    }

    public void calculateStats() throws IOException {
        new Thread(rowsAndColumns).start();
        new Thread(nullValues).start();
        new Thread(distribution).start();
        new Thread(distinctValues).start();
    }
    
    static class NumberOfRowsAndColumns implements Runnable {
        private String inputFile;
        private BufferedReader bufferR;
        private FileReader fileR;
        
        /*Numero totale tabelle*/
        private int totTables = 0;
        
        /*Righe*/
        private int numRows = 0;
        private int avgRows = 0;
        
        /*Colonne*/
        private int numColumns = 0;    
        private int avgColumns = 0;
        
        public NumberOfRowsAndColumns(String inputFile) throws FileNotFoundException {
            this.inputFile = inputFile;
            fileR = new FileReader(this.inputFile);
            bufferR = new BufferedReader(fileR);
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            String s = new String();

            for (totTables = 0;; totTables++) {
                try {
                    s = bufferR.readLine();
                    if (s == null) {
                        break;
                    }

                    JsonElement element = JsonParser.parseString(s);
                    JsonObject table = element.getAsJsonObject();
                    JsonObject dimensioni = table.getAsJsonObject("maxDimensions");

                    int righe = dimensioni.get("row").getAsInt();
                    int colonne = dimensioni.get("column").getAsInt();

                    numRows = numRows + righe;
                    numColumns = numColumns + colonne;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /*Medie*/
                avgRows = numRows/totTables;
                avgColumns = numColumns/totTables;
            }
            
            System.out.println("\nNumero di tabelle:");
            System.out.println("Numero totale di tabelle = " + totTables);
            System.out.println("\n\nNumero medio di righe e colonne");
            System.out.println("Numero medio di righe= " + avgRows);
            System.out.println("Numero medio di colonne= " + avgColumns);

            
            try {
                bufferR.close();
                fileR.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
           
            
            long end = System.currentTimeMillis();
            System.out.println("\n\nFine elaborazione. Tempo impiegato: " + (end - start) + "ms");
        }   
    }

    static class NumberOfNullValues implements Runnable{

        private String inputFile;
        private BufferedReader bufferR;
        private FileReader fileR;
        
        /*Numero totale tabelle*/
        private int totTables = 0;
        
        /*Valori nulli*/
        private int numNullValues = 0;
        private int avgNullValues = 0;

        public NumberOfNullValues(String inputFile) throws FileNotFoundException {
            this.inputFile = inputFile;
            fileR = new FileReader(this.inputFile);
            bufferR = new BufferedReader(fileR);
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            String s = new String();

            for (totTables = 0;; totTables++) {
                try {
                    s = bufferR.readLine();
                    if (s == null)
                        break;

                    JsonElement element = JsonParser.parseString(s);
                    JsonObject table = element.getAsJsonObject();
                    JsonArray cells = table.getAsJsonArray("cells");

                    for (int j = 0; j < cells.size(); j++) {
                        if (cells.get(j).getAsJsonObject().get("cleanedText").getAsString().equals("")) {
                            numNullValues++;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                avgNullValues = numNullValues/totTables;
            }
            
            try {
                bufferR.close();
                fileR.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            
            System.out.println("\nValori medi nulli");
            System.out.println("Numero medio nulli = " + avgNullValues);

            long end = System.currentTimeMillis();
            System.out.println("\n\nFine elaborazione. Tempo impiegato: " + (end - start) + "ms");
        }
    }

    static class DistributionOfRowsAndColumns implements Runnable{

        private String inputFile;
        private BufferedReader bufferR;
        private FileReader fileR;
        
        /*Numero totale tabelle*/
        private int totTables = 0;

        public DistributionOfRowsAndColumns(String inputFile) throws FileNotFoundException {
            this.inputFile = inputFile;
            fileR = new FileReader(this.inputFile);
            bufferR = new BufferedReader(fileR);
        }
        
        @Override
        public void run() {
            long start = System.currentTimeMillis();
            String s = new String();

            HashMap<Integer, Integer> distRow = new HashMap<Integer, Integer>();
            HashMap<Integer, Integer> distCol = new HashMap<Integer, Integer>();

            
            for (totTables = 0;; totTables++) {
                try {
                    s = bufferR.readLine();
                    if (s == null) {
                        break;
                    }

                    JsonElement element = JsonParser.parseString(s);
                    JsonObject table = element.getAsJsonObject();
                    JsonObject dimensioni = table.getAsJsonObject("maxDimensions");

                    int righe = dimensioni.get("row").getAsInt();
                    int colonne = dimensioni.get("column").getAsInt();

                    if (distRow.containsKey(righe)) {
                        distRow.put(righe, distRow.get(righe) + 1);
                    } else {
                        distRow.put(righe, 1);
                    }
                        
                    if (distCol.containsKey(colonne)) {
                        distCol.put(colonne, distCol.get(colonne) + 1);
                    } else {
                        distCol.put(colonne, 1);
                    }
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("\nDistribuzione numero di righe");
            distRow.forEach(new BiConsumer<Integer, Integer>() {
                @Override
                public void accept(Integer k, Integer v) {
                    System.out.println(v + " tabelle hanno " + k + " righe.");
                }
            });
            
            System.out.println("\nDistribuzione numero di colonne");
            distCol.forEach(new BiConsumer<Integer, Integer>() {
                @Override
                public void accept(Integer k, Integer v) {
                    System.out.println(v + " tabelle hanno " + k + " colonne.");
                }
            });

            try {
                bufferR.close();
                fileR.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            long end = System.currentTimeMillis();
            System.out.println("\n\nFine elaborazione. Tempo impiegato: " + (end - start) + "ms");
        } 
    }

    static class NumberOfDistinctValuesForColumn implements Runnable{

        private String inputFile;
        private BufferedReader bufferR;
        private FileReader fileR;

        /*Numero totale tabelle*/
        private int totTables = 0;
        
        public NumberOfDistinctValuesForColumn(String inputFile) throws FileNotFoundException {
            this.inputFile = inputFile;
            fileR = new FileReader(this.inputFile);
            bufferR = new BufferedReader(fileR);
        }
        
        @Override
        public void run() {
            long start = System.currentTimeMillis();
            String s = new String();

            final HashMap<Integer, Integer> distinct = new HashMap<Integer, Integer>();

            for (totTables = 0;; totTables++) {
                try {
                    s = bufferR.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (s == null) {
                    break;
                }

                JsonElement jsonTree = JsonParser.parseString(s);
                JsonObject table = jsonTree.getAsJsonObject();
                JsonArray cells = table.getAsJsonArray("cells");

                int colonne = table.getAsJsonObject("maxDimensions").get("column").getAsInt();

                HashMap<Integer, HashSet<String>> contaColonne = new HashMap<Integer, HashSet<String>>();
                for (int j = 0; j <= colonne; j++) {
                    contaColonne.put(j, new HashSet<String>());
                }

                int length = cells.size();
                for (int j = 0; j < length; j++) {
                    JsonObject cell = cells.get(j).getAsJsonObject();
                    int colonna = cell.getAsJsonObject("Coordinates").get("column").getAsInt();
                    
                    if (!cell.get("isHeader").getAsBoolean() && !cell.get("cleanedText").getAsString().equals("")) {
                        contaColonne.get(colonna).add(cell.get("cleanedText").getAsString());
                    }
                }
                contaColonne.forEach(new BiConsumer<Integer, HashSet<String>>() {
                    @Override
                    public void accept(Integer k, HashSet<String> v) {
                        int size = v.size();
                        if (size != 0) {
                            if (distinct.containsKey(size))
                                distinct.put(size, distinct.get(size) + 1);
                            else
                                distinct.put(size, 1);
                        }
                    }
                });
            }
            try {
                bufferR.close();
                fileR.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            System.out.println("\nDistribuzione numero di valori differenti per colonna");
            
            distinct.forEach(new BiConsumer<Integer, Integer>() {
                @Override
                public void accept(Integer k, Integer v) {
                    System.out.println(v + " colonne hanno " + k + " valori distinti.");
                }
            });
        
            long end = System.currentTimeMillis();
            System.out.println("\n\nFine elaborazione. Tempo impiegato: " + (end - start) + "ms");
        } 
    }
}