package domain;

import java.io.IOException;
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
}