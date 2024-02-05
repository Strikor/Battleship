public class Location
{
    private static final int UNGUESSED = 0;
    private static final int HIT = 1;
    private static final int MISSED = 2;

    boolean ship;
    boolean unguessed;
    int status; 
    
    // Location constructor. 
    public Location() {
        
    }
    
    // Was this Location a hit?
    public boolean checkHit() {
        return ship;
    }
    
    // Was this location a miss?
    public boolean checkMiss() {
        return !ship;
    }
    
    // Was this location unguessed?
    public boolean isUnguessed() {
        return unguessed;
    }
    
    // Mark this location a hit.
    public void markHit() {
        status = 1;
    }
    
    // Mark this location a miss.
    public void markMiss() {
        status = 2;
    }

    // Return whether or not this location has a ship.
    public boolean hasShip() {
        return ship;
    }
    
    // Set the value of whether this location has a ship.
    public void setShip(boolean val) {
        ship = val;
    }
    
    // Set the status of this Location.
    public void setStatus(int status) {
        this.status = status;
    }
    
    // Get the status of this Location.
    public int getStatus() {
        return status;
    }
}