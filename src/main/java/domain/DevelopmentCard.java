package domain;

import java.util.List;

public class DevelopmentCard extends Card {
    private final List<GemAmount> price;
    private final Crystal bonus;
    private final int level;
    private final String assetName;

    public DevelopmentCard(int prestige, Crystal bonus, int level, List<GemAmount> price, String assetName) {
        super (prestige);
        this.price = price;
        this.bonus = bonus;
        this.level = level;
        this.assetName = assetName;
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

    public Crystal getBonusGem(){
        return bonus;
    }

    public int getLevel() {
        return level;
    }

    public String getAssetName() {
        return assetName;
    }
}