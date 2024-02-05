public class Battleship extends ConsoleProgram
{
    /* TODO
    X   +Ship sunk notification 
    X   +Try to make multiple grids be put side by side.
    X   +Intelegent computer guessing
        X   -Store previous guesses and when hit
                continue guessing in a linear direction.
        X   -Fix Long ships not being solved
                -There is currently a bug wherein if a ship
                is made with two ships put end to end in either
                direction it is possible the computer will guess said
                ship improperly and potentialy leave the game
                in a state where the computer cannot win
        X   -Improve Random Guessing
    X       -Fix stacked ships breaking guessing
    X           -Allow the computer to go back and guess stacked ships
    */

    // public void run() {//Testing only
    //     Player test = new Player();
        
    //     compSetup(test);

    //     test.printBothGrids();

    // }
	
	private static final boolean TEST = false; //Will run a full game without user
    private static final boolean LOOP = false; //Will loop the program continuously unitl an error occurs 
    
	public void run()
    {
        Player user = new Player();
        Player computer = new Player();
        
        if(TEST || readLine("Automatic Board Setup? (Y/N)\n").toUpperCase().trim().equals("Y")) {
            compSetup(user);
        } else {
            userSetup(user);
        }
        
        compSetup(computer);
        
        // System.out.println("Your Ships");
        // user.printMyShips();
        // System.out.println();
        // System.out.println("Computer Guess");
        // user.printOpponentGuesses();
        // System.out.println();
        
        //Prints both grids side by side
        user.printBothGrids();
        
        boolean turn = false; //false player 1, true player 2
        
        if(TEST) {
            while(!testRound(user, computer, turn)) {// false = game not over, true = game over
                turn = !turn;
            }
        } else {
            while(!playRound(user, computer, turn)) {// false = game not over, true = game over
                turn = !turn;
            }
        }
        
        if(turn) {
            System.out.println("Computer wins");
            computer.printBothGrids();
        } else {
            System.out.println("Player wins");
            computer.printBothGrids();
        }
        
        if(TEST) {
            user.getClosingStatements();
            computer.getClosingStatements();
//            readLine("Press enter to continue \n");
            
            if(LOOP) {
                run();
            }
            
        }
        
    }
    
    public boolean playRound(Player u, Player c, boolean turn) {
        if(turn) {
            //computerGuess(u);
            u.computerGuess();
            
            // System.out.println("Your Ships");
            // u.printMyShips();
            // System.out.println();
            // System.out.println("Computer Guess");
            // u.printOpponentGuesses();
            // System.out.println();
            u.printBothGrids();

            readLine("Press enter to continue \n");
            System.out.println("===================================================");
            
            return checkWin(u);
            
        } else {
            System.out.println("Guess Board");
            c.printOpponentGuesses();
            
            playerGuess(c);
            
            System.out.println("Guess Board");
            c.printOpponentGuesses();
            
            readLine("Press enter to continue \n");
            System.out.println("===================================================");
            
            return checkWin(c);
            
        }
        
    }
    
    public boolean testRound(Player u, Player c, boolean turn) {
        if(turn) {
            
            System.out.println("Player 2 (Computer)");
            
            u.computerGuess();
            
            u.printBothGrids();
            
            return checkWin(u);
            
        } else {
            
            System.out.println("Player 1 (User)");
            
            c.computerGuess();
            
            c.printBothGrids();
            
            return checkWin(c);
            
        }
        
    }
    
    public boolean checkWin(Player p) {
        Grid own = p.getGrid();
        Grid opp = p.getOppGrid();
        
        for(int r = 0; r < own.numRows(); r++) {
            for (int c = 0; c < own.numCols(); c++) {
                if(opp.getStatus(r, c) == 0 && own.hasShip(r, c)) {
                    return false;
                }
            }
        }
        
        return true;
        
    }
    
    public void playerGuess(Player c) { //player opponent's board
        String location;
        int row = 0;
        int col = 0;
        int sunkStatus;
        
        while(true) {
            while(true) {
                try {
                    location = readLine("Choose row(letter) and a column(number) for guess \n").toUpperCase().trim();
                    row = location.charAt(0) - 65;
                    col = Integer.parseInt(location.substring(1)) - 1;
                    break;
                } catch(IndexOutOfBoundsException e) {
                        System.out.println("Invalid Guess\n");
                } catch(NumberFormatException e) {
                        System.out.println("Invalid Guess\n");
                }
            }
        
            if(c.recordOpponentGuess(row, col)) {
                if (c.getOppGrid().getStatus(row, col) == 1) {
                    System.out.println("Hit!");
                } else {
                    System.out.println("Miss");
                }
                break;
            }
            System.out.println("Invalid Guess\n");
        }
        
        for (int i = 0; i < c.getNumShips(); i++) {
            sunkStatus = c.shipSunk(c.getShip(i), row, col, c.getOppGrid());
            if(sunkStatus == 1) {//1 = Correct ship and sunk
                System.out.println("You sunk ship #" + (i + 1));
            } else if(sunkStatus == 2) {//2 = Correct ship not sunk
                break;
            }//0 = Next
        }
    }
    
    // public void computerGuess(Player u) {
    //     int sunkStatus;
    //     int row = 0;
    //     int col = 0;
        
    //     while(true) {
    //         row = (int)(Math.random() * u.getGrid().numRows());
    //         col = (int)(Math.random() * u.getGrid().numCols());
    //         if(u.recordOpponentGuess(row, col))
    //             break;
    //     }

    //     for(int i = 0; i < u.getNumShips(); i++) {
    //         sunkStatus = u.shipSunk(u.getShip(i), row, col, u.getOppGrid());
    //         if(sunkStatus == 1) {//1 = Correct ship and sunk
    //             System.out.println("Computer sunk ship #" + i);
    //             break;
    //         } else if(sunkStatus == 2) {//2 = Correct ship not sunk
    //             break;
    //         }//0 = Next
    //     }
    // }
    
    public void userSetup(Player u) {
        int row;
        int col;
        int direction;
        String response;
        String location;
        
        u.printMyShips();
        
        int i = 0;
        while(i < u.getNumShips()) {
            while(true) {
                try {
                    location = readLine("Choose row(letter) and a column(number) for ship " + (i + 1) + "\n").toUpperCase().trim();
                    row = location.charAt(0) - 65;
                    col = Integer.parseInt(location.substring(1)) - 1;
                    if(row >= 0 && row < u.getGrid().numRows())
                        break;
                } catch(IndexOutOfBoundsException e) {
                } catch(NumberFormatException e) {
                }
                
            System.out.println("Invalid location");
            }
            
            while(true) {
                response = readLine("Should the ship be Horizontal (h) or Vertical (v)\n").toLowerCase().trim();
                
                if(response.equals("horizontal") || response.equals("h")) {
                    direction = 0;
                    break;
                } else if (response.equals("vertical") || response.equals("v")) {
                    direction = 1;
                    break;
                }
            }
            
            if(u.chooseShipLocation(u.getShip(i), row, col, direction)) {
                u.printMyShips();
                i++;
            } else {
                println("Invalid Location");
            }
        }
    }
    
    public void compSetup(Player c) {
        int row;
        int col;
        int direction;
        
        int i = 0;
        while(i < c.getNumShips()) {
            row = (int)(Math.random() * (c.getGrid().numRows()));
            col = (int)(Math.random() * (c.getGrid().numCols()));
            
            direction = (int)(Math.random() * 2);
            
            if(c.chooseShipLocation(c.getShip(i), row, col, direction))
                i++;
        }
    }
}