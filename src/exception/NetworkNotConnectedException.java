package exception;

/**
 * Exeception en graphe non connecté
 */
public class NetworkNotConnectedException extends Exception {
    public NetworkNotConnectedException(String message) {
        super(message);
    }
}