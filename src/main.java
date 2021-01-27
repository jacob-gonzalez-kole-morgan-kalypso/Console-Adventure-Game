import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

class Main {

    private static int WIDTH = 80;
    private static int HEIGHT = 20;
    private static StringBuilder screen;

    public static void main(String[] args) throws IOException {
        
        char wall = '#';
        char empty = ' ';

        //prepares our fake screenbuffer
        screen = new StringBuilder();
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(java.io.FileDescriptor.out), "ASCII"), 512);
        boolean running = true;

        //gameloop
        while(running){

            //clear screen
            clearConsole();     
            screen.setLength(0);


            //draw screen
            for(byte i = 0;i <= HEIGHT; i++){ 
                for(byte o = 0;o <= WIDTH; o++){
                    if(o == 0 || o == WIDTH || i == 0 || i == HEIGHT){
                        screen.append(wall);
                    }else{
                        screen.append(empty);
                    }
                    
                }
                screen.append("\n");
            }


             //write to screen
             out.write(screen.toString());
             out.flush();

             //stops and waits for interaction
             askQuestion();
        }
    }

    public static void askQuestion(){
        Scanner input = new Scanner(System.in);
        input.next();
    }

    public static void drawBox(int length, int height, int x, int y, String text){
        int middle = height/2;
        StringBuilder output;
        
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