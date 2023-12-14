package admin_units;

import org.example.CSVReader;

import java.io.PrintStream;
import java.util.*;
import java.util.function.Predicate;

public class AdminUnitList {
    List<AdminUnit> units;
    public AdminUnitList(){
        this.units = new ArrayList<>();
    }
    public AdminUnitList(List<AdminUnit> list){
        //TODO sprawdzić czy to zakomendowane się sprawdzi
        //this.units = list; // czy na pewno to jest poprawnie??
        this.units = new ArrayList<>(list); // meaby ???
    }

    public void read(String filename) {
        CSVReader csvReader = new CSVReader(filename, ",");
        System.out.println(csvReader.getHeader());
        System.out.println(csvReader.columnLabelsToInt);
        Map<AdminUnit, Long> unitToParentIndex = new HashMap<>();
        Map<Long, AdminUnit> indexToUnit = new HashMap<>();

        while(csvReader.next()){
            BoundingBox box = new BoundingBox();
            for(int i = 7; i <16; i+=2){
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
        /*
        // mapping <West Pomeranian Voivodeship, 2>; we combine West Pomerania with 2. that unit with inexie 2 is the parent of West Pomerania
        // we also add a list recording <id, voivodeship>,
        // then for each unit we take the index of its parent, and look for the unit to which this index is assigned.
        // we don't take those who don't have parents.
         */

        units.forEach(unit -> unit.parent = indexToUnit.get(unitToParentIndex.get(unit))); // old version units = units.stream().peek(unit -> unit.parent = indexToUnit.get(unitToParentIndex.get(unit))).collect(Collectors.toList());
        // In two forEach, because first all parents must be completed, and only then can they be fixed

        // for each unit fix population and desity
        units.forEach(AdminUnit::fixMissingValues); //units = units.stream().peek().collect(Collectors.toList());

        // units.stream().filter(unit -> 0 == unit.density).forEach(System.out::println);
        // units.stream().filter(unit -> unit.parent == null).forEach(System.out::println);

        // dodawanie dzieci, przeszukujemy unitToParentIndex. wybieramy wszystkie klucze które jako wartość mają dany index.
        // dodajemy klucz (czyli nasz AdminUnit) do listy dzieci danego Unita.
        /*
        // adding children, we search unitToParentIndex. we select all keys that have a given index as a value.
        // we add the key (i.e. our AdminUnit) to the list of children of a given Unit.
         */
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
        /*
        AdminUnitList adminUnitList = new AdminUnitList;
        for(AdminUnit unit : units){
            String u = unit.name.toString();
            if(regex){
                if(u.matches(pattern)){
                    adminUnitList.units.add(unit);
                }
            } else{
                if(u.contains(pattern)){
                    adminUnitList.units.add(unit);
                }
            }
        }
        return adminUnitList;
        */

        return new AdminUnitList(units.stream()
                .filter(u -> regex ? u.name.matches(pattern) : u.name.contains(pattern))
                .toList());
    }
    public AdminUnitList selectByName(String pattern) {
        return selectByName(pattern, true);
    }

    // zaimplementowane jako drzewa -> pierwsza myśl, nie wyobrażałem zrobić tego brute force
    // implemented as trees -> first thought, I couldn't imagine doing it by brute force
    AdminUnitList getNeighbors(AdminUnit unit, double maxdistance){
        // przeszukujemy tylko po tych którzy nie mają rodzica -> czyli województwa wzajemnie.
        // we search only for those who do not have a parent -> i.e. each province.
        if(null == unit.parent){
            return new AdminUnitList(units.stream()
                    .filter(u -> u.parent == null && u.bbox.intersects(unit.bbox) && u.bbox.distanceTo(unit.bbox) != 0.0)
                    .toList()
            );
        }

        // 4 województwa, 6 powiaty i 7 gminy
        return new AdminUnitList(switch(unit.adminLevel){
            case 4, 6, 7 -> unit.parent.children.stream()
                    .filter(u -> u.bbox.intersects(unit.bbox)).toList();
            default -> unit.parent.children.stream()
                    // w takiej kolejności, bo u.bbox.distanceTo(unit.bbox) będzie równe 0.0 tylko w jednym przypadku. druga część wykona się dla każdego z sąsiadów + 1 czyli ten konkretny.
                    // in this order, because u.bbox.distance(unit.bbox) will be equal to 0.0 only in one case. the second part will be executed for each of the neighbors + 1, i.e. this specific one.
                    .filter(u -> u.bbox.distanceTo(unit.bbox) < maxdistance && u.bbox.distanceTo(unit.bbox) != 0.0)
                    .toList();
        });
    }

    public AdminUnitList sortInplaceByName(){
        /*
        units.sort(new Comparator<AdminUnit>() {
            @Override
            public int compare(AdminUnit o1, AdminUnit o2) {
                return o1.name.compareToIgnoreCase(o2.name);
            }
        });
        return this;
        */

        units.sort((o1, o2) -> o1.name.compareToIgnoreCase(o2.name));
        return this;
    }
    public AdminUnitList sortInplaceByArea(){
        /*
        units.sort((o1, o2) -> {
            if(o1.area == o2.area) return 0;
            return o1.area < o2.area ? -1 : 1;
        });
        return this;
        */
        units.sort(Comparator.comparingDouble(unit -> unit.area));
        return this;
    }

    public AdminUnitList sortInplaceByPopulation(){
        units.sort(Comparator.comparingDouble(unit -> unit.population));
        return this;
    }
    public AdminUnitList sortInplace(Comparator<AdminUnit> cmp){
        units.sort(cmp);
        return this;
    }

    public AdminUnitList sort(Comparator<AdminUnit> cmp){
        // Tworzy wyjściową listę
        // Kopiuje wszystkie jednostki
        // woła sortInPlace
        return new AdminUnitList(units).sortInplace(cmp);
    }

    AdminUnitList filter(Predicate<AdminUnit> pred) {
        return new AdminUnitList(units.stream().filter(pred).toList());

    }

    // Nie wiem czy tak może być ;) działa ? działa, z pewnością mało miejsca zajmuje.
    AdminUnitList filter(Predicate<AdminUnit> pred, int limit){
        return new AdminUnitList(units.stream().filter(pred).limit(limit).toList());
    }

    AdminUnitList filter(Predicate<AdminUnit> pred, int offset, int limit){
        return new AdminUnitList(units.stream().filter(pred).skip(offset).limit(limit).toList());
    }
}
