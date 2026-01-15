package entree_sortie;

import modele.*;
import java.util.*;

/**
 * Visualisation sur terminal de différents noeuds
 */
public class VisualiseurTerminal {

    // Codes couleurs pour le terminal
    private static final String RESET = "\u001B[0m";
    private static final String RED_BOLD = "\u001B[1;31m";    // Pour les Dépôts
    private static final String GREEN_BOLD = "\u001B[1;32m";  // Pour les Clients
    private static final String YELLOW_BOLD = "\u001B[1;33m"; // Pour les Restos
    private static final String CYAN = "\u001B[36m";         // Pour les liens

    public static void afficherGraphique(Graphe graphe) {
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                VISUALISATION DU RÉSEAU                   ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");

        if (graphe.getSommet().isEmpty()) {
            System.out.println("   (Ville vide)");
            return;
        }

        // On trie les sommets par ID pour que l'affichage soit stable
        List<Sommet> sommetsTries = new ArrayList<>(graphe.getSommet());
        sommetsTries.sort(Comparator.comparingInt(Sommet::getIdSommet));

        for (Sommet s : sommetsTries) {
            afficherSommet(s, graphe);
        }
        System.out.println("════════════════════════════════════════════════════════════");
    }

    private static void afficherSommet(Sommet s, Graphe g) {
        // 1. Déterminer l'icône et la couleur selon le type
        
        String couleur = RESET;
        String typeInfo = "";

        if (s instanceof Depot) {
            couleur = RED_BOLD;
            typeInfo = " [CAP: " + ((Depot)s).getCapacite() + "]";
        } else if (s instanceof Restaurant) {
            
            couleur = YELLOW_BOLD;
            typeInfo = " [CUISINE: " + ((Restaurant)s).getTempsPreparation() + "min]";
        } else if (s instanceof Client) {
            
            couleur = GREEN_BOLD;
            // On raccourcit l'adresse pour l'affichage
            String adresse = ((Client)s).getAdresse();
            if (adresse.length() > 15) adresse = adresse.substring(0, 15) + "...";
            typeInfo = " @" + adresse;
        }

        // 2. Affichage du Nœud Principal
        System.out.println(couleur + " " + s.toString() + typeInfo + RESET);

        // 3. Affichage des connexions (Les arêtes)
        List<Arete> aretes = g.getAretesIncidentes(s);
        
        if (aretes.isEmpty()) {
            System.out.println("   └── (Cul-de-sac / Isolé)");
        } else {
            for (int i = 0; i < aretes.size(); i++) {
                Arete a = aretes.get(i);
                Sommet voisin = a.getCible();
                
                // Est-ce la dernière arête de la liste ? (Pour choisir le bon trait)
                boolean isLast = (i == aretes.size() - 1);
                String prefix = isLast ? "   └── " : "   ├── ";
                
                // Récupération du poids
                double poids = (a instanceof Ponderable) ? ((Ponderable)a).getPoids() : 0.0;
                
                // Affichage de la flèche
                System.out.print(CYAN + prefix);
                System.out.print(String.format("--(%.1f km)--> ", poids));
                
                // Affichage du nom du voisin (juste le nom, pas tout le détail)
                System.out.println(getNomSimple(voisin) + RESET);
            }
        }
        System.out.println(); // Ligne vide pour aérer
    }

    // Petite méthode pour avoir un nom court sans ID
    private static String getNomSimple(Sommet s) {
        String str = s.toString();
        // Si toString renvoie "Nom(ID)", on essaie de garder que le "Nom(ID)"
        // C'est juste pour l'esthétique
        return str;
    }
}