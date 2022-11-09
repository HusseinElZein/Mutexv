package Testing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

class SharedResources2 {
    public static int amount;
}

class User2 extends Thread {
    Semaphore sem;
    Semaphore sem2;
    int id;

    public User2(Semaphore sem, Semaphore sem2, int id) {
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
                + --SharedResources2.amount + " tickets left...");

        sem2.release();
    }
}


public class HighTicketAmount {

    public static void main(String[] args) throws InterruptedException {

        SharedResources2.amount = 70;
        int amountOfUsers = 50;

        Semaphore sem = new Semaphore(SharedResources2.amount);
        Semaphore sem2 = new Semaphore(1);


        List<User2> users = new ArrayList<>();

        for (int i = 0; i < amountOfUsers; i++) {
            User2 user = new User2(sem, sem2, i+1);
            users.add(user);
        }

        for (User2 user : users) {
            user.start();
            //user.join();
        }
    }
}