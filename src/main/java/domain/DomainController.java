package domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DomainController{
	private Game splendor;
    public DomainController(){
        splendor = new Game();
    }

    public String playerLogOn(String name, int yearOfBirth){
        return splendor.addPlayerToGame(name, yearOfBirth);
    }

    public void removePlayerFromGame(String name, int yearOfBirth) { splendor.removePlayerFromGame(name, yearOfBirth); }

    public List<Player> givePlayers(){
        return splendor.getPlayers();
    }

    public void startGame() throws IOException {
        splendor.generateDevelopmentCards();
        splendor.generateGemStack();
        splendor.generateNobleCards();
        splendor.placeCardsOnBoard();
        splendor.sortPlayers();
        splendor.preparePlayerGems();
    }

    public List<NobleCard> getNobles(){
        return splendor.getNobleCards();
    }

    public DevelopmentCard[][] getDevelopmentCardsOntable(){
        return splendor.getCardsOnBoard();
    }

    public List<GemAmount> getGemStack(){
        return splendor.getGemStack();
    }

    public void addItemsToPlayers(){
        List<GemAmount> gems = new ArrayList<>();
        gems.add(new GemAmount(Crystal.Diamond, 1));
        gems.add(new GemAmount(Crystal.Onyx, 1));
        gems.add(new GemAmount(Crystal.Ruby, 1));
        for (Player player : splendor.getPlayers()){
            player.addGems(gems);
            player.addDevelopmentCard(new DevelopmentCard(2, Crystal.Ruby, 2, gems));
        }
    }
}