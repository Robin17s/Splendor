package domain;

import java.util.List;

/**
 * Defines a Card.
 * <p>
 * This card could be a noble, or it could be a development card.
 */
public abstract class Card {
    private final int prestige;
    private final String assetName;
    private final List<GemAmount> price;

    /**
     * Instantiates a new Card.
     * @param prestige The amount of prestige that's linked to this card
     * @param assetName The name of the asset it should be rendered with
     * @param price The price to buy this card
     */
    public Card(int prestige, String assetName, List<GemAmount> price) {
        this.prestige = prestige;
        this.assetName = assetName;
        this.price = price;
    }

    /**
     * @return The prestige linked to this card.
     */
    public int getPrestige() { return prestige; }

    /**
     * @return The name of the asset.
     */
    public String getAssetName() {
        return assetName;
    }

    /**
     * @return The price of the card
     */
    public List<GemAmount> getPrice() {
        return price;
    }
}