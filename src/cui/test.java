package cui;

import domain.Player;
import domain.PlayerMapper;

import java.sql.SQLException;

public class test {
    public static void main(String[] args) {
        PlayerMapper playerMapper = new PlayerMapper();
        /*System.out.print(playerMapper.findPlayer("'Thomas'").getFirstname());
        System.out.println(playerMapper.findPlayer("'Thomas'").getLastname());
        System.out.println(playerMapper.findPlayer("'Thomas'").getDateOfBirth());*/
        Player player = playerMapper.findPlayer("Thomas");
        System.out.println(player.getFirstname());
        Player test = new Player("Friso", "De Backer", (short)2004);
        playerMapper.createPlayer(test);
    }
}
