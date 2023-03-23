 package domain;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private PlayerMapper playerMapper;
    private List<Player> players;
    private List<DevelopmentCard> developmentCards;
    private List<NobleCard> nobleCards;
    private List<GemAmount> gemStack;
    private List<DevelopmentCard> cardsOnTable;

    public Game() {
        players = new ArrayList<>();
        developmentCards = new ArrayList<>();
        nobleCards = new ArrayList<>();
        gemStack = new ArrayList<>();
        cardsOnTable = new ArrayList<>();
        playerMapper = new PlayerMapper();
    }
    public void addPlayerToGame(String name, Short yearOfBirth){
        players.add(playerMapper.findPlayer(name, yearOfBirth));
    }

    public List<Player> getPlayers() {
        return players;
    }
}
