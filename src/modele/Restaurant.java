package modele;

/**
 * Implement de type restaurant
 */
public class Restaurant extends Lieu {
    private int tempsPreparation;

    public Restaurant(int id, String label, int tempsPreparation) {
        super(id, label);
        this.tempsPreparation = tempsPreparation;
    }
    public int getTempsPreparation() { return tempsPreparation; }
    @Override public String getType() { return "Restaurant"; }
    @Override public String toString() { return super.toString() + " [Cuisine: " + tempsPreparation + "min]"; }
}