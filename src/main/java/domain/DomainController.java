package domain;

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
    public void startGame(){

    }
}