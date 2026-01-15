import manager.GestionLivraison;
import modele.*;
import analyse.AnalyseCommunautaire;
import entree_sortie.VisualiseurTerminal;

import java.util.Map;
import java.util.Scanner;
//import java.io.File;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        GestionLivraison manager = new GestionLivraison();
        boolean continuer = true;

        System.out.println("==========================================");
        System.out.println("      SAFELOGISTIC - MANAGER        ");
        System.out.println("==========================================");

        while (continuer) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Charger une ville (Import)");
            System.out.println("2. Planifier une livraison");
            System.out.println("3. Modifier la ville (Ajout/Retrait)"); 
            System.out.println("4. Sauvegarder la ville (Export)");     
            System.out.println("5. Afficher les lieux");
            System.out.println("6. Analyser du réseaux(Communautés & Hubs)");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");

            String choix = scanner.nextLine();

            switch (choix) {
                case "1": chargerFichiers(manager); break;
                case "2": lancerLivraison(manager); break;
                case "3": menuModification(manager); break; // Sous-menu
                case "4": sauvegarderFichiers(manager); break;
                case "5": //afficherLieux(manager); break;
                        VisualiseurTerminal.afficherGraphique(manager.getVille());break;
                case "6": menuAnalyseReseau(manager.getVille()); break;
                case "0": continuer = false; break;
                default: System.out.println("Choix invalide.");
            }
        }
    }

    // --- SOUS-MENU DE MODIFICATION ---
    private static void menuModification(GestionLivraison manager) {
        System.out.println("\n--- MODIFICATION DU RÉSEAU ---");
        System.out.println("1. Ajouter un Lieu");
        System.out.println("2. Supprimer un Lieu (Panne/Fermeture)");
        System.out.println("3. Ajouter une Route");
        System.out.print("Choix : ");
        String choix = scanner.nextLine();

        try {
            if (choix.equals("1")) {
                ajouterLieuInteractif(manager);
            } else if (choix.equals("2")) {
                System.out.print("ID du lieu à supprimer : ");
                int id = Integer.parseInt(scanner.nextLine());
                manager.supprimerLieu(id);
            } else if (choix.equals("3")) {
                System.out.print("ID Source : ");
                int src = Integer.parseInt(scanner.nextLine());
                System.out.print("ID Destination : ");
                int dest = Integer.parseInt(scanner.nextLine());
                System.out.print("Distance (km) : ");
                double dist = Double.parseDouble(scanner.nextLine());
                manager.ajouterRoute(src, dest, dist);
            }
        } catch (NumberFormatException e) {
            System.out.println("Erreur : Entrez des nombres valides.");
        }
    }

    // --- Astuce à la création de lieux ---
    private static void ajouterLieuInteractif(GestionLivraison manager) {
        System.out.println("Quel type de lieu ?");
        System.out.println("1. Dépôt | 2. Restaurant | 3. Client");
        String type = scanner.nextLine();

        System.out.print("ID unique : ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Nom (Label) : ");
        String nom = scanner.nextLine();

        Lieu nouveauLieu = null;

        if (type.equals("1")) {
            System.out.print("Capacité : ");
            int cap = Integer.parseInt(scanner.nextLine());
            nouveauLieu = new Depot(id, nom, cap);
        } else if (type.equals("2")) {
            System.out.print("Temps de préparation (min) : ");
            int time = Integer.parseInt(scanner.nextLine());
            nouveauLieu = new Restaurant(id, nom, time);
        } else if (type.equals("3")) {
            System.out.print("Adresse : ");
            String adr = scanner.nextLine();
            nouveauLieu = new Client(id, nom, adr);
        }

        if (nouveauLieu != null) {
            manager.ajouterLieu(nouveauLieu);
        } else {
            System.out.println("Type invalide.");
        }
    }

    // --- FONCTION DE SAUVEGARDE ---
    private static void sauvegarderFichiers(GestionLivraison manager) {
        System.out.println("\n--- SAUVEGARDE ---");
        System.out.print("Nom fichier Sommets (ex: ville(version).txt) : ");
        String f1 = scanner.nextLine();
        System.out.print("Nom fichier Arêtes (ex: routes(version).txt) : ");
        String f2 = scanner.nextLine();
        
        manager.sauvegarderVille(f1, f2);
    }
    
    private static void chargerFichiers(GestionLivraison manager) {
        System.out.print("Fichier SOMMETS : ");
        String f1 = scanner.nextLine();
        System.out.print("Fichier ARETES : ");
        String f2 = scanner.nextLine();
        manager.chargerVille(f1, f2);
    }

    private static void lancerLivraison(GestionLivraison manager) {
        if (manager.getVille().getSommet().isEmpty()) {
            System.out.println("Ville vide !"); return;
        }
        try {
            System.out.print("ID Dépôt : "); int d = Integer.parseInt(scanner.nextLine());
            System.out.print("ID Resto : "); int r = Integer.parseInt(scanner.nextLine());
            System.out.print("ID Client : "); int c = Integer.parseInt(scanner.nextLine());
            
            Sommet sd = manager.getVille().getSommetParId(d);
            Sommet sr = manager.getVille().getSommetParId(r);
            Sommet sc = manager.getVille().getSommetParId(c);
            
            if (sd instanceof Depot && sr instanceof Restaurant && sc instanceof Client) {
                manager.planifierLivraison((Depot)sd, (Restaurant)sr, (Client)sc);
            } else {
                System.out.println("Erreur de types (Vérifiez les IDs).");
            }
        } catch (NumberFormatException e) { System.out.println("Erreur format nombre."); }
    }


    private static void menuAnalyseReseau(GraphePondere graphe) {
        if (graphe.getSommet().isEmpty()) {
            System.out.println("Erreur : Le graphe est vide.");
            return;
        }

        // On délègue tout le travail au Reporter spécialisé [Cite: 75]
        safenetmedia.SafeNetmediaReporter reporter = new safenetmedia.SafeNetmediaReporter(graphe);
        
        // On génère le rapport
        reporter.generateNetworkReport(graphe);
    }

}