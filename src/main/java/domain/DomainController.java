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
           /* player.addDevelopmentCard(new DevelopmentCard(2, Crystal.Ruby, 2, gems, splendor.getCardsOnBoard()[0][player.getIndex()].getAssetName()));
            player.addDevelopmentCard(new DevelopmentCard(2, Crystal.Ruby, 2, gems, splendor.getCardsOnBoard()[0][player.getIndex()].getAssetName()));
            player.addDevelopmentCard(new DevelopmentCard(2, Crystal.Ruby, 2, gems, splendor.getCardsOnBoard()[0][player.getIndex()].getAssetName()));
            player.addDevelopmentCard(new DevelopmentCard(2, Crystal.Onyx, 2, gems, splendor.getCardsOnBoard()[0][player.getIndex()].getAssetName()));
            player.addDevelopmentCard(new DevelopmentCard(2, Crystal.Onyx, 2, gems, splendor.getCardsOnBoard()[0][player.getIndex()].getAssetName()));
            player.addDevelopmentCard(new DevelopmentCard(2, Crystal.Onyx, 2, gems, splendor.getCardsOnBoard()[0][player.getIndex()].getAssetName()));
            player.addDevelopmentCard(new DevelopmentCard(2, Crystal.Onyx, 2, gems, splendor.getCardsOnBoard()[0][player.getIndex()].getAssetName()));
            player.addDevelopmentCard(new DevelopmentCard(2, Crystal.Diamond, 2, gems, splendor.getCardsOnBoard()[0][player.getIndex()].getAssetName()));
            player.addDevelopmentCard(new DevelopmentCard(2, Crystal.Diamond, 2, gems, splendor.getCardsOnBoard()[0][player.getIndex()].getAssetName()));
            player.addDevelopmentCard(new DevelopmentCard(2, Crystal.Diamond, 2, gems, splendor.getCardsOnBoard()[0][player.getIndex()].getAssetName()));
            player.addDevelopmentCard(new DevelopmentCard(2, Crystal.Diamond, 2, gems, splendor.getCardsOnBoard()[0][player.getIndex()].getAssetName()));
            player.addDevelopmentCard(new DevelopmentCard(2, Crystal.Sapphire, 2, gems, splendor.getCardsOnBoard()[0][player.getIndex()].getAssetName()));
            player.addDevelopmentCard(new DevelopmentCard(2, Crystal.Sapphire, 2, gems, splendor.getCardsOnBoard()[0][player.getIndex()].getAssetName()));
            player.addDevelopmentCard(new DevelopmentCard(2, Crystal.Sapphire, 2, gems, splendor.getCardsOnBoard()[0][player.getIndex()].getAssetName()));
            player.addDevelopmentCard(new DevelopmentCard(2, Crystal.Sapphire, 2, gems, splendor.getCardsOnBoard()[0][player.getIndex()].getAssetName()));
            player.addDevelopmentCard(new DevelopmentCard(2, Crystal.Emerald, 2, gems, splendor.getCardsOnBoard()[0][player.getIndex()].getAssetName()));
            player.addDevelopmentCard(new DevelopmentCard(2, Crystal.Emerald, 2, gems, splendor.getCardsOnBoard()[0][player.getIndex()].getAssetName()));
            player.addDevelopmentCard(new DevelopmentCard(2, Crystal.Emerald, 2, gems, splendor.getCardsOnBoard()[0][player.getIndex()].getAssetName()));
            player.addDevelopmentCard(new DevelopmentCard(2, Crystal.Emerald, 2, gems, splendor.getCardsOnBoard()[0][player.getIndex()].getAssetName()));*/


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
        if(splendor.getGemStack().get(splendor.getIndexOfGem(gem)).getAmount() >= 4){
            splendor.takeTwoGemsOfTheSameType(gem);
            splendor.endTurn();
            return "You took two gems";
        }
        return "there need to be at least 4 gems on the pile to take 2";
    }

    /**
     *
     * @param gems a list of gems that the player wants to take. The amounts do not matter!
     */
    public String takeThreeGemsOfDifferentTypes(List<GemAmount> gems){
        if(gems.stream().allMatch(gem -> splendor.getGemStack().get(splendor.getIndexOfGem(gem)).getAmount() > 0)){
            splendor.takeThreeGemsOfDifferentTypes(gems);
            splendor.endTurn();
            return "You took three gems";
        }
        return "At least 1 type of the chosen gems is not available";
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

    public boolean canPlayerGetNobleCard(List<NobleCard> ref){
        ref.addAll(splendor.candidateNobles());
        if (ref.size() > 0){
            return true;
        }
        return false;
    }

    public void setPlayerNoble(NobleCard card){
        splendor.giveNobleToPlayer(card);
    }
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

    public boolean canTakeOneGem(GemAmount gem){
        return splendor.canTakeOneGem(gem);
    }
    public boolean canTakeTwoGems(GemAmount gem){
        return splendor.canTakeTwoGems(gem);
    }
}