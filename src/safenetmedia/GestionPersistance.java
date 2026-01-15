package safenetmedia;

import modele.Graphe;
import java.io.*;

public class GestionPersistance {

    /**
     * Sauvegarde l'état complet du graphe en binaire.
     */
    public static void saveGraph(Graphe g, String filename) throws IOException {
        if (!filename.endsWith(".ser")) filename += ".ser";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(g);
            System.out.println("Graphe sauvegardé avec succès dans " + filename);
        }
    }

    /**
     * Charge un graphe depuis un fichier binaire.
     */
    public static Graphe loadGraph(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (Graphe) ois.readObject();
        }
    }
}