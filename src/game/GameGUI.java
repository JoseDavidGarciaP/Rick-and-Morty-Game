package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import game.AccesoADatos.DatabaseHandler;

public class GameGUI extends JFrame {
    private JComboBox<String> rickSelector, mortySelector;
    private JTextArea battleLog;
    private JLabel rickStats, mortyStats;
    private JButton attackButton, defendButton, healButton, specialButton;
    private JLabel turnIndicator;
    private Rick selectedRick;
    private Morty selectedMorty;
    private int roundCounter = 0; // Contador de rondas
    private boolean rickTurn;

    private List<Rick> ricks;
    private List<Morty> mortys;

    // Barras de vida
    private JProgressBar rickHealthBar, mortyHealthBar;

    public GameGUI() {
        setTitle("Rick vs Morty - Juego de Batalla");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new CardLayout());

        try {
            DatabaseHandler.Characters characters = DatabaseHandler.loadCharacters();
            ricks = characters.getRicks();
            mortys = characters.getMortys();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los personajes: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Pantalla de selección de personajes
        JPanel selectionPanel = createSelectionPanel();
        add(selectionPanel, "Selection");

        // Pantalla de batalla
        JPanel battlePanel = createBattlePanel();
        add(battlePanel, "Battle");

        // Mostrar la pantalla inicial
        ((CardLayout) getContentPane().getLayout()).show(getContentPane(), "Selection");
    }

    private JPanel createSelectionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Espaciado entre componentes
        gbc.anchor = GridBagConstraints.CENTER;

        // Títulos
        JLabel rickLabel = new JLabel("Selecciona tu Rick:");
        JLabel mortyLabel = new JLabel("Selecciona tu Morty:");

        // Selectores de personajes con colores más intensos
        rickSelector = new JComboBox<>();
        mortySelector = new JComboBox<>();
        ricks.forEach(r -> rickSelector.addItem(r.getName()));
        mortys.forEach(m -> mortySelector.addItem(m.getName()));

        rickSelector.setBackground(new Color(255, 230, 230));
        mortySelector.setBackground(new Color(255, 230, 230)); 

        // Botón de comenzar con un color intenso
        JButton startButton = new JButton("Comenzar Batalla");
        startButton.addActionListener(e -> startBattle());
        startButton.setBackground(new Color(50, 50, 50)); // Gris oscuro
        startButton.setForeground(Color.WHITE);
        startButton.setPreferredSize(new Dimension(150, 50));

        // Panel con recuadro de fondo para la pantalla de selección
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        backgroundPanel.setBackground(new Color(224, 255, 255));  // Fondo azul claro
        backgroundPanel.setPreferredSize(new Dimension(700, 400));

        // Agregar el recuadro con borde redondeado
        backgroundPanel.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50), 5));
        backgroundPanel.setBackground(new Color(211, 211, 211)); // Fondo blanco dentro del recuadro

        // Organizar los componentes usando GridBagLayout
        gbc.gridx = 0; gbc.gridy = 0;
        backgroundPanel.add(rickLabel, gbc);

        gbc.gridx = 1;
        backgroundPanel.add(rickSelector, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        backgroundPanel.add(mortyLabel, gbc);

        gbc.gridx = 1;
        backgroundPanel.add(mortySelector, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        backgroundPanel.add(startButton, gbc);

        panel.add(backgroundPanel);  // Agregar el fondo con el recuadro

        return panel;
    }

    private JPanel createBattlePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
    
        // Panel de estadísticas
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(1, 2, 20, 20));  // 20px de separación entre personajes
    
        JPanel rickPanel = new JPanel();
        rickPanel.setLayout(new BoxLayout(rickPanel, BoxLayout.Y_AXIS));  // Organizar verticalmente
        rickStats = new JLabel();
        rickHealthBar = new JProgressBar(0, 100);
        rickHealthBar.setValue(100);
        rickHealthBar.setStringPainted(true);
        rickHealthBar.setForeground(Color.GREEN);
    
        rickPanel.add(rickStats);
        rickPanel.add(rickHealthBar);
    
        JPanel mortyPanel = new JPanel();
        mortyPanel.setLayout(new BoxLayout(mortyPanel, BoxLayout.Y_AXIS));  // Organizar verticalmente
        mortyStats = new JLabel();
        mortyHealthBar = new JProgressBar(0, 100);
        mortyHealthBar.setValue(100);
        mortyHealthBar.setStringPainted(true);
        mortyHealthBar.setForeground(Color.GREEN);
    
        mortyPanel.add(mortyStats);
        mortyPanel.add(mortyHealthBar);
    
        statsPanel.add(rickPanel);
        statsPanel.add(mortyPanel);
    
        // Registro de batalla
        battleLog = new JTextArea();
        battleLog.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(battleLog);
        battleLog.setBackground(new Color(245, 245, 245));
    
        // Botones de acciones con colores y tamaño ajustado
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Separación entre botones
        attackButton = createButton("Atacar", new Color(255, 0, 0, 229));
        defendButton = createButton("Defender", new Color(0, 0, 255, 229));
        healButton = createButton("Curarse", new Color(0, 255, 0, 229));
        specialButton = createButton("Habilidad Especial", new Color(255, 165, 0, 229));
    
        actionsPanel.add(attackButton);
        actionsPanel.add(defendButton);
        actionsPanel.add(healButton);
        actionsPanel.add(specialButton);
    
        // Listeners de botones
        attackButton.addActionListener(e -> executeAction(1));
        defendButton.addActionListener(e -> executeAction(2));
        healButton.addActionListener(e -> executeAction(3));
        specialButton.addActionListener(e -> executeAction(4));
    
        // Indicador de turno con un panel contenedor adicional para ajustarle el espacio
        JPanel turnIndicatorPanel = new JPanel();
        turnIndicatorPanel.setLayout(new BorderLayout());
        turnIndicator = new JLabel("Es el turno de: ", JLabel.CENTER);
        turnIndicator.setFont(new Font("Arial", Font.BOLD, 16));
        turnIndicator.setForeground(Color.BLACK);
        turnIndicatorPanel.add(turnIndicator, BorderLayout.CENTER);
        
        // Aquí aumentamos el espacio ajustando el preferredSize del panel
        turnIndicatorPanel.setPreferredSize(new Dimension(turnIndicatorPanel.getPreferredSize().width, 60)); // Espacio extra
    
        // Contenedor central para organizar el indicador de turno y el registro de batalla
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(turnIndicatorPanel, BorderLayout.NORTH); // Indicador de turno arriba
        centerPanel.add(logScrollPane, BorderLayout.CENTER);  // Registro de batalla en el centro
    
        panel.add(statsPanel, BorderLayout.NORTH); // Estadísticas en la parte superior
        panel.add(centerPanel, BorderLayout.CENTER); // El panel central con turno y registro
        panel.add(actionsPanel, BorderLayout.SOUTH); // Los botones en la parte inferior
    
        panel.setBackground(new Color(211, 211, 211));
    
        return panel;
    }
    
    

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }

    private void startBattle() {
        selectedRick = ricks.get(rickSelector.getSelectedIndex());
        selectedMorty = mortys.get(mortySelector.getSelectedIndex());

        updateStats();
        battleLog.setText("¡La batalla comienza!\n");

        rickTurn = Math.random() < 0.5;
        battleLog.append((rickTurn ? selectedRick.getName() : selectedMorty.getName()) + " comienza el combate.\n");

        // Cambiar a la pantalla de batalla
        ((CardLayout) getContentPane().getLayout()).show(getContentPane(), "Battle");
        updateTurnIndicator();
    }

    private void executeAction(int option) {
        
    
        Character player = rickTurn ? selectedRick : selectedMorty;
        Character opponent = rickTurn ? selectedMorty : selectedRick;
    
        switch (option) {
            case 1 -> player.attack(opponent, battleLog);
            case 2 -> player.defend(battleLog);
            case 3 -> player.heal(battleLog);
            case 4 -> {
                if (roundCounter % 3 == 0) {
                    player.useSpecialAbility(opponent, battleLog);
                } else {
                    battleLog.append(player.getName() + " no puede usar su habilidad especial aún.\n");
                }
            }
        }
    
        // Actualizar las barras de salud después de cada acción
        updateStats();
        
    
        // Verificar si la batalla ha terminado
        if (selectedRick.getHealth() <= 0 || selectedMorty.getHealth() <= 0) {
            battleLog.append((selectedRick.getHealth() <= 0 ? selectedRick.getName() : selectedMorty.getName()) + " ha perdido.\n");
            JOptionPane.showMessageDialog(this, "Fin del combate", "Juego Terminado", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    
        roundCounter++;
        rickTurn = !rickTurn;
        updateTurnIndicator();
    }
    

    private void updateTurnIndicator() {
        turnIndicator.setText("Es el turno de: " + (rickTurn ? selectedRick.getName() : selectedMorty.getName()));
    }

    private void updateStats() {
        rickStats.setText("<html>" + selectedRick.getName() + "<br>Salud: " + selectedRick.getHealth() + "</html>");
        mortyStats.setText("<html>" + selectedMorty.getName() + "<br>Salud: " + selectedMorty.getHealth() + "</html>");

        rickHealthBar.setValue(selectedRick.getHealth());
        mortyHealthBar.setValue(selectedMorty.getHealth());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameGUI game = new GameGUI();
            game.setVisible(true);
        });
    }
}
