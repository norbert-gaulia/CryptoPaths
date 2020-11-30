/***************************************************************************
*  FILE: UserInterface.java
*  AUTHOR: Bilal Farah
*  PURPOSE: Contains methods that handle user input and output.
*  LAST MOD: 01/10/20
*  REQUIRES: NONE
*  As submitted in all practicals as well as in ultiple assignments of COMP1007.
***************************************************************************/
import java.util.*;
public class UserInterface 
{
    //will be called by the input submodules
    private static Scanner sc = new Scanner(System.in);
    //formatted output
    private static String in = ""+"\033[32m>>>\033[0m"+"";

    /********************************************************************
     * SUBMODULE: userInput
     * IMPORT: (float, int, double, long, String, char) - lower , upper
     * EXPORT: (float, int, double, long, String, char ) - input
     * ASSERTION: returns the user input the appropriate data type based on the set limits in the parameters
     *********************************************************************/

    public static int userInput(String prompt, int lower, int upper)
    {
        String outMsg = prompt, errMsg = "Error! Input must be between " + lower + " and " + upper + ".";
        int input = 0;
        do
        {
            try
            {
                System.out.println(outMsg);
                System.out.print(in);
                input = sc.nextInt();
            }
            //exception handling 
            catch(InputMismatchException e)
            {   
                //displayError(e.getMessage());
                //clear the invalid input from buffer
                sc.next();
                //set input to an invalid so as not exit loop.
                input = lower - 1;
            }
            sc.nextLine(); // Skip the newline after the value is read.
            outMsg = errMsg + " " + prompt;
        }while((input < lower) || (input > upper));
        return input;
    }
    public static double userInput(String prompt, double lower, double upper)
    {
        String outMsg = prompt, errMsg = "Error! Input must be between " + lower + " and " + upper + ".";
        double input = 0;
        do
        {
            try
            {
                System.out.println(outMsg);
                System.out.print(in);
                input = sc.nextDouble();
            }
            //exception handling
            catch(InputMismatchException e)
            {
                //displayError(e.getMessage());
                //clear the invalid input from buffer
                sc.next();
                //set input to an invalid so as not exit loop.
                input = upper + 1.0;
            }
            sc.nextLine(); // Skip the newline after the value is read.
            outMsg = errMsg + " " + prompt;
        }while((input < lower) || (input > upper));
        return input;
    }
    public static long userInput(String prompt, long lower, long upper)
    {
        String outMsg = prompt, errMsg = "Error! Input must be between " + lower + " and " + upper + ".";
        long input = 0;
        do
        {
            try
            {
                System.out.println(outMsg);
                System.out.print(in);
                input = sc.nextLong();
            }
            //exception handling
            catch(InputMismatchException e)
            {
                //displayError(e.getMessage());
                //clear the invalid input from buffer
                sc.next();
                //set input to an invalid so as not exit loop.
                input = lower * 2;
            }
            sc.nextLine(); // Skip the newline after the value is read.
            outMsg = errMsg + " " + prompt;
        }while((input < lower) || (input > upper));
        return input;
    }
    public static float userInput(String prompt, float lower, float upper)
    {
        String outMsg = prompt, errMsg = "Error! Input must be between " + lower + " and " + upper + ".";
        float input = 0;
        do
        {
            try
            {
                System.out.println(outMsg);
                System.out.print(in);
                input = sc.nextFloat();
            }
            //exception handling
            catch(InputMismatchException e)
            {
                //displayError(e.getMessage());
                //clear the invalid input from buffer
                sc.next();
                //set input to an invalid so as not exit loop.
                input = upper * 8;
            }
            sc.nextLine(); // Skip the newline after the value is read.
            outMsg = errMsg + " " + prompt;
        }while((input < lower) || (input > upper));
        return input;
    }
    public static char userInput(String prompt, char lower, char upper)
    {
        String outMsg = prompt, errMsg = "Error! Input must be between " + lower + " and " + upper + ".";
        char input;
        do
        {
            try
            {
                System.out.println(outMsg);
                System.out.print(in);
                input = sc.next().charAt(0);
            }
            catch(InputMismatchException e)
            {
                sc.next();
                input = (char)((int)(lower - 1));
            }
            sc.nextLine(); // Skip the newline after the value is read.
            outMsg = errMsg + " " + prompt;
        }while((input < lower) || (input > upper));
        return input;
    }
    
    public static String userInput(String prompt)
    {
        String str = "";
        System.out.println(prompt);
        System.out.print(in);
        str = sc.nextLine();
        return str;
    }
    /********************************************************************
     * SUBMODULE: displayError
     * IMPORT: error (String)
     * EXPORT: none
     * ASSERTION: prints out the error message.
     *********************************************************************/
    public static void displayError(String error)
    {
        String redC = "\033[31m", endC = "\033[0m";
        //this is for better visibility on the terminal where \033[31m will switchs the text color to red and \033[0m switches it back to normal.
        System.out.println(redC+"Error: "+ endC + error);
    }
    /********************************************************************
     * SUBMODULE: displayMsg
     * IMPORT: msg (String)
     * EXPORT: none
     * ASSERTION: prints out the message.
     *********************************************************************/
    public static void displayMsg(String msg)
    {
        System.out.println(msg);
    }
    
    public static void displayMsgS(String msg) {
        System.out.print(msg);
    }
    /********************************************************************
     * SUBMODULE: displayMsg
     * IMPORT: msg (String)
     * EXPORT: none
     * ASSERTION: prints out the message.
     *********************************************************************/
    public static void displayScs(String msg)
    {
        System.out.println("\033[32mSuccess:\033[0m"+msg);
    }

}