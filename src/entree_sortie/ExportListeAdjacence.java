package entree_sortie;
import modele.*;
import java.io.*;

/**
 * Exporte le graphe en liste d'adjacence.
 */
public class ExportListeAdjacence implements ExportStrategie {
    @Override
    public void exporter(Graphe g, String cheminFichier) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichier))) {
            for (Sommet s : g.getSommet()) {
                StringBuilder sb = new StringBuilder();
                sb.append(s.getIdSommet()).append(" :");
                
                for (Sommet v : g.getVoisins(s)) {
                    sb.append(" ").append(v.getIdSommet());
                }
                
                // On écrit la ligne seulement s'il y a des voisins
                if (!g.getVoisins(s).isEmpty()) {
                    writer.write(sb.toString());
                    writer.newLine();
                }
            }
        }
    }
}