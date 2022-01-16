package panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class TownPanel extends JPanel {

    private String name;
    private int x;
    private int y;
    private int width;
    private int height;
    private GameScreen gameScreen;
    private ElfBootPanel elfBootPanel;

    public TownPanel(String pName, int x, int y, int pWidth, int pHeight, GameScreen pGameScreen) {
        this.name = pName;
        this.x = x;
        this.y = y;
        this.width = pWidth;
        this.height = pHeight;
        this.gameScreen = pGameScreen;
        this.elfBootPanel = new ElfBootPanel(this, x, y+height, gameScreen.getWidth()*72/1440, gameScreen.getHeight()*48/900, pGameScreen);

        this.setBounds(this.x, this.y, this.width, this.height);
        this.setOpaque(false);
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        this.addMouseListener(new ElfBootController(gameScreen, this));
    }

    public ElfBootPanel getElfBootPanel() { return this.elfBootPanel; }
}

