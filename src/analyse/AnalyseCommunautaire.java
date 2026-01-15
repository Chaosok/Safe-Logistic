package analyse;

import modele.*;
import java.util.*;

/**
 * Service d'analyse.
 * Détecte les zones isolées et propose des emplacements stratégiques.
 */
public class AnalyseCommunautaire {

    /**
     * Identifie les communautés (zones connectées).
     * @return Une liste d'ensembles de sommets.
     */
    public List<Set<Sommet>> findCommunities(Graphe g) {
        List<Set<Sommet>> communautes = new ArrayList<>();
        Set<Sommet> visites = new HashSet<>();

        for (Sommet s : g.getSommet()) {
            if (!visites.contains(s)) {
                Set<Sommet> groupe = new HashSet<>();
                explorerGroupe(g, s, visites, groupe);
                
                // FILTRAGE On ne garde la communauté que si elle est pertinente
                boolean estPertinente = false;
                for (Sommet membre : groupe) {
                    if (membre instanceof Client || membre instanceof Restaurant) {
                        estPertinente = true;
                        break;
                    }
                }
                
                if (estPertinente) {
                    communautes.add(groupe);
                }
            }
        }
        return communautes;
    }

    // aide par DFS
    private void explorerGroupe(Graphe g, Sommet u, Set<Sommet> visites, Set<Sommet> groupe) {
        visites.add(u);
        groupe.add(u);
        for (Sommet v : g.getVoisins(u)) {
            if (!visites.contains(v)) {
                explorerGroupe(g, v, visites, groupe);
            }
        }
    }

    /**
     * ANALYSE STRATÉGIQUE (Élection du Hub)
     * Pour chaque communauté, trouve le sommet le plus central.
     * @return Une Map : { Index_Zone -> Sommet_Leader }
     */
    public Map<Integer, Sommet> trouverChefsLocaux(Graphe g) {
        
        List<Set<Sommet>> groupes = findCommunities(g);
        
        Map<Integer, Sommet> chefs = new LinkedHashMap<>(); 

        int numeroZone = 1;
        
        for (Set<Sommet> groupe : groupes) {
            Sommet chef = elireChef(g, groupe);
            if (chef != null) {
                chefs.put(numeroZone, chef);
                numeroZone++;
            }
        }
        return chefs;
    }

    // Le paramètre est maintenant un Set<Sommet>
    private Sommet elireChef(Graphe g, Set<Sommet> groupe) {
        Sommet meilleur = null;
        int maxConnexions = -1;

        for (Sommet s : groupe) {
            int connexions = g.getVoisins(s).size();
            
            if (connexions > maxConnexions) {
                maxConnexions = connexions;
                meilleur = s;
            }
        }
        return meilleur; 
    }
}