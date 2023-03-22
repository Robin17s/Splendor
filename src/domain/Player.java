package domain;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String firstname;
    private final String lastname;
    private final Short dateOfBirth;
    private List<DevelopmentCard> developmentCards;
    private NobleCard nobleCard;
    public Player(String firstname, String lastname, Short dateOfBirth){
        developmentCards = new ArrayList<>();
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Short getDateOfBirth() {
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
