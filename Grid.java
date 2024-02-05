import java.util.ArrayList;
public class Grid
{
    private Location[][] grid;

    // Constants for number of rows and columns.
    public static final int NUM_ROWS = 10;
    public static final int NUM_COLS = 10;
    
    private ArrayList<String> queue = new ArrayList<String>();
    
    // Create a new Grid. Initialize each Location in the grid
    // to be a new Location object.
    public Grid() {
        grid = new Location[NUM_ROWS][NUM_COLS];
        
        for (int r = 0; r < NUM_ROWS; r++) {
            for (int c = 0; c < NUM_COLS; c++) {
                grid[r][c] = new Location();
            }
        }
    }
    
    // Mark a hit in this location by calling the markHit method
    // on the Location object.  
    public void markHit(int row, int col) {
        grid[row][col].markHit();
    }
    
    // Mark a miss on this location.    
    public void markMiss(int row, int col) {
        grid[row][col].markMiss();
    }
    
    // Set the status of this location object.
    public void setStatus(int row, int col, int status){
        grid[row][col].setStatus(status);
    }
    // Get the status of this location in the grid  
    public int getStatus(int row, int col){
        if(row >= 0 && col >= 0 && row < NUM_ROWS && col < NUM_COLS){
            return grid[row][col].getStatus();
        }
        
        return -1;
    }
    // Return whether or not this Location has already been guessed.
    public boolean alreadyGuessed(int row, int col) {
        return !grid[row][col].isUnguessed();
    }
    
    // Set whether or not there is a ship at this location to the val   
    public void setShip(int row, int col, boolean val){
        grid[row][col].setShip(val);
    }
    
    // Return whether or not there is a ship here   
    public boolean hasShip(int row, int col) {
        return grid[row][col].hasShip();
    }
    
    // Add a ship to your own board
    public boolean addShip(Ship s) {
        if (s.getDirection() == 0 && s.getCol() + s.getLength() <= NUM_COLS) { //horizontal
            for(int i = 0; i < s.getLength(); i++) {
                if(hasShip(s.getRow(), s.getCol() + i))
                    return false;
            }
            for(int i = 0; i < s.getLength(); i++) {
                setShip(s.getRow(), s.getCol() + i, true);
            }
        } else if (s.getDirection() == 1 && s.getRow() + s.getLength() <= NUM_ROWS) { //vertical
            for(int i = 0; i < s.getLength(); i++) {
                if(hasShip(s.getRow() + i, s.getCol()))
                    return false;
            }
            for(int i = 0; i < s.getLength(); i++) {
                setShip(s.getRow() + i, s.getCol(), true);
            }
        } else {
            return false;
        }
        return true;
    }
    
    // Get the Location object at this row and column position
    public Location get(int row, int col) {
        return grid[row][col];
    }
    
    // Return the number of rows in the Grid
    public int numRows(){
        return NUM_ROWS;
    }
    
    // Return the number of columns in the grid
    public int numCols(){
        return NUM_COLS;
    }
    
    
    // Print the Grid status including a header at the top
    // that shows the columns 1-10 as well as letters across
    // the side for A-J
    // If there is no guess print a -
    // If it was a miss print a O
    // If it was a hit, print an X
    // A sample print out would look something like this:
    // 
    //   1 2 3 4 5 6 7 8 9 10 
    // A - - - - - - - - - - 
    // B - - - - - - - - - - 
    // C - - - O - - - - - - 
    // D - O - - - - - - - - 
    // E - X - - - - - - - - 
    // F - X - - - - - - - - 
    // G - X - - - - - - - - 
    // H - O - - - - - - - - 
    // I - - - - - - - - - - 
    // J - - - - - - - - - - 
    // 
    public void printStatus() {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        
        for(int r = 0; r < NUM_ROWS; r++) {
            System.out.print((char)(r+65) + " ");
            for (int c = 0; c < NUM_COLS; c++) {
                if(grid[r][c].getStatus() == 0) {
                    System.out.print("- ");
                } else if (grid[r][c].getStatus() == 1) {
                    System.out.print("X ");
                } else {
                    System.out.print("O ");
                }
            }
            System.out.println();
        }
    }
    
    public ArrayList<String> arrayStat() {
        queue.add("  1 2 3 4 5 6 7 8 9 10");
        
        for(int r = 0; r < NUM_ROWS; r++) {
            queue.add((char)(r + 65) + " ");
            for (int c = 0; c < NUM_COLS; c++) {
                if(grid[r][c].getStatus() == 0) {
                    queue.set(r + 1, queue.get(r + 1) + "- ");
                } else if (grid[r][c].getStatus() == 1) {
                    queue.set(r + 1, queue.get(r + 1) + "X ");
                } else {
                    queue.set(r + 1, queue.get(r + 1) + "O ");
                }
            }
        }
        
        return queue;
    }
    
    // Print the grid and whether there is a ship at each location.
    // If there is no ship, you will print a - and if there is a
    // ship you will print a X. You can find out if there was a ship
    // by calling the hasShip method.
    //
    //   1 2 3 4 5 6 7 8 9 10 
    // A - - - - - - - - - - 
    // B - X - - - - - - - - 
    // C - X - - - - - - - - 
    // D - - - - - - - - - - 
    // E X X X - - - - - - - 
    // F - - - - - - - - - - 
    // G - - - - - - - - - - 
    // H - - - X X X X - X - 
    // I - - - - - - - - X - 
    // J - - - - - - - - X - 
    public void printShips() {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        
        for(int r = 0; r < NUM_ROWS; r++) {
            System.out.print((char)(r+65) + " ");
            for (int c = 0; c < NUM_COLS; c++) {
                if(grid[r][c].hasShip()) {
                    System.out.print("X ");
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }
    }
    
    public ArrayList<String> arrayGrid() {
        queue.add("  1 2 3 4 5 6 7 8 9 10");
        
        for(int r = 0; r < NUM_ROWS; r++) {
            queue.add((char)(r + 65) + " ");
            for (int c = 0; c < NUM_COLS; c++) {
                if(grid[r][c].hasShip()) {
                    queue.set(r + 1, queue.get(r + 1) + "X ");
                } else {
                    queue.set(r + 1, queue.get(r + 1) + "- ");
                }
            }
        }
        
        return queue;
    }
}