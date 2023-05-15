package domain;

import domain.i18n.I18n;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User playing the game
 */
public class Player {
    private final String name;
    private final int dateOfBirth;
    private int prestige;
    private final List<DevelopmentCard> developmentCards;
    private NobleCard nobleCard;
    private List<GemAmount> gemStack;

    /**
     * Instantiates a new player, based on the given parameters
     * @param name The name of the player
     * @param dateOfBirth The year they were born in
     */
    public Player(String name, int dateOfBirth){
        developmentCards = new ArrayList<>();
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        gemStack = new ArrayList<>();
        prestige = 0;
    }

    /**
     * @return The name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the Player's Gem Stack to the given input
     * @param input The Gem Stack to set
     */
    public void setGemStack(List<GemAmount> input){
        this.gemStack = input;
    }

    /**
     * @return The year the player was born in
     */
    public int getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @return A List of the development cards the player has
     */
    public List<DevelopmentCard> getDevelopmentCards() {
        return developmentCards;
    }

    /**
     * @return The noble card that visited the player
     */
    public NobleCard getNobleCard() {
        return nobleCard;
    }

    /**
     * Makes a noble visit the player
     * @param nobleCard The noble visiting the player
     */
    public void setNobleCard(NobleCard nobleCard) {
        this.nobleCard = nobleCard;
        updatePrestige();
    }

    /**
     * @return The amount of prestige the player has
     */
    public int getPrestige() {
        return prestige;
    }

    /**
     * Sets the prestige of the current player.
     * @param prestige The prestige to set
     */
    public void setPrestige(int prestige){
        this.prestige = prestige;
    }

    /**
     * @return Which gems, along with their amount the player has
     */
    public List<GemAmount> getGems() {
        return gemStack;
    }

    /**
     * Adds a development card to the player inventory
     * @param developmentCard The card to add
     */
    public void addDevelopmentCard(DevelopmentCard developmentCard){
        developmentCards.add(developmentCard);
        updatePrestige();
    }

    /**
     * Recalculates the player prestige, based on the cards they own, and the noble that might have visited them
     */
    private void updatePrestige(){
        prestige = 0;
        for(DevelopmentCard developmentCard : developmentCards){
            prestige += developmentCard.getPrestige();
        }
        if (nobleCard != null)
            prestige += nobleCard.getPrestige();
    }

    /**
     * Calculates the extra gems the player own, based on what their development cards give them
     * @return The extra gems
     */
    public List<GemAmount> getBonusGems(){
        List<GemAmount> temp = new ArrayList<>(Arrays.asList(new GemAmount(Crystal.Diamond, 0),
                new GemAmount(Crystal.Onyx, 0),
                new GemAmount(Crystal.Emerald, 0),
                new GemAmount(Crystal.Sapphire, 0),
                new GemAmount(Crystal.Ruby, 0)));
        for (DevelopmentCard developmentCard : developmentCards){
            temp.stream()
                    .filter(x -> x.getType() == developmentCard.getBonusGem())
                    .findFirst()
                    .ifPresent(x -> temp.set(temp.indexOf(x), new GemAmount(x.getType(), x.getAmount() + 1)));
        }
        return temp;
    }

    /**
     * Initialises the Player's gem stack
     */
    public void generateGemStack() {
        gemStack = Arrays.asList(
                new GemAmount(Crystal.Diamond, 0),
                new GemAmount(Crystal.Onyx, 0),
                new GemAmount(Crystal.Emerald, 0),
                new GemAmount(Crystal.Sapphire, 0),
                new GemAmount(Crystal.Ruby, 0));
    }

    /**
     * Adds gems to the Player's Gem Stack
     * @param gems The gems to add
     */
    public void addGems(List<GemAmount> gems) {
    	switch (gems.size()){
            case 1 -> gemStack
                    .stream()
                    .filter(gem -> gem.getType() == gems.get(0).getType())
                    .findFirst()
                    .ifPresent(gemAmount -> gemStack.set(gemStack.indexOf(gemAmount), new GemAmount(gemAmount.getType(), gemAmount.getAmount() + 2)));
            case 3 -> {
                for (GemAmount amount : gems){
                    gemStack
                            .stream()
                            .filter(gem -> gem.getType() == amount.getType())
                            .findFirst()
                            .ifPresent(gemAmount -> gemStack.set(gemStack.indexOf(gemAmount), new GemAmount(gemAmount.getType(), gemAmount.getAmount() + 1)));
                }
            }
        }
    }

    /**
     * Calculates how much gems the player has, including passive noble- and development card bonuses
     * @return The amount of gems the player has in total
     */
    public List<GemAmount> getTotalGems(){
        List<GemAmount> temp = new ArrayList<>(gemStack);
        List<GemAmount> bonusGems = getBonusGems();
        for (GemAmount gem : bonusGems){
            if (gem.getAmount() > 0){
                temp.stream().filter(x -> x.getType() == gem.getType())
                        .findFirst()
                        .ifPresent(g -> temp.set(temp.indexOf(g), new GemAmount(g.getType(), g.getAmount() + gem.getAmount())));
            }
        }
        return temp;
    }

    /**
     * @return A String representation of the amount of gems the player has
     */
    public String getGemsAsString(){
        StringBuilder output = new StringBuilder();
        for(GemAmount cost : getTotalGems()){
            output.append(String.format("%s: %d\n", I18n.translate(cost.getType().getTranslationKey()), cost.getAmount()));
        }
        return output.substring(0, output.length() - ((output.length() == 0) ? 0 : 1));
    }

    /**
     * @return A String representation of the development cards the player has
     */
    public String getDevelopmentCardsAsString(){
        StringBuilder output = new StringBuilder();
        for(DevelopmentCard card : developmentCards){
            output.append(String.format("[%s]\n", card.showCard()));
        }
        //return output.isEmpty() ? output : output.substring(0, output.length() - 1);
        return output.substring(0, output.length() - ((output.length() == 0) ? 0 : 1));
    }

    /**
     * Converts the current Player instance to its DTO variant
     * @return The DTO variant of {@link Player}
     */
    public PlayerDTO toDTO(){
        return new PlayerDTO(this.name, this.dateOfBirth, this.developmentCards, this.gemStack, this.prestige, this.nobleCard);
    }

    /**
     * DTO variant of {@link Player}
     * @param name The name of the player
     * @param dateOfBirth The year they were born in
     * @param developmentCards The development cards from the player
     * @param gemStack The gems from the player
     * @param prestige The prestige the player has
     * @param nobleCard The noble card from the player
     */
    public record PlayerDTO(String name, int dateOfBirth, List<DevelopmentCard> developmentCards, List<GemAmount> gemStack, int prestige, NobleCard nobleCard) {
        /**
         * Converts the current PlayerDTO instance to its normal variant
         * @return The normal Player variant
         */
        public Player unpack(){
            Player player = new Player(this.name, this.dateOfBirth);
            player.setGemStack(this.gemStack);
            this.developmentCards.forEach(player::addDevelopmentCard);
            player.setPrestige(this.prestige);
            player.setNobleCard(this.nobleCard);
            return player;
        }
    }
}
