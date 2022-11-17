package Testing;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

class SharedResources3 {
    public static int amount;
}

class User3 extends Thread {
    Semaphore sem;
    Semaphore sem2;
    int id;

    public User3(Semaphore sem, Semaphore sem2, int id) {
        this.sem = sem;
        this.sem2 = sem2;
        this.id = id;
    }

    @Override
    public void run() {
        Random r = new Random();
        int willBuy = r.nextInt(100) % 2;

        System.out.println("User " + id + ": waiting for a permit.");
        try {
            sem2.acquire();
        } catch (InterruptedException ignored) {
        }

        try {
            if (SharedResources3.amount == 0) {
                sem2.release();
                if (!sem.tryAcquire(10000, TimeUnit.MILLISECONDS)) {
                    System.out.println("User " + id + ": After 10 seconds, there are still no ticket left. Now killing the Thread");
                    return;
                }
            } else {
                sem.acquire();
            }

        } catch (InterruptedException e) {
        }

        System.out.println("User " + id + ": Acquired permit");

        int randomInt = r.nextInt(300) + 10;

        try {
            Thread.sleep(randomInt);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // access shared resource
        System.out.println("User " + id + " just bought a ticket, there are " + --SharedResources3.amount + " tickets left...");

        if (willBuy == 0) {
            System.out.println("User " + id + " cancels the reservation of a ticket, there are still " + ++SharedResources3.amount + " tickets left...");
            sem.release();
            sem2.release();
            return;
        }
        sem2.release();
    }
}


public class TooManyUsers {

    public static void main(String[] args) throws InterruptedException {

        SharedResources3.amount = 10;
        int amountOfUsers = 30;

        Semaphore sem = new Semaphore(SharedResources3.amount);
        Semaphore sem2 = new Semaphore(1);


        List<User3> users = new ArrayList<>();

        for (int i = 0; i < amountOfUsers; i++) {
            User3 user = new User3(sem, sem2, i + 1);
            users.add(user);
        }

        for (User3 user : users) {
            user.start();
            //user.join();
        }
    }
}