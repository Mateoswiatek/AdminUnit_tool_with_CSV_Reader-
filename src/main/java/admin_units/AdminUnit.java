package admin_units;

public class AdminUnit {
    long id;
    String name;
    int adminLevel;
    double population;
    double area;
    double density;
    AdminUnit parent;
    // AdminUnit children;
    BoundingBox bbox;

    @Override
    public String toString() {
        return "AdminUnit{" +
                "\n name='" + name + '\'' +
                "\n adminLevel=" + adminLevel +
                "\n population=" + population +
                "\n area=" + area +
                "\n density=" + density +
                "\n parent id=" + parent.id +
                "\n bbox=" + bbox +
                "}\n";
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

}


// jeśli środki są poniżej 15 km, to są sąsiadami
// sąsiedzi musza być na tych samych levelach.
// Intersection - wyznaczyć przecięcia ?

/*
sąsiedzi -> muszą mieć tego samego sąsiada. poszukujemy tylko o dwa poziomy wyżej ? zaproponować algo ktore chodzi po "lesie".
RTree - Prostokąty które są, dzielimy na 4 i to nam daje kolejne warstwy drzewa. i tak dalej i tak dalej. obok ineksów są indeksy przestrzene, w którym miejscu jest.
 */