package admin_units;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminUnitListTest {
    AdminUnitList adminUnitList = new AdminUnitList();
//    @BeforeEach
    void init(){
        adminUnitList.read("src/admin-units.csv");
    }

    @Test
    void read() {
        adminUnitList.read("src/admin-units.csv");
//        System.out.println(adminUnitList.units.size());
        adminUnitList.list(20);
    }

    @Test
    void list() {
        adminUnitList.list(System.out, 0, 2);
    }

    @Test
    void testListPrintStream() {
        adminUnitList.list(System.out);
    }

    @Test
    void testListLimit() {
        adminUnitList.list(20);
    }

    @Test
    void testSelectByName() {
        adminUnitList.selectByName("Południowa", false).list(10);
    }

    @Test
    void testShortSelectByName() {
        adminUnitList.selectByName("Południowa", false).list(10);
    }
}