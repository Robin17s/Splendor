package cui;

import domain.*;

import java.io.IOException;
import java.util.Scanner;

public class DemoConsole {
    public static void main(String[] args) throws IOException {
        DemoConsole app = new DemoConsole();
        app.start();
    }
    public void start() throws IOException {
        Scanner scanner = new Scanner(System.in);
        ask("Do you want to start a game?[Y/N]: ");
        if (scanner.next().equalsIgnoreCase("Y")){
            DomainController domainController = new DomainController();
            while (domainController.givePlayers().size() < 2 || domainController.givePlayers().size() < 4){
                ask("Enter the players name: ");
                String name = scanner.next();
                ask("Enter the players year of birth: ");
                int date = scanner.nextInt();
                System.out.println(domainController.playerLogOn(name, date));
                if (domainController.givePlayers().size() >= 2 && domainController.givePlayers().size() < 4)
                    ask("Do you want to add another player?[Y/N]: ");
                if (domainController.givePlayers().size() == 4)
                    break;
                if (domainController.givePlayers().size() >= 2 && scanner.next().equalsIgnoreCase("N"))
                    break;
            }
            domainController.startGame();
            System.out.println("Game created with the following players:");
            domainController.sortPlayers();
            for (Player player :
                    domainController.givePlayers()) {
                System.out.printf("Name: %s, year of birth: %d\n", player.getName(), player.getDateOfBirth());
            }
            System.out.println("Game created with the following development cards on table:");
            for (int level = 0; level < 3; level++){
                for (int card = 0; card < 4; card++){
                    System.out.println(domainController.getDevelopmentCardsOntable()[level][card].showCard());
                }
            }
            System.out.println("Game created with the following noble cards on table:");
            for (NobleCard card :
                    domainController.getNobles()) {
                System.out.println(card.showCard());
            }
            System.out.println("Game created with the following gems on table:");
            for (GemAmount gem :
                    domainController.getGemStack()) {
                System.out.printf("%s %d%n", gem.getType(), gem.getAmount());
            }
        }
    }
    private void ask(String question){
        System.out.print(question);
    }
}
