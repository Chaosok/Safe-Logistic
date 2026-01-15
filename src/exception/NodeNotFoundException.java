package exception;

/**
 * Exception en cas non trouvable d'un noeud
 */
public class NodeNotFoundException extends RuntimeException {
    public NodeNotFoundException(int id) {
        super("Erreur critique : Le nœud avec l'ID " + id + " est introuvable dans le graphe.");
    }
}