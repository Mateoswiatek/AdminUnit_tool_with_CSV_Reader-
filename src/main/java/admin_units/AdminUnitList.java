package admin_units;

import org.example.CSVReader;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;


public class AdminUnitList {
    List<AdminUnit> units;
    public AdminUnitList(List<AdminUnit> units){
        this.units = units;
    }
    public AdminUnitList(){
        this.units = new ArrayList<>();
    }

    /**
     * Czyta rekordy pliku i dodaje do listy
     * @param filename nazwa pliku
     */

    public void read(String filename) {
        CSVReader csvReader = new CSVReader(filename, ",");
        System.out.println(csvReader.getHeader());
        Map<AdminUnit, Long> uniToParentIndex = new HashMap<>();
        Map<Long, AdminUnit> indexToUnit = new HashMap<>();

        while(csvReader.next()){
            List<Double> lx = new ArrayList<>();
            List<Double> ly = new ArrayList<>();
            for(int i = 1; i<=5; i++){ // 1 to 4
                lx.add(csvReader.getDouble("x" + i));
            }
            for(int i = 1; i<=5; i++){
                ly.add(csvReader.getDouble("x" + i));
            }
            AdminUnit unit = new AdminUnit(
                    csvReader.getLong("id"),
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
                    ));

            uniToParentIndex.put(unit, csvReader.getLong("parent"));
            indexToUnit.put(unit.id, unit);
            units.add(unit);
        }
//        System.out.println("rozmiar: " + units.size());
        // mapowanie <Zachodniopomorskie, 2>; łączymy zachodniopomorsie z 2. że unit o inexie 2 jest rodzicem zachodniopomorskiego
        // dodajemy rówineż listę zapisującą <id, wojewodztwo>,
        // następnie dla każdego unitu bierzemy index jego rodzica, i szukamy unita do którego ten index jest przypisany.
        // nie bierzemy tych którzy nie mają rodziców.

        units = units.stream()
                .peek(unit -> unit.parent = indexToUnit.get(uniToParentIndex.get(unit)))
                .collect(Collectors.toList());
//        System.out.println("rozmiar 2 : " + units.size());
    }
    public void list(PrintStream out){
        units.stream()
                .forEach(out::println);
    }

    public void list(PrintStream out,int offset, int limit ){
        units.stream()
                .skip(offset)
                .limit(limit)
                .forEach(out::println);
    }
    public void list(int limit){
        list(System.out, 0, limit);
    }
    public AdminUnitList selectByName(String pattern, boolean regex) {
        List<AdminUnit> units1 = units.stream()
                .filter(unit -> {
                    String u = unit.toString();
                    return regex ? u.matches(pattern) : u.contains(pattern);
                })
                .toList();
        return new AdminUnitList(units1);
    }
    public AdminUnitList selectByName(String pattern) {
        return selectByName(pattern, false);
    }
}
