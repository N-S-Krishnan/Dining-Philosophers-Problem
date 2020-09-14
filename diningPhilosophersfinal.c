/**
  Dining Philosophers Problem
  Written by David J Pfeiffer
  Solution Name: Resource Hierarchy
  Compile on UNIX system: gcc -o main -std=c99 main.c -lpthread
**/

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>
#include <time.h>
#include <stdlib.h>
#include <unistd.h>

typedef struct {
  int position;
  int count;
  sem_t *leftFork;
  sem_t *rightFork;
} philosopher_t;

void create_forks(sem_t *forks, int num_philosophers);
void start_threads(pthread_t *threads, sem_t *forks, int num_philosophers);

void *philosopherCallback(void *arg);

void think(philosopher_t *philosopher);
void eat(philosopher_t *philosopher);
void takeForks(philosopher_t *philosopher);
void returnForks(philosopher_t *philosopher);

int shouldTakeLeftForkFirst(philosopher_t *philosopher);
int getRandomNumber(int max, int min);

int main(int argc, char *args[])
{
  srand(time(NULL));
  int num_philosophers = getRandomNumber(2, 2);
  
  printf("Number of Philosophers: %d\n", num_philosophers);
  
  sem_t forks[num_philosophers];
  pthread_t threads[num_philosophers];
  
  create_forks(forks, num_philosophers);
  start_threads(threads, forks, num_philosophers);
  
  pthread_exit(NULL);
}

void create_forks(sem_t* forks, int num_philosophers)
{
  for(int i = 0; i < num_philosophers; i++) {
    sem_init(&forks[i], 0, 1);
  }
}

void start_threads(pthread_t *threads, sem_t *forks, int num_philosophers)
{
  for(int i = 0; i < num_philosophers; i++) {
    philosopher_t *philosopher = malloc(sizeof(philosopher_t));
    
    philosopher->position = i;
    philosopher->count = num_philosophers;
    philosopher->leftFork = &forks[i];
    philosopher->rightFork = &forks[i + 1 % num_philosophers];
    
    pthread_create(&threads[i], NULL, philosopherCallback, (void *)philosopher);
  }
}

void *philosopherCallback(void *arg)
{
  philosopher_t *philosopher = (philosopher_t *)arg;
  
  while(1)
  {
    think(philosopher);
    takeForks(philosopher);
    eat(philosopher);
    returnForks(philosopher);
  }
}

void think(philosopher_t *philosopher)
{
  printf("Philosopher %d started thinking\n", philosopher->position + 1);
  
  do
  {
    sleep(getRandomNumber(7, 1));
  } while (getRandomNumber(1, 0));
  
  printf("Philosopher %d stopped thinking\n", philosopher->position + 1);
}

void eat(philosopher_t *philosopher)
{
  printf("Philosopher %d started eating\n", philosopher->position + 1);
  
  do
  {
    sleep(getRandomNumber(5, 1));
  } while (getRandomNumber(1, 0));
  
  printf("Philosopher %d stopped eating\n", philosopher->position + 1);
}

void takeForks(philosopher_t *philosopher)
{
  if (shouldTakeLeftForkFirst(philosopher))
  {
    // this branch never executes!
    printf("Phil %d TAKING THE LEFT FORK FIRST??\n", philosopher->position+1);
    sem_wait(philosopher->leftFork);
    printf("Phil %d Took LEFT fork\n", philosopher->position+1);
    sem_wait(philosopher->rightFork);
  }
  else
  {
    printf("Phil %d Taking RIGHT fork first\n", philosopher->position+1);
    sem_wait(philosopher->rightFork);
    printf("Phil %d Took RIGHT fork\n", philosopher->position+1);
    sleep(getRandomNumber(20, 1));
    sem_wait(philosopher->leftFork);
  }
}

void returnForks(philosopher_t *philosopher)
{
  sem_post(philosopher->rightFork);
  sem_post(philosopher->leftFork);
}

int shouldTakeLeftForkFirst(philosopher_t *philosopher)
{
  return (philosopher->position - 1) == philosopher->count;
}

int getRandomNumber(int max, int min)
{
  int result = (rand() % (max + 1));
  if (result < min) result = min;
  return result;
}