package admin_units;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdminUnitListTest {
    AdminUnitList adminUnitList = new AdminUnitList();
    @BeforeEach
    @Test
    void init(){
        adminUnitList.read("src/admin-units.csv");
    }

    @Test
    void read() {
        adminUnitList.read("src/admin-units.csv");
        System.out.println(adminUnitList.units.size());
        adminUnitList.list(System.out);
    }

    @Test
    void list() {
        adminUnitList.list(System.out, 2, 2);
    }

    @Test
    void testListPrintStream() {
        adminUnitList.list(System.out);
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
        adminUnitList.selectByName("Kraków", true).list(System.out);
        adminUnitList.selectByName("Kraków").list(System.out);
    }
    @Test
    void testShortSelectByName() {
        adminUnitList.selectByName("Kraków", false).list(System.out);
    }

    @Test
    void testGetNeighborsCity(){
        //wyznaczyć i wypisać listę sąsiadów ->
        // dla Kolonia Wschodnia -> Kolonia Wschodnia, Kolonia Zachodnia, Kolonia Południowa

        //adminUnitList.getNeighbors(adminUnitList.selectByName("Bębło").units.get(0), 150000);
        adminUnitList.getNeighbors(adminUnitList.selectByName("Kolonia Wschodnia").units.get(0), 150000).list(System.out);
    }
    @Test
    void testGetNeighborsDistrict(){
        //adminUnitList.selectByName("województwo lubuskie", false).list(System.out);
        //adminUnitList.selectByName("województwo zachodniopomorskie", false).list(System.out);

        //wyznaczyć i wypisać listę sąsiadów ->zachodniopomorskie -> województwo lubuskie, województwo wielkopolskie, województwo pomorskie.
        //adminUnitList.selectByName("województwo zachodniopomorskie").list(System.out);
        adminUnitList.getNeighbors(adminUnitList.selectByName("województwo zachodniopomorskie").units.get(0), 150000).list(System.out);
    }

}