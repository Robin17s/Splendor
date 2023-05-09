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

public class Game {
    private final PlayerMapper playerMapper;
    private final List<Player> players;
    private List<DevelopmentCard> developmentCards;
    private List<NobleCard> nobleCards;
    private List<GemAmount> gemStack;
    private final DevelopmentCard[][] matrix;
    private int currentPlayerIndex;
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 4;
    private boolean finalRound;
    private final List<Player> winners;

    public Game() {
        players = new ArrayList<>();
        developmentCards = new ArrayList<>();
        nobleCards = new ArrayList<>();
        gemStack = new ArrayList<>();
        playerMapper = new PlayerMapper();
        matrix = new DevelopmentCard[3][4];
        currentPlayerIndex = 0;
        finalRound = false;
        winners = new ArrayList<>();
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

    public boolean getFinalRound(){
        return finalRound;
    }

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

    public void endTurn(){
        decideTooManyGems();
        decideFinalRound();
        if (!finalRound){
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
        else {
            if (currentPlayerIndex == players.size() - 1){
                currentPlayerIndex++;
            }
            else{
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            }
        }
        candidateNobles();
    }

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
        List<GemAmount> newAmounts = amounts.stream().map(gemAmount -> gemAmount.getType() == crystal ? new GemAmount(gemAmount.getType(), gemAmount.getAmount() - 1) : gemAmount).collect(Collectors.toList());
        gemStack = gemStack.stream().map(gemAmount -> gemAmount.getType() == crystal ? new GemAmount(gemAmount.getType(), gemAmount.getAmount() + 1) : gemAmount).collect(Collectors.toList());

        player.setGemStack(newAmounts);
        decideTooManyGems();
    }

    public void decideFinalRound(){
        if (!finalRound){
            for (Player player : players){
                int DR_GAME_END = 15;
                if (player.getPrestige() >= DR_GAME_END){
                    finalRound = true;
                    break;
                }
            }
        }
    }

    public void decideWinners(){
        int highestPrestige = 0;
        int devCards = 500;
        for (Player player : players){
            if (player.getPrestige() > highestPrestige)
                highestPrestige = player.getPrestige();
        }
        final int p = highestPrestige;
        winners.addAll(players.stream().filter(x -> x.getPrestige() == p).toList());
        if (winners.size() != 1){
            for (Player player : winners){
                if (player.getDevelopmentCards().size() < devCards)
                    devCards = player.getDevelopmentCards().size();
            }
            final int cards = devCards;
            winners.clear();
            winners.addAll(players.stream().filter(x -> x.getPrestige() == p && x.getDevelopmentCards().size() == cards).toList());
        }
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
            List<GemAmount> originalPlayerGems = new ArrayList<>(players.get(currentPlayerIndex).getGems());

            for (GemAmount cost : card.getPrice()){
                for (GemAmount inv : players.get(currentPlayerIndex).getBonusGems()){
                    if (cost.getType() == inv.getType()){
                        if (cost.getAmount() > inv.getAmount() && cost.getAmount() != 0){
                            temp.stream()
                                    .filter(x -> x.getType() == cost.getType())
                                    .findFirst()
                                    .ifPresent(g -> temp.set(temp.indexOf(g), new GemAmount(inv.getType(), players.get(currentPlayerIndex)
                                            .getTotalGems()
                                            .stream().filter(c -> c.getType() == inv.getType())
                                            .toList()
                                            .get(0)
                                            .getAmount() - cost.getAmount())));
                        }
                        else{
                            temp.stream()
                                    .filter(x -> x.getType() == cost.getType()).findFirst()
                                    .ifPresent(g -> temp.set(temp.indexOf(g), new GemAmount(inv.getType(), players.get(currentPlayerIndex)
                                            .getGems()
                                            .stream()
                                            .filter(c -> c.getType() == cost.getType())
                                            .toList().
                                            get(0).getAmount())));
                        }
                    }
                }
            }
            players.get(currentPlayerIndex).setGemStack(temp);
            matrix[card.getLevel()-1][index] = developmentCards.stream().filter(x -> x.getLevel() == card.getLevel()).findFirst()
                    .orElseThrow(() -> new RuntimeException("Card not found"));
            developmentCards.remove(matrix[card.getLevel()-1][index]);
            players.get(currentPlayerIndex).addDevelopmentCard(card);
            List<GemAmount> trueCardCost = new ArrayList<>();
            for (GemAmount current : players.get(currentPlayerIndex).getGems()){
                for (GemAmount old : originalPlayerGems){
                    if (current.getType() == old.getType()){
                        trueCardCost.add(new GemAmount(old.getType(), old.getAmount() - current.getAmount()));
                    }
                }
            }
            for (GemAmount gemReturn : trueCardCost){
                for (GemAmount gem : gemStack){
                    if (gem.getType() == gemReturn.getType()){
                        gemStack.set(gemStack.indexOf(gem), new GemAmount(gem.getType(), gem.getAmount() + gemReturn.getAmount()));
                    }
                }
            }
            msg[0] = I18n.translate("game.card.buy.success");
            return true;
        }
        msg[0] = I18n.translate("game.card.buy.fail");
        return false;
    }

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
            if (nobleCards.get(i).getAssetName().equals(noble.getAssetName())){
                players.get(playerIndex).setNobleCard(nobleCards.get(i));
                nobleCards.remove(i);
                break;
            }
        }
    }

    public int getIndexOfGem(GemAmount gem){
        return IntStream.range(0, gemStack.size()).filter(x -> gemStack.get(x).getType() == gem.getType()).findFirst().orElseThrow(() -> new RuntimeException("Gem not found"));
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

    public void removePlayerFromGame(String name, int yearOfBirth) { players.removeIf(player -> player.getName().equalsIgnoreCase(name) && player.getDateOfBirth() == yearOfBirth); }
}
