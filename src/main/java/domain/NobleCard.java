package domain;

import java.util.List;

public class NobleCard extends Card {
    public NobleCard(int prestige, String assetName, List<GemAmount> price) {
        super (prestige, assetName, price);
    }

    public String showCard(){
        return String.format("[Prestige: %s] [COST: %s]", getPrestige(), makeCostString());
    }

    private String makeCostString(){
        StringBuilder output = new StringBuilder();
        for(GemAmount cost : getPrice()){
            if (cost.getAmount() > 0)
                output.append(String.format("[%s: %d] ", cost.getType(), cost.getAmount()));
        }
        return output.substring(0, output.length() - 1);
    }
}