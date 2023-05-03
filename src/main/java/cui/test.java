package cui;

import domain.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class test {
    public static void main(String[] args) throws IOException {
        test app = new test();
        app.start();
    }
    public void start() throws IOException {
        Scanner scanner = new Scanner(System.in);

        ask("Do you want to start a game?[Y/N]: ");

        // start gathering info
        if (scanner.next().equalsIgnoreCase("Y")){
            DomainController domainController = new DomainController();

            while (domainController.givePlayers().size() < 2 || domainController.givePlayers().size() < 4){
                // add a player to the game
                addPlayer(scanner, domainController);

                // break out of the loop when 4 players have been added to the game
                if (domainController.givePlayers().size() == 4)
                    break;

                // if the game has 2 or more players, ask to add another player
                if (domainController.givePlayers().size() >= 2)
                    ask("Do you want to add another player?[Y/N]: ");

                // if no additional players need to be added, break out of the loop.
                // The check if 2 or more players are already added is necessary.
                // Without this, the scanner will listen after a player is added without the question being asked if another player should be added
                if (domainController.givePlayers().size() >= 2 && scanner.next().equalsIgnoreCase("N"))
                    break;
            }

            // start the game; this method basically loads everything needed to get started
            domainController.startGame();
            //domainController.givePlayersIndex();

            // print players
            System.out.println("\nGame created with the following players:");
            printPlayers(domainController);

            // print table
            System.out.println("\nGame created with the following development cards on table:");
            printDevelopmentCards(domainController);

            // print nobles
            System.out.println("\nGame created with the following noble cards on table:");
            printNobles(domainController);

            // print gems
            System.out.println("\nGame created with the following gems on table:");
            printAvailableGems(domainController);

            // print starting player (first player in the list)
            System.out.print("\nStarting player: ");
            System.out.println(domainController.givePlayers().get(0).getName());

            //domainController.addItemsToPlayers();

            // print all players with the items they have
            System.out.print("\nPlayers and the items they have: ");
            printPlayersWithCardsAndGems(domainController);

            domainController.takeTwoGemsOfTheSameType(new GemAmount(Crystal.Diamond, 0));

            // print gems
            System.out.println("\nGame created with the following gems on table:");
            printAvailableGems(domainController);

            // print all players with the items they have
            System.out.print("\nPlayers and the items they have: ");
            printPlayersWithCardsAndGems(domainController);

            System.out.println(domainController.getCurrentPlayerIndex());

            List<GemAmount> gems = new ArrayList<>();
            gems.add(new GemAmount(Crystal.Diamond, 1));
            gems.add(new GemAmount(Crystal.Onyx, 1));
            gems.add(new GemAmount(Crystal.Ruby, 1));

            domainController.takeThreeGemsOfDifferentTypes(gems);

            // print gems
            System.out.println("\nGame created with the following gems on table:");
            printAvailableGems(domainController);

            // print all players with the items they have
            System.out.print("\nPlayers and the items they have: ");
            printPlayersWithCardsAndGems(domainController);

            System.out.println(domainController.getCurrentPlayerIndex());
        }
    }
    private void ask(String question){
        System.out.print(question);
    }

    private void printDevelopmentCards(DomainController domainController){
        int cardCount = 1;
        for (int level = 0; level < 3; level++){
            for (int card = 0; card < 4; card++){
                System.out.println(String.format("Card %d: %s", cardCount, domainController.getDevelopmentCardsOntable()[level][card].showCard()));
                cardCount++;
            }
        }
    }

    private void printNobles(DomainController domainController){
        for (NobleCard card : domainController.getNobles()) {
            System.out.println(card.showCard());
        }
    }

    private void printAvailableGems(DomainController domainController){
        for (GemAmount gem : domainController.getGemStack()) {
            System.out.printf("%s %d%n", gem.getType(), gem.getAmount());
        }
    }

    private void printPlayers(DomainController domainController){
        for (Player player : domainController.givePlayers()) {
            System.out.printf("Name: %s, year of birth: %d, Index: %d\n", player.getName(), player.getDateOfBirth(), player.getIndex());
        }
    }

    private void addPlayer(Scanner scanner, DomainController domainController) {
        String playerName;
        int birthYear;
        while (true) {
            try {
                ask("Enter the player's name: ");
                playerName = scanner.next();
                ask("Enter the player's year of birth: ");
                birthYear = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please try again.");
                scanner.next(); // consume the invalid input
            }
        }
        System.out.println(domainController.playerLogOn(playerName, birthYear));
    }

    private void printPlayersWithCardsAndGems(DomainController domainController){
        for (Player player : domainController.givePlayers()){
            System.out.println(String.format("\nPlayer %s has the following items:\n%s\n%s", player.getName(), player.getDevelopmentCardsAsString(), player.getGemsAsString()));
        }
    }
}
