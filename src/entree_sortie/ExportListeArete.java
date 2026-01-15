package entree_sortie;
import modele.*;
import java.io.*;

/**
 * Exporte le graphe en liste d'arêtes.
 * Gère le filtre anti-doublon pour les graphes non orientés.
 */
public class ExportListeArete implements ExportStrategie {
    @Override
    public void exporter(Graphe g, String cheminFichier) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichier))) {
            for (Sommet s : g.getSommet()) {
                for (Arete a : g.getAretesIncidentes(s)) {
                    
                    // Si Non Orienté, on n'écrit que si ID_Source < ID_Cible pour éviter A-B et B-A
                    if (!g.estOriente()) {
                        if (s.getIdSommet() > a.getCible().getIdSommet()) continue;
                    }

                    writer.write(s.getIdSommet() + " " + a.getCible().getIdSommet());
                    
                    if (a instanceof Ponderable) {
                        // On "cast" (transforme) 'a' en Ponderable pour accéder à getPoids()
                        writer.write(" " + ((Ponderable) a).getPoids());
                    }
                    
                    else if (g instanceof GraphePondere) {
                        writer.write(" " + 1.0);
                    }
                    writer.newLine();
                }
            }
        }
    }
}