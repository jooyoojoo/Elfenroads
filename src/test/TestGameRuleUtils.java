package test;

import domain.*;
import enums.GameVariant;
import enums.TravelCardType;
import org.minueto.MinuetoTool;
import gamescreen.GameScreen;
import utils.GameRuleUtils;

import javax.swing.*;
import java.util.Arrays;

public class TestGameRuleUtils {

    public static void main(String[] args) {

        JFrame gameScreen = new JFrame("GameScreen");
        gameScreen.setSize(MinuetoTool.getDisplayWidth(), MinuetoTool.getDisplayHeight());
        gameScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameScreen.add(GameScreen.init(new JFrame(), GameVariant.ELFENLAND_CLASSIC));
        gameScreen.setVisible(false);

        // test initialization
        GameMap gameMap = GameMap.init(GameScreen.getInstance(), GameVariant.ELFENLAND_CLASSIC);
        Town elvenhold = gameMap.getTownByName("Elvenhold");
        Town feodor = gameMap.getTownByName("Feodor");
        Town beata = gameMap.getTownByName("Beata");
        TravelCard unicorn = new TravelCard(TravelCardType.UNICORN, 1, 1);
        TravelCard raft = new TravelCard(TravelCardType.RAFT, 1, 1);
        TravelCard cloud = new TravelCard(TravelCardType.MAGICCLOUD, 1, 1);

        // no road from Elvenhold to Feodor, expect false
        assert GameRuleUtils.validateMove(gameMap, elvenhold, feodor, Arrays.asList(unicorn, raft)) == null;
        assert GameRuleUtils.validateMove(gameMap, elvenhold, feodor, Arrays.asList(unicorn, raft, raft)) == null;

        // a river and a plain road from Beata to Elvenhold
        assert GameRuleUtils.validateMove(gameMap, elvenhold, beata, Arrays.asList(raft, raft)) != null;
        assert GameRuleUtils.validateMove(gameMap, beata, elvenhold, Arrays.asList(raft)) != null;
        assert GameRuleUtils.validateMove(gameMap, elvenhold, beata, Arrays.asList(unicorn)) == null;
        assert GameRuleUtils.validateMove(gameMap, elvenhold, beata, Arrays.asList(cloud)) == null;
        assert GameRuleUtils.validateMove(gameMap, elvenhold, beata, Arrays.asList(cloud, cloud)) != null;
        assert GameRuleUtils.validateMove(gameMap, elvenhold, beata, Arrays.asList(cloud, cloud, cloud)) == null;

        // test shortest distance algorithm
        assert gameMap.getDistanceBetween(elvenhold, feodor) == 2;
        assert gameMap.getDistanceBetween(feodor, elvenhold) == 2;
        assert gameMap.getDistanceBetween(elvenhold, beata) == 1;
        assert gameMap.getDistanceBetween(beata, elvenhold) == 1;

        testTownCardDeckShuffle();
    }

    private static void testTownCardDeckShuffle() {
        TownCardDeck deck = new TownCardDeck("123456");
        System.out.println(deck.getTownNames());
    }
}
