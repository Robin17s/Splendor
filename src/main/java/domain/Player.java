package domain;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private final int dateOfBirth;
    private List<DevelopmentCard> developmentCards;
    private NobleCard nobleCard;
    public Player(String name, int dateOfBirth){
        developmentCards = new ArrayList<>();
        this.name = name;
        this.dateOfBirth = dateOfBirth;
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
    }
}
