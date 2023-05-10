package domain;

import domain.i18n.I18n;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    public List<Player> givePlayers(){
        return splendor.getPlayers();
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
    public List<NobleCard> getNobles(){
        return splendor.getNobleCards();
    }

    /**
     * @return The list of DevelopmentCards in-game
     */
    public DevelopmentCard[][] getDevelopmentCardsOnTable(){
        return splendor.getCardsOnBoard();
    }

    /**
     * @return The list of gems in-game.
     */
    public List<GemAmount> getGemStack(){
        return splendor.getGemStack();
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
    public List<Player> getWinners(){
        splendor.decideWinners();
        return splendor.getWinners();
    }

    /**
     * @param gem the gem the player takes. The amount does not matter!
     */
    public String takeTwoGemsOfTheSameType(GemAmount gem){
        if(splendor.getGemStack().get(splendor.getIndexOfGem(gem)).getAmount() >= 4){
            splendor.takeTwoGemsOfTheSameType(gem);
            splendor.endTurn();
            return I18n.translate("boardscreen.gems.succesfullytooktwo");
        }
        return I18n.translate("boardscreen.gems.failedtotaketwo");
    }

    /**
     *
     * @param gems a list of gems that the player wants to take. The amounts do not matter!
     */
    public String takeThreeGemsOfDifferentTypes(List<GemAmount> gems){
        if(gems.stream().allMatch(gem -> splendor.getGemStack().get(splendor.getIndexOfGem(gem)).getAmount() > 0)){
            splendor.takeThreeGemsOfDifferentTypes(gems);
            splendor.endTurn();
            return I18n.translate("boardscreen.gems.succesfullytookthree");
        }
        return I18n.translate("boardscreen.gems.failedtotakethree");
    }

    /**
     *
     * @param card the development card the player wants to take.
     */
    public String takeDevelopmentCard(DevelopmentCard card){
        String[] msg = { "placeholder" };
        if (splendor.takeDevelopmentCard(card, msg)){
            splendor.endTurn();
            return msg[0];
        }
        return msg[0];
    }

    /**
     * @param ref The list of nobles
     * @return Whether or not the Player is able to be visited by a noble.
     */
    public boolean canPlayerGetNobleCard(List<NobleCard> ref){
        ref.addAll(splendor.candidateNobles());
        return ref.size() > 0;
    }

    /**
     * Makes a noble visit the player.
     * @param card The nobe visiting the player
     */
    public void setPlayerNoble(NobleCard card){
        splendor.giveNobleToPlayer(card);
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