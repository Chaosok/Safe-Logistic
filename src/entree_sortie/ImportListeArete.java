package entree_sortie;
import modele.*;
import java.io.*;
import java.util.*;

/**
 * Importation de liste d'arretes
 */
public class ImportListeArete implements ImportStrategie {
    private Map<Integer, Sommet> cache;

    public ImportListeArete() { this.cache = new HashMap<>(); }
    public ImportListeArete(Map<Integer, Sommet> c) { this.cache = c; }

    @Override
    public void importer(Graphe g, String chemin) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(chemin))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] p = line.trim().split("\\s+");
                if(p.length < 2) continue;
                int srcId = Integer.parseInt(p[0]), destId = Integer.parseInt(p[1]);
                
                Sommet src = cache.computeIfAbsent(srcId, k -> new Sommet(k, "S"+k));
                Sommet dest = cache.computeIfAbsent(destId, k -> new Sommet(k, "S"+k));
                
                if(p.length >= 3 && g instanceof GraphePondere) {
                    ((GraphePondere)g).ajouteAretePondere(src, dest, Double.parseDouble(p[2]));
                } else {
                    g.ajouteArete(src, dest);
                }
            }
        }
    }
}