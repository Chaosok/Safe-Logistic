package modele;

/**
 * Classe de client pour instancier objet client
 */
public class Client extends Lieu {
    private String adresse;

    public Client(int id, String label, String adresse) {
        super(id, label);
        this.adresse = adresse;
    }
    public String getAdresse() { return adresse; }
    @Override public String getType() { return "Client"; }
    @Override public String toString() { return super.toString() + " @" + adresse; }
}