/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unlucky.robot;

import java.util.Random;
import java.util.Scanner;

/**
 *
 * @Philippe Ton-That
 */
public class UnluckyRobot {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int totalScore = 300;
        int itrCount = 0;
        int reward;
        char direction = inputDirection();
        int x = 0;
        int y = 0;
        do {
            displayInfo(x, y, itrCount, totalScore); //Will display position, count and total score every move
            itrCount++; //Adds to the count every move
            direction = inputDirection(); //assigns input key to direction
            if (doesExceed(x, y, direction)) {
                itrCount++; //adds count
                totalScore -= 2000; //decrements by 2000 totalScore
                totalScore += pointsLost(direction); //decrements totalScore by 10 or 50 depending on direction
                System.out.println("Exceed boundary, -2000 damage applied");
                reward = reward(); //initializes the reward method (dice throw)
                reward = punishOrMercy(direction, reward);
                totalScore += reward;

            } else {
                /**
                 * This switch case increments/decrements x or y based on direction
                 */
                switch (direction) {
                    case 'u':
                        y++;
                    case 'd':
                        y--;
                    case 'l':
                        x--;
                    default:
                        x++;
                }
                totalScore += pointsLost(direction);
                reward = reward();
                reward = punishOrMercy(direction, reward);
                totalScore += reward;
            }
        } while (isGameOver(x, y, totalScore, itrCount) == false); // This while loop will repeat everything under do until the player loses
        System.out.println("Please enter your username (Must be two words separated by a space: "); 
        Scanner userName = new Scanner(System.in);
        String str = userName.nextLine();
        evaluation(totalScore, str); //will execute evaluation method as well as toTitleCase method

    }
    public static void displayInfo(int x, int y, int itrCount, int totalScore){
        System.out.printf("For point (x=%d, y=%d) at iterations: %d the total score is: %d\n", x, y, itrCount, totalScore);
    }
    /*
    *This function checks if the robot has exceeded the limits of the grid
    *Cannot fall under 0 or go over 4 for x and y
    */
    public static boolean doesExceed(int x, int y, char direction) {
        switch (direction) {
            case 'u':
                y += 1;
            case 'd':
                y -= 1;
            case 'l':
                x -= 1;
            default:
                x += 1;
        }
        if (x < 0 || x > 4 || y < 0 || y > 4) { //if any of these are true, the robot has fallen off the grid
            return true;
        } else {
            return false;
        }    
    }
    /**
     * each number on the dice returns a positive or negative reward
     * @return 
     */
    public static int reward(){
        int reward = 0;
    Random diceThrow = new Random();
    int diceNum = 1 + diceThrow.nextInt(6); //generates random num between 1 and 6
    switch (diceNum) {
            case (1):               
                reward = -100;
                break;
            case (2):
                reward = -200;
                break;
            case (3):
                reward = -300;
                break;
            case (4):
                reward = 300;
                break;
            case (5):
                reward = 400;
                break;
            case (6):
                reward = 600;
                break;
        }
    System.out.println("Dice: " + diceNum + ", reward: " + reward);
    return reward;
}
    /**
     * Method is used to encourage moving upwards by implementing a coin flip
     * @param direction
     * @param reward
     * @return 
     */
    public static int punishOrMercy(char direction, int reward){
        Random coinFlip = new Random();
        int coinRes = coinFlip.nextInt(2);
        if(reward < 0 && direction == 'u') //if reward is negative or direction ir up, coin will be flipped
            if(coinRes == 0){
                reward = 0;
                System.out.println("Coin: tail | " + "Mercy, the negative reward is removed"); //Tails = negative reward will be reset
            } else{
                System.out.println("Coin: head | No mercy, the negative rewarded is applied"); //Heads, nothing will happen. The negative reward stays
            }
        return reward;
    }

    /**
     *This method will collect user input for direction
     *If input is not u,d,l or r the function will loop and ask again until one of these letters are used
     * @return 
     */
    public static char inputDirection(){
        Scanner console = new Scanner(System.in);
        System.out.println("Please input a direction, \"u\" for up, \"d\" for down, \"l\" for left and \"r\" for right");
        char direction = console.next().charAt(0);
        
        while(!(direction == 'u' || direction == 'd' || direction == 'l' || direction == 'r')){ //will loop until satisfactory input is received
            System.out.println("Please input a direction, \"u\" for up, \"d\" for down, \"l\" for left and \"r\" for right");
            direction = console.next().charAt(0); 

        }
        
        switch(direction){ //returns and stores input
            case 'u':
                return 'u';
            case 'd':
                return 'd';    
            case 'l':
                return 'l';
            default:
                return 'r';    
        }
        
    }
    /**
     * Method to turn 2 word name into titleCase
     * @param str
     * toTitle separates the first letter from both words and capitalizes them
     * toTitle takes the letters after first letter of both words using (0,1) and (i+1,i+2) to lower case the rest of the letters
     * integer i represents the space in between the 2 words
     * @return 
     */
    public static String toTitleCase(String str){
        int i = str.indexOf(' ');
        String toTitle = str.substring(0,1).toUpperCase() + str.substring(1, i).toLowerCase() 
        + " " + str.substring(i + 1,i + 2).toUpperCase() + str.substring(i+2).toLowerCase();
        return toTitle;
    }
    /**
     * This method will check if the robot has won
     * toTitleCase method is included to format the name at the same time
     * @param totalScore
     * @param str 
     */
    public static void evaluation(int totalScore, String str){
        if(totalScore >= 2000)
            System.out.println("Victory, " + toTitleCase(str) + ", your score is: " + totalScore); 
        else
            System.out.println("Mission failed, " + toTitleCase(str) + ", your score is: " + totalScore);
    }
    /**
     * Used to check if robot has completed the game
     * If count is above 20 or totalScore lower than -1000 or totalScore over 2000 or reached one of the end squares, game is completed.
     * @param x
     * @param y
     * @param totalScore
     * @param itrCount
     * @return 
     */
    public static boolean isGameOver(int x, int y, int totalScore, int itrCount){
        if(itrCount > 20 || totalScore < -1000 || totalScore > 2000 || x == 4 && y == 4
                || x == 4 && y == 0)
            return true;
        else
            return false;
        
    }
    /**
     * Method used to count points lost each step the robot takes
     * @param direction
     * @return 
     */
    public static int pointsLost(char direction){
        int pointLoss;
        if (direction == 'u') { //direction needs to be up to reduce loss to only 10 points
            pointLoss = -10;
        } else {
            pointLoss = -50; //any other direction will result in a loss of 50 points
        }
        return pointLoss;
    }
    
}
