package domain;

public abstract class Card {
    private final int prestige;
    private final String assetName;

    public Card(int prestige, String assetName) {
        this.prestige = prestige;
        this.assetName = assetName;
    }

    public int getPrestige() { return prestige; }

    public String getAssetName() {
        return assetName;
    }
}