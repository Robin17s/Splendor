 package domain;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

 public class Game {
    private PlayerMapper playerMapper;
    private List<Player> players;
    private List<DevelopmentCard> developmentCards;
    private List<NobleCard> nobleCards;
    private List<GemAmount> gemStack;
    private List<DevelopmentCard> cardsOnTable;

    public Game() {
        players = new ArrayList<>();
        developmentCards = new ArrayList<>();
        nobleCards = new ArrayList<>();
        gemStack = new ArrayList<>();
        cardsOnTable = new ArrayList<>();
        playerMapper = new PlayerMapper();
    }
    public void addPlayerToGame(String name, Short yearOfBirth){
        players.add(playerMapper.findPlayer(name, yearOfBirth));
    }

    public List<Player> getPlayers() {
        return players;
    }
    public void generateGemStack(){
        gemStack = Arrays.asList(
                new GemAmount(Crystal.Diamond, 5),
                new GemAmount(Crystal.Onyx, 5),
                new GemAmount(Crystal.Emerald, 5),
                new GemAmount(Crystal.Sapphire, 5),
                new GemAmount(Crystal.Ruby, 5));
    }

    /*public void generateDevelopmentCards() throws IOException {
        //csv info: [Level, Bonus gem, Prestige value, Diamond, Sapphire, Emerald, Ruby, Onyx]
        Path path = Paths.get("C:\\Users\\friso\\OneDrive - Hogeschool Gent\\Bureaublad\\splendorCards.csv");
        List<String> cards = Files.readAllLines(path);
        while (!cards.isEmpty()){
            String card = cards.get(0);
            cards.remove(0);
            String[] values = card.split(",");
            developmentCards.add(new DevelopmentCard(Integer.parseInt(values[2]), parseCrystal(values[1]), Integer.parseInt(values[0]), parseCost(values)));
        }
        //shuffle deck
        //Collections.shuffle(developmentCards);
    }*/

    /*public void generateDevelopmentCards() throws IOException {
        // Path to the CSV file containing the development card information
        Path path = Paths.get("src/main/splendorCards.csv");

        // List to store the lines of the CSV file
        List<String> cards;

        // Use a try-with-resources block to automatically close the stream when done
        try (Stream<String> lines = Files.lines(path)) {
            // Read all the lines from the file and collect them into a list
            cards = lines.collect(Collectors.toList());
        }

        // Iterate over each line in the list of cards and parse the card information
        for (String card : cards) {
            // Split the line into an array of strings using commas as the delimiter
            String[] values = card.split(",");

            // Create a new development card using the parsed information and add it to the list of development cards
            developmentCards.add(new DevelopmentCard(
                    Integer.parseInt(values[2]),  // Prestige value
                    parseCrystal(values[1]),      // Bonus gem
                    Integer.parseInt(values[0]),  // Level
                    parseCost(values)));          // Cost
        }

        // Shuffle the deck of development cards
        Collections.shuffle(developmentCards);
    }

    public void generateNobleCards() throws IOException {
        //csv info: [Filler, Filler, Prestige value, Diamond, Sapphire, Emerald, Ruby, Onyx]
        Path path = Paths.get("src/main/nobleCards.csv");

        // List to store the lines of the CSV file
        List<String> cards;

        // Use a try-with-resources block to automatically close the stream when done
        try (Stream<String> lines = Files.lines(path)) {
            // Read all the lines from the file and collect them into a list
            cards = lines.collect(Collectors.toList());
        }
        for (String card :
                cards) {
            String[] values = card.split(",");
            nobleCards.add(new NobleCard(Integer.parseInt(values[2]), parseCost(values)));
        }
    }*/
    public void printDevelopmentCards(){
        for (DevelopmentCard card:
             developmentCards) {
            System.out.println(card.showCard());
        }
    }
    private Crystal parseCrystal(String string) {
        switch (string) {
            case "Onyx":
                return Crystal.Onyx;
            case "Diamond":
                return Crystal.Diamond;
            case "Sapphire":
                return Crystal.Sapphire;
            case "Ruby":
                return Crystal.Ruby;
            case "Emerald":
                return Crystal.Emerald;
            default:
                return null;
        }
    }

    private List<GemAmount> parseCost(String[] values){
        /*List<GemAmount> list = new ArrayList<>();
        list.add(new GemAmount(Crystal.Diamond, Integer.parseInt(values[3])));
        list.add(new GemAmount(Crystal.Sapphire, Integer.parseInt(values[4])));
        list.add(new GemAmount(Crystal.Emerald, Integer.parseInt(values[5])));
        list.add(new GemAmount(Crystal.Ruby, Integer.parseInt(values[6])));
        list.add(new GemAmount(Crystal.Onyx, Integer.parseInt(values[7])));
        //remove items with value 0
        //list.removeIf(x -> x.getAmount() == 0);
        return list;*/

        //better
        List<GemAmount> list = new ArrayList<>();
        for (int i = 3; i < values.length; i++) {
            int amount = Integer.parseInt(values[i]);
            if (amount != 0) {
                list.add(new GemAmount(Crystal.values()[i - 3], amount));
            }
        }
        return list;
    }
     private List<? extends Card> readCardsFromFile(String fileName, String cardType) throws IOException {
         //csv info development: [Level, Gem, Prestige value, Diamond, Sapphire, Emerald, Ruby, Onyx]
         //csv info noble: [Filler, Filler, Prestige value, Diamond, Sapphire, Emerald, Ruby, Onyx]
         Path path = Paths.get("src/main/" + fileName);

         List<String> cards;
         // Use a try-with-resources block to automatically close the stream when done
         try (Stream<String> lines = Files.lines(path)) {
             cards = lines.collect(Collectors.toList());
         }

         List<Card> result = new ArrayList<>();
         for (String card : cards) {
             String[] values = card.split(",");
             if (cardType.equals("development")) {
                 result.add(new DevelopmentCard(
                         Integer.parseInt(values[2]),  // Prestige value
                         parseCrystal(values[1]),      // Bonus gem
                         Integer.parseInt(values[0]),  // Level
                         parseCost(values)));          // Cost
             } else if (cardType.equals("noble")) {
                 result.add(new NobleCard(
                         Integer.parseInt(values[2]),  // Prestige value
                         parseCost(values)));          // Cost
             }
         }
         Collections.shuffle(result);

         return result;
     }
     public void generateDevelopmentCards() throws IOException {
         developmentCards = (List<DevelopmentCard>) readCardsFromFile("splendorCards.csv", "development");
     }

     public void generateNobleCards() throws IOException {
         nobleCards = (List<NobleCard>) readCardsFromFile("nobleCards.csv", "noble");
     }
     /*public List<NobleCard> getNobleCards(){
        return nobleCards;
     }*/
 }
