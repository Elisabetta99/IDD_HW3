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


public class Statistics {

    private String inputFile;
    private BufferedReader bufferR;
    private FileReader fileR;

    /*Numero totale tabelle*/
    private int totTables = 0;

    /*Righe*/
    private int numRows = 0;

    /*Colonne*/
    private int numColumns = 0;    

    /*Valori nulli*/
    private int numNullValues = 0;


    public Statistics(String inputFile) throws IOException {
        this.inputFile = inputFile;
    }

    public void calculateStats() throws IOException {
        NumberOfRowsAndColumns(inputFile);
        NumberOfNullValues(inputFile);
        DistributionOfRowsAndColumns(inputFile);
        NumberOfDistinctValuesForColumn(inputFile);
    }

    private void NumberOfRowsAndColumns(String inputFile) throws IOException  {

        this.inputFile = inputFile;
        fileR = new FileReader(this.inputFile);
        bufferR = new BufferedReader(fileR);

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

        }

        System.out.println("\nNumero di tabelle:");
        System.out.println("Numero totale di tabelle = " + totTables);
        System.out.println("\n\nNumero medio di righe e colonne");
        System.out.println("Numero medio di righe= " + numRows/totTables);
        System.out.println("Numero medio di colonne= " + numColumns/totTables);


        try {
            bufferR.close();
            fileR.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        long end = System.currentTimeMillis();
        System.out.println("\n\nFine elaborazione. Tempo impiegato: " + (end - start) + "ms");

    }

    private void NumberOfNullValues(String inputFile) throws FileNotFoundException {

        this.inputFile = inputFile;
        fileR = new FileReader(this.inputFile);
        bufferR = new BufferedReader(fileR);

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

        }

        try {
            bufferR.close();
            fileR.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("\nValori medi nulli");
        System.out.println("Numero medio nulli = " + numNullValues/totTables);

        long end = System.currentTimeMillis();
        System.out.println("\n\nFine elaborazione. Tempo impiegato: " + (end - start) + "ms");
    }

    private void DistributionOfRowsAndColumns(String inputFile) throws FileNotFoundException {

        this.inputFile = inputFile;
        fileR = new FileReader(this.inputFile);
        bufferR = new BufferedReader(fileR);

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

    private void NumberOfDistinctValuesForColumn(String inputFile) throws FileNotFoundException {


        this.inputFile = inputFile;
        fileR = new FileReader(this.inputFile);
        bufferR = new BufferedReader(fileR);

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

            JsonElement element = JsonParser.parseString(s);
            JsonObject table = element.getAsJsonObject();
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