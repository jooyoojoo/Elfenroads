package panel;

import domain.*;
import enums.CounterType;
import networking.ActionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CounterPanel extends JPanel {

    private final int x;
    private final int y;
    private GameScreen gameScreen;
    private Road road;

    public CounterPanel(int x, int y, Road road, GameScreen pScreen) {
        this.x = pScreen.getWidth() * x / 1440;
        this.y = pScreen.getHeight() * y / 900;
        this.road = road;
        this.gameScreen = pScreen;

        gameScreen.addElement(this);

        this.setBounds(this.x, this.y, gameScreen.getWidth() * 40 / 1440, gameScreen.getHeight() * 40 / 900);
        this.setOpaque(false);
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ActionManager.getInstance().setSelectedRoad(CounterPanel.this.road);
                update();
            }
        });
    }

    public void setTransportationCounter(TransportationCounter transportationCounter) {
        this.add(transportationCounter.getDisplay());
    }

    public void placeObstacle(Obstacle obstacle) {
    	JLabel obstacleDisplay = obstacle.getDisplay();
    	gameScreen.addAncestorListener(null);
    	obstacleDisplay.setBounds(this.x, this.y + gameScreen.getHeight() * 40 / 900, gameScreen.getWidth() * 40 / 1440, gameScreen.getHeight() * 40 / 900);
    }

    public void update() {
        this.repaint();
        this.revalidate();
    }

    public void clear() {
        this.removeAll();
        update();
    }
}
