package modele;

/**
 * Implément de type route
 */
public class Route extends Arete implements Ponderable {
    private double distance;

    public Route(Sommet source, Sommet cible, double distance) {
        super(source, cible);
        this.distance = distance;
    }

    @Override public double getPoids() { return distance; }
    @Override public String toString() { return "Route " + distance + "km"; }
}