package algo;

import modele.*;
import java.util.*;

/**
 * Parcours en Largeur (Breadth-First Search).
 * Utilise une File (Queue).
 */
public class BFS implements AlgorithmeParcours {
    @Override
    public List<Sommet> parcourir(Graphe graphe, Sommet depart) {
        List<Sommet> resultat = new ArrayList<>();
        if (!graphe.getSommet().contains(depart)) return resultat;

        Set<Sommet> visite = new HashSet<>();
        Queue<Sommet> file = new LinkedList<>();

        file.add(depart);
        visite.add(depart);

        while (!file.isEmpty()) {
            Sommet courant = file.poll();
            resultat.add(courant);

            for (Sommet voisin : graphe.getVoisins(courant)) {
                if (!visite.contains(voisin)) {
                    visite.add(voisin);
                    file.add(voisin);
                }
            }
        }
        return resultat;
    }
}