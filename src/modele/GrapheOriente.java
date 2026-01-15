package modele;

/**
 * Implémentation d'un graphe orienté (Sens unique).
 */
public class GrapheOriente extends Graphe {

    @Override
    public void ajouteArete(Sommet source, Sommet destination) {
        verifieConstraints(source, destination);
        ajoutSommet(source);
        ajoutSommet(destination);
        // Ajout simple : Source -> Destination
        this.listeAdjacente.get(source).add(new Arete(source, destination));
    }

    @Override
    public boolean estOriente() {
        return true;
    }
}