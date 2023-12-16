import os

def renommer_images(dossier):
    # Vérifier si le dossier existe
    if not os.path.exists(dossier):
        print(f"Le dossier {dossier} n'existe pas.")
        return

    # Obtenir la liste des fichiers dans le dossier
    fichiers = os.listdir(dossier)

    # Parcourir chaque fichier dans le dossier
    for index, fichier in enumerate(fichiers):
        # Construire le nouveau nom du fichier
        nom_du_dossier = os.path.basename(dossier)
        nom_fichier, extension = os.path.splitext(fichier)
        nouveau_nom = f"{nom_du_dossier.lower()}_{index}{extension}"

        # Construire le chemin complet des fichiers actuels et nouveaux
        chemin_actuel = os.path.join(dossier, fichier)
        nouveau_chemin = os.path.join(dossier, nouveau_nom)

        # Renommer le fichier
        os.rename(chemin_actuel, nouveau_chemin)

        # Afficher le changement
        print(f"Renommage de {fichier} en {nouveau_nom}")

path_dir = 'Autres'
renommer_images(path_dir)
