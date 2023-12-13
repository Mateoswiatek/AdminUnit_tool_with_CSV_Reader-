package admin_units;

import org.example.CSVReader;

import java.io.PrintStream;
import java.util.*;

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
        System.out.println(csvReader.columnLabelsToInt);
        Map<AdminUnit, Long> unitToParentIndex = new HashMap<>();
        Map<Long, AdminUnit> indexToUnit = new HashMap<>();

        while(csvReader.next()){
            BoundingBox box = new BoundingBox();
            for(int i = 7; i <16; i+=2){
//                System.out.println(i);
//                System.out.println(i+1);
                box.addPoint(csvReader.getDouble(i), csvReader.getDouble(i+1));
            }

            AdminUnit unit = new AdminUnit(
                    csvReader.getLong("id"),
                    csvReader.getString("name"),
                    csvReader.getInt("admin_level"),
                    csvReader.getDouble("population"),
                    csvReader.getDouble("area"),
                    csvReader.getDouble("density"),
                    box);

            unitToParentIndex.put(unit, csvReader.getLong("parent"));
            indexToUnit.put(unit.id, unit);
            units.add(unit);
        }

        // mapowanie <Zachodniopomorskie, 2>; łączymy zachodniopomorsie z 2. że unit o inexie 2 jest rodzicem zachodniopomorskiego
        // dodajemy rówineż listę zapisującą <id, wojewodztwo>,
        // następnie dla każdego unitu bierzemy index jego rodzica, i szukamy unita do którego ten index jest przypisany.
        // nie bierzemy tych którzy nie mają rodziców.

        units.forEach(unit -> unit.parent = indexToUnit.get(unitToParentIndex.get(unit))); // old version units = units.stream().peek(unit -> unit.parent = indexToUnit.get(unitToParentIndex.get(unit))).collect(Collectors.toList());
        // Na dwa, bo najpierw wszyscy rodzice muszą być uzupełnieni, a dopiero potem mozna fixowac
        // for each unit fix population and desity

        units.forEach(AdminUnit::fixMissingValues); //units = units.stream().peek().collect(Collectors.toList());

        units.stream().filter(unit -> 0 == unit.density).forEach(System.out::println); // units.stream().filter(unit -> unit.parent == null).forEach(System.out::println);

        // dodawanie dzieci, przeszukujemy unitToParentIndex. wybieramy wszystkie klucze które jako wartość mają dany index.
        // dodajemy klucz (czyli nasz AdminUnit) do listy dzieci danego Unita.
        units.forEach(unit -> {
            for (Map.Entry<AdminUnit, Long> entry : unitToParentIndex.entrySet()) {
                if (entry.getValue().equals(unit.id)) {
                    unit.children.add(entry.getKey());
                }
            }
        });
    }
    public void list(PrintStream out){
        units.forEach(out::println);
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
