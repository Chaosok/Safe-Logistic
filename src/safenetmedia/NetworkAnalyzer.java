package safenetmedia;

import modele.*;
import java.util.*;

/**
 * L'analyse SafeNetmedia.
 * Analyse la robustesse et la structure du réseau.
 */
public class NetworkAnalyzer {

    private Graphe graphe;

    // Composition : L'analyseur "possède" le graphe à analyser
    public NetworkAnalyzer(Graphe graphe) {
        this.graphe = graphe;
    }

    /**
     * Vérifie si le réseau est entièrement connecté.
     * @return true si on peut aller de n'importe quel point à n'importe quel autre.
     */
    public boolean isConnected() {
        // 1. Un graphe vide ou avec 1 seul sommet est considéré connexe
        if (graphe.getSommet().isEmpty() || graphe.getSommet().size() == 1) {
            return true;
        }

        // 2. On prend un point de départ arbitraire (le premier qu'on trouve)
        Sommet depart = graphe.getSommet().iterator().next();

        // 3. On compte combien de sommets on peut atteindre depuis ce départ
        int nombreVisites = compterSommetsAccessibles(graphe, depart);

        // 4. Si nbVisites == nbTotalSommets, alors tout est connecté !
        return nombreVisites == graphe.getSommet().size();
    }

    /**
     * DÉTECTION DES PONTS
     * Identifie les arêtes critiques dont la suppression couperait le réseau en deux.
     * Stratégie : On retire une arête temporairement, on teste la connectivité, on la remet.
     */
    public List<Arete> detecterPonts() {
        List<Arete> ponts = new ArrayList<>();
        List<Arete> toutesLesAretes = recupererAretesUniques();

        // Pour chaque route unique du réseau...
        for (Arete a : toutesLesAretes) {
            Sommet u = a.getSource();
            Sommet v = a.getCible();
            double poids = (a instanceof Ponderable) ? ((Ponderable) a).getPoids() : 1.0;

            // --- SIMULATION DE PANNE ---
            // 1. On supprime l'arête 
            graphe.supprimerArete(u, v);
            if (!graphe.estOriente()) {
                graphe.supprimerArete(v, u);
            }

            // 2. On vérifie si le réseau est cassé
            if (!isConnected()) {
                // C'est un PONT !
                ponts.add(a);
            }

            // --- RESTAURATION ---
            // 3. On remet l'arête exactement comme avant
            if (graphe instanceof GraphePondere) {
                ((GraphePondere) graphe).ajouteAretePondere(u, v, poids);
            } else {
                graphe.ajouteArete(u, v);
            }
        }
        return ponts;
    }


    /**
     * Parcours (BFS/DFS) pour compter les nœuds accessibles.
     */
    private int compterSommetsAccessibles(Graphe g, Sommet depart) {
        Set<Sommet> visites = new HashSet<>();
        Queue<Sommet> file = new LinkedList<>();

        file.add(depart);
        visites.add(depart);

        while (!file.isEmpty()) {
            Sommet courant = file.poll();
            for (Sommet voisin : g.getVoisins(courant)) {
                if (!visites.contains(voisin)) {
                    visites.add(voisin);
                    file.add(voisin);
                }
            }
        }
        return visites.size();
    }

    /**
     * Récupère une liste propre des arêtes sans doublons (A-B et B-A).
     * Utile pour ne pas tester deux fois la même route.
     */
    private List<Arete> recupererAretesUniques() {
        List<Arete> uniques = new ArrayList<>();
        Set<String> signatures = new HashSet<>();

        for (Sommet s : graphe.getSommet()) {
            for (Arete a : graphe.getAretesIncidentes(s)) {
                // On crée une signature unique pour la paire (ex: "1-2" et "2-1" deviennent la même clé)
                int id1 = a.getSource().getIdSommet();
                int id2 = a.getCible().getIdSommet();
                
                // On ordonne les IDs pour que 1-2 et 2-1 aient la même signature "1-2"
                String signature = (id1 < id2) ? id1 + "-" + id2 : id2 + "-" + id1;

                if (!signatures.contains(signature)) {
                    signatures.add(signature);
                    uniques.add(a);
                }
            }
        }
        return uniques;
    }

    /**
     * CALCULE LA CENTRALITÉ D'INTERMÉDIARITÉ (Betweenness Centrality).
     * Compte combien de fois chaque nœud se trouve sur le chemin le plus court 
     * entre deux autres nœuds.
     * * @return Une Map { Sommet -> Score de centralité }
     */
    public Map<Sommet, Double> calculateBetweennessCentrality() {
        Map<Sommet, Double> scores = new HashMap<>();
        
        // 1. Initialisation des scores à 0
        for (Sommet s : graphe.getSommet()) {
            scores.put(s, 0.0);
        }

        // Il faut un graphe pondéré pour Dijkstra
        if (!(graphe instanceof GraphePondere)) {
            System.out.println("Attention: Centralité calculée sans pondération (Graphe simple).");
            return scores;
        }

        GraphePondere gp = (GraphePondere) graphe;
        algo.Dijkstra algorithme = new algo.Dijkstra();

        // 2. Pour CHAQUE couple de points (Départ -> Arrivée)
        for (Sommet start : gp.getSommet()) {
            // On calcule tous les chemins partant de 'start'
            algo.Dijkstra.Resultat res = algorithme.calculer(gp, start);

            for (Sommet end : gp.getSommet()) {
                if (start.equals(end)) continue; // Pas de chemin vers soi-même

                try {
                    // On récupère le chemin : [Depart, A, B, Arrivee]
                    List<Sommet> chemin = res.getCheminVers(end);
                    
                    // 3. On donne +1 point à tous les nœuds INTERMÉDIAIRES
                    // On exclut le premier (0) et le dernier (size-1)
                    for (int i = 1; i < chemin.size() - 1; i++) {
                        Sommet intermediaire = chemin.get(i);
                        double nouveauScore = scores.get(intermediaire) + 1.0;
                        scores.put(intermediaire, nouveauScore);
                    }
                } catch (Exception e) {
                    // Pas de chemin possible (graphe non connexe), on ignore
                }
            }
        }
        return scores;
    }
}