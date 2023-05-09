package domain;

public enum Crystal {
    Diamond("gem.diamond"),
    Sapphire("gem.sapphire"),
    Emerald("gem.emerald"),
    Ruby("gem.ruby"),
    Onyx("gem.onyx");

    private final String translationKey;

    Crystal(String translationKey) {
        this.translationKey = translationKey;
    }

    public String getTranslationKey() { return translationKey; }
}