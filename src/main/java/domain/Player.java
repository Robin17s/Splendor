package domain;

import domain.i18n.I18n;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player {
    private final String name;
    private final int dateOfBirth;
    private int prestige;
    private final List<DevelopmentCard> developmentCards;
    private NobleCard nobleCard;
    private List<GemAmount> gemStack;
    
    public Player(String name, int dateOfBirth){
        developmentCards = new ArrayList<>();
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        gemStack = new ArrayList<>();
        prestige = 0;
    }

    public String getName() {
        return name;
    }
    public void setGemStack(List<GemAmount> input){
        this.gemStack = input;
    }

    public int getDateOfBirth() {
        return dateOfBirth;
    }
    
    public List<DevelopmentCard> getDevelopmentCards() {
        return developmentCards;
    }

    public NobleCard getNobleCard() {
        return nobleCard;
    }

    public void setNobleCard(NobleCard nobleCard) {
        this.nobleCard = nobleCard;
        updatePrestige();
    }

    public int getPrestige() {
        return prestige;
    }

    public List<GemAmount> getGems() {
        return gemStack;
    }

    public void addDevelopmentCard(DevelopmentCard developmentCard){
        developmentCards.add(developmentCard);
        updatePrestige();
    }

    private void updatePrestige(){
        prestige = 0;
        for(DevelopmentCard developmentCard : developmentCards){
            prestige += developmentCard.getPrestige();
        }
        if (nobleCard != null)
            prestige += nobleCard.getPrestige();
    }

    public List<GemAmount> getBonusGems(){
        List<GemAmount> temp = new ArrayList<>(Arrays.asList(new GemAmount(Crystal.Diamond, 0),
                new GemAmount(Crystal.Onyx, 0),
                new GemAmount(Crystal.Emerald, 0),
                new GemAmount(Crystal.Sapphire, 0),
                new GemAmount(Crystal.Ruby, 0)));
        for (DevelopmentCard developmentCard : developmentCards){
            temp.stream()
                    .filter(x -> x.getType() == developmentCard.getBonusGem())
                    .findFirst()
                    .ifPresent(x -> temp.set(temp.indexOf(x), new GemAmount(x.getType(), x.getAmount() + 1)));
        }
        return temp;
    }

    public void generateGemStack() {
        gemStack = Arrays.asList(
                new GemAmount(Crystal.Diamond, 0),
                new GemAmount(Crystal.Onyx, 0),
                new GemAmount(Crystal.Emerald, 0),
                new GemAmount(Crystal.Sapphire, 0),
                new GemAmount(Crystal.Ruby, 0));
    }
    
    public void addGems(List<GemAmount> gems) {
    	switch (gems.size()){
            case 1 -> gemStack
                    .stream()
                    .filter(gem -> gem.getType() == gems.get(0).getType())
                    .findFirst()
                    .ifPresent(gemAmount -> gemStack.set(gemStack.indexOf(gemAmount), new GemAmount(gemAmount.getType(), gemAmount.getAmount() + 2)));
            case 3 -> {
                for (GemAmount amount : gems){
                    gemStack
                            .stream()
                            .filter(gem -> gem.getType() == amount.getType())
                            .findFirst()
                            .ifPresent(gemAmount -> gemStack.set(gemStack.indexOf(gemAmount), new GemAmount(gemAmount.getType(), gemAmount.getAmount() + 1)));
                }
            }
        }
    }

    public List<GemAmount> getTotalGems(){
        List<GemAmount> temp = new ArrayList<>(gemStack);
        List<GemAmount> bonusGems = getBonusGems();
        for (GemAmount gem : bonusGems){
            if (gem.getAmount() > 0){
                temp.stream().filter(x -> x.getType() == gem.getType())
                        .findFirst()
                        .ifPresent(g -> temp.set(temp.indexOf(g), new GemAmount(g.getType(), g.getAmount() + gem.getAmount())));
            }
        }
        return temp;
    }
    public String getGemsAsString(){
        StringBuilder output = new StringBuilder();
        for(GemAmount cost : getTotalGems()){
            output.append(String.format("%s: %d\n", I18n.translate(cost.getType().getTranslationKey()), cost.getAmount()));
        }
        return output.substring(0, output.length() - ((output.length() == 0) ? 0 : 1));
    }
    public String getDevelopmentCardsAsString(){
        StringBuilder output = new StringBuilder();
        for(DevelopmentCard card : developmentCards){
            output.append(String.format("[%s]\n", card.showCard()));
        }
        //return output.isEmpty() ? output : output.substring(0, output.length() - 1);
        return output.substring(0, output.length() - ((output.length() == 0) ? 0 : 1));
    }
}
