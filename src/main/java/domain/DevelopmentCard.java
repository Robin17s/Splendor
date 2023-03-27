package domain;

import java.util.ArrayList;
import java.util.List;

public class DevelopmentCard extends Card {
    private final List<GemAmount> price;
    private final Crystal bonus;
    private final int level;

    public DevelopmentCard(int prestige, Crystal bonus, int level) {
        super (prestige);
        this.price = new ArrayList<>();
        this.bonus = bonus;
        this.level = level;
    }
}