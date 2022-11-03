package main.strutturaTabelle;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class Parser {

    private FileReader f;
    private BufferedReader b;
    private String s;
    private IndexWriter writer;
    private int count;
    
    ObjectMapper objectMapper;

    public Parser(String file, IndexWriter writer) throws IOException {
        this.f = new FileReader(file);
        this.b = new BufferedReader(f);
        this.s = new String();
        this.writer = writer;
        this.objectMapper = new ObjectMapper();
    }

    public void parse() throws IOException {

        this.count=0;

        /*Qui aumento il conto ad ogni iterazione e poi lo inserisco nell'indice*/
        for(this.count = 0;; this.count++) {
            s = b.readLine();
            if(s == null) {
                break;
            }

            JsonElement jsonTree = JsonParser.parseString(s);
            JsonObject table = jsonTree.getAsJsonObject();

            Document doc = new Document();
            doc.add(new TextField("index", String.valueOf(this.count), Field.Store.YES));

            JsonArray cells = table.getAsJsonArray("cells"); //Prendo dall'oggetto Json offerto dalla libreria le celle
            int length = cells.size();
            for (int j = 0; j < length; j++) {
                JsonObject jsonobject = cells.get(j).getAsJsonObject(); //Con un for ottengo tutti gli oggetti delle celle
                if (jsonobject.get("isHeader").getAsBoolean() == false){
                    String cell = jsonobject.get("cleanedText").getAsString();
                    if (cell.equals("") == false) //Se la cella non è vuota aggiungila al contenuto
                        doc.add(new StringField("contenuto", cell, Field.Store.NO));
                }
            }

            writer.addDocument(doc);
            if (this.count % 100000 == 0) {
                System.out.println("Documento indicizzato # " + this.count);
                writer.commit();
            }
        }
        System.out.println("Documento indicizzato # " + this.count);
        b.close();
        f.close();
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