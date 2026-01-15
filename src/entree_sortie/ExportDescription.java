package entree_sortie;
import modele.*;
import java.io.*;

/**
 * sauvegarde de ville en expotatant en fichier
 */
public class ExportDescription {
    public void exporter(Graphe g, String chemin) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(chemin))) {
            for (Sommet s : g.getSommet()) {
                StringBuilder sb = new StringBuilder();
                sb.append(s.getIdSommet()).append(";");
                
                if (s instanceof Restaurant) {
                    sb.append("Restaurant;").append(getNom(s)).append(";");
                    sb.append(((Restaurant) s).getTempsPreparation());
                } else if (s instanceof Client) {
                    sb.append("Client;").append(getNom(s)).append(";");
                    sb.append(((Client) s).getAdresse());
                } else if (s instanceof Depot) {
                    sb.append("Depot;").append(getNom(s)).append(";");
                    sb.append(((Depot) s).getCapacite());
                } else {
                    sb.append("Lieu;").append(getNom(s)).append(";0");
                }
                writer.write(sb.toString());
                writer.newLine();
            }
        }
    }

    
    private String getNom(Sommet s) {

        String str = s.toString();
        if (str.contains("(")) {
            return str.substring(0, str.indexOf("("));
        }
        return str; 
    }
}