package cui;

import domain.*;
import domain.i18n.I18n;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Main class for the console edition of Splendor.
 * <p>
 * To play the console edition of Splendor, execute the main method of this class.
 */
public class DemoConsole {
    /**
     * Entrypoint for the console edition of Splendor.
     * @param args The arguments the JVM started with
     * @throws IOException When an input is invalid
     */
    public static void main(String[] args) throws IOException {
        I18n.loadTranslationFile("en_US");
        DemoConsole app = new DemoConsole();
        app.start();
    }

    /**
     * Starts the game loop of Splendor console edition.
     * <p>
     * While it is possible to call this method on its own, it's always recommended to use the {@link #main(String[])} method instead, since this method always initialises the game state the way it should be.
     * @throws IOException When an input is invalid
     */
    public void start() throws IOException {
        Scanner scanner = new Scanner(System.in);

        ask(I18n.translate("console.start.start_game"));

        // start gathering info
        if (scanner.next().equalsIgnoreCase("Y")){
            DomainController domainController = new DomainController();

            while (domainController.givePlayers().size() < Game.MIN_PLAYERS || domainController.givePlayers().size() < Game.MAX_PLAYERS){
                // add a player to the game
                addPlayer(scanner, domainController);

                // break out of the loop when 4 players have been added to the game
                if (domainController.givePlayers().size() == Game.MAX_PLAYERS)
                    break;

                // if the game has 2 or more players, ask to add another player
                if (domainController.givePlayers().size() >= Game.MIN_PLAYERS)
                    ask(I18n.translate("console.start.add_other"));

                // if no additional players need to be added, break out of the loop.
                // The check if 2 or more players are already added is necessary.
                // Without this, the scanner will listen after a player is added without the question being asked if another player should be added
                if (domainController.givePlayers().size() >= Game.MIN_PLAYERS && scanner.next().equalsIgnoreCase("N"))
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
            System.out.println(domainController.givePlayers().get(0).name());

            domainController.addItemsToPlayers();

            // print all players with the items they have
            // TODO: fix 1 item displaying 2 times
            System.out.println(I18n.translate("console.start.scenario"));
            printPlayersWithCardsAndGems(domainController);
        }
    }

    /**
     * Prints a question to the console output.
     * @param question The question to print out
     */
    private void ask(String question){
        System.out.print(question);
    }

    /**
     * Prints all development cards present on the board of this game.
     * @param domainController The DomainController managing this game instance.
     */
    private void printDevelopmentCards(DomainController domainController){
        int cardCount = 1;
        for (int level = 0; level < 3; level++){
            for (int card = 0; card < 4; card++){
                System.out.println(I18n.translate("console.devcards.print", String.valueOf(cardCount), domainController.showDevelopmentCard(domainController.getDevelopmentCardsOnTable()[level][card])));
                cardCount++;
            }
        }
    }

    /**
     * Prints all nobles present on the board of this game.
     * @param domainController The DomainController managing this game instance.
     */
    private void printNobles(DomainController domainController){
        for (NobleCard.NobleCardDTO card : domainController.getNobles()) {
            System.out.println(domainController.showNobleCard(card));
        }
    }

    /**
     * Prints all available gems on the board of this game.
     * @param domainController The DomainController managing this game instance.
     */
    private void printAvailableGems(DomainController domainController){
        for (GemAmount.GemAmountDTO gem : domainController.getGemStack()) {
            System.out.println(I18n.translate("console.gems.print", gem.type().toString(), String.valueOf(gem.amount())));
        }
    }

    /**
     * Prints all players taking part in this game.
     * @param domainController The DomainController managing this game instance.
     */
    private void printPlayers(DomainController domainController){
        for (Player.PlayerDTO player : domainController.givePlayers()) {
            System.out.println(I18n.translate("console.players.print", player.name(), String.valueOf(player.dateOfBirth()), String.valueOf(domainController.givePlayers().indexOf(player))));
        }
    }

    /**
     * Queries the console for a valid player, and attempts to add it to the game instance.
     * @param scanner A Scanner instance used for fetching user input.
     * @param domainController The DomainController managing this game instance.
     */
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

    /**
     * Prints all players, including their statistics, like gems and cards, taking part in this game.
     * @param domainController The DomainController managing this game instance.
     */
    private void printPlayersWithCardsAndGems(DomainController domainController){
        for (Player.PlayerDTO player : domainController.givePlayers()){
            System.out.println(I18n.translate("console.printall", player.name(), domainController.getPlayerDevelopmentcardsAsString(player), domainController.getPlayerGemsAsString(player)));
        }
    }
}
