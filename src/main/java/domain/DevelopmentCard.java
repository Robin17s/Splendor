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
        return String.format("[Level: %s] [Bonus gem: %s] [Prestige: %s] [COST: %s]", level, bonus, getPrestige(), makeCostString());
    }

    private String makeCostString(){
        String output = "";
        for(GemAmount cost : price){
            if (cost.getAmount() > 0)
                output += String.format("[%s: %d] ", cost.getType(), cost.getAmount());
        }
        return output.substring(0, output.length() - 1);
    }

    public List<GemAmount> getPrice(){
        return price;
    }

    public int getLevel() {
        return level;
    }
}