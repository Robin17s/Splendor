package domain;

import java.util.List;

/**
 * Defines a NobleCard.
 */
public class NobleCard extends Card {
    /**
     * Instantiates a new Noble Card.
     * @param prestige The prestige it brings
     * @param assetName The asset to load from
     * @param price The price to buy it
     */
    public NobleCard(int prestige, String assetName, List<GemAmount> price) {
        super (prestige, assetName, price);
    }

    /**
     * Gives a textual representation of the noble.
     * @return The String version of this card.
     */
    public String showCard(){
        return String.format("[Prestige: %s] [COST: %s]", getPrestige(), makeCostString());
    }

    /**
     * Makes a String with the total cost of the card.
     * @return The cost string.
     */
    private String makeCostString(){
        StringBuilder output = new StringBuilder();
        for(GemAmount cost : getPrice()){
            if (cost.getAmount() > 0)
                output.append(String.format("[%s: %d] ", cost.getType(), cost.getAmount()));
        }
        return output.substring(0, output.length() - 1);
    }
}