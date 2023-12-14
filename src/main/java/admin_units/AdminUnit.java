package admin_units;

import java.util.ArrayList;
import java.util.List;

public class AdminUnit {
    long id;
    String name;
    int adminLevel;
    double population;
    double area;
    double density;
    AdminUnit parent;
    List<AdminUnit> children = new ArrayList<>();
    BoundingBox bbox;
    // Writing "children" is recommended here to avoid spam by voivodeship
    @Override
    public String toString() {
        String parentId;
        if(parent == null){
            parentId = "none";
        } else{
            parentId = String.valueOf(parent.id);
        }
        return "AdminUnit{" +
                "\n name='" + name + '\'' +
                "\n adminLevel=" + adminLevel +
                "\n population=" + population +
                "\n area=" + area +
                "\n density=" + density +
                "\n bbox=" + bbox.getWKT() +
                "\n this   id= " + id +
                "\n parent id= " + parentId +
//                "\n childrens = " + children.toString() +
                "}\n\n\n";
    }

    public AdminUnit(long id, String name, int adminLevel, double population, double area, double density, BoundingBox bbox) {
        this.id = id;
        this.name = name;
        this.adminLevel = adminLevel;
        this.population = population;
        this.area = area;
        this.density = density;
        this.bbox = bbox;
    }
    public void fixMissingValues(){
        if(0 == density){
            if(null != parent){
                parent.fixMissingValues();
                density = parent.density;
            } else {
                density = 0;
            }
            population = area * density;
        }
    }


}


// jeśli środki są poniżej 15 km, to są sąsiadami
// sąsiedzi musza być na tych samych levelach.
// Intersection - wyznaczyć przecięcia ?

/*
sąsiedzi -> muszą mieć tego samego sąsiada. poszukujemy tylko o dwa poziomy wyżej ? zaproponować algo ktore chodzi po "lesie".
RTree - Prostokąty które są, dzielimy na 4 i to nam daje kolejne warstwy drzewa. i tak dalej i tak dalej. obok ineksów są indeksy przestrzene, w którym miejscu jest.
 */