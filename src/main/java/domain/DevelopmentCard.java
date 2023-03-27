package domain;

import java.util.ArrayList;
import java.util.List;

public class DevelopmentCard extends Card {
    private final List<GemAmount> price;
    private final Crystal bonus;
    private final byte level;

    public DevelopmentCard(byte prestige, Crystal bonus, byte level) {
        super (prestige);
        this.price = new ArrayList<>();
        this.bonus = bonus;
        this.level = level;
    }
}