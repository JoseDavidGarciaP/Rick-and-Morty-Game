package game;

public class Character {
    private String name;
    private int health;
    private int maxHealth;
    private int power;
    private boolean defense = false;

    public Character(String name, int health, int power) {
        this.name = name; // Almacena el nombre
        this.health = health; // Almacena la salud
        this.maxHealth = health; // Almacena la salud máxima
        this.power = power; // Almacena el poder
    }

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
    public void characterStats() {
        System.out.printf("Nombre: %s, Salud: %d, Poder: %d%n", name, health, power);
    }

    // Método para atacar
    public void attack(Character opponent) {
        int damage = this.power;
        if (opponent.defense) {
            damage /= 2; // Si el oponente está defendiendo, reduce el daño a la mitad
            System.out.printf("%s se defiende y solo recibe %d puntos de daño%n", opponent.name, damage);
            opponent.defense = false; // Termina el estado de defensa
        } else {
            System.out.printf("%s ataca a %s y le quita %d puntos de salud%n", name, opponent.name, damage);
        }
        opponent.health -= damage;
    }

    // Método para curarse
    public void heal() {
        if (health < maxHealth) { // Solo se cura si ha perdido salud
            int lostHealth = maxHealth - health;
            int healing = (int) (lostHealth * 0.3); // Cura el 30% de la salud perdida
            health = Math.min(health + healing, maxHealth); // Asegura no superar la salud máxima
            System.out.printf("%s se ha curado %d puntos de salud. Salud actual: %d%n", name, healing, health);
        } else {
            System.out.printf("%s tiene la salud completa y no puede curarse.%n", name);
        }
    }

    // Método para defenderse
    public void defend() {
        defense = true;
        System.out.printf("%s se prepara para defenderse del próximo ataque.%n", name);
    }

    // Método para usar habilidad especial (abstracto)
    public void useSpecialAbility(Character opponent) {
        // Este método se implementará en las subclases
    }
}
