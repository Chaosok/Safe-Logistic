package modele;

/**
 * Graphe supportant les poids sur les arêtes.
 * Hérite de Graphe.
 */
public class GraphePondere extends Graphe {

    /**
     * Ajout d'arête standard (poids par défaut = 1.0).
     */
    @Override
    public void ajouteArete(Sommet source, Sommet destination) {
        ajouteAretePondere(source, destination, 1.0);
    }

    /**
     * Ajoute une arête avec un poids spécifique.
     * @param poids La distance ou le coût.
     */
    public void ajouteAretePondere(Sommet source, Sommet destination, double poids) {
        verifieConstraints(source, destination);
        ajoutSommet(source);
        ajoutSommet(destination);
        
        // Ici on considère qu'un graphe pondéré peut être orienté (pour Dijkstra)
        this.listeAdjacente.get(source).add(new Route(source, destination, poids));
    }

    @Override
    public boolean estOriente() {
        return true; // Par choix d'implémentation pour Dijkstra
    }
}