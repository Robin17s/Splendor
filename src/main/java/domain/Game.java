 package domain;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
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

    public void generateDevelopmentCards() throws IOException {
        //csv info: [Level, Bonus gem, Prestige value, Diamond, Sapphire, Emerald, Ruby, Onyx]
        Path path = Paths.get("C:\\Users\\friso\\OneDrive - Hogeschool Gent\\Bureaublad\\splendorCards.csv");
        List<String> cards = Files.readAllLines(path);
        while (!cards.isEmpty()){
            String card = cards.get(0);
            cards.remove(0);
            String[] values = card.split(",");
            developmentCards.add(new DevelopmentCard(Integer.parseInt(values[2]), parseCrystal(values[1]), Integer.parseInt(values[0]), parseCost(values)));
        }
        //shuffle deck
        //Collections.shuffle(developmentCards);
    }
    public void printDevelopmentCards(){
        for (DevelopmentCard card:
             developmentCards) {
            System.out.println(card.showCard());
        }
    }
    private Crystal parseCrystal(String string) {
        switch (string) {
            case "Onyx":
                return Crystal.Onyx;
            case "Diamond":
                return Crystal.Diamond;
            case "Sapphire":
                return Crystal.Sapphire;
            case "Ruby":
                return Crystal.Ruby;
            case "Emerald":
                return Crystal.Emerald;
            default:
                return null;
        }
    }

    private List<GemAmount> parseCost(String[] values){
        /*List<GemAmount> list = new ArrayList<>();
        list.add(new GemAmount(Crystal.Diamond, Integer.parseInt(values[3])));
        list.add(new GemAmount(Crystal.Sapphire, Integer.parseInt(values[4])));
        list.add(new GemAmount(Crystal.Emerald, Integer.parseInt(values[5])));
        list.add(new GemAmount(Crystal.Ruby, Integer.parseInt(values[6])));
        list.add(new GemAmount(Crystal.Onyx, Integer.parseInt(values[7])));
        //remove items with value 0
        //list.removeIf(x -> x.getAmount() == 0);
        return list;*/

        //better?
        List<GemAmount> list = new ArrayList<>();
        for (int i = 3; i < values.length; i++) {
            int amount = Integer.parseInt(values[i]);
            if (amount != 0) {
                list.add(new GemAmount(Crystal.values()[i - 3], amount));
            }
        }
        return list;
    }
}
