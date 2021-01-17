package threads.basic;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class nine {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);
        var philosophersThreads = new ArrayList<Thread>();
        for (int i = 0; i < 7; i++) {
            philosophersThreads.add(new Thread(new Philosophy(semaphore, i)));
        }
        philosophersThreads.forEach(Thread::start);
        
    }
}
class Philosophy implements Runnable {
    Semaphore sem;
    int COUNT = 0;
    int id;
    public Philosophy(Semaphore semaphore, int i){
        this.sem=semaphore;
        this.id = i;
    }
    @Override
    public void run() {
        try {
            while (COUNT < 3){
                sem.acquire();
                System.out.println("Философ " + id + " сел кушотб");
                Thread.sleep(500);
                COUNT++;
                System.out.println("Философ " + id + " покушолб");
                sem.release();
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
