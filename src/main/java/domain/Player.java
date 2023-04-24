package domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player {
    private final String name;
    private final int dateOfBirth;
    private int prestige;
    private List<DevelopmentCard> developmentCards;
    private NobleCard nobleCard;
    private List<GemAmount> gemStack;
    private int index;
    
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
    }

    public void addDevelopmentCard(DevelopmentCard developmentCard){
        developmentCards.add(developmentCard);
        updatePrestige();
    }
    
    public int getPrestige() {
    	return prestige;
    }
    
    public void addPrestige(int prestige) {
    	this.prestige += prestige;
    }

    private void updatePrestige(){
        prestige = 0;
        for(DevelopmentCard developmentCard : developmentCards){
            prestige += developmentCard.getPrestige();
        }
    }

    public List<GemAmount> getBonusGems(){
        List<GemAmount> temp = new ArrayList<>();
        temp.addAll(Arrays.asList(new GemAmount(Crystal.Diamond, 0),
                new GemAmount(Crystal.Onyx, 0),
                new GemAmount(Crystal.Emerald, 0),
                new GemAmount(Crystal.Sapphire, 0),
                new GemAmount(Crystal.Ruby, 0)));
        for (DevelopmentCard developmentCard : developmentCards){
            temp.stream().filter(x -> x.getType() == developmentCard.getBonusGem()).findFirst().ifPresent(x -> temp.set(temp.indexOf(x), new GemAmount(x.getType(), x.getAmount() + 1)));
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
            case 0 -> {
                break;
            }
            case 1 -> {
                gemStack
                        .stream()
                        .filter(gem -> gem.getType() == gems.get(0).getType())
                        .findFirst()
                        .ifPresent(gemAmount -> gemStack.set(gemStack.indexOf(gemAmount), new GemAmount(gemAmount.getType(), gemAmount.getAmount() + 2)));
            }
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
    
    public void removeGems() {
    	
    }
    
    public List<GemAmount> getGems() {
    	return gemStack;
    }
    public String getGemsAsString(){
        String output = "";
        for(GemAmount cost : gemStack){
            if (cost.getAmount() > 0)
                output += String.format("[%s: %d]\n", cost.getType(), cost.getAmount());
        }
        return output.substring(0, output.length() - (output.isEmpty() ? 0 : 1));
    }
    public String getDevelopmentCardsAsString(){
        String output = "";
        for(DevelopmentCard card : developmentCards){
            output += String.format("[%s]\n", card.showCard());
        }
        //return output.isEmpty() ? output : output.substring(0, output.length() - 1);
        return output.substring(0, output.length() - (output.isEmpty() ? 0 : 1));
    }
    
    public int getIndex() {
    	return index;
    }
    
    public void setIndex(int index) {
    	this.index = index;
    }
}
