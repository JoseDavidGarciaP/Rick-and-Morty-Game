package game;

public class Morty extends Character {
    public Morty(String name, int health, int power) {
        super(name, health, power);
    }

    // Implementación de la habilidad especial de Morty
    @Override
    public void useSpecialAbility(Character opponent) {
        int healAmount = (int) (this.maxHealth * 0.4); // Se cura el 40% de su salud máxima
        this.health = Math.min(this.health + healAmount, this.maxHealth);
        System.out.printf("%s usa su habilidad especial y se cura %d puntos de salud. Salud actual: %d%n", name, healAmount, this.health);
    }
}
