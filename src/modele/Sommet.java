package modele;
import java.io.Serializable;
import java.util.Objects;

/**
 * Représente un nœud dans le graphe.
 * Identifié de manière unique par un ID entier.
 */
public class Sommet implements Serializable, Cloneable{
    private int id_sommet;
    private String label;
    // Version pour la sérialisation
    private static final long serialVersionUID = 1L; 

    /**
     * Constructeur d'un sommet.
     * @param id L'identifiant unique.
     * @param label Le nom lisible (ex: "Paris").
     */
    public Sommet(int id, String label) {
        this.id_sommet = id;
        this.label = label;
    }

    /**
     * @return L'identifiant du sommet.
     */
    public int getIdSommet() {
        return id_sommet;
    }

    @Override
    public String toString() {
        return label + "(" + id_sommet + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sommet sommet = (Sommet) o;
        return id_sommet == sommet.id_sommet;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_sommet);
    }

    // Méthode de clonage (Copie simple de l'objet Sommet)
    @Override
    public Sommet clone() {
        try {
            return (Sommet) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Erreur clonage Sommet");
        }
    }
}