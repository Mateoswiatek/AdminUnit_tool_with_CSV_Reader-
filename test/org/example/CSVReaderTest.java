package org.example;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class CSVReaderTest {
    @Test
    void constructors() throws IOException {
        assertThrowsExactly(FileNotFoundException.class, () -> new CSVReader("tekst", "_", true, true, 2, ""));
        try {
            String name = "src/with-header.csv";
            String delimiter = ";";
            boolean hasHeader = true;
            boolean replacing = false;
            CSVReader csvReader2 = new CSVReader(name, delimiter, false, true, 2, "aaa");
            assertInstanceOf(BufferedReader.class, csvReader2.reader);
            assertEquals(delimiter, csvReader2.delimiter);
            assertEquals(false, csvReader2.hasHeader);
            assertEquals(true, csvReader2.replacing);
            assertEquals(2, csvReader2.default_number);
            assertEquals("aaa", csvReader2.default_string);

            CSVReader csvReader1 = new CSVReader(name, delimiter, true);
            assertInstanceOf(BufferedReader.class, csvReader1.reader);
            assertEquals(delimiter, csvReader1.delimiter);
            assertEquals(hasHeader, csvReader1.hasHeader);
            assertEquals(replacing, csvReader1.replacing);
            assertEquals(0, csvReader1.default_number);
            assertEquals("", csvReader1.default_string);

            String csvString = "ala ma kota,12,,,,4,,";
            CSVReader csvReader3 = new CSVReader(new StringReader(csvString), delimiter, hasHeader);
            assertInstanceOf(BufferedReader.class, csvReader1.reader);
            assertEquals(delimiter, csvReader3.delimiter);
            assertEquals(hasHeader, csvReader3.hasHeader);
            assertEquals(replacing, csvReader3.replacing);
            assertEquals(0, csvReader3.default_number);
            assertEquals("", csvReader3.default_string);

        } catch(Exception e){
            fail("Wyrzucilo bledy kiedy nie powinno: " + e);
        }
    }

    @Test
    void parseHeader() {
    }

    @Test
    void next() {
    }

    @Test
    void getInt() {
    }

    @Test
    void getDouble() {
    }

    @Test
    void getString() {
    }

    @Test
    void getLong() {
    }

    @Test
    void isMissing() {
    }

    @Test
    void getColumnLabels() {
    }

    @Test
    void getRecordLength() {
    }

    @Test
    void getParsedValue() {
    }
}