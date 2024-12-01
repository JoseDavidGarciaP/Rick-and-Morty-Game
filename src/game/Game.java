package game;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import game.AccesoADatos.DatabaseHandler;

public class Game {
    private static Scanner scanner = new Scanner(System.in);
    private static List<Rick> ricks;
    private static List<Morty> mortys;
    private static Rick selectedRick;
    private static Morty selectedMorty;
    private static int roundCounter = 0; // Contador de rondas

    public static void main(String[] args) {
        try {
            // Cargar los personajes desde la base de datos
            DatabaseHandler.Characters characters = DatabaseHandler.loadCharacters(); //metodo que cargar los personajes
            ricks = characters.getRicks(); //se extraen las listas para ser usadas
            mortys = characters.getMortys();
        } catch (IOException e) {
            System.out.println("Error al cargar los personajes: " + e.getMessage()); //manejo de errores
            return;
        }

        // Seleccionar personajes
        selectedRick = selectCharacter(ricks, "Rick");
        selectedMorty = selectCharacter(mortys, "Morty");


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

    // Método de tipos generico para seleccionar ambos personajes 
    public static <T extends Character> T selectCharacter(List<T> characters, String characterType) {
        while (true) { // Continuar hasta que se haga una selección válida
            try {
                System.out.printf("\nSelecciona tu %s:%n", characterType);
                for (int i = 0; i < characters.size(); i++) {
                    System.out.printf("%d. %s (Salud: %d, Poder: %d)%n", 
                                    i + 1, 
                                    characters.get(i).name, 
                                    characters.get(i).health, 
                                    characters.get(i).power);
                }
                int selection = getIntInput(String.format("Elige un %s: ", characterType)) - 1;
                return characters.get(selection); // Devuelve el personaje seleccionado
            } catch (IndexOutOfBoundsException e) {
                System.out.printf("Selección inválida. Por favor, selecciona un número dentro del rango de %ss.%n", characterType);
            } catch (Exception e) {
                System.out.println("Ha ocurrido un error inesperado: " + e.getMessage());
            }
        }
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
        try {
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
                    if (roundCounter % 3 == 0) {
                        player.useSpecialAbility(opponent);
                    } else {
                        System.out.printf("%s no puede usar su habilidad especial todavía. Debe esperar hasta la próxima ronda.%n", player.name);
                    }
                    break;
                default:
                    System.out.println("Opción no válida, intenta nuevamente.");
            }
        } catch (Exception e) {
            System.out.println("Se produjo un error al ejecutar la acción: " + e.getMessage());
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
