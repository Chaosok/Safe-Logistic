package entree_sortie;
import modele.*;
import java.io.*;
import java.util.*;

/**
 * Importe un fichier au format : ID_SRC : ID_VOISIN1 ID_VOISIN2 ...
 */
public class ImportListeAdjacence implements ImportStrategie {
    @Override
    public void importer(Graphe g, String cheminFichier) throws IOException {
        Map<Integer, Sommet> cache = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length < 2) continue;

                int idSrc = Integer.parseInt(parts[0].trim());
                Sommet src = cache.computeIfAbsent(idSrc, k -> new Sommet(k, "S" + k));

                String[] voisins = parts[1].trim().split("\\s+");
                for (String vId : voisins) {
                    if (vId.isEmpty()) continue;
                    int idDest = Integer.parseInt(vId);
                    Sommet dest = cache.computeIfAbsent(idDest, k -> new Sommet(k, "S" + k));
                    g.ajouteArete(src, dest);
                }
            }
        }
    }
}