package modele;
import java.io.Serializable;
import java.util.*;

/**
 * Classe abstraite définissant la structure générique d'un graphe.
 * Gère le stockage des sommets et des arêtes via une liste d'adjacence.
 */
public abstract class Graphe implements Serializable, Cloneable {

    // Map : Clé = Sommet, Valeur = Liste des arêtes partant de ce sommet
    protected Map<Sommet, List<Arete>> listeAdjacente;
    private static final long serialVersionUID = 1L;

    public Graphe() {
        this.listeAdjacente = new HashMap<>();
    }

    /**
     * Ajoute un sommet au graphe s'il n'existe pas encore.
     */
    public void ajoutSommet(Sommet s) {
        listeAdjacente.putIfAbsent(s, new ArrayList<>());
    }

    /**
     * Méthode abstraite pour ajouter une arête.
     * Le comportement dépendra du type de graphe (Orienté/Non Orienté).
     */
    public abstract void ajouteArete(Sommet source, Sommet destination);

    /**
     * Indique si le graphe est orienté ou non.
     * Utile pour les stratégies d'exportation.
     */
    public abstract boolean estOriente();

    /**
     * @return L'ensemble de tous les sommets du graphe.
     */
    public Set<Sommet> getSommet() {
        return listeAdjacente.keySet();
    }

    /**
     * Récupère les voisins directs d'un sommet.
     */
    public List<Sommet> getVoisins(Sommet s) {
        List<Sommet> voisins = new ArrayList<>();
        if (listeAdjacente.containsKey(s)) {
            for (Arete a : listeAdjacente.get(s)) {
                voisins.add(a.getCible());
            }
        }
        return voisins;
    }

    /**
     * Récupère les objets Arete partant d'un sommet.
     * Nécessaire pour Dijkstra et l'Export (accès aux poids).
     */
    public List<Arete> getAretesIncidentes(Sommet s) {
        return listeAdjacente.getOrDefault(s, new ArrayList<>());
    }

    /**
     * Vérifie les règles du graphe (Pas de boucles, pas de doublons).
     */
    protected void verifieConstraints(Sommet source, Sommet cible) {
        if (source.equals(cible)) {
            throw new IllegalArgumentException("Les boucles (noeud vers lui-même) sont interdites.");
        }
        // Vérification simplifiée des multi-arêtes
        if (listeAdjacente.containsKey(source)) {
            for (Arete a : listeAdjacente.get(source)) {
                if (a.getCible().equals(cible)) {
                    // Pour un graphe pondéré, on pourrait autoriser des multi-arêtes de poids différents,
                    
                    throw new IllegalArgumentException("L'arête existe déjà.");
                }
            }
        }
    }


    /**
     * Supprime l'arête reliant source à cible.
     */
    public void supprimerArete(Sommet source, Sommet cible) {
        if (listeAdjacente.containsKey(source)) {
            // On utilise un itérateur pour supprimer proprement dans une boucle
            Iterator<Arete> it = listeAdjacente.get(source).iterator();
            while (it.hasNext()) {
                Arete a = it.next();
                if (a.getCible().equals(cible)) {
                    it.remove();
                    break; // On supprime une seule instance
                }
            }
        }
    }

	/**
     * Supprime un sommet et toutes les arêtes qui y sont connectées.
     */
    public void supprimerSommet(Sommet s) {
        // 1. Supprimer le sommet de la liste principale
        if (listeAdjacente.containsKey(s)) {
            listeAdjacente.remove(s);
        }

        // 2. Supprimer toutes les arêtes qui pointent VERS ce sommet
        // (On parcourt tous les autres sommets pour voir s'ils ont un lien vers 's')
        for (List<Arete> aretes : listeAdjacente.values()) {
            Iterator<Arete> it = aretes.iterator();
            while (it.hasNext()) {
                Arete a = it.next();
                if (a.getCible().equals(s)) {
                    it.remove(); // On coupe le lien
                }
            }
        }
    }

	// Méthode utilitaire pour retrouver un sommet par son ID (pour le Main)
	public Sommet getSommetParId(int id) {
		for (Sommet s : getSommet()) {
			if (s.getIdSommet() == id) return s;
		}
		return null;
	}

    /**
     * DEEP CLONE (Copie en profondeur)
     * Crée un graphe totalement indépendant pour les simulations SafeNetmedia.
     */
    @Override
    public Graphe clone() {
        try {
            // 1. On clone la coquille (l'objet Graphe lui-même)
            Graphe copie = (Graphe) super.clone();
            
            // 2. On vide ses listes pour ne pas partager les références avec l'original
            copie.listeAdjacente = new HashMap<>(); // Supposons que c'est votre structure de stockage

            // 3. COPIE DES SOMMETS (Création de nouveaux objets)
            // On garde une map "Ancien ID -> Nouveau Sommet" pour reconstruire les liens après
            Map<Integer, Sommet> mapping = new HashMap<>();
            
            for (Sommet original : this.getSommet()) {
                Sommet cloneSommet = original.clone(); 
                copie.ajoutSommet(cloneSommet);
                mapping.put(original.getIdSommet(), cloneSommet);
            }

            // 4. COPIE DES ARÊTES (Reconstruction des routes)
            for (Sommet originalSrc : this.getSommet()) {
                for (Arete a : this.getAretesIncidentes(originalSrc)) {
                    // On retrouve les équivalents dans le nouveau graphe
                    Sommet nouvelleSrc = mapping.get(originalSrc.getIdSommet());
                    Sommet nouvelleDest = mapping.get(a.getCible().getIdSommet());

                    // On recrée l'arête
                    if (copie instanceof GraphePondere && a instanceof Ponderable) {
                        ((GraphePondere) copie).ajouteAretePondere(nouvelleSrc, nouvelleDest, ((Ponderable) a).getPoids());
                    } else {
                        copie.ajouteArete(nouvelleSrc, nouvelleDest);
                    }
                }
            }
            return copie;

        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Le clonage n'est pas supporté, ce qui est impossible ici.");
        }
    }

    
}