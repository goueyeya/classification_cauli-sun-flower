package AppClassification;

//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;

public class App extends JFrame {
    private JButton addImageButton;
    private JButton processButton;
    private JLabel imageLabel;
    private ImageIcon loadedImage;

    public App() {
        setTitle("Application de traitement d'image");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Création des boutons
        addImageButton = new JButton("Ajouter une image");
        processButton = new JButton("Traitement");

        // Création du conteneur pour les boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1));
        buttonPanel.add(addImageButton);
        buttonPanel.add(processButton);

        // Création de l'étiquette pour l'image
        imageLabel = new JLabel("", SwingConstants.CENTER);

        // Ajout des composants à la fenêtre principale
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(buttonPanel, BorderLayout.WEST);
        contentPane.add(imageLabel, BorderLayout.CENTER);

        // Gestionnaire d'événements pour le bouton "Ajouter une image"
        addImageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        BufferedImage img = ImageIO.read(selectedFile);
                        loadedImage = new ImageIcon(img.getScaledInstance(150, 150, Image.SCALE_SMOOTH));
                        imageLabel.setIcon(loadedImage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Gestionnaire d'événements pour le bouton "Traitement"
        processButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Ajoutez ici le traitement de l'image chargée
                // Cette partie du code sera exécutée lorsque le bouton de traitement sera cliqué
                // Vous pouvez ajouter votre logique de traitement de l'image ici
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                App app = new App();
                app.setVisible(true);
            }
        });
    }
}
