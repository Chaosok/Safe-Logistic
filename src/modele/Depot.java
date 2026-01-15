package modele;

/**
 *  Classe de client pour instancier objet client
 */
public class Depot extends Lieu {
    private int capacite;

    public Depot(int id, String label, int capacite) {
        super(id, label);
        this.capacite = capacite;
    }
    public int getCapacite() { return capacite; }
    @Override public String getType() { return "Depot"; }
    @Override public String toString() { return super.toString() + " (Cap: " + capacite + ")"; }
}