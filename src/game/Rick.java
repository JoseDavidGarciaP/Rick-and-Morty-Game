package game;

import javax.swing.*;

public class Rick extends Character {
    public Rick(String name, int health, int power) {
        super(name, health, power);
    }

    // Implementación de la habilidad especial de Rick
    @Override
    public void useSpecialAbility(Character opponent, JTextArea battleLog) {
        int damage = this.getPower() * 2; // Doble daño
        opponent.setHealth(opponent.getHealth() - damage); // Aplica daño al oponente
        battleLog.append(String.format("%s usa su habilidad especial y causa %d puntos de daño a %s%n", getName(), damage, opponent.getName()));
    }
}
