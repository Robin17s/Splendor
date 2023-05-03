package domain;

import domain.i18n.I18n;

import java.util.List;

public class DevelopmentCard extends Card {
    private final List<GemAmount> price;
    private final Crystal bonus;
    private final int level;

    public DevelopmentCard(int prestige, Crystal bonus, int level, List<GemAmount> price, String assetName) {
        super (prestige,assetName);
        this.price = price;
        this.bonus = bonus;
        this.level = level;
    }
    public String showCard(){
        return I18n.translate("developmentcard.showcard", String.valueOf(level), String.valueOf(bonus), String.valueOf(getPrestige()), makeCostString());
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
}