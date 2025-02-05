import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameMenu extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private GameSettings settings;
    private Color backgroundColor = new Color(33, 33, 33);
    private Color textColor = new Color(240, 240, 240);
    private Color buttonColor = new Color(60, 60, 60);
    private Font titleFont = new Font("Arial", Font.BOLD, 36);
    private Font menuFont = new Font("Arial", Font.BOLD, 18);

    public GameMenu() {
        settings = new GameSettings();
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        setTitle("Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));

        createMainMenu();
        createSettingsMenu();
        createCustomizationMenu();

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void createMainMenu() {
        JPanel menuPanel = createStyledPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        // Title
        JLabel titleLabel = createStyledLabel("SNAKE GAME", titleFont);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(Box.createVerticalStrut(50));
        menuPanel.add(titleLabel);
        menuPanel.add(Box.createVerticalStrut(50));

        // Menu buttons
        String[] menuItems = {"Play Game", "Settings", "Customization", "Exit"};
        for (String item : menuItems) {
            JButton button = createStyledButton(item);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.addActionListener(e -> handleMainMenuAction(item));
            menuPanel.add(button);
            menuPanel.add(Box.createVerticalStrut(20));
        }

        mainPanel.add(menuPanel, "MainMenu");
    }

    private void createSettingsMenu() {
        JPanel settingsPanel = createStyledPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = createStyledLabel("Settings", titleFont);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsPanel.add(titleLabel);
        settingsPanel.add(Box.createVerticalStrut(30));

        // Game Speed
        JPanel speedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        speedPanel.setOpaque(false);
        JLabel speedLabel = createStyledLabel("Game Speed:", menuFont);
        JSlider speedSlider = new JSlider(50, 200, settings.getGameSpeed());
        styleSlider(speedSlider);
        speedSlider.addChangeListener(e -> settings.setGameSpeed(speedSlider.getValue()));
        speedPanel.add(speedLabel);
        speedPanel.add(speedSlider);
        settingsPanel.add(speedPanel);

        // Initial Length
        JPanel lengthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lengthPanel.setOpaque(false);
        JLabel lengthLabel = createStyledLabel("Initial Length:", menuFont);
        SpinnerNumberModel lengthModel = new SpinnerNumberModel(6, 3, 10, 1);
        JSpinner lengthSpinner = new JSpinner(lengthModel);
        styleSpinner(lengthSpinner);
        lengthSpinner.addChangeListener(e ->
                settings.setInitialLength((Integer)lengthSpinner.getValue()));
        lengthPanel.add(lengthLabel);
        lengthPanel.add(lengthSpinner);
        settingsPanel.add(lengthPanel);

        // Grid Lines
        JCheckBox gridLinesBox = new JCheckBox("Show Grid Lines");
        styleCheckBox(gridLinesBox);
        gridLinesBox.setSelected(settings.isGridLines());
        gridLinesBox.addActionListener(e ->
                settings.setGridLines(gridLinesBox.isSelected()));
        settingsPanel.add(gridLinesBox);

        // Dark Mode
        JCheckBox darkModeBox = new JCheckBox("Dark Mode");
        styleCheckBox(darkModeBox);
        darkModeBox.setSelected(settings.isDarkMode());
        darkModeBox.addActionListener(e ->
                settings.setDarkMode(darkModeBox.isSelected()));
        settingsPanel.add(darkModeBox);

        // Back Button
        JButton backButton = createStyledButton("Back to Main Menu");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsPanel.add(Box.createVerticalStrut(30));
        settingsPanel.add(backButton);

        mainPanel.add(settingsPanel, "Settings");
    }

    private void createCustomizationMenu() {
        JPanel customPanel = createStyledPanel();
        customPanel.setLayout(new BoxLayout(customPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = createStyledLabel("Customization", titleFont);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        customPanel.add(titleLabel);
        customPanel.add(Box.createVerticalStrut(30));

        // Snake Customization
        addCustomizationSection(customPanel, "Snake Color",
                new String[]{"Green", "Blue", "Red", "Yellow", "Purple"});
        addCustomizationSection(customPanel, "Snake Shape",
                new String[]{"Rectangle", "Circle", "Triangle"});

        // Food Customization
        addCustomizationSection(customPanel, "Food Color",
                new String[]{"Red", "Gold", "Blue", "Green"});
        addCustomizationSection(customPanel, "Food Shape",
                new String[]{"Circle", "Star", "Apple"});
        addCustomizationSection(customPanel, "Food Type",
                new String[]{"Regular", "Special", "Golden"});

        // Back Button
        JButton backButton = createStyledButton("Back to Main Menu");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        customPanel.add(Box.createVerticalStrut(30));
        customPanel.add(backButton);

        mainPanel.add(customPanel, "Customization");
    }
    private void addCustomizationSection(JPanel panel, String title, String[] options) {
        JPanel sectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sectionPanel.setOpaque(false);
        JLabel label = createStyledLabel(title + ":", menuFont);
        JComboBox<String> comboBox = new JComboBox<>(options);
        styleComboBox(comboBox);
        comboBox.addActionListener(e -> {
            String selected = (String) comboBox.getSelectedItem();
            switch(title) {
                case "Snake Color":
                    settings.setSnakeColor(getColorFromString(selected));
                    break;
                case "Snake Shape":
                    settings.setSnakeShape(selected);
                    break;
                case "Food Color":
                    settings.setFoodColor(getColorFromString(selected));
                    break;
                case "Food Shape":
                    settings.setFoodShape(selected);
                    break;
                case "Food Type":
                    settings.setFoodType(selected);
                    break;
            }
        });
        sectionPanel.add(label);
        sectionPanel.add(comboBox);
        panel.add(sectionPanel);
        panel.add(Box.createVerticalStrut(10));
    }
    private Color getColorFromString(String color) {
        switch(color) {
            case "Blue": return Color.BLUE;
            case "Red": return Color.RED;
            case "Yellow": return Color.YELLOW;
            case "Purple": return new Color(128, 0, 128);
            case "Gold": return new Color(255, 215, 0);
            case "Green":
            default: return Color.GREEN;
        }
    }

    // Styling methods
    private JPanel createStyledPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(backgroundColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return panel;
    }
    private JLabel createStyledLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(textColor);
        return label;
    }
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(menuFont);
        button.setForeground(textColor);
        button.setBackground(buttonColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
        return button;
    }
    private void styleCheckBox(JCheckBox checkBox) {
        checkBox.setFont(menuFont);
        checkBox.setForeground(textColor);
        checkBox.setBackground(backgroundColor);
        checkBox.setFocusPainted(false);
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(menuFont);
        comboBox.setForeground(textColor);
        comboBox.setBackground(buttonColor);
    }

    private void styleSlider(JSlider slider) {
        slider.setOpaque(false);
        slider.setForeground(textColor);
    }

    private void styleSpinner(JSpinner spinner) {
        spinner.getEditor().setBackground(buttonColor);
        ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().setForeground(textColor);
    }

    private void handleMainMenuAction(String action) {
        switch (action) {
            case "Play Game":
                startGame();
                break;
            case "Settings":
                cardLayout.show(mainPanel, "Settings");
                break;
            case "Customization":
                cardLayout.show(mainPanel, "Customization");
                break;
            case "Exit":
                System.exit(0);
                break;
        }
    }
    private void startGame() {
        this.dispose();
        new GameFrame(settings);
    }
}