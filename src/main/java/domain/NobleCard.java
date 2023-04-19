package domain;

import java.util.ArrayList;
import java.util.List;

public class NobleCard extends Card {
    private final List<GemAmount> price;

    public NobleCard(int prestige, List<GemAmount> cost) {
        super (prestige);
        this.price = cost;
    }
    public String showCard(){
        return String.format("[Prestige: %s] [COST: %s]", getPrestige(), makeCostString());
    }
    private String makeCostString(){
        String output = "";
        for(GemAmount cost : price){
            if (cost.getAmount() > 0)
                output += String.format("[%s: %d] ", cost.getType(), cost.getAmount());
        }
        return output.substring(0, output.length() - 1);
    }
}