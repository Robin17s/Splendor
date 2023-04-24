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
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Game {
    private final PlayerMapper playerMapper;
    private final List<Player> players;
    private List<DevelopmentCard> developmentCards;
    private List<NobleCard> nobleCards;
    private List<GemAmount> gemStack;
    private DevelopmentCard[][] matrix;
    private int currentPlayerIndex;

    public Game() {
        players = new ArrayList<>();
        developmentCards = new ArrayList<>();
        nobleCards = new ArrayList<>();
        gemStack = new ArrayList<>();
        playerMapper = new PlayerMapper();
        matrix = new DevelopmentCard[3][4];
        currentPlayerIndex = 0;
    }

    //region [getters and setters]
    public List<NobleCard> getNobleCards() {
        return nobleCards;
    }

    public List<GemAmount> getGemStack() {
        return gemStack;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public DevelopmentCard[][] getCardsOnBoard(){
        return matrix;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    //endregion

    //region [Game preparation methods]
    /**
     * Generates all the development cards when called
     * @throws IOException when a problem occurs when reading the csv file
     */
    public void generateDevelopmentCards() throws IOException {
        developmentCards = readCardsFromFile("splendorCards.csv", "development")
                .stream()
                .map(card -> (DevelopmentCard) card)
                .collect(Collectors.toList());
    }

    /**
     * Generates the noble cards based on the amount of players when called
     * @throws IOException when a problem occurs when reading the csv file
     */
    public void generateNobleCards() throws IOException {
        int numPlayers = getPlayers().size();
        int limit = switch (numPlayers) {
            case 2 -> 3;
            case 3 -> 4;
            case 4 -> 5;
            default -> 0; // handle invalid player counts
        };
        nobleCards = readCardsFromFile("nobleCards.csv", "noble")
                .stream()
                .limit(limit)
                .map(card -> (NobleCard) card)
                .collect(Collectors.toList());
    }

    /**
     * Generates the gems on the game board based on the amount of players
     */
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

    public void placeCardsOnBoard() {
        for(int level = 0; level < 3; level++) {
            int lvl = level + 1;
            List<DevelopmentCard> cards = developmentCards.stream()
                    .filter(x -> x.getLevel() == lvl)
                    .limit(4)
                    .collect(Collectors.toList());
            for(int col = 0; col < 4; col++) {
                matrix[level][col] = cards.get(0);
                cards.remove(0);
            }
            developmentCards.removeAll(cards);
        }
    }

    public void sortPlayers() {
        players.sort(Comparator.comparingInt(Player::getDateOfBirth));
        Collections.reverse(players);
    }

    public void preparePlayerGems(){
        for (Player player : players){
            player.generateGemStack();
        }
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
    //endregion

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

    public void endTurn(){
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public void takeThreeGems(List<GemAmount> gems){
        for(GemAmount gem : gems){
            int index = getIndexOfGem(gem);
            gemStack.get(index).subtractOne();
        }
        players.get(currentPlayerIndex).addGems(gems);
    }

    public void takeOneGem(GemAmount gem){
        gemStack.get(getIndexOfGem(gem)).subtractTwo();
        List<GemAmount> gemList = new ArrayList<>();
        gemList.add(gem);
        players.get(currentPlayerIndex).addGems(gemList);
    }

    public void takeDevelopmentCard(DevelopmentCard card){
        int index = 0;
        for (int i = 0; i<4; i++){
            if (matrix[card.getLevel()][i] == card){
                index = i;
                break;
            }
        }
        matrix[card.getLevel()][index] = developmentCards.stream().filter(x -> x.getLevel() == card.getLevel()).findFirst().get();
        developmentCards.remove(matrix[card.getLevel()][index]);
        players.get(currentPlayerIndex).addDevelopmentCard(card);
    }

    public boolean canPlayerAffordCard(DevelopmentCard card){
        for (GemAmount cost : card.getPrice()) {
            boolean hasGem = false;
            for (GemAmount temp : players.get(currentPlayerIndex).getGems()) {
                if (temp.getType() == cost.getType() && temp.getAmount() >= cost.getAmount()) {
                    hasGem = true;
                    break;
                }
            }
            if (!hasGem) {
                return false;
            }
        }
        return true;
    }

    private int getIndexOfGem(GemAmount gem){
        return IntStream.range(0, gemStack.size()).filter(x -> gemStack.get(x).getType() == gem.getType()).findFirst().getAsInt();
    }

    public void removePlayerFromGame(String name, int yearOfBirth) { players.removeIf(player -> player.getName().equals(name) && player.getDateOfBirth() == yearOfBirth); }
}
