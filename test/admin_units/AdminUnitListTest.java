package admin_units;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;
import java.util.Comparator;

class AdminUnitListTest {
    AdminUnitList adminUnitList = new AdminUnitList();
    PrintStream out = System.out;

    @BeforeEach
    @Test
    void init(){
        adminUnitList.read("src/admin-units.csv");
    }

    @Test
    void read() {
        adminUnitList.read("src/admin-units.csv");
        System.out.println(adminUnitList.units.size());
        adminUnitList.list(out);
    }

    @Test
    void list() {
        adminUnitList.list(out, 2, 2);
    }

    @Test
    void testListPrintStream() {
        adminUnitList.list(out);
    }

    @Test
    void testListLimit() {
        adminUnitList.list(200);
    }

    @Test
    void testSelectByName() {
        adminUnitList.selectByName("Południowa", false).list(10);
    }


    @Test
    void testShortSelectByNameRegex() {
        //znaleźć wybraną jednostkę na danym poziomie hierarchii
        adminUnitList.selectByName("Kraków", true).list(out);
        adminUnitList.selectByName("Kraków").list(out);
    }
    @Test
    void testShortSelectByName() {
        adminUnitList.selectByName("Kraków", false).list(out);
    }

    @Test
    void testGetNeighborsCity(){
        //wyznaczyć i wypisać listę sąsiadów ->
        // dla Kolonia Wschodnia -> Kolonia Wschodnia, Kolonia Zachodnia, Kolonia Południowa

        //adminUnitList.getNeighbors(adminUnitList.selectByName("Bębło").units.get(0), 150000);
        adminUnitList.getNeighbors(adminUnitList.selectByName("Kolonia Wschodnia").units.get(0), 150000).list(out);
    }
    @Test
    void testGetNeighborsDistrict(){
        //adminUnitList.selectByName("województwo lubuskie", false).list(System.out);
        //adminUnitList.selectByName("województwo zachodniopomorskie", false).list(System.out);

        //wyznaczyć i wypisać listę sąsiadów ->zachodniopomorskie -> województwo lubuskie, województwo wielkopolskie, województwo pomorskie.
        //adminUnitList.selectByName("województwo zachodniopomorskie").list(System.out);
        adminUnitList.getNeighbors(adminUnitList.selectByName("województwo zachodniopomorskie").units.get(0), 150000).list(out);
    }


    @Test
    void testFilterK(){
        adminUnitList.filter(a->a.name.startsWith("K")).sortInplaceByArea().list(out);
    }
    @Test
    void testFilterName(){ // jesli jest nullem, to drugiego nie sprawdzamy, gdy zamienimy kolejnoscia to moze byc problem.
        adminUnitList.filter(a-> a.parent != null && a.parent.name.contains("województwo małopolskie")).sortInplaceByArea().list(out);
    }
    @Test
    void testFilterDensity(){
        // Wyświetlamy Units które mają gęstość większą od średniej
        double avg = adminUnitList.units.stream().mapToDouble(u -> u.density).average().orElse(Double.NaN);
        adminUnitList.filter(a-> a.density > avg).sortInplaceByArea().list(out);
    }

    @Test
    void execute1() {
        AdminUnitQuery query = new AdminUnitQuery()
                .selectFrom(adminUnitList)
                .where(a->a.area>1000)
                .or(a->a.name.startsWith("Sz"))
                .sort((a,b)->Double.compare(a.area,b.area))
                .limit(100);
        query.execute().list(out);
    }
    @Test
    void execute2() {
        AdminUnitQuery query = new AdminUnitQuery()
                .selectFrom(adminUnitList)
                .where(a->a.population>200)
                .and(a->a.area>5000)
                .sort(Comparator.comparingDouble(unit -> unit.area))
                .limit(5);
        query.execute().list(out);
    }
    @Test
    void execute3() {
        AdminUnitQuery query = new AdminUnitQuery()
                .selectFrom(adminUnitList)
                .where(a->a.area>1000)
                .or(a->a.children.size() > 10)
                .sort((a,b)->Double.compare(a.area,b.area))
                .limit(100);
        query.execute().list(out);
    }

}