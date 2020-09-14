# Dining-Philosophers-Problem
For CSE 4095 Cloud Computing and Distributed Systems
Narendran Krishnan
CSE 4095 Take Home Quiz 1
Professor Bradford

Basic explanation of Java code and how the solution works.
There are 3 solutions to deadlock issue of the dining philosopher problem posed by Dijkstra. Two them are simple to explain. One is the arbitrator solution, which involves telling a philosopher to only pick up two forks at a time. This way, one fork does not fall into the hands of each philosopher. However, it is called because the “waiter” has to deliver this instruction to the philosopher. The philosopher can only pick up the forks when the signal that there are two available forks is relayed to him/her. 
The other solution, which is used here, is the resource hierarchy solution. Simply put, the philosopher picks a numbered fork next to him whose number is lower than the other adjacent fork. By numbering the forks, in the case where each philosopher picks up the lower numbered fork at the same time (Philosopher 1 takes fork 1, philosopher 2 cannot take fork 2 because the philosopher has to take the lower numbered fork (fork 1) first. 
The code uses some utilities that should be explained. The first is semaphores. Semaphores allow the user to manage access to a critical resource that is being used alternatively by multiple concurrent processes. In this case, the forks are represented as an array of semaphores. The philosophers are represented as a class extension of the thread class. The philosopher threads are numbered, and have a number for a left and right fork. The philosopher thread have a few methods that represent their behaviors: eat, think, take fork, and return fork. They think and eat for a random amount of time, and at which the process threads are halted by the sleep command. When picking up the nearest fork. They share the fork number variable and use the built in semaphore method .acquire to take the fork and simultaneously signify to the other philosopher that the fork next to them has been taken, and .release (in the returnForks method, for the leftfork) to place the fork back to the table and to signify to the other philosophers that the fork can be taken.
TO COMPILE AND RUN ON THE COMMAND LINE
COMPILE
(mimir is the online IDE I used to compile and run the program as a test, and Scrap is the directory I saved it to.)
user@mimir: ~/Scrap > javac DiningPhilosophers.java
RUN 
user@mimir: ~/Scrap > java DiningPhilosophers
