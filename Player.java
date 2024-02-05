import java.util.ArrayList;
public class Player
{
    // These are the lengths of all of the ships.
    private static final int[] SHIP_LENGTHS = {2, 3, 3, 4, 5};
    private Ship[] ships;
    
    //Shows during game behavior
    private String closingStatements = "";

    private int numShips = 0;
    private Grid own = new Grid();
    private Grid opp = new Grid();
    
    private boolean rand = true;//When true computer will random guess
    private boolean extShips = false;//Stacked ships are left unsolved
    private int[] prvRows = {-1, -1, -1};
    private int[] prvCols = {-1, -1, -1};
    
    public Player() {
        ships = new Ship[SHIP_LENGTHS.length];
        for (int i = 0; i < SHIP_LENGTHS.length; i++) {
            ships[i] = new Ship(SHIP_LENGTHS[i]);
        }
        
    }
    
    public void getClosingStatements() {
        if(closingStatements != "") {
            System.out.println(closingStatements);
            System.exit(0);
        }
    }
    
    // Print your ships on the grid
    public void printMyShips() {
        own.printShips();
    }
    
    public void printBothGrids() {
        ArrayList<String> s = own.arrayGrid();
        ArrayList<String> o = opp.arrayStat();
        
        System.out.print("Your Ships");
        for(int i = 0; i < s.get(0).length() - 9; i++) {
            System.out.print(" ");
        }
        System.out.println(" |  Computer Guess");
        
        for(int i = 0; i < s.size(); i++) {
            System.out.println(s.get(i) + "  |  " + o.get(i));
        }
        
        s.clear();
        o.clear();
    }
    
    
    
    // Print opponent guesses
    public void printOpponentGuesses() {
        opp.printStatus();
    }
    
    // Print your guesses
    public void printMyGuesses() {
        own.printStatus();
    }
    
    public boolean chooseShipLocation(Ship s, int row, int col, int direction) {
        if (numShips < SHIP_LENGTHS.length) {
            s.setLocation(row, col);
            s.setDirection(direction);
            if(own.addShip(s)) {
                numShips++;
                return true;
            }
        }
        return false;
    }
    
    public void computerGuess() {
        int sunkStatus;
        int row = 0;
        int col = 0;
        
        if(rand && !extShips){//Random Hit!
            int badSpot = 0;
            while(true) {
                row = (int)(Math.random() * own.numRows());
                col = (int)(Math.random() * own.numCols());
                
                //Throws out spots which have two or more marks around it.
                if(!(row > 0) || (row > 0 && opp.getStatus(row - 1, col) != 0)) {
                    badSpot++;
                }
                if(!(row < (opp.numRows() - 1)) || (row < (opp.numRows() - 1) && opp.getStatus(row + 1, col) != 0)) {
                    badSpot++;   
                }
                if(badSpot != 2 && (!(col > 0) || (col > 0 && opp.getStatus(row, col - 1) != 0))) {
                    badSpot++;
                }
                if(badSpot != 2 && !(col < (opp.numRows() - 1)) || (col < (opp.numRows() - 1) && opp.getStatus(row, col + 1) != 0)) {
                    badSpot++;
                }
                
                if(own.hasShip(row, col) || badSpot < 2) {
                    if(recordOpponentGuess(row, col)) 
                        break;
                }
                
                badSpot = 0;
            }
            
            if(opp.getStatus(row, col) == 1) {
                prvRows[2] = row;
                prvCols[2] = col;
            }
            
        } else { // Logic after hit!
            if(prvRows[1] == -1){//First hit registered but no second
                if(prvRows[0] > 0 && opp.getStatus(prvRows[0] - 1, prvCols[0]) == 0) {
                    recordOpponentGuess(prvRows[0] - 1, prvCols[0]);
                    row = prvRows[0] - 1;
                    col = prvCols[0];
                    
                } else if(prvCols[0] > 0 && opp.getStatus(prvRows[0], prvCols[0] - 1) == 0) {
                    recordOpponentGuess(prvRows[0], prvCols[0] - 1);
                    row = prvRows[0];
                    col = prvCols[0] - 1;
                    
                } else if(prvRows[0] < own.numRows() && opp.getStatus(prvRows[0] + 1, prvCols[0]) == 0) {
                    recordOpponentGuess(prvRows[0] + 1, prvCols[0]);
                    row = prvRows[0] + 1;
                    col = prvCols[0];
                    
                } else if (prvCols[0] < own.numCols() && opp.getStatus(prvRows[0], prvCols[0] + 1) == 0) {
                    recordOpponentGuess(prvRows[0], prvCols[0] + 1);
                    row = prvRows[0];
                    col = prvCols[0] + 1;
                    
                } else { //If all others fail it means the guess is surrounded
                    //Check two out
                    if(opp.getStatus(prvRows[0] - 1, prvCols[0]) == 1 && opp.getStatus(prvRows[0] - 2, prvCols[0]) == 0){
                        recordOpponentGuess(prvRows[0] - 2, prvCols[0]);
                        row = prvRows[0] - 2;
                        col = prvCols[0];
                        
                    } else if(opp.getStatus(prvRows[0], prvCols[0] - 1) == 1 && opp.getStatus(prvRows[0], prvCols[0] - 2) == 0) {
                        recordOpponentGuess(prvRows[0], prvCols[0] - 2);
                        row = prvRows[0];
                        col = prvCols[0] - 2;
                        
                    } else if(opp.getStatus(prvRows[0] + 1, prvCols[0]) == 1 && opp.getStatus(prvRows[0] + 2, prvCols[0]) == 0) {
                        recordOpponentGuess(prvRows[0] + 2, prvCols[0]);
                        row = prvRows[0] + 2;
                        col = prvCols[0];
                        
                    } else if(opp.getStatus(prvRows[0], prvCols[0] + 1) == 1 && opp.getStatus(prvRows[0], prvCols[0] + 2) == 0) {
                        recordOpponentGuess(prvRows[0], prvCols[0] + 2);
                        row = prvRows[0];
                        col = prvCols[0] + 2;
                        
                    } else {//if the program can truly not find the next possible spot it will resort to random guessing
//                        System.out.println("Computer resorted to random guessing as a last ditch effort\n");
                        closingStatements += "Computer resorted to random guessing as a last ditch effort\n";
                        rand = true;
                        extShips = false;
                        
                        while(true) {
                            int badSpot = 0;
                            row = (int)(Math.random() * own.numRows());
                            col = (int)(Math.random() * own.numCols());
                
                            //Throws out spots which have two or more marks around it.
                            if(!(row > 0) || (row > 0 && opp.getStatus(row - 1, col) != 0)) {
                                badSpot++;
                            }
                            if(!(row < (opp.numRows() - 1)) || (row < (opp.numRows() - 1) && opp.getStatus(row + 1, col) != 0)) {
                                badSpot++;   
                            }
                            if(badSpot != 2 && (!(col > 0) || (col > 0 && opp.getStatus(row, col - 1) != 0))) {
                                badSpot++;
                            }
                            if(badSpot != 2 && !(col < (opp.numRows() - 1)) || (col < (opp.numRows() - 1) && opp.getStatus(row, col + 1) != 0)) {
                                badSpot++;
                            }
                
                            if(own.hasShip(row, col) || badSpot < 2) {
                                if(recordOpponentGuess(row, col)) 
                                    break;
                            }
                
                            badSpot = 0;
                        }
                    }
                    
                }
                
            } else {
                boolean placed = false;
                if(prvRows[1] - prvRows[0] != 0) {//Row disparity
                    //Iterate over all row starting at the intital point until reach wall or a missed mark
                    for(int i = prvRows[1] - 1; i >= 0; i--) {
                        if(opp.getStatus(i, prvCols[1]) == 0) {
                            recordOpponentGuess(i, prvCols[1]);
                            row = i;
                            col = prvCols[1];
                            placed = true;
                            break;
                        } else if(opp.getStatus(i, prvCols[1]) == 2) {
                            break;
                        }
                    }
                    if(!placed) {
                        for(int i = prvRows[1] + 1; i < own.numRows(); i++) {
                            if(opp.getStatus(i, prvCols[1]) == 0) {
                                recordOpponentGuess(i, prvCols[1]);
                                row = i;
                                col = prvCols[1];
                                placed = true;
                                break;
                            }
                        }
                    }
                    //Attempt to fix stacked ships
                    if(!placed && (Math.abs(row - prvRows[0]) > 1 || Math.abs(col - prvCols[0]) > 1)) {//If ships are stacked improperly
                        if(prvCols[1] > 0 && opp.getStatus(prvRows[2], prvCols[2] - 1) == 0) {
                            recordOpponentGuess(prvRows[2], prvCols[2] - 1);
                            row = prvRows[2];
                            col = prvCols[2] - 1;
                            setPrvRows(row, prvRows[2]);
                            setPrvCols(col, prvCols[2]);
                        } else {//if(prvRows[1] < own.numRows() && opp.getStatus(prvRows[1] + 1, prvCols[2]) == 0){
                            recordOpponentGuess(prvRows[2], prvCols[2] + 1);
                            row = prvRows[2];
                            col = prvCols[2] + 1;
                        }
                        extShips = true;
                    }
                } else {//Col disparity
                    //Iterate over all col starting at the intital point until reach wall or a missed mark
                    for(int i = prvCols[1] - 1; i >= 0; i--) {
                        if(opp.getStatus(prvRows[1], i) == 0) {
                            recordOpponentGuess(prvRows[1], i);
                            
                            row = prvRows[1];
                            col = i;
                            placed = true;
                            break;
                            
                        } else if(opp.getStatus(prvRows[1], i) == 2) {
                            break;
                        }
                    }
                    if(!placed) {
                        for(int i = prvCols[1] + 1; i < own.numCols(); i++) {
                            if(opp.getStatus(prvRows[1], i) == 0) {
                                recordOpponentGuess(prvRows[1], i);
                                    row = prvRows[1];
                                    col = i;
                                    placed = true;
                                    break;
                            }
                        }
                    }
                    //Attempt to fix stacked ships
                    if(!placed && (Math.abs(row - prvRows[0]) > 1 || Math.abs(col - prvCols[0]) > 1)) {//If ships are stacked improperly
                        if(prvRows[1] > 0 && opp.getStatus(prvRows[2] - 1, prvCols[2]) == 0) {
                            recordOpponentGuess(prvRows[2] - 1, prvCols[2]);
                            row = prvRows[2] - 1;
                            col = prvCols[2];
                        } else {//if(prvRows[1] < own.numRows() && opp.getStatus(prvRows[1] + 1, prvCols[2]) == 0){
                            recordOpponentGuess(prvRows[2] + 1, prvCols[2]);
                            row = prvRows[2] + 1;
                            col = prvCols[2];
                        }
                        extShips = true;
                    }
                }
            }
        }
        
        System.out.println("(" + (char)(row + 65) + ", " + (col + 1) + ")");
        
        if(opp.getStatus(row, col) == 1) {
            System.out.println("Hit!");
            setPrvRows(row, prvRows[0]);
            setPrvCols(col, prvCols[0]);
            
            for(int i = 0; i < numShips; i++) {
                sunkStatus = shipSunk(i, row, col);
                
                if(sunkStatus == 1) {//1 = Correct ship and sunk
                    System.out.println("Computer sunk ship #" + (i + 1));
                    
                    rand = true;
                    setPrvRows(-1, -1, -1);
                    setPrvCols(-1, -1, -1);
                    
                    if(extShips) {
                        findVoidShips();
                    }
                    
                    break;
                } else if(sunkStatus == 2) {//2 = Correct ship not sunk
                    rand = false;
                    break;
                }//0 = Next
            }
        } else {
            System.out.println("Miss");
        }
        
    }
    
    //Ok I'm writing this a year later, this part might be useless
    private void findVoidShips() {
        
        for(int r = 0; r < own.numRows(); r++) {
            for(int c = 0; c < own.numCols(); c++) {
                if(opp.getStatus(r, c) == 1) {
                    for(int s = 0; s < numShips; s++) {
                        if(shipSunk(s, r, c) == 2) {
                            prvRows[0] = r;
                            prvCols[0] = c;
                            closingStatements += "Void ships got something\n";
                            return;
                        } 
                    }
                }
            }
        }
        extShips = false;
    }
    
    private void setPrvRows(int a, int b) {
        prvRows[0] = a;
        prvRows[1] = b;
    }
    
    private void setPrvRows(int a, int b, int c) {
        prvRows[0] = a;
        prvRows[1] = b;
        prvRows[2] = c;
    }
    
    private void setPrvCols(int a, int b) {
        prvCols[0] = a;
        prvCols[1] = b;
    }
    
    private void setPrvCols(int a, int b, int c) {
        prvCols[0] = a;
        prvCols[1] = b;
        prvCols[2] = c;
    }
    
    // Record a guess from the opponent
    public boolean recordOpponentGuess(int row, int col) {
        if ((row >= 0 && row < own.numRows()) && (col >= 0 && col < own.numCols()) && own.hasShip(row, col) && opp.getStatus(row, col) == 0) {
            opp.markHit(row, col);
            return true;
        } else if ((row >= 0 && row < own.numRows()) && (col >= 0 && col < own.numCols()) && opp.getStatus(row, col) == 0) {
            opp.markMiss(row, col);
            return true;
        }
        return false;
    }
    
    //Internal shipSunk
    private int shipSunk(int s, int row, int col) {//0 = move to next, 1 = Correct ship and sunk, 2 = Correct ship not sunk
        if (ships[s].getDirection() == 0 && ships[s].getRow() == row) { //horizontal
            for(int i = 0; i < ships[s].getLength(); i++) { //Iterates over ship col
                if(ships[s].getCol() + i == col) { //Confirms that the guess is the same as the ship
                    //Check every spot for the ship and see if all are hit
                    for(int j = 0; j < ships[s].getLength(); j++) {
                        if(opp.getStatus(ships[s].getRow(), ships[s].getCol() + j) == 0) {
                            return 2; // This ship is not fully guessed
                        }
                    }
                return 1;
                }
            }
        
        } else if(ships[s].getDirection() == 1 && ships[s].getCol() == col) { //Vertical
            for(int i = 0; i < ships[s].getLength(); i++) { //Iterates over ship col
                if(ships[s].getRow() + i == row) { //Confirms that the guess is the same as the ship
                    //Check every spot for the ship and see if all are hit
                    for(int j = 0; j < ships[s].getLength(); j++) {
                        if(opp.getStatus(ships[s].getRow() + j, ships[s].getCol()) == 0) {
                            return 2; // This ship is not fully guessed
                        }
                    }
                    return 1;
                }
            }
        }
        return 0;
    }
    
    //External shipSunk
    public int shipSunk(Ship s, int row, int col, Grid g) {//0 = move to next, 1 = Correct ship and sunk, 2 = Correct ship not sunk
        if (s.getDirection() == 0 && s.getRow() == row) { //horizontal
            for(int i = 0; i < s.getLength(); i++) { //Iterates over ship col
                if(s.getCol() + i == col) { //Confirms that the guess is the same as the ship
                    //Check every spot for the ship and see if all are hit
                    for(int j = 0; j < s.getLength(); j++) {
                        if(g.getStatus(s.getRow(), s.getCol() + j) == 0) {
                            return 2; // This ship is not fully guessed
                        }
                    }
                return 1;
                }
            }
        
        } else if(s.getDirection() == 1 && s.getCol() == col) { //Vertical
            for(int i = 0; i < s.getLength(); i++) { //Iterates over ship col
                if(s.getRow() + i == row) { //Confirms that the guess is the same as the ship
                    //Check every spot for the ship and see if all are hit
                    for(int j = 0; j < s.getLength(); j++) {
                        if(g.getStatus(s.getRow() + j, s.getCol()) == 0) {
                            return 2; // This ship is not fully guessed
                        }
                    }
                    return 1;
                }
            }
        }
        return 0;
    }
    
    public int getNumShips() {
        return SHIP_LENGTHS.length;
    }
    
    public Ship getShip(int s) {
        return ships[s];
    }
    
    public Grid getGrid() {
        return own;
    }
    
    public Grid getOppGrid() {
        return opp;
    }
    
}