package Testing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

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

        System.out.println("Testing.User " + id + ": waiting for a permit.");
        try {
            sem2.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // acquire the permit
        if (!sem.tryAcquire()) {
            System.out.println("Testing.User " + id + " cannot acquire a permit as all tickets have been bought");
            sem2.release();
            return;
        }
        System.out.println("Testing.User " + id + ": Acquired permit");

        Random r = new Random();
        int randomInt = r.nextInt(300) + 10;

        try {
            Thread.sleep(randomInt);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // access shared resource
        System.out.println("Testing.User " + id + " just bought a ticket, there are "
                + --SharedResources3.amount + " tickets left...");

        sem2.release();
    }
}


public class TooManyUsers {

    public static void main(String[] args) throws InterruptedException {

        SharedResources3.amount = 5;
        int amountOfUsers = 10;

        Semaphore sem = new Semaphore(SharedResources3.amount);
        Semaphore sem2 = new Semaphore(1);


        List<User3> users = new ArrayList<>();

        for (int i = 0; i < amountOfUsers; i++) {
            User3 user = new User3(sem, sem2, i+1);
            users.add(user);
        }

        for (User3 user : users) {
            user.start();
            //user.join();
        }
    }
}