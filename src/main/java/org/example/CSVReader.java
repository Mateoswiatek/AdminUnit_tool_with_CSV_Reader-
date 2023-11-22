package org.example;

import java.io.*;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FileReader czyta znaki,
 * BufferedReader czyta linię.
 * isMissing -> co w tedy robić, split na przecinku, numer kolumny, czy nie jest pusty
 * Sposob generowania czasu, w jakim typie są czasy.
 * własne wyjątki mozna
 *
 * Domyślna konfiguracja - nie zamienia wartości których nie ma
 * Jeśli user nie wybrał zamieniania brakujących wartości:
 *      - Liczbowe - Exceptions
 *      - String - pusty string "" cos tam
 *
 */
public class CSVReader {
    static final boolean DEFAULT_REPLACING = false;
    static final int DEFAULT_NUMBER = 0;
    static final String DEFAULT_STRING = "";
    static final String DEFAULT_DELIMITER = ";";
    static final boolean DEFAULT_HEADER = true;
    static final String DEFAULT_TIME_FORMATTER = "HH:mm:ss.SSS";
    static final String DEFAULT_DATA_FORMATTER = "yyyy-MM-dd";
    static final String DEFAULT_DATA_TIME_FORMATTER = "yyyy-MM-dd HH:mm:ss.SSS";
    static final String DEFAULT_CHARSET_NAME = "UTF-8";

    String timeFormatter = DEFAULT_TIME_FORMATTER;
    String dataFormatter = DEFAULT_DATA_FORMATTER;
    String dataTimeFormatter = DEFAULT_DATA_TIME_FORMATTER;
    String charset_name = DEFAULT_CHARSET_NAME;

    BufferedReader reader;
    String delimiter; // rozdzielacz
    String[] current;
    List<String[]> records = new ArrayList<>();
    boolean hasHeader;
    boolean replacing;
    int default_number;
    String default_string;
    List<String> columnLabels = new ArrayList<>();
    Map<String,Integer> columnLabelsToInt = new HashMap<>();

    /**
     * @param filename - nazwa pliku
     * @param delimiter - separator pól
     * @param hasHeader - czy plik ma wiersz nagłówkowy
     * @param replacing - czy Reader ma zamieniać brakujące pola (Wartości liczbowe) na zadaną wartość
     * @param default_number - Wartość na jaką będziemy zamieniać brakujące pola Liczbowe
     * @param default_string - Wartość na jaką będziemy zamieniać brakujące pola String.
     * @throws IOException
     */

    public CSVReader(String filename, String delimiter, boolean hasHeader, boolean replacing, int default_number, String default_string) throws IOException {
        reader = new BufferedReader(new FileReader(filename, Charset.forName(charset_name)));
        this.delimiter = delimiter;
        this.hasHeader = hasHeader;
        if(hasHeader) parseHeader();
        this.replacing = replacing;
        this.default_number = default_number;
        this.default_string = default_string;
    }
    public CSVReader(String filename,String delimiter,boolean hasHeader) throws IOException {
        this(filename, delimiter, hasHeader, DEFAULT_REPLACING, DEFAULT_NUMBER, DEFAULT_STRING);
    }

    public CSVReader(String filename) throws IOException {
        this(filename, DEFAULT_DELIMITER, DEFAULT_HEADER);
    }

    public CSVReader(Reader reader, String delimiter, boolean hasHeader, boolean replacing, int default_number, String default_string) throws IOException {
        this.reader = new BufferedReader(reader);
        this.delimiter = delimiter;
        this.hasHeader = hasHeader;
        if(hasHeader) parseHeader();
        this.replacing = replacing;
        this.default_number = default_number;
        this.default_string = default_string;
    }
    public CSVReader(Reader reader, String delimiter, boolean hasHeader) throws IOException {
        this(reader, delimiter, hasHeader, DEFAULT_REPLACING, DEFAULT_NUMBER, DEFAULT_STRING);
    }

    void parseHeader() throws IOException {
        // wczytaj wiersz
        String line  = reader.readLine();
        if(line==null){
            return;
        }
        // podziel na pola
        String []header = line.split(delimiter);
        // przetwarzaj dane w wierszu
        for(int i=0;i<header.length;i++){
            String str = header[i];
            columnLabels.add(str);
            columnLabelsToInt.put(str, i);
        }
    }

    /**
     * Metoda zapisuje koeljną linię do current oraz dodaje ją do records
     * @return czy udało się odczytać
     * @throws IOException
     */
    boolean next() throws IOException {
        String line = reader.readLine();
        if(null == line) return false;
        current = line.split(delimiter);
        records.add(current);
        return true;
    }

    /**
     * Zwraca w postaci inta wartość która jest pod podanym indeksam / nazwie kolumny.
     * @param o Integer / String
     * @return IntegerValueFromCol
     */
    public int getInt(Object o){
        if(isMissing(o) && replacing) return default_number;

        return switch(o){
            case Integer i -> Integer.parseInt(current[i]);
            case String s -> Integer.parseInt(current[columnLabelsToInt.get(s)]);
            default -> throw new IllegalStateException("Unexpected value: " + o);
        };
    }

    /**
     * Zwraca w postaci doubla wartość która jest pod podanym indeksam / nazwie kolumny.
     * @param o Integer / String
     * @return DoubleValueFromCol
     */
    public double getDouble(Object o){
        if(isMissing(o) && replacing) return default_number;

        return switch(o){
            case Integer i -> Double.parseDouble(current[i]);
            case String s -> Double.parseDouble(current[columnLabelsToInt.get(s)]);
            default -> throw new IllegalStateException("Unexpected value: " + o);
        };
    }

    /**
     * Zwraca w postaci inta wartość która jest pod podanym indeksam / nazwie kolumny.
     * @param o Integer / String
     * @return
     */
    public String getString(Object o){
        if(isMissing(o)) return default_string;

        return switch(o){
            case Integer i -> current[i];
            case String s -> current[columnLabelsToInt.get(s)];
            default -> throw new IllegalStateException("Unexpected value: " + o);
        };
    }

    /**
     * Zwraca w postaci longa wartość która jest pod podanym indeksam / nazwie kolumny.
     * @param o Integer / String
     * @return
     */
    public long getLong(Object o){
        if(isMissing(o) && replacing) return default_number;

        return switch(o){
            case Integer i -> Long.parseLong(current[i]);
            case String s -> Long.parseLong(current[columnLabelsToInt.get(s)]);
            default -> throw new IllegalStateException("Unexpected value: " + o);
        };
    }

    /**
     * Zwraca czy brakuje wartości w podanym numerze/nazwie kolumny.
     * @param o Integer / String
     * @return
     */
    boolean isMissing(Object o){

        int index = switch(o){
            case Integer i -> i;
            case String s -> columnLabelsToInt.get(s);
            default -> throw new IllegalStateException("Unexpected value: " + o);
        };

        boolean x;
        try{
            x = current[index].isBlank(); // isBlank - zwraca true jesli sa tylko biale znaki.
        } catch(ArrayIndexOutOfBoundsException e){
            x = false;
            System.out.println("Poza zakresem ale działa e=" + e);
        }
        return x;
    }

    /**
     *
     * @return Nazwy kolumn
     */
    List<String> getColumnLabels(){
        return columnLabels;
    }

    /**
     *
     * @return Długość aktualnego rekordu
     */
    int getRecordLength(){
        return current.length;
    }

    /**
     * Czas w przekazanym formacie
     * @param format format w jakim ma się wyświetlić czas
     * @return String
     */
    public String getTime(String format){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalTime.now().format(formatter); // .truncatedTo(java.time.temporal.ChronoUnit.SECONDS)
    }

    /**
     * Czas w domyślnym formacie
     * @return String
     * @see timeFormatter
     */
    public String getTime(){
        return getTime(timeFormatter);
    }

    /**
     * Data w określonym formacie
     * @param format format zapisu
     * @return String
     */
    public String getLocalDate(String format){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.now().format(formatter);
    }

    /**
     * Data w domyślnym formacie
     * @return String
     * @see dataFormatter
     */
    public String getLocalDate(){
        return getLocalDate(dataFormatter);
    }

    /**
     * Data i Czas w przekazanym formacie
     * @param format Daty i Czasu
     * @return String
     */
    public String getLocalDataTime(String format){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.now().format(formatter);
    }
    /**
     * Data i Czas w domyślnym formacie
     * @return String
     * @see dataTimeFormatter
     */
    public String getLocalDataTime(){
        return getLocalDataTime(dataTimeFormatter);
    }


    public void setTimeFormatter(String dataFormater){
        this.dataFormatter = dataFormater;
    }

    public void setDataFormater(String dataFormater) {
        this.dataFormatter = dataFormater;
    }

    public void setDataTimeFormater(String dataTimeFormater) {
        this.dataTimeFormatter = dataTimeFormater;
    }

    public void setCharsetName(String charset) {
        this.charset_name = charset;
    }




    /*
    public <*> getPam(){
        // rzutowanie
    }
    */

    public Object getParsedValue(String valueToParse) {
        try {
            return Integer.parseInt(valueToParse);
        } catch (NumberFormatException e1) {
            try {
                return Double.parseDouble(valueToParse);
            } catch (NumberFormatException e2) {
                try{
                    return Boolean.parseBoolean(valueToParse);
                } catch (NumberFormatException e3){
                    return valueToParse; // Zwracanie Stringa, jeśli nie udało się zrzutować
                }
            }
        }
    }

}
