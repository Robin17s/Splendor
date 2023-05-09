package domain;

import java.util.List;

public abstract class Card {
    private final int prestige;
    private final String assetName;
    private final List<GemAmount> price;

    public Card(int prestige, String assetName, List<GemAmount> price) {
        this.prestige = prestige;
        this.assetName = assetName;
        this.price = price;
    }

    public int getPrestige() { return prestige; }

    public String getAssetName() {
        return assetName;
    }
    public List<GemAmount> getPrice() {
        return price;
    }
}