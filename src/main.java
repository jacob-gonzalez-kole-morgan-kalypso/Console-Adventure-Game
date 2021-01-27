import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import pkg.*;

class Main {

    private static int WIDTH = 80;
    private static int HEIGHT = 20;
    private static StringBuilder screen;
    private static Player player;

    public static void main(String[] args) throws IOException {
        
        char wall = '#';
        char empty = ' ';

        player = new Player(); 

        //prepares our fake screenbuffer
        screen = new StringBuilder();
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(java.io.FileDescriptor.out), "ASCII"), 512);
        boolean running = startQuestion();

        askName();
        //gameloop
        while(running){

            //clear screen
            clearConsole();     
            screen.setLength(0);


            //draw screen
            for(byte i = 0;i <= HEIGHT; i++){ 
                for(byte o = 0;o <= WIDTH; o++){
                    screen.append(empty); 
                }
                screen.append("\n");//creates next line
            }

            //Player Hud
            drawPlayerHud();

            //write to screen
            out.write(screen.toString());
            out.flush();

            //stops and waits for interaction
            askQuestion(out, screen);

            //checks for death
            player.checkStats();
        }
        
        clearConsole();    
        System.out.println("Thank you for playing");
    }

    public static boolean startQuestion(){
        System.out.println("Are you ready to begin (Y/N)?: ");
        Scanner input = new Scanner(System.in);
        String answer = input.nextLine();
        if(answer.length() == 0) return true; //catches null input
        if(answer.toLowerCase().charAt(0) == 'n'){
            return false; //if user says no
        }
        return true;
    }

    public static void drawPlayerHud(){
        wordDraw("Health: " + player.getHealth(), screen, 1,0);
        wordDraw("Energy: " + player.getEnergy(), screen, 1,1);
        wordDraw(player.getName(), screen, WIDTH-(player.getName().length()), 0 );
    }

    public static void askName(){
        System.out.println("What is your name?: ");
        Scanner input = new Scanner(System.in);
        String answer = input.nextLine();
        String name;
        if(answer.length() == 0){  //catches null input
            System.out.println("I asked for your name please");
            askName();
        }else{
            name = answer;
            System.out.println("Your name is " + answer + " , Correct (Y/N)? : ");
            answer = input.nextLine();
            if(answer.toLowerCase().charAt(0) == 'n'){  //if user says no
                System.out.println("Okay then what is it?");
                askName();
            }else{
                System.out.println("Welcome to ________ , " + name);
                player.setName(name);
            }
        }
        
        
       
    }

    public static void askQuestion(BufferedWriter buffer, StringBuilder scr) throws IOException {
        wordDraw("Do you engage in combat? Y/N: ", screen, 1, HEIGHT-2);
        buffer.write(scr.toString());
        buffer.flush();
        Scanner input = new Scanner(System.in);
        String answer = input.nextLine();
        if(answer.toLowerCase().equals("n")){
            wordDraw("It attacks you in the back, you're dead", screen, 1, HEIGHT-2);
            //gameover
        }else if(answer.toLowerCase().equals("y")){
            wordDraw("What would you like to do? Attack : Magic: Item : Flee", screen, 1, HEIGHT-2);
            answer = input.nextLine();
            if(answer.toLowerCase().equals("attack")){
                wordDraw("You attacked the monster for 6 points of damage", screen, 1, HEIGHT-2);
                //combat hit point stuff
            }else if(answer.toLowerCase().equals("magic")){
                wordDraw("What would you like to cast? Fireball : Sleep", screen, 1, HEIGHT-2);
                //energy be quite limited
                answer = input.nextLine();
                if(answer.toLowerCase().equals("fireball")){
                    //uses the most energy to cast
                    //if enough energy cast, if not tell not enough energy return to menu
                    wordDraw("You cast fireball, you do 10 damage", screen, 1, HEIGHT-2);
                    //deduct energy
                }else if(answer.toLowerCase().equals("sleep")){
                    //cost middle amount
                    wordDraw("You cast Sleep, the monster fell asleep!", screen, 1, HEIGHT-2);
                    //attacker skips 2 turns
                }

            } else if(answer.toLowerCase().equals("item")){
                wordDraw("What would you like to consume? Potion : Ether", screen, 1, HEIGHT-2);
                answer = input.nextLine();
                if(answer.toLowerCase().equals("potion")){
                    wordDraw("You healed 10 points of health", screen, 1, HEIGHT-2);
                }else if(answer.toLowerCase().equals("ether")){
                    wordDraw("You recovered 10 points of energy", screen, 1, HEIGHT-2);
                }
            }else if(answer.toLowerCase().equals("flee")){
                //roll to see if can flee, if not get whacked
            }
        }
        //logic here
    }
    
    public static void plot(char input, StringBuilder scr, int x, int y){ 
        if((y*WIDTH + y*1) + x + (y*1) >= scr.length()) return; //checks for overflow
        if((y*WIDTH + y*1) + x + (y*1) <= 0) return; //checks for overflow


        scr.setCharAt(
            ( (y*WIDTH + y*1) + x + (y*1) ),
            input);
    }

    public static void wordDraw(String input, StringBuilder scr, int x, int y){ 
        for(int i = 0; i < input.length();i++){
            plot( (char) input.charAt(i) , scr, x+i ,y);
        }
    }


    public static void lineDraw(char input, StringBuilder scr, int x1, int y1, int x2, int y2){

        int x, y, dx, dy, dx1, dy1, px, py, xe, ye, i;
        // Calculate line deltas
        dx = x2 - x1;
        dy = y2 - y1;
        // Create a positive copy of deltas (makes iterating easier)
        dx1 = Math.abs(dx);
        dy1 = Math.abs(dy);
        // Calculate error intervals for both axis
        px = 2 * dy1 - dx1;
        py = 2 * dx1 - dy1;

        // The line is X-axis dominant
        if (dy1 <= dx1) {
            // Line is drawn left to right
            if (dx >= 0) {
                x = x1; y = y1; xe = x2;
            } else { // Line is drawn right to left (swap ends)
                x = x2; y = y2; xe = x1;
            }
            plot(input, scr, x, y); // Draw first pixel
            for (i = 0; x < xe; i++) {
                x = x + 1;
                if (px < 0) {
                    px = px + 2 * dy1;
                } else {
                    if ((dx < 0 && dy < 0) || (dx > 0 && dy > 0)) {
                        y = y + 1;
                    } else {
                        y = y - 1;
                    }
                    px = px + 2 * (dy1 - dx1);
                }
                // Draw pixel from line span at
                // currently rasterized position
                plot(input, scr, x, y);
            }
        } else { // The line is Y-axis dominant
            // Line is drawn bottom to top
            if (dy >= 0) {
                x = x1; y = y1; ye = y2;
            } else { // Line is drawn top to bottom
                x = x2; y = y2; ye = y1;
            }
            plot(input, scr, x, y); // Draw first pixel
            // Rasterize the line
            for (i = 0; y < ye; i++) {
                y = y + 1;
                if (py <= 0) {
                    py = py + 2 * dx1;
                } else {
                    if ((dx < 0 && dy<0) || (dx > 0 && dy > 0)) {
                        x = x + 1;
                    } else {
                        x = x - 1;
                    }
                    py = py + 2 * (dx1 - dy1);
                }
                // Draw pixel from line span at
                // currently rasterized position
                plot(input, scr, x, y);
            }
        }
        
    }

    public final static void clearConsole(){
        System.out.print("\033[H\033[2J");  
        System.out.flush(); 
    }

}