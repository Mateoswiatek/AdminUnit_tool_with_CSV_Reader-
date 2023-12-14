package admin_units;

public class BoundingBox {
    double xmin;
    double ymin;
    double xmax;
    double ymax;
    boolean empty = true;

    public BoundingBox(double xmin, double ymin, double xmax, double ymax) {
        this.xmin = xmin;
        this.ymin = ymin;
        this.xmax = xmax;
        this.ymax = ymax;
        empty = false;
    }
    public BoundingBox(){}
    public void addPoint(double x, double y){
        if(isEmpty()){ xmin = xmax = x; ymin = ymax = y; }
        else{
            if(x < xmin){
                xmin = x;
            } else if (x > xmax) {
                xmax = x;
            }
            if(y < ymin){
                ymin = y;
            } else if (y > ymax) {
                ymax = y;
            }
        }
        empty = false;
    }

    public boolean contains(double x, double y){
        return (xmin < x && x < xmax) && (ymin < y && y < ymax);
    }

    public boolean intersects(BoundingBox bb){
        return !(xmin > bb.xmax || xmax < bb.xmin || ymin > bb.ymax || ymax < bb.ymin);
        /* bierzemy wszystkie kiedy nie nachodzą i negeujemy.
        nie nachodzą się prostokąty gdy:
        prawa krawedz xmin jest większa od lewej krawędzi drugiego
        lub lewa krawedx xmax jest mniejsza od prawej krawedzi drugiego
        lub dolna krawedz ymin jest wieksza od gornej krawedzi drugiego
        lub gorna krawedz ymax jest mniejsza od dolnej krawedzi drugiego.
         */
        /* we take all of them if they do not overlap and negate them.
        rectangles do not overlap when:
        the right edge of xmin is larger than the left edge of the other
        or the left edge of xmax is smaller than the right edge of the other
        or the lower edge of ymin is larger than the upper edge of the other
        or the upper edge of ymax is smaller than the lower edge of the other.
         */
    }
    public BoundingBox add(BoundingBox bb){
        if(isEmpty()){
            xmin = bb.xmin;
            xmax = bb.xmax;
            ymin = bb.ymin;
            ymax = bb.ymax;
            empty = false;
        }
        addPoint(bb.xmin, bb.ymin);
        addPoint(bb.xmin, bb.ymax);
        addPoint(bb.xmax, bb.ymin);
        addPoint(bb.xmax, bb.ymax);
        return this;
    }
    public boolean isEmpty(){
        return empty;
    }
    @Override
    public boolean equals(Object o){
        if(o instanceof BoundingBox bo){
            return bo.xmin == xmin && bo.xmax == xmax && bo.ymax == ymin && bo.ymin == ymin;
        }
        return false;
    }
    double getCenterX(){
        if(isEmpty()){
            throw new NullPointerException("Empty box X");
        }
        return (xmin + xmax) / 2;
    }
    double getCenterY(){
        if(isEmpty()){
            throw new NullPointerException("Empty box Y");
        }
        return (ymin + ymax) / 2;
    }
    double distanceTo(BoundingBox bbx){
        if(isEmpty()){
            throw new NullPointerException("Empty box");
        }
        return Haversine.haversine(getCenterX(), getCenterY(), bbx.getCenterX(), bbx.getCenterY());
    }

    public String getWKT(){
        StringBuilder stringBuilder = new StringBuilder("POLYGON ((");

        stringBuilder.append(xmin).append(" ").append(ymin).append(", ")
                .append(xmin).append(" ").append(ymax).append(", ")
                .append(xmax).append(" ").append(ymax).append(", ")
                .append(xmax).append(" ").append(ymin).append(", ")
                .append(xmin).append(" ").append(ymin).append("))");
        return stringBuilder.toString();
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


// Program that calculates the Haversine formula
// https://www.geeksforgeeks.org/haversine-formula-to-find-distance-between-two-points-on-a-sphere/
class Haversine {

    static double haversine(double lat1, double lon1,
                            double lat2, double lon2)
    {
        // distance between latitudes and longitudes
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
    }

    // Driver Code
    public static void main(String[] args)
    {
        double lat1 = 51.5007;
        double lon1 = 0.1246;
        double lat2 = 40.6892;
        double lon2 = 74.0445;
        System.out.println(haversine(lat1, lon1, lat2, lon2) + " K.M.");
    }
}
