package domain;

import java.util.ArrayList;
import java.util.List;

public class NobleCard extends Card {
    private final List<Bonus> bonuses;

    public NobleCard(byte prestige) {
        super (prestige);
        this.bonuses = new ArrayList<>();
    }
}