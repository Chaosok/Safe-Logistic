package algo;

import modele.*;
import java.util.*;

/**
 * Parcours en Profondeur (Depth-First Search).
 * Utilise une Pile (Stack).
 */
public class DFS implements AlgorithmeParcours {
    @Override
    public List<Sommet> parcourir(Graphe graphe, Sommet depart) {
        List<Sommet> resultat = new ArrayList<>();
        if (!graphe.getSommet().contains(depart)) return resultat;

        Set<Sommet> visite = new HashSet<>();
        Stack<Sommet> pile = new Stack<>();

        pile.push(depart);

        while (!pile.isEmpty()) {
            Sommet courant = pile.pop();
            if (!visite.contains(courant)) {
                visite.add(courant);
                resultat.add(courant);

                List<Sommet> voisins = graphe.getVoisins(courant);
                // Inversion pour ordre "naturel" de lecture
                Collections.reverse(voisins); 
                
                for (Sommet v : voisins) {
                    if (!visite.contains(v)) {
                        pile.push(v);
                    }
                }
            }
        }
        return resultat;
    }
}