package domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DomainController{
    private Game splendor;
    public DomainController(){
        splendor = new Game();
    }

    public String playerLogOn(String name, int yearOfBirth){
        return splendor.addPlayerToGame(name, yearOfBirth);
    }

    public void removePlayerFromGame(String name, int yearOfBirth) { splendor.removePlayerFromGame(name, yearOfBirth); }

    public List<Player> givePlayers(){
        return splendor.getPlayers();
    }

    public void startGame() throws IOException {
        splendor.generateDevelopmentCards();
        splendor.generateGemStack();
        splendor.generateNobleCards();
        splendor.placeCardsOnBoard();
        splendor.sortPlayers();
        splendor.preparePlayerGems();
        splendor.setPlayerIndexes();
    }

    public List<NobleCard> getNobles(){
        return splendor.getNobleCards();
    }

    public DevelopmentCard[][] getDevelopmentCardsOntable(){
        return splendor.getCardsOnBoard();
    }

    public List<GemAmount> getGemStack(){
        return splendor.getGemStack();
    }

    public void addItemsToPlayers(){
        List<GemAmount> gems = new ArrayList<>();
        gems.add(new GemAmount(Crystal.Diamond, 1));
        gems.add(new GemAmount(Crystal.Onyx, 1));
        gems.add(new GemAmount(Crystal.Ruby, 1));
        for (Player player : splendor.getPlayers()){
            player.addGems(gems);
            player.addDevelopmentCard(new DevelopmentCard(2, Crystal.Ruby, 2, gems, splendor.getCardsOnBoard()[0][player.getIndex()].getAssetName()));
            //player.setNobleCard(splendor.getNobleCards().get(0));
        }
        /*for (int i = 0; i<10;i++){
            splendor.getPlayers().get(0).addDevelopmentCard(new DevelopmentCard(2, Crystal.Ruby, 2, gems, splendor.getCardsOnBoard()[0][0].getAssetName()));
        }*/
    }
    /**
     *
     * @param gem the gem the player takes. The amount does not matter!
     */
    public String takeTwoGemsOfTheSameType(GemAmount gem){
        splendor.takeTwoGemsOfTheSameType(gem);
        splendor.endTurn();
        return "You took two gems";
    }

    /**
     *
     * @param gems a list of gems that the player wants to take. The amounts do not matter!
     */
    public String takeThreeGemsOfDifferentTypes(List<GemAmount> gems){
        splendor.takeThreeGemsOfDifferentTypes(gems);
        splendor.endTurn();
        return "You took three gems";
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
     *
     * @return the index of the player whos turn it is.
     */
    public int getCurrentPlayerIndex(){
        return splendor.getCurrentPlayerIndex();
    }
    public boolean canTakeOneGem(GemAmount gem){
        return splendor.canTakeOneGem(gem);
    }
    public boolean canTakeTwoGems(GemAmount gem){
        return splendor.canTakeTwoGems(gem);
    }
}