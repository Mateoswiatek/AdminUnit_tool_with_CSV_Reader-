package admin_units;

import org.example.CSVReader;

import java.util.ArrayList;
import java.util.List;

public class AdminUnitList {
    List<AdminUnit> adminUnitList = new ArrayList<>();

    /**
     * Czyta rekordy pliku i dodaje do listy
     * @param filename nazwa pliku
     */

    public void read(String filename) {
        CSVReader csvReader = new CSVReader(filename);
        System.out.println(csvReader.getHeader());
        /*while(csvReader.next()){
            adminUnitList.add(new AdminUnit(
                    csvReader.getInt("id"),

            ));
        }*/

    }
}
