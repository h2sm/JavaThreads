package threads.basic;
import java.util.ArrayList;

public class eight {
    public static void main(String[] args) throws InterruptedException {
        var piThreads = new ArrayList<Thread>();
        for (int i = 0; i < 5; i++) {
            piThreads.add(new Thread(new Counter(i)));
        }
        piThreads.forEach(Thread::start);
    }
}
class PiCount{
    double PI = 0;
    void countPI(){
        for (int i = 1; i < 1000000000; i += 4) {
            PI += 8.0 / (i * (i + 2L));
        }
        System.out.println(PI);
    }
}

class Counter implements Runnable{
    int threadNumber;
    public Counter(int id){
        this.threadNumber=id;
    }
    PiCount piCount = new PiCount();
    @Override
    public void run() {
        piCount.countPI();
        System.out.println(Thread.currentThread().getName());
    }
}
