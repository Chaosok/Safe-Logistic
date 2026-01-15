package entree_sortie;
import modele.Graphe;
import java.io.IOException;

/**
 * Interface pour exportation de ville
 */
public interface ExportStrategie {
    void exporter(Graphe g, String cheminFichier) throws IOException;
}