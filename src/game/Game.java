package game;

import java.util.Random;
import java.util.Scanner;

public class Game {
    private static Scanner scanner = new Scanner(System.in);
    private static Rick[] ricks;
    private static Morty[] mortys;
    private static Rick selectedRick;
    private static Morty selectedMorty;
    private static int roundCounter = 0; // Contador de rondas

    public static void main(String[] args) {
        // Inicializar ricks
        ricks = new Rick[]{
            new Rick("Rick Sánchez C-137", 100, 50),
            new Rick("Rick Prime", 100, 55),
            new Rick("Doofus Rick", 80, 30),
            new Rick("Simple Rick", 70, 35),
            new Rick("Tiny Rick", 90, 45),
            new Rick("Pickle Rick", 95, 60),
            new Rick("Rick D. Sanchez", 85, 40)
        };

        // Inicializar mortys
        mortys = new Morty[]{
            new Morty("Morty Smith C-137", 80, 40),
            new Morty("Evil Morty", 85, 45),
            new Morty("President Morty", 90, 50),
            new Morty("Hammer Morty", 75, 35),
            new Morty("Big Head Morty", 70, 30),
            new Morty("Morty Jr.", 60, 25),
            new Morty("Ghost in a Jar Morty", 65, 20)
        };

        // Seleccionar personajes
        selectedRick = selectRick();
        selectedMorty = selectMorty();

        // Mostrar personajes seleccionados
        System.out.println("\nRick seleccionado:");
        selectedRick.characterStats();

        System.out.println("\nMorty seleccionado:");
        selectedMorty.characterStats();

        // Decidir aleatoriamente quién comienza
        boolean rickTurn = decideInitialTurn();

        // Juego principal
        boolean exit = false;

        while (!exit) {
            if (rickTurn) {
                System.out.println("\nEs el turno de Rick: " + selectedRick.name);
                showOptions();
                int option = getIntInput("Elige una opción: ");
                executeAction(selectedRick, selectedMorty, option);
            } else {
                System.out.println("\nEs el turno de Morty: " + selectedMorty.name);
                showOptions();
                int option = getIntInput("Elige una opción: ");
                executeAction(selectedMorty, selectedRick, option);
            }

            // Incrementar el contador de rondas después de cada turno
            roundCounter++;

            // Verificar si algún jugador ha sido derrotado
            if (selectedRick.health <= 0 || selectedMorty.health <= 0) {
                exit = true;
                if (selectedRick.health <= 0) {
                    System.out.println("¡Rick ha sido derrotado! ¡Morty gana!");
                } else {
                    System.out.println("¡Morty ha sido derrotado! ¡Rick gana!");
                }
            }

            rickTurn = !rickTurn; // Alternar turno
        }

        scanner.close();
    }

    // Método para seleccionar Rick
    public static Rick selectRick() {
        System.out.println("\nSelecciona tu Rick:");
        for (int i = 0; i < ricks.length; i++) {
            System.out.printf("%d. %s (Salud: %d, Poder: %d)%n", i + 1, ricks[i].name, ricks[i].health, ricks[i].power);
        }
        int selection = getIntInput("Elige un Rick: ") - 1;
        return ricks[selection];
    }

    // Método para seleccionar Morty
    public static Morty selectMorty() {
        System.out.println("\nSelecciona tu Morty:");
        for (int i = 0; i < mortys.length; i++) {
            System.out.printf("%d. %s (Salud: %d, Poder: %d)%n", i + 1, mortys[i].name, mortys[i].health, mortys[i].power);
        }
        int selection = getIntInput("Elige un Morty: ") - 1;
        return mortys[selection];
    }

    // Mostrar opciones de acción
    public static void showOptions() {
        System.out.println("\n¿Qué quieres hacer en esta ronda?");
        System.out.println("1. Atacar");
        System.out.println("2. Defender");
        System.out.println("3. Curarse");
        System.out.println("4. Usar habilidad especial (disponible cada 3 rondas)");
    }

    // Ejecutar acción seleccionada
    public static void executeAction(Character player, Character opponent, int option) {
        switch (option) {
            case 1:
                player.attack(opponent);
                break;
            case 2:
                player.defend();
                break;
            case 3:
                player.heal();
                break;
            case 4:
                // Comprobar si se puede usar la habilidad especial
                if (roundCounter % 3 == 0) {
                    player.useSpecialAbility(opponent);
                } else {
                    System.out.printf("%s no puede usar su habilidad especial todavía. Debe esperar hasta la próxima ronda.%n", player.name);
                }
                break;
            default:
                System.out.println("Opción no válida, intenta nuevamente.");
        }
    }

    // Determinar quién comienza
    public static boolean decideInitialTurn() {
        Random random = new Random();
        boolean rickTurn = random.nextBoolean();
        String firstTurn = rickTurn ? "Rick comienza" : "Morty comienza";
        System.out.println("\n" + firstTurn);
        return rickTurn;
    }

    // Método para obtener entrada de número entero
    public static int getIntInput(String message) {
        while (true) {
            try {
                System.out.println(message);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingresa una opción válida.");
            }
        }
    }
}
