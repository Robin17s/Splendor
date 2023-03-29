package domain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game {
    private final PlayerMapper playerMapper;
    private final List<Player> players;
    private List<DevelopmentCard> developmentCards;
    private List<NobleCard> nobleCards;
    private List<GemAmount> gemStack;
    private final List<DevelopmentCard> cardsOnTable;
    private Player currentPlayer;
    private Player firstPlayer;
    private int numberOfPlayers;
    private DevelopmentCard[][] matrix = new DevelopmentCard[3][4];

    public Game() {
        players = new ArrayList<>();
        developmentCards = new ArrayList<>();
        nobleCards = new ArrayList<>();
        gemStack = new ArrayList<>();
        cardsOnTable = new ArrayList<>();
        playerMapper = new PlayerMapper();
    }

    public List<NobleCard> getNobleCards() {
        return nobleCards;
    }

    public List<DevelopmentCard> getCardsOnTable() {
        return cardsOnTable;
    }

    public String addPlayerToGame(String name, int yearOfBirth) {
        for (Player player : players) {
            if (player.getName().equals(name) && player.getDateOfBirth() == yearOfBirth) {
                return "Player already added!";
            }
        }
        Player player = playerMapper.findPlayer(name, yearOfBirth);
        if (player == null) {
            return "Player not found!";
        }
        players.add(player);
        return "Player added successfully!";
    }

    public List<GemAmount> getGemStack() {
        return gemStack;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void generateGemStack() {
        int numPlayers = getPlayers().size();
        int numGems;
        switch (numPlayers) {
            case 2 -> numGems = 4;
            case 3 -> numGems = 5;
            case 4 -> numGems = 7;
            default -> {
                // handle invalid number of players
                return;
            }
        }
        gemStack = Arrays.asList(
                new GemAmount(Crystal.Diamond, numGems),
                new GemAmount(Crystal.Onyx, numGems),
                new GemAmount(Crystal.Emerald, numGems),
                new GemAmount(Crystal.Sapphire, numGems),
                new GemAmount(Crystal.Ruby, numGems));
    }

    private Crystal parseCrystal(String string) {
        return switch (string) {
            case "Onyx" -> Crystal.Onyx;
            case "Diamond" -> Crystal.Diamond;
            case "Sapphire" -> Crystal.Sapphire;
            case "Ruby" -> Crystal.Ruby;
            case "Emerald" -> Crystal.Emerald;
            default -> null;
        };
    }

    private List<GemAmount> parseCost(String[] values) {
        List<GemAmount> list = new ArrayList<>();
        for (int i = 3; i < values.length; i++) {
            int amount = Integer.parseInt(values[i]);
            if (amount != 0) {
                list.add(new GemAmount(Crystal.values()[i - 3], amount));
            }
        }
        return list;
    }

    private List<? extends Card> readCardsFromFile(String fileName, String cardType) throws IOException {
        //csv info development: [Level, Gem, Prestige value, Diamond, Sapphire, Emerald, Ruby, Onyx]
        //csv info noble: [Filler, Filler, Prestige value, Diamond, Sapphire, Emerald, Ruby, Onyx]
        Path path = Paths.get("src/main/" + fileName);

        List<String> cards;
        // Use a try-with-resources block to automatically close the stream when done
        try (Stream<String> lines = Files.lines(path)) {
            cards = lines.collect(Collectors.toList());
        }

        List<Card> result = new ArrayList<>();
        for (String card : cards) {
            String[] values = card.split(",");
            if (cardType.equals("development")) {
                result.add(new DevelopmentCard(
                        Integer.parseInt(values[2]),  // Prestige value
                        parseCrystal(values[1]),      // Bonus gem
                        Integer.parseInt(values[0]),  // Level
                        parseCost(values)));          // Cost
            } else if (cardType.equals("noble")) {
                result.add(new NobleCard(
                        Integer.parseInt(values[2]),  // Prestige value
                        parseCost(values)));          // Cost
            }
        }
        Collections.shuffle(result);

        return result;
    }

    public void generateDevelopmentCards() throws IOException {
        developmentCards = readCardsFromFile("splendorCards.csv", "development").stream().map(card -> (DevelopmentCard) card).toList();
    }

    public void generateNobleCards() throws IOException {
        int numPlayers = getPlayers().size();
        int limit = switch (numPlayers) {
            case 2 -> 3;
            case 3 -> 4;
            case 4 -> 5;
            default -> 0; // handle invalid player counts
        };
        nobleCards = readCardsFromFile("nobleCards.csv", "noble").stream().limit(limit).map(card -> (NobleCard) card).toList();
    }

    public void fillTableCardsDeck() {
        // loop through the different levels of development cards
        for (int level = 1; level <= 3; level++) {
            // declare finalLevel variable and assign it the value of the loop variable
            int finalLevel = level;
            // create a stream of the developmentCards list, filter the cards by level, limit the cards to 4, and collect them into a new list named cards
            List<DevelopmentCard> cards = developmentCards.stream()
                    .filter(x -> x.getLevel() == finalLevel)
                    .limit(4)
                    .toList();
            // remove the selected cards from the developmentCards list
            developmentCards.removeAll(cards);
            // add the selected cards to the cardsOnTable list
            cardsOnTable.addAll(cards);
        }
    }
    
    public void placeCardsOnBoard() {
    	
    	for(int level = 0; level < 3; level++) {
    		int lvl = level + 1;
    		List<DevelopmentCard> cards = cardsOnTable.stream()
                    .filter(x -> x.getLevel() == lvl)
                    .limit(4)
                    .toList();
    		for(int col = 0; col < 4; col++) {
    			matrix[level][col] = cards.get(0);
    			cards.remove(0);
    		}
    	}
    }
    
    public DevelopmentCard[][] getCardsOnBoard(){
    	return matrix;
    }
    
    
    public void sortPlayers() {
    	players.sort(Comparator.comparingInt(Player::getDateOfBirth));
    }
    
    public void determineFirstPlayer() {
    	numberOfPlayers = players.size();
    	firstPlayer = players.get(0);
    	currentPlayer = players.get(0);
    }
    
    public Player getFirstPlayer() {
    	return firstPlayer;
    }
    
    public Player getCurrentPlayer() {
    	return currentPlayer;
    }
    
}
