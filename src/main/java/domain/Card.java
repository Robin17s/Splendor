package domain;

public abstract class Card {
    private final byte prestige;

    public Card(byte prestige) { this.prestige = prestige; }

    public byte getPrestige() { return prestige; }
}