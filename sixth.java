//https://imgur.com/a/QMsCeyh - вывод

package threads.basic;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class sixth {
    public static void main(String[] args) throws InterruptedException {
        var producersThreads = new ArrayList<Thread>();
        var consumersThreads = new ArrayList<Thread>();
        for (int i = 0; i < 10; i++) {
            producersThreads.add(new Thread(new Producers()));
            consumersThreads.add(new Thread(new Consumers()));
        }

        producersThreads.forEach(Thread::start);
        consumersThreads.forEach(Thread::start);

        //Thread.sleep(10000);

//        producersThreads.forEach(Thread::interrupt);
//        consumersThreads.forEach(Thread::interrupt);
    }

}
class BufferFile{
    static ReentrantLock reentrantLock = new ReentrantLock();
    static Condition condition;
    BufferFile(){
        condition= reentrantLock.newCondition();
    }
    static int[] SHAREDMEMORY = new int[1];
    void add(){//добавляем в буфер
        reentrantLock.lock();
        try {
            while (SHAREDMEMORY[0]!=0) condition.await();
            SHAREDMEMORY[0]++;
            showSum("add");
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }
    }
    void retrieve(){//забираем из буфера
        reentrantLock.lock();
        try {
            while (SHAREDMEMORY[0]!=1) condition.await();
            SHAREDMEMORY[0]--;
            showSum("retrieve");
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }
    }
    void showSum(String str){
        System.out.println(Arrays.toString(SHAREDMEMORY) + " from " + str);
    }
    int sum(){
        return Arrays.stream(SHAREDMEMORY).sum();
    }
}

class Producers implements Runnable{
    BufferFile bufferFile = new BufferFile();
    @Override
    public void run() {
        bufferFile.add();
        System.out.println(Thread.currentThread().getName());
    }
}
class Consumers implements Runnable{
    BufferFile bufferFile = new BufferFile();
    @Override
    public void run() {
        bufferFile.retrieve();
        System.out.println(Thread.currentThread().getName());

    }
}


