package domain;

import java.util.ArrayList;
import java.util.List;

public class DevelopmentCard extends Card {
    private final List<GemAmount> price;
    private final Crystal bonus;
    private final int level;

    public DevelopmentCard(int prestige, Crystal bonus, int level, List<GemAmount> price) {
        super (prestige);
        this.price = price;
        this.bonus = bonus;
        this.level = level;
    }
    public String showCard(){
        return String.format("Level: %s, Gem: %s, Prestige: %s", level, bonus, getPrestige());
    }
}