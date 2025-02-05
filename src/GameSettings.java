import java.awt.*;

public class GameSettings {
    private Color snakeColor = Color.GREEN;
    private Color foodColor = Color.RED;
    private Color backgroundColor = Color.BLACK;
    private int gameSpeed = 150;
    private int initialLength = 6;
    private Dimension gridSize = new Dimension(800, 600);
    private String snakeShape = "Rectangle";
    private String foodShape = "Circle";
    private String foodType = "Regular";
    private boolean gridLines = false;
    private boolean darkMode = true;
    private int unitSize = 50;
    // Scoring multipliers
    private int regularFoodScore = 1;
    private int specialFoodScore = 2;
    private int goldenFoodScore = 5;
    // Getters and Setters
    public Color getSnakeColor() { return snakeColor; }
    public void setSnakeColor(Color color) { this.snakeColor = color; }
    public Color getFoodColor() { return foodColor; }
    public void setFoodColor(Color color) { this.foodColor = color; }
    public int getGameSpeed() { return gameSpeed; }
    public void setGameSpeed(int speed) { this.gameSpeed = speed; }
    public String getSnakeShape() { return snakeShape; }
    public void setSnakeShape(String shape) { this.snakeShape = shape; }
    public String getFoodShape() { return foodShape; }
    public void setFoodShape(String shape) { this.foodShape = shape; }
    public String getFoodType() { return foodType; }
    public void setFoodType(String type) { this.foodType = type; }
    public boolean isGridLines() { return gridLines; }
    public void setGridLines(boolean gridLines) { this.gridLines = gridLines; }
    public boolean isDarkMode() { return darkMode; }
    public void setDarkMode(boolean darkMode) { this.darkMode = darkMode; }
    public int getUnitSize() { return unitSize; }
    public void setUnitSize(int size) { this.unitSize = size; }
    public int getInitialLength() { return initialLength; }
    public void setInitialLength(int length) { this.initialLength = length; }
    public Dimension getGridSize() { return gridSize; }
    public void setGridSize(Dimension size) { this.gridSize = size; }
    public int getScoreMultiplier() {
        switch(foodType) {
            case "Special": return specialFoodScore;
            case "Golden": return goldenFoodScore;
            default: return regularFoodScore;
        }
    }
    public Color getBackgroundColor() {
        return darkMode ? Color.BLACK : Color.WHITE;
    }
    public Color getGridColor() {
        return darkMode ? Color.DARK_GRAY : Color.LIGHT_GRAY;
    }
    public Color getTextColor() {
        return darkMode ? Color.WHITE : Color.BLACK;
    }
}