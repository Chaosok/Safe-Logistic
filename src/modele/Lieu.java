package modele;

/**
 * Abstraction représentant un lieu physique
 */
public abstract class Lieu extends Sommet {
    public Lieu(int id, String label) {
        super(id, label);
    }
    public abstract String getType();
}