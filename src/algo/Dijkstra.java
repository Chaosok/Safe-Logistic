package algo;
import modele.*;
import java.util.*;
import exception.NetworkNotConnectedException;

/**
 * Parcours avec algorithme de Dijkstra
 * Pour chercher le plus court chemin entre 2 noeuds
 */
public class Dijkstra {
    
    // Classe interne encapsulant le résultat complet

    public static class Resultat {
        private Sommet depart;
        private Map<Sommet, Double> distances;
        private Map<Sommet, Sommet> predecesseurs;

        public Resultat(Sommet depart, Map<Sommet, Double> distances, Map<Sommet, Sommet> predecesseurs) {
            this.depart = depart;
            this.distances = distances;
            this.predecesseurs = predecesseurs;
        }

        public Map<Sommet, Double> getDistances() { return distances; }
        public Map<Sommet, Sommet> getPredecesseurs() { return predecesseurs; }

        /**
         * Reconstruit le chemin vers la cible.
         * Lance une exception si la cible est introuvable.
         */
        public List<Sommet> getCheminVers(Sommet cible) throws exception.NetworkNotConnectedException {
            LinkedList<Sommet> chemin = new LinkedList<>();

            // Vérification : La cible est-elle accessible ?
            if (!distances.containsKey(cible) || distances.get(cible) == Double.MAX_VALUE) {
                throw new exception.NetworkNotConnectedException(
                    "Impossible d'atteindre " + cible + " depuis " + depart
                );
            }

            // Reconstruction (en remontant les prédécesseurs)
            Sommet step = cible;
            if (predecesseurs.get(step) == null && !step.equals(depart)) {
                 // Cas rare : distance trouvée mais chemin cassé
                 return chemin; 
            }
            
            chemin.add(step);
            while (predecesseurs.containsKey(step)) {
                step = predecesseurs.get(step);
                chemin.add(step);
            }
            
            // On remet dans l'ordre (Départ -> Arrivée)
            Collections.reverse(chemin);
            return chemin;
        }
    }
    /**
     * Calcule de distance Dijkstra
     */
    public Resultat calculer(Graphe g, Sommet depart) {
        Map<Sommet, Double> dist = new HashMap<>();
        Map<Sommet, Sommet> pred = new HashMap<>();
        PriorityQueue<Sommet> pq = new PriorityQueue<>(Comparator.comparingDouble(s -> dist.getOrDefault(s, Double.MAX_VALUE)));
        Set<Sommet> done = new HashSet<>();

        for(Sommet s : g.getSommet()) dist.put(s, Double.MAX_VALUE);
        dist.put(depart, 0.0);
        pq.add(depart);

        while(!pq.isEmpty()) {
            Sommet u = pq.poll();
            if(done.contains(u)) continue;
            done.add(u);

            for(Arete a : g.getAretesIncidentes(u)) {
                Sommet v = a.getCible();
                if(done.contains(v)) continue;
                
                double w = (a instanceof Ponderable) ? ((Ponderable)a).getPoids() : 1.0;
                if(dist.get(u) + w < dist.get(v)) {
                    dist.put(v, dist.get(u) + w);
                    pred.put(v, u);
                    pq.remove(v); pq.add(v);
                }
            }
        }
        return new Resultat(depart, dist, pred);
    }
}