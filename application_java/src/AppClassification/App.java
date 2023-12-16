package AppClassification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class App extends JFrame {
    private static final long serialVersionUID = 1L;
    private JButton ajouterImageBouton;
    private JButton traitementBouton;
    private JLabel etiquetteImage;
    private ImageIcon imageChargee;
    private JPanel panelImageTraitee;

    private static final int LARGEUR_IMG_CIBLE = 224;
    private static final int HAUTEUR_IMG_CIBLE = 224;

    private static final double FACTEUR_ECHELLE = 1.f;

    private String cheminImageActuel;

    // Chargement de la bibliothèque native OpenCV
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public App() {
        setTitle("Application de traitement d'image");
        setSize(800, 600); // Taille augmentée
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Création de la barre de menu
        JMenuBar barreMenu = new JMenuBar();
        JMenu menuAide = new JMenu("Aide");
        JMenuItem aProposMenu = new JMenuItem("À propos");
        menuAide.add(aProposMenu);
        barreMenu.add(menuAide);
        setJMenuBar(barreMenu);

        // Action pour l'option À propos dans le menu
        aProposMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(App.this, "Application de classification Cauli/Sun-Flower\n\nDéveloppé par :\n- Gaëtan Oueyeya\n- Lucas Faria\n- Antoine Deurveilher",
                        "À propos", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Création des boutons
        ajouterImageBouton = new JButton("Ajouter une image");
        traitementBouton = new JButton("Traitement");

        // Création du panneau de boutons
        JPanel panneauBoutons = new JPanel();
        panneauBoutons.setLayout(new GridLayout(2, 1));
        panneauBoutons.add(ajouterImageBouton);
        panneauBoutons.add(traitementBouton);

        // Création de l'étiquette pour l'image
        etiquetteImage = new JLabel("", SwingConstants.CENTER);

        // Création du panneau pour les images traitées
        panelImageTraitee = new JPanel();
        panelImageTraitee.setLayout(new GridLayout(1, 1));

        // Ajout des composants au conteneur principal
        Container conteneurPrincipal = getContentPane();
        conteneurPrincipal.setLayout(new BorderLayout());
        conteneurPrincipal.add(panneauBoutons, BorderLayout.WEST);
        conteneurPrincipal.add(etiquetteImage, BorderLayout.CENTER);
        conteneurPrincipal.add(panelImageTraitee, BorderLayout.SOUTH);

        // Gestionnaire d'événements pour le bouton "Ajouter une image"
        ajouterImageBouton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser selecteurFichier = new JFileChooser();
                int resultat = selecteurFichier.showOpenDialog(null);

                if (resultat == JFileChooser.APPROVE_OPTION) {
                    File fichierSelectionne = selecteurFichier.getSelectedFile();
                    
                    // Vérifier si le fichier est une image
                    if (estImage(fichierSelectionne)) {
                        try {
                            BufferedImage img = ImageIO.read(fichierSelectionne);
                            imageChargee = new ImageIcon(img.getScaledInstance(300, 300, Image.SCALE_SMOOTH));
                            etiquetteImage.setIcon(imageChargee);
                            cheminImageActuel = fichierSelectionne.getAbsolutePath();
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null,
                                    "Erreur lors de la lecture de l'image.",
                                    "Avertissement", JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Le fichier n'est pas une image ou le format n'est pas pris en charge.",
                                "Avertissement", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }

            // Méthode pour vérifier si le fichier est une image
            private boolean estImage(File fichier) {
                String nomFichier = fichier.getName().toLowerCase();
                return nomFichier.endsWith(".jpg") || nomFichier.endsWith(".jpeg") || nomFichier.endsWith(".png") ||
                       nomFichier.endsWith(".gif") || nomFichier.endsWith(".bmp") || nomFichier.endsWith(".wbmp");
            }
        });

        // Gestionnaire d'événements pour le bouton "Traitement"
        traitementBouton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cheminImageActuel != null) {
                    traiterImage(cheminImageActuel);
                } else {
                    JOptionPane.showMessageDialog(App.this, "Veuillez d'abord ajouter une image.", "Avertissement",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    // Méthode pour traiter l'image et afficher sa classification
    private void traiterImage(String cheminImage) {
        try {
            List<String> etiquettes = Files.readAllLines(Paths.get("./model/model_labels.txt"));
            Mat image = Imgcodecs.imread(cheminImage);
            Mat blobEntree = Dnn.blobFromImage(image, FACTEUR_ECHELLE, new Size(LARGEUR_IMG_CIBLE, HAUTEUR_IMG_CIBLE),
                    new Scalar(1.f), true, false);
            Net reseauDnn = Dnn.readNet("./model/model.pb");
            reseauDnn.setInput(blobEntree);
            Mat classification = reseauDnn.forward();
            Core.MinMaxLocResult mm = Core.minMaxLoc(classification);
            double indiceMaxValeur = mm.maxLoc.x;
            String nomClasse = etiquettes.get((int) indiceMaxValeur);
            double confiance = mm.maxVal;
            String messageResultat = "Classe: " + nomClasse + "\nConfiance: " + confiance;

            // Afficher le résultat dans une boîte de dialogue
            JOptionPane.showMessageDialog(this, messageResultat, "Résultat de classification", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                App application = new App();
                application.setVisible(true);
            }
        });
    }
}
