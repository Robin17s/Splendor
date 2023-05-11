package domain;

import domain.i18n.I18n;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages the entire game. It holds the game instance, and interfaces with all other parts of the application.
 */
public class DomainController{
    private final Game splendor;

    /**
     * Instantiates the Domain Controller.
     */
    public DomainController(){
        splendor = new Game();
    }

    /**
     * Adds a player to the game.
     * @param name The name of the player
     * @param yearOfBirth The year of birth of the player
     * @return The status of the registration, e.g. success; failure
     */
    public String playerLogOn(String name, int yearOfBirth){
        return splendor.addPlayerToGame(name, yearOfBirth);
    }

    /**
     * Removes a player from the game.
     * @param name The name of the player
     * @param yearOfBirth The year the player is born
     */
    public void removePlayerFromGame(String name, int yearOfBirth) { splendor.removePlayerFromGame(name, yearOfBirth); }

    /**
     * @return The list of players taking part in the game.
     */
    public List<Player.PlayerDTO> givePlayers(){
        return splendor.getPlayers().stream().map(Player::toDTO).collect(Collectors.toList());
    }

    /**
     *
     * @param player The player to get the gems from
     * @return The gems from the player formatted as a string
     */
    public String getPlayerGemsAsString(Player.PlayerDTO player){
        return player.unpack().getGemsAsString();
    }

    /**
     *
     * @param playerDTO The player to get the development cards from
     * @return The development cards from the player formatted as a string
     */
    public String getPlayerDevelopmentcardsAsString(Player.PlayerDTO playerDTO){
        return playerDTO.unpack().getDevelopmentCardsAsString();
    }

    /**
     * Starts the game.
     * @throws IOException When an I/O error occurs.
     */
    public void startGame() throws IOException {
        splendor.generateDevelopmentCards();
        splendor.generateGemStack();
        splendor.generateNobleCards();
        splendor.placeCardsOnBoard();
        splendor.sortPlayers();
        splendor.preparePlayerGems();
    }

    /**
     * @return The list of nobles in-game.
     */
    public List<NobleCard.NobleCardDTO> getNobles(){
        return splendor.getNobleCards().stream().map(NobleCard::toDTO).collect(Collectors.toList());
    }

    /**
     *
     * @param nobleCardDTO The noble card to show
     * @return The noble card formatted as a string
     */
    public String showNobleCard(NobleCard.NobleCardDTO nobleCardDTO){
        return nobleCardDTO.unpack().showCard();
    }

    /**
     * @return The list of DevelopmentCards in-game
     */
    public DevelopmentCard.DevelopmentCardDTO[][] getDevelopmentCardsOnTable(){
        DevelopmentCard[][] cardsOnTable = splendor.getCardsOnBoard();
        DevelopmentCard.DevelopmentCardDTO[][] cardsOnTableDTO = new DevelopmentCard.DevelopmentCardDTO[cardsOnTable.length][cardsOnTable[0].length];

        for (int i = 0; i < cardsOnTable.length; i++) {
            for (int j = 0; j < cardsOnTable[i].length; j++) {
                cardsOnTableDTO[i][j] = cardsOnTable[i][j].toDTO();
            }
        }
        return cardsOnTableDTO;
    }

    /**
     *
     * @param developmentCardDTO The development card to show
     * @return The development card formatted as a string
     */
    public String showDevelopmentCard(DevelopmentCard.DevelopmentCardDTO developmentCardDTO){
        return developmentCardDTO.unpack().showCard();
    }

    /**
     * @return The list of gems in-game.
     */
    public List<GemAmount.GemAmountDTO> getGemStack(){
        return splendor.getGemStack().stream().map(GemAmount::toDTO).toList();
    }

    /**
     * @return Whether or not the game is in its final round.
     */
    public boolean isFinalRound(){
        return splendor.getFinalRound();
    }

    /**
     * @return The winners of the game.
     */
    public List<Player.PlayerDTO> getWinners(){
        splendor.decideWinners();
        return splendor.getWinners().stream().map(Player::toDTO).collect(Collectors.toList());
    }

    /**
     * @param gem the gem the player takes. The amount does not matter!
     */
    public String takeTwoGemsOfTheSameType(GemAmount.GemAmountDTO gem){
        if(splendor.getGemStack().get(splendor.getIndexOfGem(gem.unpack())).getAmount() >= 4){
            splendor.takeTwoGemsOfTheSameType(gem.unpack());
            splendor.endTurn();
            return I18n.translate("boardscreen.gems.succesfullytooktwo");
        }
        return I18n.translate("boardscreen.gems.failedtotaketwo");
    }

    /**
     *
     * @param gems a list of gems that the player wants to take. The amounts do not matter!
     */
    public String takeThreeGemsOfDifferentTypes(List<GemAmount.GemAmountDTO> gems){
        if(gems.stream().allMatch(gem -> splendor.getGemStack().get(splendor.getIndexOfGem(gem.unpack())).getAmount() > 0)){
            splendor.takeThreeGemsOfDifferentTypes(gems.stream().map(GemAmount.GemAmountDTO::unpack).collect(Collectors.toList()));
            splendor.endTurn();
            return I18n.translate("boardscreen.gems.succesfullytookthree");
        }
        return I18n.translate("boardscreen.gems.failedtotakethree");
    }

    /**
     *
     * @param card the development card the player wants to take.
     */
    public String takeDevelopmentCard(DevelopmentCard.DevelopmentCardDTO card){
        String[] msg = { "placeholder" };
        if (splendor.takeDevelopmentCard(card.unpack(), msg)){
            splendor.endTurn();
            return msg[0];
        }
        return msg[0];
    }

    /**
     * @param ref The list of nobles
     * @return Whether or not the Player is able to be visited by a noble.
     */
    public boolean canPlayerGetNobleCard(List<NobleCard.NobleCardDTO> ref){
        ref.addAll(splendor.candidateNobles().stream().map(NobleCard::toDTO).toList());
        return ref.size() > 0;
    }

    /**
     * Makes a noble visit the player.
     * @param card The nobe visiting the player
     */
    public void setPlayerNoble(NobleCard.NobleCardDTO card){
        splendor.giveNobleToPlayer(card.unpack());
        splendor.decideFinalRound();
    }

    /**
     * Skips the turn of the current player.
     */
    public void skipTurn(){
        splendor.endTurn();
    }

    /**
     *
     * @return the index of the player whos turn it is.
     */
    public int getCurrentPlayerIndex(){
        return splendor.getCurrentPlayerIndex();
    }

    //region [debug]
    public void addItemsToPlayers(){
        List<GemAmount> gems = new ArrayList<>();
        gems.add(new GemAmount(Crystal.Diamond, 1));
        gems.add(new GemAmount(Crystal.Onyx, 1));
        gems.add(new GemAmount(Crystal.Ruby, 1));
        for (Player player : splendor.getPlayers()){
            player.addGems(gems);
            player.addDevelopmentCard(new DevelopmentCard(2, Crystal.Ruby, 2, gems, splendor.getCardsOnBoard()[0][0].getAssetName()));
        }
    }
    //endregion
}