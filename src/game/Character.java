package game;

import javax.swing.*;

public abstract class Character { // Cambiar a clase abstracta
    private String name;
    protected int health;
    private int maxHealth;
    private int power;
    private boolean defense = false;

    public Character(String name, int health, int power) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.power = power;
    }

    // Métodos getter y setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public boolean isDefense() {
        return defense;
    }

    public void setDefense(boolean defense) {
        this.defense = defense;
    }

    // Método para mostrar las estadísticas del personaje
    public void characterStats(JTextArea battleLog) {
        battleLog.append("Nombre: " + name + ", Salud: " + health + ", Poder: " + power + "\n");
    }

    // Método para atacar
    public void attack(Character opponent, JTextArea battleLog) {
        int damage = this.power;
        if (opponent.defense) {
            damage /= 2; // Si el oponente está defendiendo, reduce el daño a la mitad
            battleLog.append(opponent.name + " se defiende y solo recibe " + damage + " puntos de daño\n");
            opponent.defense = false; // Termina el estado de defensa
        } else {
            battleLog.append(this.name + " ataca a " + opponent.name + " y le quita " + damage + " puntos de salud\n");
        }
        opponent.health -= damage;
    }

    // Método para curarse
    public void heal(JTextArea battleLog) {
        int lostHealth = this.maxHealth - this.health; // Calcula la salud perdida
        int healing = (int) (lostHealth * 0.3); // Cura el 30% de la salud perdida
        this.health += healing; // Incrementa la salud actual
        this.health = Math.min(this.health, this.maxHealth); // Asegura que no supere la salud máxima
        battleLog.append(name + " se ha curado " + healing + " puntos de salud. Salud actual: " + health + "\n");
    
    }

    // Método para defenderse
    public void defend(JTextArea battleLog) {
        defense = true;
        battleLog.append(name + " se prepara para defenderse del próximo ataque.\n");
    }

    // Método abstracto para habilidad especial (se implementará en subclases)
    public abstract void useSpecialAbility(Character opponent, JTextArea battleLog);
}
