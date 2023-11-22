package org.example;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        CSVReader reader = new CSVReader("src/with-header.csv",";",true);
        reader.next();
        System.out.println(reader.getInt(0));
        System.out.println(reader.getInt("id"));

        System.out.println("siema");

        CSVReader testowy = new CSVReader("src/przyklad.csv", ",", false);
        testowy.next();
        System.out.println(testowy.isMissing(2));

        System.out.println(testowy.getTime());
        System.out.println(testowy.getLocalDate());
        System.out.println(testowy.getLocalDataTime());
        System.out.println(CSVReader.getDefaultParameters());

    }
}