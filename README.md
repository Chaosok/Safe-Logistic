# TP4 : gestion de graphe
# Réalisé par KONG Chaosok

## Description

Ce projet sert à inserer un graphe depuis la liste d'arête ou celle adjacence. Le graphe possède également les algorithme de parcoours du graphe

# Séance 1:

-réalisé le diagramme de classe 
-mis en oeuvre: classe Sommet, Arete, Graphe et leurs variables d'instance + leurs méthodes

# Séance d'autonomie

-Définit les autres classes restantes puis l'implémentation de l'interface AlgoritmeParcours qui a été implementé via Bfs et Dsf
-De plus, le gestion de entré et sorti de fichier .txt sous forme de la liste adjacente et la liste d'arêtes
-le dossier bin contient des fichiers .class générés.

# Fichier de tests:
-Main.java : pour effectuer le test complet de systeme sous forme de terminal comme en C -> importation de fichier est obligatoire au lancement de menu.
-test.txt : fichier à importer pour tester sur importation, ce fichier contient de données sous forme de liste d'arête -> il faut bien choissir la bonne option à l'importer.

# Exemple de test sur un terminal
-placer dans le répertoire GESTION_GRAPHE
-pour compiler: javac -d bin -sourcepath src src/Main.java
-pour executer: java -cp bin Main