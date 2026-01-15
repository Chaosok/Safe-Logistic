package entree_sortie;
import modele.*;
import java.io.*;
import java.util.*;

/**
 * Importation des données de ville
 */
public class ImportDescription {
    public Map<Integer, Sommet> importer(Graphe g, String chemin) throws IOException {
        Map<Integer, Sommet> map = new HashMap<>();
        try(BufferedReader br = new BufferedReader(new FileReader(chemin))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] p = line.split(";");
                if(p.length < 3) continue;
                int id = Integer.parseInt(p[0]);
                String type = p[1], nom = p[2];
                
                Sommet s;
                switch(type) {
                    case "Restaurant": s = new Restaurant(id, nom, Integer.parseInt(p[3])); break;
                    case "Client": s = new Client(id, nom, p[3]); break;
                    case "Depot": s = new Depot(id, nom, Integer.parseInt(p[3])); break;
                    default: s = new Sommet(id, nom);
                }
                g.ajoutSommet(s);
                map.put(id, s);
            }
        }
        return map;
    }
}