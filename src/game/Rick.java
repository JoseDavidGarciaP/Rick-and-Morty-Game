package game;

public class Rick extends Character {
    public Rick(String name, int health, int power) {
        super(name, health, power);
    }

    // Implementación de la habilidad especial de Rick
    @Override
    public void useSpecialAbility(Character opponent) {
        int damage = this.getPower() * 2; // Doble daño
        opponent.setHealth(opponent.getHealth() - damage); // Aplica daño al oponente
        System.out.printf("%s usa su habilidad especial y causa %d puntos de daño a %s%n", getName(), damage, opponent.getName());
    }
}
