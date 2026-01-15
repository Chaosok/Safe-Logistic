package entree_sortie;
import modele.Graphe;
import java.io.IOException;

/**
 * Interface de stratégie d'importation 
 */
public interface ImportStrategie {
    void importer(Graphe g, String cheminFichier) throws IOException;
}