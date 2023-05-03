package cui;

import domain.*;
import domain.i18n.I18n;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DemoConsole {
    public static void main(String[] args) throws IOException {
        I18n.loadTranslationFile("en_US");
        DemoConsole app = new DemoConsole();
        app.start();
    }
    public void start() throws IOException {
        Scanner scanner = new Scanner(System.in);

        ask(I18n.translate("console.start.start_game"));

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
                    ask(I18n.translate("console.start.add_other"));

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
            System.out.println(I18n.translate("console.start.created.players"));
            printPlayers(domainController);

            // print table
            System.out.println(I18n.translate("console.start.created.developmentcards"));
            printDevelopmentCards(domainController);

            // print nobles
            System.out.println(I18n.translate("console.start.created.nobles"));
            printNobles(domainController);

            // print gems
            System.out.println(I18n.translate("console.start.created.gems"));
            printAvailableGems(domainController);

            // print starting player (first player in the list)
            System.out.println(I18n.translate("console.start.starter"));
            System.out.println(domainController.givePlayers().get(0).getName());

            domainController.addItemsToPlayers();

            // print all players with the items they have
            System.out.println(I18n.translate("console.start.scenario"));
            printPlayersWithCardsAndGems(domainController);
        }
    }
    private void ask(String question){
        System.out.print(question);
    }

    private void printDevelopmentCards(DomainController domainController){
        int cardCount = 1;
        for (int level = 0; level < 3; level++){
            for (int card = 0; card < 4; card++){
                System.out.println(I18n.translate("console.devcards.print", String.valueOf(cardCount), domainController.getDevelopmentCardsOntable()[level][card].showCard()));
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
            System.out.println(I18n.translate("console.gems.print", gem.getType().toString(), String.valueOf(gem.getAmount())));
        }
    }

    private void printPlayers(DomainController domainController){
        for (Player player : domainController.givePlayers()) {
            System.out.println(I18n.translate("console.players.print", player.getName(), String.valueOf(player.getDateOfBirth()), String.valueOf(player.getIndex())));
        }
    }

    private void addPlayer(Scanner scanner, DomainController domainController) {
        String playerName;
        int birthYear;
        while (true) {
            try {
                ask(I18n.translate("console.addplayer.name"));
                playerName = scanner.next();
                ask(I18n.translate("console.addplayer.birth"));
                birthYear = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println(I18n.translate("console.addplayer.exception"));
                scanner.next(); // consume the invalid input
            }
        }
        System.out.println(domainController.playerLogOn(playerName, birthYear));
    }

    private void printPlayersWithCardsAndGems(DomainController domainController){
        for (Player player : domainController.givePlayers()){
            System.out.println(I18n.translate("console.printall", player.getName(), player.getDevelopmentCardsAsString(), player.getGemsAsString()));
        }
    }
}
