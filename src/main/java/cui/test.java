package cui;

import domain.DomainController;
import domain.Player;
import domain.PlayerMapper;

import java.sql.SQLException;
import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        //PlayerMapper playerMapper = new PlayerMapper();
        /*System.out.println(playerMapper.findPlayer("Thomas").getFirstname());
        System.out.println(playerMapper.findPlayer("Thomas").getLastname());
        System.out.println(playerMapper.findPlayer("Thomas").getDateOfBirth());*/
        //Player player = playerMapper.findPlayer("De Backer");
        //System.out.println(player.getFirstname());
        //Player test = new Player("Robin", "idk", (short)2000);
        //playerMapper.createPlayer(test);
        //System.out.println(playerMapper.removePlayer("Friso"));
        //System.out.println(playerMapper.findPlayer("De Backer"));
        test app = new test();
        app.start();
    }
    public void start(){
        Scanner scanner = new Scanner(System.in);
        ask("Do you want to start a game?[Y/N]: ");
        if (scanner.next().toUpperCase().equals("Y")){
            DomainController domainController = new DomainController();
            for (int i = 0; i<4; i++){
                ask("Enter the players name: ");
                String name = scanner.next();
                ask("Enter the players year of birth: ");
                Short date = scanner.nextShort();
                domainController.playerLogOn(name, date);
                if (i != 3)
                    ask("Do you want to add another player?[Y/N]: ");
                if (i == 3 || !scanner.next().toUpperCase().equals("Y"))
                    break;
            }
            domainController.startGame();
            System.out.println("Game created with the following players:");
            for (Player player :
                    domainController.givePlayers()) {
                System.out.printf("Name: %s, year of birth: %d\n", player.getName(), player.getDateOfBirth());
            }
        }
    }
    private void ask(String question){
        System.out.print(question);
    }
}
