package manager;
import modele.*;
import algo.*;
import entree_sortie.*;
import java.io.IOException;

/**
 * Gestion de livraison d'un noeud source au noeud cible
 */
public class GestionLivraison {
    private GraphePondere ville;
    private Dijkstra algorithme;

    public GestionLivraison() {
        this.ville = new GraphePondere();
        this.algorithme = new Dijkstra();
    }

    public void chargerVille(String fileSommets, String fileAretes) {
        try {
            ImportDescription impDesc = new ImportDescription();
            java.util.Map<Integer, Sommet> map = impDesc.importer(ville, fileSommets);
            
            ImportStrategie impLiens = new ImportListeArete(map);
            impLiens.importer(ville, fileAretes);
            System.out.println("Ville chargée !");
        } catch(IOException e) {
            System.err.println("Erreur chargement : " + e.getMessage());
        }
    }

    public GraphePondere getVille() {
        return this.ville;
    }

    /**
     * Planifier le plan de livraison
     */
    public void planifierLivraison(Depot d, Restaurant r, Client c) {
        algo.Dijkstra algo = new algo.Dijkstra();

        //  Calcul du trajet Dépôt -> Restaurant
        algo.Dijkstra.Resultat t1 = algo.calculer(ville, d);
        
        // Calcul du trajet Restaurant -> Client
        algo.Dijkstra.Resultat t2 = algo.calculer(ville, r);

        double distResto = t1.getDistances().getOrDefault(r, Double.MAX_VALUE);
        double distClient = t2.getDistances().getOrDefault(c, Double.MAX_VALUE);

        if (distResto == Double.MAX_VALUE || distClient == Double.MAX_VALUE) {
            System.out.println(" ECHEC : Un des lieux est inaccessible (Île isolée) !");
        } else {
            System.out.println(">>> RAPPORT DE TRAJET <<<");
            
            // On gère l'exception NetworkNotConnectedException
            try {
                System.out.println("1. Ramassage (" + t1.getDistances().get(r) + "km) : " + t1.getCheminVers(r));
                System.out.println("2. Livraison (" + t2.getDistances().get(c) + "km) : " + t2.getCheminVers(c));
                
                double total = t1.getDistances().get(r) + t2.getDistances().get(c);
                System.out.println("TOTAL : " + total + "km");
            
            } catch (exception.NetworkNotConnectedException e) {
                System.out.println("Erreur critique de réseau : " + e.getMessage());
            }
        }
    }

    /**
     * Ajoute un nouveau lieu (Depot, Resto ou Client) dans la ville.
     */
    public void ajouterLieu(Lieu lieu) {
        // On vérifie si l'ID existe déjà pour éviter d'écraser n'importe quoi
        if (ville.getSommetParId(lieu.getIdSommet()) != null) {
            System.out.println("Erreur : L'ID " + lieu.getIdSommet() + " est déjà pris.");
        } else {
            ville.ajoutSommet(lieu);
            System.out.println("Lieu ajouté : " + lieu);
        }
    }

    /**
     * Supprime un lieu (et toutes les routes connectées) via son ID.
     */
    public void supprimerLieu(int id) {
        Sommet s = ville.getSommetParId(id);
        if (s != null) {
            ville.supprimerSommet(s); // Supprime le nœud et nettoie les arêtes
            System.out.println("Lieu (ID " + id + ") supprimé du système.");
        } else {
            System.out.println("Erreur : ID " + id + " introuvable.");
        }
    }

    /**
     * Ajoute une route entre deux lieux existants.
     */
    public void ajouterRoute(int idSrc, int idDest, double distance) {
        Sommet s1 = ville.getSommetParId(idSrc);
        Sommet s2 = ville.getSommetParId(idDest);
        
        if (s1 != null && s2 != null) {
            ville.ajouteAretePondere(s1, s2, distance);
            System.out.println("Route ajoutée entre " + idSrc + " et " + idDest);
        } else {
            System.out.println("Erreur : Un des lieux n'existe pas.");
        }
    }

    /**
     * Sauvegarde l'état actuel de la ville dans deux fichiers.
     */
    public void sauvegarderVille(String fileSommets, String fileAretes) {
        try {
            // Sauvegarde des Descriptions (sommets)
            ExportDescription expDesc = new ExportDescription();
            expDesc.exporter(ville, fileSommets);
            
            // Sauvegarde de la Structure (arrêtes)
            ExportStrategie expLiens = new ExportListeArete();
            expLiens.exporter(ville, fileAretes);
            
            System.out.println("Sauvegarde réussie dans " + fileSommets + " et " + fileAretes);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }
}