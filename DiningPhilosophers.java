//
// java program to run dining philosophers
// uses semaphores
//
import java.util.concurrent.*;
import java.util.Random;
import java.util.Scanner;

// main class
public class DiningPhilosophers
{

    public static void main(String args[]) throws InterruptedException //exception used when thread wait/sleep/or occupied
    {
        Random rand = new Random();
        //System.out.print( "Enter Number of Philosopers: " );
        //int myInput = rand.nextInt(20);
        //int num_philosophers = myInput.nextInt();
        int num_philosophers = rand.nextInt(20)+5;
        // creating a Semaphore object
        // with number of permits 1
        //Semaphore forks[num_philosophers];
        PhilosopherThread[] mts; //Array of philosopherThread objects
        mts = new PhilosopherThread[num_philosophers];
        Shared.forks = new Semaphore[num_philosophers]; //the for loop initializes the values of this semaphore array
        for(int i = 0; i < num_philosophers; i++) { //This loop creates a new semaphore to represent the forks
          Shared.forks[i] = new Semaphore(1);
          mts[i] = new PhilosopherThread(i, num_philosophers);
        }

        // stating threads
        for(int i = 0; i < num_philosophers; i++) {
          mts[i].start();
        }

    }
}
//A shared resource/class that holds a sempahore array for forks
class Shared
{
    static Semaphore[] forks;
}
// Philosopher implemented by a thread
class PhilosopherThread extends Thread
{
    int threadNum;  // philosopher number
    Random rand;
    int leftfork;
    int rightfork;
    int nphil;
    //my getrand
    
    public int getrandnum (int a, int b)
    {
        return rand.nextInt(a-b)+b;
    }

    public PhilosopherThread( int threadNum, int num_philosophers)
    {
        super(); //super calls the BASE class constructor, in this case it would be the THREAD class (PhilosopherThread is an ext of Thread)
        this.threadNum = threadNum; //refers only to THIS instance of the class PhilosopherThread
        leftfork = threadNum;
        rightfork = (threadNum+1) % num_philosophers; // hardcoded
        rand = new Random();
        nphil = num_philosophers;
    }

    // think between 1 and 5 secs
    public void think( int threadNum)throws InterruptedException
    {
        System.out.println("Philosopher " + Integer.toString(threadNum) + " thinking");
        int n = getrandnum(10000, 5000);
        this.sleep(n);
        
    }
    //pick up forks
    public void takeForks(int threadNum)throws InterruptedException
    {
        if (shouldTakeLeftForkFirst())
        {
            Shared.forks[leftfork].acquire();
            System.out.println("Philosopher " + Integer.toString(threadNum) + " picked up fork " + Integer.toString(leftfork));
            Shared.forks[rightfork].acquire();
            System.out.println("Philosopher " + Integer.toString(threadNum) + " picked up fork " + Integer.toString(rightfork));
        }
        else
        {
            Shared.forks[rightfork].acquire();
            System.out.println("Philosopher " + Integer.toString(threadNum) + " picked up fork " + Integer.toString(rightfork));            
            Shared.forks[leftfork].acquire();
            System.out.println("Philosopher " + Integer.toString(threadNum) + " picked up fork " + Integer.toString(leftfork));
        }
    }
    // eat taking 1 to 3 secs
    public void eat( int threadNum)throws InterruptedException
    {
        System.out.println("Philosopher " + Integer.toString(threadNum) + " eating");
        int n = getrandnum(5000, 1000);
        this.sleep(n);
    }
    // return forks
    public void returnForks( int threadNum)throws InterruptedException
    {
        System.out.println("Philosopher " + Integer.toString(threadNum) + " dropping fork " + Integer.toString(leftfork));
        Shared.forks[leftfork].release();
        System.out.println("Philosopher " + Integer.toString(threadNum) + " dropping fork " + Integer.toString(rightfork));
        Shared.forks[rightfork].release();
    }
    // this enforces resource hierarchy
    // so the "last" numbered philosopher takes the lower numbered fork first like the other philosophers
    boolean shouldTakeLeftForkFirst()
    {
        if (this.threadNum == nphil)
            return(true);
        return false;
    }

    // the run method puts the philosopher (thread) in a loop of
    // thinking, picking up forks if they can, eating, dropping the forks, thinking etc
    @Override
    public void run() {

        System.out.println("Starting " + Integer.toString(threadNum));
        // run by thread threadNum
        while (true)
        {
            try
            {
                // think
                think(threadNum);
                // pick up forks
                takeForks(threadNum);
                // eat
                eat(threadNum);
                // teturn forks to table
                returnForks(threadNum);

            }
            catch (InterruptedException exc) {
                    System.out.println(exc);

            }
        }

    }
}


