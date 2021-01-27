package pkg;

public class Player {
    
    private static int health;
    private static int energy;
    private static int maxHealth;
    private static int maxEnergy;
    private static String name;

    public static void player(){
        maxHealth = 30; //initial values
        maxEnergy = 20;
        restoreFullHealth();
        restoreFullEnergy();
    }

    public static void restoreFullHealth(){
        health = maxHealth; 
    }

    public void setName(String input){
        name = input;
    }

    public String getName(){
        return name;
    }

    public static void restoreFullEnergy(){
        energy = maxEnergy;
    }
    
    public int getHealth(){
        return health;
    }

    public int getEnergy(){
        return energy;
    }

    public boolean checkStats(){
        if(health >= 0){
            return true;
        }
        return false;
    }

}
