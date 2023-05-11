package domain;

import domain.i18n.I18n;

import java.util.List;

/**
 * Defines a DevelopmentCard.
 */
public class DevelopmentCard extends Card {
    private final Crystal bonus;
    private final int level;

    /**
     * Instantiates a new DevelopmentCard.
     * @param prestige The prestige this card brings
     * @param bonus The gem bonus this card brings
     * @param level The level of the card
     * @param price The price to buy the card
     * @param assetName The asset that's linked to this card
     */
    public DevelopmentCard(int prestige, Crystal bonus, int level, List<GemAmount> price, String assetName) {
        super (prestige, assetName, price);
        this.bonus = bonus;
        this.level = level;
    }

    /**
     * Shows a textual representation of this card.
     * @return The String form of this card.
     */
    public String showCard(){
        return I18n.translate("developmentcard.showcard", String.valueOf(level), String.valueOf(bonus), String.valueOf(getPrestige()), makeCostString());
    }

    /**
     * Creates a String containing the cost linked to this card.
     * @return The cost String
     */
    private String makeCostString(){
        StringBuilder output = new StringBuilder();
        for(GemAmount cost : getPrice()){
            if (cost.getAmount() > 0)
                output.append(String.format("[%s: %d] ", cost.getType(), cost.getAmount()));
        }
        return output.substring(0, output.length() - 1);
    }

    /**
     * @return The bonus gem linked to this card.
     */
    public Crystal getBonusGem(){
        return bonus;
    }

    /**
     * @return The level this card has.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Converts the current DevelopmentCard instance to its DTO variant
     * @return The DTO variant of {@link DevelopmentCard}
     */
    public DevelopmentCardDTO toDTO(){
        return new DevelopmentCardDTO(this.getPrestige(), this.bonus, this.level, this.getPrice(), this.getAssetName());
    }

    /**
     * DTO variant of {@link DevelopmentCard}
     * @param prestige The prestige this card brings
     * @param bonus The gem bonus this card brings
     * @param level The level of the card
     * @param price The price to buy the card
     * @param assetName The asset that's linked to this card
     */
    public record DevelopmentCardDTO(int prestige, Crystal bonus, int level, List<GemAmount> price, String assetName){
        /**
         * Converts the current DevelopmentCardDTO instance to its normal variant
         * @return The normal DevelopmentCard variant
         */
        public DevelopmentCard unpack(){
            return new DevelopmentCard(this.prestige, this.bonus, this.level, this.price, this.assetName);
        }
    }
}