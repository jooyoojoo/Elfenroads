package loginwindow;

import domain.GameManager;
import domain.Player;
import enums.Colour;
import networking.GameSession;
import networking.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;


public class ChooseBootWindow extends JPanel {

    private JLabel background_elvenroads;
    private String sessionID;
    private JPanel bootPanel;
    private JPanel textPanel;

    public ChooseBootWindow() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
        setOpaque(false);
        setLayout(new FlowLayout());

        this.background_elvenroads = MainFrame.instance.getElfenroadsBackground();
        this.sessionID = sessionID;

        this.bootPanel = new JPanel();
        this.bootPanel.setBounds(0, MainFrame.mainPanel.getHeight()*4/10,
                MainFrame.mainPanel.getWidth(), MainFrame.mainPanel.getHeight()/3);
        this.bootPanel.setOpaque(false);

        this.textPanel = new JPanel();
        this.textPanel.setBounds(0, MainFrame.mainPanel.getHeight()*3/10,
                MainFrame.mainPanel.getWidth(), MainFrame.mainPanel.getHeight()/10);
        this.textPanel.setOpaque(false);
        JLabel text = new JLabel("Please choose from one of the available boot colours below.");
        text.setFont(new Font("Serif", Font.PLAIN, 30));
        this.textPanel.add(text);

        displayAvailableColours();

        background_elvenroads.add(textPanel);
        background_elvenroads.add(bootPanel);
        add(background_elvenroads);

        setVisible(true);
    }

    void displayAvailableColours() {
        ArrayList<Colour> colours = GameManager.getAvailableColours();

        for (Colour c : colours) {
            ImageIcon bootIcon = new ImageIcon("./assets/boppels-and-boots/boot-" + c + ".png");
            Image bootResized = bootIcon.getImage().getScaledInstance(MainFrame.mainPanel.getWidth()/11,
                    MainFrame.mainPanel.getHeight()/7,  java.awt.Image.SCALE_SMOOTH);
            JLabel bootImage = new JLabel(new ImageIcon(bootResized));

            bootImage.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    GameManager.getInstance().setThisPlayer(new Player(c));

                    //TODO: for now this will send the user back to the lobby screen
                    // TODO: but we need to implement an intermediary screen between lobby and gameScreen
                    remove(background_elvenroads);
                    MainFrame.mainPanel.add(new LobbyWindow(), "lobby");
                    MainFrame.cardLayout.show(MainFrame.mainPanel,"lobby");
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });

            this.bootPanel.add(bootImage);
        }
    }
}