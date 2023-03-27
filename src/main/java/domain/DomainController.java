package domain;

import java.io.IOException;
import java.util.List;

public class DomainController{
	private Game splendor;
    public DomainController(){
        splendor = new Game();
    }
    public void playerLogOn(String name, Short yearOfBirth){
        splendor.addPlayerToGame(name, yearOfBirth);
    }
    public List<Player> givePlayers(){
        return splendor.getPlayers();
    }
    public void startGame() throws IOException {
        splendor.generateDevelopmentCards();
        splendor.generateGemStack();
        splendor.generateNobleCards();
    }
}