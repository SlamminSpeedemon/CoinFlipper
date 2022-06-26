import java.io.*;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CoinHandler {

    //instance variables
    private int headConsecutive;
    private int tailConsecutive;
    private int consecutiveCounter;
    private int headTies;
    private int tailTies;
    private int headsRecord;
    private int tailsRecord;

    private File recordsFile;

    public CoinHandler() {
        recordsFile = new File("src/Records.txt");

        try {
            FileReader fileReader = new FileReader(recordsFile);
            Scanner reader = new Scanner(recordsFile);
            headsRecord = Integer.parseInt(reader.nextLine());
            tailsRecord = Integer.parseInt(reader.nextLine());
        } catch (FileNotFoundException e) {
            System.out.println("Error in opening file");
            System.out.println("Creating new file...");
            headsRecord = 0;
            tailsRecord = 0;
            setRecords();
            //throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            System.out.println("Error in reading file");
            System.out.println("Creating new file...");
            headsRecord = 0;
            tailsRecord = 0;
            setRecords();
            System.out.println("new file successfully created!");
        } catch (NoSuchElementException e) {
            System.out.println("There are no entries in the file\nCreating a new one...");
            headsRecord = 0;
            tailsRecord = 0;
            setRecords();
            System.out.println("new file successfully created!");
        }

        headConsecutive = 0;
        tailConsecutive = 0;
        consecutiveCounter = 1;
    }

    //does (numOfFlips) amount of coin tosses, records consecutives in instance variables
        //did not make it static because it would be less organized, instance variables help
        //with readability and debugging, it also makes it easier to add future functionality
    public void doFlips(int numOfFlips) {
        int lastFlip = -1; //last flip's value; set to -1 so it can't be consecutive on first flip
        int randomizer; //integer used to determine if heads or tails; 1 = heads, 0 = tails
        String flipPrinter;

        for (int i = 0; i < numOfFlips; i++) {
            randomizer = (int) (Math.random() * 2);
            if (randomizer == 1) {
                flipPrinter = "H";
            } else {
                flipPrinter = "T";
            }

            //System.out.println(randomizer);

            if (randomizer == lastFlip) {//is consecutive flip
                consecutiveCounter++;
                System.out.print("\t" + flipPrinter);
            } else {//is not consecutive flip

                if (consecutiveCounter != 1) {
                    //find out which type of consecutive, and store it if its higher

                    if (lastFlip == 1 && consecutiveCounter >= headConsecutive)  {//was a heads consecutive
                        if (consecutiveCounter == headConsecutive) {//tied highest
                            headTies++;
                        } else {
                            headConsecutive = consecutiveCounter;
                            headTies = 0;
                        }
                    } else if (lastFlip == 0 && consecutiveCounter >= tailConsecutive) {//was a tails consecutive
                        if (consecutiveCounter == tailConsecutive) {//tied highest
                            tailTies++;
                        } else {
                            tailConsecutive = consecutiveCounter;
                            tailTies = 0;
                        }
                    }
                    System.out.print("\t\tConsecutives: " + consecutiveCounter); //finish consecutives line
                    consecutiveCounter = 1; //reset consecutives counter
                } //end of consecutive storage if statement

                System.out.print("\n" + flipPrinter); //print the isolated case in a new line
            }
            lastFlip = randomizer;

        }//end of for loop

        //print results
            //had to have +1 for ties because counter starts at 0
        System.out.println("\n Highest number of Consecutive Heads: " + headConsecutive);
        if (headTies != 0) System.out.println("\tHad " + (headTies + 1) + " ties for head highest!");
        System.out.println(" Highest number of Consecutive Tails: " + tailConsecutive);
        if (tailTies != 0) System.out.println("\tHad " + (tailTies + 1) + " ties for tail highest!");

        checkRecords();
    }

    //checks to see if a record has been set, if so it will call the function to set the record
    public void checkRecords() {
        boolean updateRecords = false;

        System.out.println("\nThe old record for consecutive heads was: " + headsRecord);
        if (headConsecutive >= headsRecord) { //check if heads made record
            if (headConsecutive == headsRecord) {
                System.out.println("Congrats you tied the previous heads record of " + headsRecord);
            } else {
                System.out.println("You set a new record for consecutive heads: " + headConsecutive);
                headsRecord = headConsecutive;
                updateRecords = true;
            }
        }

        System.out.println("\nThe old record for consecutive tails was: " + tailsRecord);
        if (tailConsecutive >= tailsRecord) {
            if (tailConsecutive == tailsRecord) {
                System.out.println("Congrats you tied the previous tails record of " + tailsRecord);
            } else {
                System.out.println("You set a new record for consecutive tails: " + tailConsecutive);
                tailsRecord = tailConsecutive;
                updateRecords = true;
            }
        }

        //only rewrite the file if new records are set
        if (updateRecords) setRecords();
    }

    //updates proper record in records file by overwriting it with the given values
    public void setRecords() {
        try {
            PrintWriter printWriter = new PrintWriter(recordsFile);
            printWriter.println(headsRecord);
            printWriter.println(tailsRecord);
            printWriter.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //does recursion until it gets a positive integer
    public int getInput() {
        Scanner input = new Scanner(System.in);
        int numOfFlips = -1;
        try {
            numOfFlips = input.nextInt();
        } catch (InputMismatchException e){
            System.out.println("Put an integer.");
        }
        if (numOfFlips < 0) {
            System.out.println("\t Let's try again.");
            return getInput();
        }

        return numOfFlips;
    }


    public static void main(String[] args) {
        CoinHandler coinHandler = new CoinHandler();
        int numOfFlips = 1;

        while (numOfFlips > 0) {
            System.out.print("How many times do you want to flip a coin: ");
            numOfFlips = coinHandler.getInput();

            if (numOfFlips != 0) {
                coinHandler.doFlips(numOfFlips);

                System.out.println("Well that was fun! Lets do it again! Type 0 to quit.");
                numOfFlips = coinHandler.getInput();
            } else {
                System.out.println("Jeez louizz, you really don't want to play");
            }

        }
        System.out.println("Well goodbye!");

    }
}
