package domain;

import java.util.ArrayList;
import java.util.List;

public class NobleCard extends Card {
    private final List<GemAmount> price;

    public NobleCard(int prestige) {
        super (prestige);
        this.price = new ArrayList<>();
    }
}