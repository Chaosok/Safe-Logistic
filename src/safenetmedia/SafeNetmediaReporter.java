package safenetmedia;

import modele.*;
import analyse.AnalyseCommunautaire;
import java.util.*;

/**
 * Générateur de rapports stratégiques pour SafeNetmedia.
 * Synthétise la robustesse, la centralité et les communautés.
 */
public class SafeNetmediaReporter {

    private NetworkAnalyzer analyzer;
    private AnalyseCommunautaire communityDetector;

    public SafeNetmediaReporter(Graphe graphe) {
        // Composition : Le reporter possède les outils d'analyse 
        this.analyzer = new NetworkAnalyzer(graphe);
        this.communityDetector = new AnalyseCommunautaire();
    }

    /**
     * Génère et affiche le rapport complet 
     */
    public void generateNetworkReport(Graphe graphe) {
        System.out.println("##############################################");
        System.out.println("#   RAPPORT INTELLIGENCE SAFENETMEDIA   #");
        System.out.println("##############################################\n");

        // 1. STATUT DE CONNECTIVITÉ 
        System.out.println("--- 1. ROBUSTESSE DU RÉSEAU ---");
        boolean isConnected = analyzer.isConnected();
        if (isConnected) {
            System.out.println("Statut : RÉSEAU OPÉRATIONNEL (Connexe)");
        } else {
            System.out.println("Statut : RÉSEAU FRAGMENTÉ (Non connexe)");
            System.out.println("   /!\\ Certaines livraisons seront impossibles.");
        }

        // 2. LISTE DES PONTS 
        List<Arete> ponts = analyzer.detecterPonts();
        if (ponts.isEmpty()) {
            System.out.println("   -> Aucune vulnérabilité critique détectée.");
        } else {
            System.out.println("   -> ALERTE : " + ponts.size() + " Pont(s) identifié(s).");
            System.out.println("      Ces routes sont des points de défaillance uniques :");
            for (Arete a : ponts) {
                System.out.println("      - [PONT] " + a.getSource() + " <---> " + a.getCible());
            }
        }

        // 3. TOP 3 CENTRALITÉ (Betweenness) 
        System.out.println("\n--- 2. NŒUDS STRATÉGIQUES (Centralité) ---");
        Map<Sommet, Double> scores = analyzer.calculateBetweennessCentrality();
        
        System.out.println("   (Lieux où le trafic est le plus dense)");
        scores.entrySet().stream()
              .sorted(Map.Entry.<Sommet, Double>comparingByValue().reversed())
              .limit(3)
              .forEach(entry -> {
                  System.out.printf("%s - Score: %.1f\n", entry.getKey(), entry.getValue());
              });

        // 4. COMMUNAUTÉS DÉTECTÉES 
        System.out.println("\n--- 3. ZONAGE (Communautés) ---");
        List<Set<Sommet>> communautes = communityDetector.findCommunities(graphe);
        
        System.out.println("   Nombre de zones détectées : " + communautes.size());
        int zoneId = 1;
        for (Set<Sommet> zone : communautes) {
            System.out.println("   [ZONE " + zoneId + "] Contient " + zone.size() + " lieux.");
            // Affichage compact des membres
            System.out.print("      Membres : ");
            int count = 0;
            for(Sommet s : zone) {
                if (count++ > 5) { System.out.print("... (et autres)"); break; }
                System.out.print(s.getIdSommet() + " ");
            }
            System.out.println();
            zoneId++;
        }
        
        System.out.println("\n");

    }
}