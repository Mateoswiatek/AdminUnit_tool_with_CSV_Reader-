package admin_units;

import org.example.CSVReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminUnitList {
    List<AdminUnit> units = new ArrayList<>();

    /**
     * Czyta rekordy pliku i dodaje do listy
     * @param filename nazwa pliku
     */

    public void read(String filename) {
        CSVReader csvReader = new CSVReader(filename, ",");
        System.out.println(csvReader.getHeader());


        while(csvReader.next()){
            List<Double> lx = new ArrayList<>();
            List<Double> ly = new ArrayList<>();
            for(int i = 1; i<=5; i++){ // 1 to 4
                lx.add(csvReader.getDouble("x" + i));
            }
            for(int i = 1; i<=5; i++){
                ly.add(csvReader.getDouble("x" + i));
            }
            units.add(new AdminUnit(
                    csvReader.getString("name"),
                    csvReader.getInt("admin_level"),
                    csvReader.getDouble("population"),
                    csvReader.getDouble("area"),
                    csvReader.getDouble("density"),
                    new BoundingBox(
                            Collections.min(lx),
                            Collections.min(ly),
                            Collections.max(lx),
                            Collections.max(ly)
                    )
            ));
        }
        //System.out.println(units);


    }
}
