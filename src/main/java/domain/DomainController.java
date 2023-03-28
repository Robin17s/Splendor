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
    public List<Player> givePlayers(){
        return splendor.getPlayers();
    }
    public void startGame() throws IOException {
        splendor.generateDevelopmentCards();
        splendor.generateGemStack();
        splendor.generateNobleCards();
        splendor.fillTableCardsDeck();
    }
    public List<? extends Card>getTable(String cardType){
        if (cardType.equals("development")){
            return splendor.getCardsOnTable();
        } else if (cardType.equals("noble")) {
            return splendor.getNobleCards();
        } else
            return null;
    }
    public List<GemAmount> getGemStack(){
        return splendor.getGemStack();
    }
}