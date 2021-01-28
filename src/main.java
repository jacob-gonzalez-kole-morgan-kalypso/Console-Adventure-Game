import java.util.Scanner; 

class Main{

    private static boolean running;
    private static String pName;
    private static int pHealth;
    private static int mHealth;
    private static Scanner input;


    public static void main(String[] args) {
        
        input = new Scanner(System.in); //we create this here so we can close it off in the end
        running = startGameQuestion(); // this will set the loop off
        pHealth = 30;

        askNameQuestion();

        while(running){
            spawnMonster((int) Math.floor(Math.random() * 20)+10); //cast health to a random number 10-30
        }
        input.close();

        System.out.println("Game over");
    }

    public static boolean startGameQuestion(){
        
        System.out.println("Would you like to play a game (Y/N)?");
        String answer = input.nextLine();
        
        if(answer.length() <= 0) { //check for empty string or it will crash
            clearConsole();
            System.out.println("No answer?");
            startGameQuestion(); //recursion 
        }

        if(answer.toLowerCase().charAt(0) == 'y'){ //we check for only the first letter casted to lower case because its user friendly
            return true;
        }
        return false;
    }

    public static void askNameQuestion(){

        System.out.println("What is your name?");
        String answer = input.nextLine();
        if(answer.length() <= 0) { //check for empty string or it will crash
            clearConsole();
            System.out.println("No name?");
            askNameQuestion(); //recursion 
        }
        pName = answer; //assign name to variable
        System.out.println("Your name is : " + pName);
    }

    public static void spawnMonster(int input){
        mHealth = input;
        playerTurn();
    }

    public static void playerTurn(){
        
        if(pHealth <= 0){
            clearConsole();
            System.err.println("You died!");
            running = false;
            return; //goes back to the loop
        }

   
        System.out.println("Attack : Heal : Run");
        String answer = input.nextLine();
        if(answer.length() <= 0) { //check for empty string or it will crash
            playerTurn(); //recursion 
        }

        switch(answer.toLowerCase().charAt(0)){
            case 'a': //attack
                    int dmg = (int) Math.floor(Math.random() * 3);
                    mHealth -= dmg; //do damage
                    
                    System.out.println("You did " + dmg + " damage to the monster!");
                break;

            case 'h': //heal
                    int heal = (int) Math.floor(Math.random() * 3);
                    pHealth += heal; //heal
                    
                    System.out.println("You healed yourself for " + heal + " points!");
                break;

            case 'r': //run
                    pHealth += 5; //do damage
                    return; //respawns new monster

            default: //invalid input
                playerTurn(); //recursion 
                break;
        }

        monsterTurn();
    }

    public static void monsterTurn(){

        if(mHealth <= 0){
            
            System.err.println("The monster has been defeated!");
            return; //goes back to the loop
        }

        int dmg = (int) Math.floor(Math.random() * 3);
        pHealth -= dmg;


        System.out.println("You were hit for " + dmg + " health!");
        displayStats();
        playerTurn();

    }

    public static void displayStats(){

        System.out.println("You have " + pHealth + " health left.");
    }

    public final static void clearConsole(){
        System.out.print("\033[H\033[2J");  
        System.out.flush(); 
    }
}   