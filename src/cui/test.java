package cui;

import domain.PlayerMapper;

import java.sql.SQLException;

public class test {
    public static void main(String[] args) {
        PlayerMapper playerMapper = new PlayerMapper();
        /*System.out.print(playerMapper.findPlayer("'Thomas'").getFirstname());
        System.out.println(playerMapper.findPlayer("'Thomas'").getLastname());
        System.out.println(playerMapper.findPlayer("'Thomas'").getDateOfBirth());*/
        playerMapper.findPlayer("Thomas");
    }
}
