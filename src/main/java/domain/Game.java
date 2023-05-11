package domain;

import domain.i18n.I18n;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import persistence.PlayerMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Main game class.
 */
public class Game {
    private final PlayerMapper playerMapper;
    private final List<Player> players;
    private List<DevelopmentCard> developmentCards;
    private List<NobleCard> nobleCards;
    private List<GemAmount> gemStack;
    private final DevelopmentCard[][] matrix;
    private int currentPlayerIndex;

    /**
     * The minimum amount of players that can play Splendor
     */
    public static final int MIN_PLAYERS = 2;

    /**
     * The maximum amount of players that can play Splendor
     */
    public static final int MAX_PLAYERS = 4;

    private boolean finalRound;
    private final List<Player> winners;

    /**
     * Instantiates a new Game instance, along with all data needed to make it work.
     */
    public Game() {
        players = new ArrayList<>();
        developmentCards = new ArrayList<>();
        nobleCards = new ArrayList<>();
        gemStack = new ArrayList<>();
        playerMapper = new PlayerMapper();

        // Load players in background
        new Thread(playerMapper::loadPlayers, "PlayerLoader-Worker").start();

        matrix = new DevelopmentCard[3][4];
        currentPlayerIndex = 0;
        finalRound = false;
        winners = new ArrayList<>();
    }

    //region [getters and setters]

    /**
     * @return The List of nobles.
     */
    public List<NobleCard> getNobleCards() {
        return nobleCards;
    }

    /**
     * @return The List of Gem-Amount tuples.
     */
    public List<GemAmount> getGemStack() {
        return gemStack;
    }

    /**
     * @return The list of Players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * @return The matrix of cards on the board
     */
    public DevelopmentCard[][] getCardsOnBoard(){
        return matrix;
    }

    /**
     * @return The index of the current player
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * @return Whether or not this is the final round.
     */
    public boolean getFinalRound(){
        return finalRound;
    }

    /**
     * @return The list of winners, if applicable.
     */
    public List<Player> getWinners(){
        return winners;
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

    /**
     * Places the cards on the board.
     */
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

    /**
     * Sorts the player, to determine who can start.
     */
    public void sortPlayers() {
        // DR_SPEL_STARTER De startspeler is de jongste speler.
        // Zijn er meerder spelers even oud dan wordt de speler met de langste gebruikersnaam genomen.
        // Als ook die gelijk is wordt de speler genomen van wie de gebruikersnaam na omgekeerd alfabetisch sorteren (Z -> A) het eerste voorkomt.

        // sort players
        players.sort(Comparator.comparingInt(Player::getDateOfBirth).thenComparingInt((Player p) -> p.getName().length()).thenComparing((Player p) -> p.getName().substring(0, 1)).reversed());
    }

    /**
     * Generates a Gem Stack for every player.
     */
    public void preparePlayerGems(){
        for (Player player : players){
            player.generateGemStack();
        }
    }

    /**
     * Parses a csv file, and read the contents into a List of cards
     * @param fileName The file to read
     * @param cardType The card type [e.g. noble, development]
     * @return A List of parsed cards
     * @throws IOException If something goes wrong whilst parsing.
     */
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

    /**
     * Parses a crystal, based on a String
     * @param string The crystal type, as a String
     * @return The Crystal type, as a Crystal
     */
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

    /**
     * Parses a list of Crystal-Amount Tuples, based on a StringArray.
     * @param values The array to parse data from
     * @return The parsed list
     */
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

    /**
     * Tries to add a Player to the game
     * @param name The name of the player
     * @param yearOfBirth The year the player was born in
     * @return The status message, e.g. success; failure
     */
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

    /**
     * Ends the player's turn.
     */
    public void endTurn(){
        decideTooManyGems();
        decideFinalRound();
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        if (finalRound && currentPlayerIndex == 0)
            currentPlayerIndex = players.size();
        candidateNobles();
    }

    /**
     * Determines whether or not the player has too much gems in their inventory, and shows a popup if that's the case
     */
    public void decideTooManyGems() {
        Player player = players.get(currentPlayerIndex);
        int amount = player.getGems().stream().mapToInt(GemAmount::getAmount).sum();

        if (amount <= 10) return;

        Map<Crystal, Integer> amountOfGems = new HashMap<>();
        player.getGems().forEach(gemAmount -> amountOfGems.put(gemAmount.getType(), gemAmount.getAmount()));

        // TODO Move to gui
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(I18n.translate("game.toomanygems.title"));
        alert.setHeaderText(I18n.translate("game.toomanygems.title"));
        alert.setContentText(I18n.translate("game.toomanygems.message",
                amountOfGems.get(Crystal.Diamond).toString(),
                amountOfGems.get(Crystal.Sapphire).toString(),
                amountOfGems.get(Crystal.Emerald).toString(),
                amountOfGems.get(Crystal.Ruby).toString(),
                amountOfGems.get(Crystal.Onyx).toString(),
                String.valueOf(amount)));
        alert.getButtonTypes().add(new ButtonType(I18n.translate("gem.diamond")));
        alert.getButtonTypes().add(new ButtonType(I18n.translate("gem.sapphire")));
        alert.getButtonTypes().add(new ButtonType(I18n.translate("gem.emerald")));
        alert.getButtonTypes().add(new ButtonType(I18n.translate("gem.ruby")));
        alert.getButtonTypes().add(new ButtonType(I18n.translate("gem.onyx")));

        String result = alert.showAndWait().orElseGet(() -> new ButtonType("")).getText().toLowerCase();

        if (result.isEmpty()) {
            decideTooManyGems();
            return;
        }

        Crystal crystal =
                result.equalsIgnoreCase(I18n.translate("gem.diamond")) ? Crystal.Diamond :
                        result.equalsIgnoreCase(I18n.translate("gem.sapphire")) ? Crystal.Sapphire :
                                result.equalsIgnoreCase(I18n.translate("gem.emerald")) ? Crystal.Emerald :
                                        result.equalsIgnoreCase(I18n.translate("gem.ruby")) ? Crystal.Ruby :
                                                result.equalsIgnoreCase(I18n.translate("gem.onyx")) ? Crystal.Onyx : null;

        if (crystal == null) {
            decideTooManyGems();
            return;
        }

        List<GemAmount> amounts = player.getGems();

        if (amounts.stream().filter(gemAmount -> gemAmount.getType() == crystal).findFirst().orElseGet(() -> new GemAmount(crystal, 0)).getAmount() == 0) {
            decideTooManyGems();
            return;
        }

        List<GemAmount> newAmounts = amounts.stream().map(gemAmount -> gemAmount.getType() == crystal ? new GemAmount(gemAmount.getType(), gemAmount.getAmount() - 1) : gemAmount).collect(Collectors.toList());
        gemStack = gemStack.stream().map(gemAmount -> gemAmount.getType() == crystal ? new GemAmount(gemAmount.getType(), gemAmount.getAmount() + 1) : gemAmount).collect(Collectors.toList());

        player.setGemStack(newAmounts);
        decideTooManyGems();
    }

    /**
     * Decides whether or not the final round should be played.
     */
    public void decideFinalRound() {
        final int PRESTIGE_LEVEL_GAME_END = 5;
        if (!finalRound) {
            finalRound = players.stream()
                    .anyMatch(player -> player.getPrestige() >= PRESTIGE_LEVEL_GAME_END);
        }
    }

    /**
     * Determines the winner of the game.
     */
    public void decideWinners(){
        int highestPrestige = 0;
        int maxDevCards = Integer.MAX_VALUE;
        for (Player player : players) {
            int prestige = player.getPrestige();
            if (prestige > highestPrestige)
                highestPrestige = prestige;
        }
        final int finalHighestPrestige = highestPrestige;
        winners.addAll(players.stream().filter(x -> x.getPrestige() == finalHighestPrestige).toList());
        if (winners.size() != 1){
            for (Player player : winners) {
                int numDevCards = player.getDevelopmentCards().size();
                if (numDevCards < maxDevCards)
                    maxDevCards = numDevCards;
            }
            final int finalMaxDevCards = maxDevCards;
            winners.clear();
            winners.addAll(players.stream().filter(x -> x.getPrestige() == finalHighestPrestige && x.getDevelopmentCards().size() == finalMaxDevCards).toList());
        }
    }

    /**
     * Provides the logic to subtract 3x1 gems from the gem stack in the board.
     * @param gems The gems to remove
     */
    public void takeThreeGemsOfDifferentTypes(List<GemAmount> gems){
        for(GemAmount gem : gems){
            int index = getIndexOfGem(gem);
            gemStack.get(index).subtractOne();
        }
        players.get(currentPlayerIndex).addGems(gems);
    }

    /**
     * Provides the logic to subtract 1x2 gems from the gem stack in the board.
     * @param gem The gem to remove
     */
    public void takeTwoGemsOfTheSameType(GemAmount gem){
        gemStack.get(getIndexOfGem(gem)).subtractTwo();
        List<GemAmount> gemList = new ArrayList<>();
        gemList.add(gem);
        players.get(currentPlayerIndex).addGems(gemList);
    }

    /**
     * Adds the possibility to buy a development card
     * @param card What developmentcard will be bought
     * @param msg A reference to a message
     * @return Whether or not a Development Card may be bought, with more details in the 'msg' pointer
     */
    public boolean takeDevelopmentCard(DevelopmentCard card, String[] msg){
        if (!canPlayerAffordCard(card)){
            msg[0] = I18n.translate("game.card.buy.fail");
            return false;
        }

        //get card position
        int column = 0;
        int row = card.getLevel()-1;
        for (int i = 0; i<4; i++){
            if (matrix[row][i] == card){
                column = i;
                break;
            }
        }

        //subtract the bonus gems the player has from the price of the card to get the remaining cost
        List<GemAmount> remainingCost = new ArrayList<>();
        for (GemAmount price : card.getPrice()){
            GemAmount bonusGem = players.get(currentPlayerIndex).getBonusGems().stream().filter(bonus -> bonus.getType() == price.getType()).findFirst().orElseThrow(() -> new RuntimeException("Gem not found"));
            int remainingAmount = Math.max(0, price.getAmount() - bonusGem.getAmount());
            if (remainingAmount != 0)
                remainingCost.add(new GemAmount(price.getType(), remainingAmount));
        }

        //the remaining cost will be added back to the games gemstack as well as be subtracted of the gems that the player has
        for (GemAmount remaining : remainingCost){
            //update gemstack
            gemStack.stream()
                    .filter(gemAmount -> gemAmount.getType() == remaining.getType())
                    .findFirst()
                    .ifPresent(gemAmount -> gemStack.set(gemStack.indexOf(gemAmount),
                            new GemAmount(gemAmount.getType(), gemAmount.getAmount() + remaining.getAmount())));
            //update player gems
            players.get(currentPlayerIndex).getGems().stream()
                    .filter(gemAmount -> gemAmount.getType() == remaining.getType())
                    .findFirst()
                    .ifPresent(gemAmount -> players.get(currentPlayerIndex).getGems().set(players.get(currentPlayerIndex).getGems().indexOf(gemAmount),
                            new GemAmount(gemAmount.getType(), gemAmount.getAmount() - remaining.getAmount())));
        }

        //place a new card on the board and give the player his card
        //TODO: catch null in gui
        Optional<DevelopmentCard> newDevelopmentCard = developmentCards.stream().filter(x -> x.getLevel() == card.getLevel()).findFirst();
        matrix[row][column] = newDevelopmentCard.orElse(null);
        developmentCards.remove(matrix[row][column]);
        players.get(currentPlayerIndex).addDevelopmentCard(card);

        msg[0] = I18n.translate("game.card.buy.success");
        return true;
    }

    /**
     * Determines whether or not a player is able to afford a card
     * @param card The card the player wants to buy
     * @return true if the player can afford the card; false otherwise
     */
    private boolean canPlayerAffordCard(DevelopmentCard card){
        for (GemAmount cost : card.getPrice()) {
            boolean hasGem = false;
            for (GemAmount temp : players.get(currentPlayerIndex).getTotalGems()) {
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

    /**
     * Determines whether or not the player is eligible to be visited by a noble
     * @param card The noble
     * @return true if the player can be visited, false otherwise
     */
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

    /**
     * Lets a noble visit the player
     * @param noble The noble visiting
     */
    public void giveNobleToPlayer(NobleCard noble){
        int playerIndex = currentPlayerIndex - 1 >= 0 ? currentPlayerIndex - 1 : players.size() - 1;
        for (int i = 0; i<nobleCards.size(); i++){
            if (nobleCards.get(i).getAssetName().equals(noble.getAssetName())){
                players.get(playerIndex).setNobleCard(nobleCards.get(i));
                nobleCards.remove(i);
                break;
            }
        }
    }

    /**
     * @param gem The gem to fetch the index of
     * @return The index of the gem
     */
    public int getIndexOfGem(GemAmount gem){
        return IntStream.range(0, gemStack.size()).filter(x -> gemStack.get(x).getType() == gem.getType()).findFirst().orElseThrow(() -> new RuntimeException("Gem not found"));
    }

    /**
     * Returns a List with all nobles that may be able to visit a player.
     * @return The list
     */
    public List<NobleCard> candidateNobles(){
        List<NobleCard> temp = new ArrayList<>();
        for (NobleCard card : nobleCards){
            if (canPlayerAffordCard(card)){
                temp.add(card);
            }
        }
        return temp;
    }

    /**
     * Removes a player from the game
     * @param name The name of the player
     * @param yearOfBirth The year they were born in
     */
    public void removePlayerFromGame(String name, int yearOfBirth) { players.removeIf(player -> player.getName().equalsIgnoreCase(name) && player.getDateOfBirth() == yearOfBirth); }
}
