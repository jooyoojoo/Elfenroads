package windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartWindow extends JPanel {

    private JLabel background_elvenroads;
    private JPanel buttons;
    private JButton startButton;
    private JButton aboutButton;
    private JButton exitButton;
    private Font font;

    public StartWindow() {
        MP3Player track1 = new MP3Player("./assets/Music/JLEX5AW-ui-medieval-click-heavy-positive-01.mp3");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());

        ImageIcon background_image = new ImageIcon("./assets/sprites/elfenroads.jpeg");
        Image background_image_resized = background_image.getImage().getScaledInstance((int) screenSize.getWidth(), (int) screenSize.getHeight(), java.awt.Image.SCALE_SMOOTH);
        background_elvenroads = new JLabel(new ImageIcon(background_image_resized));

        // startButton config
        startButton = new JButton("START");
        startButton.setPreferredSize(new Dimension(150, 70));
        startButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                track1.play();
                remove(background_elvenroads);
                MainFrame.mainPanel.add(new LoginWindow(), "login");
                MainFrame.cardLayout.show(MainFrame.mainPanel, "login");
            }

        });

        // aboutButton config
        aboutButton = new JButton("ABOUT");
        aboutButton.setPreferredSize(new Dimension(150, 70));
        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                track1.play();
                remove(background_elvenroads);
                MainFrame.mainPanel.add(new AboutWindow(), "about");
                MainFrame.cardLayout.show(MainFrame.mainPanel, "about");
            }

        });

        // exitButton config
        exitButton = new JButton("EXIT");
        exitButton.setPreferredSize(new Dimension(150, 70));
        exitButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                track1.play();
                System.exit(0);

            }

        });

        Font font = new Font("Arial", Font.BOLD, 16);
        buttons = new JPanel();
        buttons.setOpaque(false);
        startButton.setFont(font);
        aboutButton.setFont(font);
        exitButton.setFont(font);
        buttons.add(startButton);
        buttons.add(aboutButton);
        buttons.add(exitButton);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        background_elvenroads.setLayout(layout);
        background_elvenroads.add(buttons, gbc);

        add(background_elvenroads);
    }
}
