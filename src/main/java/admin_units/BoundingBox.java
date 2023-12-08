package admin_units;

public class BoundingBox {
    double xmin;
    double ymin;
    double xmax;
    double ymax;

    public BoundingBox(double xmin, double ymin, double xmax, double ymax) {
        this.xmin = xmin;
        this.ymin = ymin;
        this.xmax = xmax;
        this.ymax = ymax;
    }
    public BoundingBox(){
        this(0.0, 0.0, 0.0, 0.0);
    }

    @Override
    public String toString() {
        return "BoundingBox{" +
                "xmin=" + xmin +
                ", ymin=" + ymin +
                ", xmax=" + xmax +
                ", ymax=" + ymax +
                '}';
    }
}