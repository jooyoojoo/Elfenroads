package test;

import domain.*;
import org.minueto.MinuetoTool;
import panel.GameScreen;
import utils.GameRuleUtils;

import javax.swing.*;
import java.util.Arrays;

public class TestGameRuleUtils {
    public static void main(String[] args) {

        JFrame gameScreen = new JFrame("GameScreen");
        gameScreen.setSize(MinuetoTool.getDisplayWidth(), MinuetoTool.getDisplayHeight());
        gameScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameScreen.add(GameScreen.getInstance(gameScreen));
        gameScreen.setVisible(false);

        // test initialization
        GameMap gameMap = GameMap.getInstance(GameScreen.getInstance(new JFrame()));
        Town elvenhold = gameMap.getTownByName("Elvenhold");
        Town feodor = gameMap.getTownByName("Feodor");
        Town beata = gameMap.getTownByName("Beata");
        TravelCard unicorn = new TravelCard(TravelCardType.UNICORN, 1, 1);
        TravelCard raft = new TravelCard(TravelCardType.RAFT, 1, 1);
        TravelCard cloud = new TravelCard(TravelCardType.MAGICCLOUD, 1, 1);

        // no road from Elvenhold to Feodor, expect false
        assert !GameRuleUtils.validateMove(gameMap, elvenhold, feodor, Arrays.asList(unicorn, raft));
        assert !GameRuleUtils.validateMove(gameMap, elvenhold, feodor, Arrays.asList(unicorn, raft, raft));

        // a river and a plain road from Beata to Elvenhold
        assert GameRuleUtils.validateMove(gameMap, elvenhold, beata, Arrays.asList(raft, raft));
        assert GameRuleUtils.validateMove(gameMap, beata, elvenhold, Arrays.asList(raft));
        assert !GameRuleUtils.validateMove(gameMap, elvenhold, beata, Arrays.asList(unicorn));
        assert !GameRuleUtils.validateMove(gameMap, elvenhold, beata, Arrays.asList(cloud));
        assert GameRuleUtils.validateMove(gameMap, elvenhold, beata, Arrays.asList(cloud, cloud));
        assert !GameRuleUtils.validateMove(gameMap, elvenhold, beata, Arrays.asList(cloud, cloud, cloud));
    }


}