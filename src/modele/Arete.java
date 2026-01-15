package modele;
import java.io.Serializable;

/**
 * Représente une liaison entre deux sommets
 * 
 */
public class Arete implements Serializable{
    private Sommet source;
    private Sommet cible;
    private static final long serialVersionUID = 1L;
    

    /**
     * Constructeur pour arete
     * 
     */
    public Arete(Sommet source, Sommet cible) {
        this.source = source;
        this.cible = cible;
    }

    public Sommet getSource() { return source; }
    public Sommet getCible() { return cible; }
    
}