package algo;

import modele.Graphe;
import modele.Sommet;
import java.util.List;

/**
 * Interface Stratégie pour les algorithmes de parcours simples.
 */
public interface AlgorithmeParcours {
    /**
     * Parcourt le graphe et retourne l'ordre de visite.
     */
    List<Sommet> parcourir(Graphe g, Sommet depart);
}