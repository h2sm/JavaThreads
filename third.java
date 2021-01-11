package threads.basic;
import java.util.ArrayList;

public class third {
    public static void main(String[] args) throws InterruptedException {
        var threads = new ArrayList<Thread>();
        for (int i = 0; i < 10; i++) threads.add(new Thread(new newWorker(i)));
        threads.forEach(Thread::start);

        threads.forEach(third::join);
        System.out.println("main finished");

    }
    private static void join(Thread t) {
        try { t.join(); }
        catch (Exception e) { /*nothing*/ }
    }
}

class newWorker implements Runnable{
    int numberOfThread;
    private boolean isActive;
    final private long timeStart = System.currentTimeMillis();//время запуска треда
    private int counter = 0;

    void disable(){
        isActive=false;
    }

    public newWorker(int numThread){
        this.numberOfThread=numThread;
        isActive=true;
    }
    @Override
    public void run() {
        try {
            while(isActive) {
                counter++;
                long timeEnd = System.currentTimeMillis();
                System.out.println("hello from thread " + numberOfThread + " working for " + (float) (timeEnd - timeStart) / 1000F);
                Thread.sleep(1000);
                if (counter==5){
                    disable();
                }
            }
            System.out.println(Thread.currentThread().getName() + " interrupted");
        }
        catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " interrupted");
        }
    }
}

