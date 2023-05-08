package domain;

import domain.i18n.I18n;
import persistence.PlayerMapper;

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
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 4;

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
            developmentCards.removeAll(cards);
            for(int col = 0; col < 4; col++) {
                matrix[level][col] = cards.get(0);
                cards.remove(0);
            }
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

    public void setPlayerIndexes(){
        for (int i = 0; i<=players.size() - 1; i++){
            players.get(i).setIndex(i);
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
        for (String card : cards) { //  1,Onyx,0,1,1,1,1,0,1Onyx011110
            String[] values = card.split(",");
            if (cardType.equals("development")) {
                result.add(new DevelopmentCard(
                        Integer.parseInt(values[2]),  // Prestige value
                        parseCrystal(values[1]),      // Bonus gem
                        Integer.parseInt(values[0]),  // Level
                        parseCost(values),            // Cost
                        values[8]));                  // AssetName
            } else if (cardType.equals("noble")) {
                result.add(new NobleCard(
                        Integer.parseInt(values[2]),  // Prestige value
                        values[8],                    // AssetName
                        parseCost(values)));          // Cost
            }
        }
        Collections.shuffle(result);

        return result;
    }

    private Crystal parseCrystal(String string) {
        return switch (string) {
            case "onyx" -> Crystal.Onyx;
            case "diamond" -> Crystal.Diamond;
            case "sapphire" -> Crystal.Sapphire;
            case "ruby" -> Crystal.Ruby;
            case "emerald" -> Crystal.Emerald;
            default -> null;
        };
    }

    private List<GemAmount> parseCost(String[] values) {
        List<GemAmount> list = new ArrayList<>();
        for (int i = 3; i < values.length-1; i++) {
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

    public List<NobleCard> endTurn(){
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        return candidateNobles();
    }

    public void takeThreeGemsOfDifferentTypes(List<GemAmount> gems){
        for(GemAmount gem : gems){
            int index = getIndexOfGem(gem);
            gemStack.get(index).subtractOne();
        }
        players.get(currentPlayerIndex).addGems(gems);
    }

    public void takeTwoGemsOfTheSameType(GemAmount gem){
        gemStack.get(getIndexOfGem(gem)).subtractTwo();
        List<GemAmount> gemList = new ArrayList<>();
        gemList.add(gem);
        players.get(currentPlayerIndex).addGems(gemList);
    }

    public boolean takeDevelopmentCard(DevelopmentCard card, String[] msg){
        if (canPlayerAffordCard(card)){
            int index = 0;
            for (int i = 0; i<4; i++){
                if (matrix[card.getLevel()-1][i] == card){
                    index = i;
                    break;
                }
            }
            List<GemAmount> temp = players.get(currentPlayerIndex).getGems();
            for (GemAmount cost : card.getPrice()){
                for (GemAmount inv : players.get(currentPlayerIndex).getBonusGems()){
                    if (cost.getType() == inv.getType()){
                        if (cost.getAmount() > inv.getAmount() && cost.getAmount() != 0){
                            temp.stream().filter(x -> x.getType() == cost.getType()).findFirst().ifPresent(g -> temp.set(temp.indexOf(g), new GemAmount(inv.getType(), players.get(currentPlayerIndex).getTotalGems().stream().filter(c -> c.getType() == inv.getType()).collect(Collectors.toList()).get(0).getAmount() - cost.getAmount())));
                        }
                        else{
                            temp.stream().filter(x -> x.getType() == cost.getType()).findFirst().ifPresent(g -> temp.set(temp.indexOf(g), new GemAmount(inv.getType(), players.get(currentPlayerIndex).getGems().stream().filter(c -> c.getType() == cost.getType()).collect(Collectors.toList()).get(0).getAmount())));
                        }
                    }
                }
            }
            players.get(currentPlayerIndex).setGemStack(temp);
            matrix[card.getLevel()-1][index] = developmentCards.stream().filter(x -> x.getLevel() == card.getLevel()).findFirst().get();
            developmentCards.remove(matrix[card.getLevel()-1][index]);
            players.get(currentPlayerIndex).addDevelopmentCard(card);
            msg[0] = I18n.translate("game.card.buy.success");
            return true;
        }
        msg[0] = I18n.translate("game.card.buy.fail");
        return false;
    }

    private List<GemAmount> makeGemList(){
        List<GemAmount> temp = new ArrayList<>();
        temp.addAll(Arrays.asList(new GemAmount(Crystal.Diamond, 0),
                new GemAmount(Crystal.Onyx, 0),
                new GemAmount(Crystal.Emerald, 0),
                new GemAmount(Crystal.Sapphire, 0),
                new GemAmount(Crystal.Ruby, 0)));
        return temp;
    }

    private List<GemAmount> subtractTwoGemAmountLists(List<GemAmount> list1, List<GemAmount> list2){
        List<GemAmount> output = new ArrayList<>();
        for (GemAmount gem1 : list1){
            for (GemAmount gem2 : list2){
                if (gem1.getType() == gem2.getType()){
                    output.add(new GemAmount(gem1.getType(), gem1.getAmount() - gem2.getAmount()));
                }
            }
        }
        return output;
    }

    private boolean canPlayerAffordCard(DevelopmentCard card){
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

    private boolean canPlayerAffordCard(NobleCard card){
        int playerIndex = currentPlayerIndex - 1 >= 0 ? currentPlayerIndex - 1 : players.size() - 1;
        if (players.get(playerIndex).getNobleCard() == null){
            for (GemAmount cost : card.getPrice()) {
                boolean hasGem = false;
                for (GemAmount temp : players.get(playerIndex).getBonusGems()) {
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
        return false;
    }

    public void giveNobleToPlayer(NobleCard noble){
        int playerIndex = currentPlayerIndex - 1 >= 0 ? currentPlayerIndex - 1 : players.size() - 1;
        for (int i = 0; i<nobleCards.size(); i++){
            if (nobleCards.get(i).getAssetName() == noble.getAssetName()){
                players.get(playerIndex).setNobleCard(nobleCards.get(i));
                nobleCards.remove(i);
                break;
            }
        }
    }

    private int getIndexOfGem(GemAmount gem){
        return IntStream.range(0, gemStack.size()).filter(x -> gemStack.get(x).getType() == gem.getType()).findFirst().getAsInt();
    }


    public boolean canTakeTwoGems(GemAmount gem){
        return compareGemValue(gem, 4);
    }

    public boolean canTakeOneGem(GemAmount gem){
        return compareGemValue(gem, 1);
    }

    private boolean compareGemValue(GemAmount gem, int value){
        GemAmount temp = gemStack.stream().filter(x -> x.getType() == gem.getType()).toList().get(0);
        return temp.getAmount() >= value ;
    }

    public List<NobleCard> candidateNobles(){
        List<NobleCard> temp = new ArrayList<>();
        for (NobleCard card : nobleCards){
            if (canPlayerAffordCard(card)){
                temp.add(card);
            }
        }
        return temp;
    }

    public void removePlayerFromGame(String name, int yearOfBirth) { players.removeIf(player -> player.getName().equals(name) && player.getDateOfBirth() == yearOfBirth); }
}
