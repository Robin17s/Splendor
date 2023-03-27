package domain;

import java.util.ArrayList;
import java.util.List;

public class NobleCard extends Card {
    private final List<GemAmount> price;

    public NobleCard(int prestige, List<GemAmount> cost) {
        super (prestige);
        this.price = cost;
    }
}