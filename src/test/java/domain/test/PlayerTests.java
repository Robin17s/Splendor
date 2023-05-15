package domain.test;

import domain.Crystal;
import domain.DevelopmentCard;
import domain.NobleCard;
import domain.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

public class PlayerTests {
    @Test
    public void player_dto_equals() {
        Player player = new Player("Test", 2004);
        Player.PlayerDTO dto = player.toDTO();

        Assertions.assertEquals(player.getName(), dto.name());
        Assertions.assertEquals(player.getDateOfBirth(), dto.dateOfBirth());
    }

    @Test
    public void player_dto_unpack_equals() {
        Player player = new Player("Test", 2004);

        Assertions.assertEquals(player.getName(), player.toDTO().unpack().getName());
        Assertions.assertEquals(player.getDateOfBirth(), player.toDTO().unpack().getDateOfBirth());
    }

    @Test
    public void player_addnoble_increasesprestige() {
        Player player = new Player("Test", 2004);
        NobleCard card = new NobleCard(5, "_asset", Collections.emptyList());

        player.setNobleCard(card);

        Assertions.assertEquals(card.getPrestige(), player.getPrestige());
    }

    @Test
    public void player_adddevelopmentcard_doesntincreaseprestige() {
        Player player = new Player("Test", 2004);
        DevelopmentCard card = new DevelopmentCard(0, Crystal.Diamond, 1, Collections.emptyList(), "");

        player.addDevelopmentCard(card);

        Assertions.assertEquals(0, player.getPrestige());
    }

    @Test
    public void player_adddevelopmentcard_increasesprestige() {
        Player player = new Player("Test", 2004);
        DevelopmentCard card = new DevelopmentCard(2, Crystal.Diamond, 1, Collections.emptyList(), "");

        player.addDevelopmentCard(card);

        Assertions.assertEquals(2, player.getPrestige());
    }
}