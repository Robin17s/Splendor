package cui;

import domain.PlayerMapper;

public class test {
    public static void main(String[] args) {
        PlayerMapper playerMapper = new PlayerMapper();
        System.out.print(playerMapper.getPlayers());
    }
}
