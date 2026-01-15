package modele;

/**
 * Implémentation d'un graphe non orienté (Double sens).
 */
public class GrapheNonOriente extends Graphe {

    @Override
    public void ajouteArete(Sommet source, Sommet destination) {
        verifieConstraints(source, destination);
        ajoutSommet(source);
        ajoutSommet(destination);
        // Ajout double : A -> B et B -> A
        this.listeAdjacente.get(source).add(new Arete(source, destination));
        this.listeAdjacente.get(destination).add(new Arete(destination, source));
    }

    @Override
    public boolean estOriente() {
        return false;
    }
}