package cui;

import domain.*;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class SplendorApplicatie {
	public static void main(String[] args) throws IOException {
		DemoUc1Console app = new DemoUc1Console();
		app.start();
	}

	public void start() throws IOException {
		Scanner scanner = new Scanner(System.in);
		String startgame = ask ("Do you want to start a game?[Y/N]: ",scanner);
		if (startgame.equalsIgnoreCase("Y")) {
			DomainController domainController = new DomainController();
			
			
			String playercount = ask("Provide the number of players >2 <=4", scanner);
			int pc = Integer.parseInt(playercount);
			for(int i = 1; i <= pc; i++) {
				String name = ask("Enter the players name: ",scanner);
				String yearofbirth = ask("Enter the players year of birth: ",scanner);
				System.out.println(domainController.playerLogOn(name, Integer.parseInt(yearofbirth)));
			}
			
			
			
			domainController.startGame();
			domainController.sortPlayers();
			System.out.println("Game created with the following players:");
			for (Player player : domainController.givePlayers()) {
				System.out.printf("Name: %s, year of birth: %d\n", player.getName(), player.getDateOfBirth());
			}
			System.out.println("Game created with the following development cards on table:");
			for (DevelopmentCard card : (List<DevelopmentCard>) domainController.getTable("development")) {
				System.out.println(card.showCard());
			}
			System.out.println("Game created with the following noble cards on table:");
			for (NobleCard card : (List<NobleCard>) domainController.getTable("noble")) {
				System.out.println(card.showCard());
			}
			System.out.println("Game created with the following gems on table:");
			for (GemAmount gem : domainController.getGemStack()) {
				System.out.printf("%s %d%n", gem.getType(), gem.getAmount());
			}
		}
	}

	private String ask(String question,Scanner scanner) {
			System.out.print(question); 
			return scanner.next();
		
	}
}

// Bord ligt er (randomized?)
// 
// spelers aangemaakt (
// startspeler bepalen
// phase speler 1 start, is hand  
// 
//MAKE BOARD ARRAY [0,0 - 1,0 - 2,0]
//SHUFFLE CARDS & LAY ON BOARD

//BOARD ARRAY 

//
