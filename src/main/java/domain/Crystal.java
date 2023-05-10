package domain;

public enum Crystal {
    /**
     * A Diamond gem.
     */
    Diamond("gem.diamond"),

    /**
     * A Sapphire gem.
     */
    Sapphire("gem.sapphire"),

    /**
     * An Emerald gem.
     */
    Emerald("gem.emerald"),

    /**
     * A Ruby gem.
     */
    Ruby("gem.ruby"),

    /**
     * An Onyx gem.
     */
    Onyx("gem.onyx");

    private final String translationKey;

    Crystal(String translationKey) {
        this.translationKey = translationKey;
    }

    /**
     * @return The translation key associated with this gem.
     */
    public String getTranslationKey() { return translationKey; }
}