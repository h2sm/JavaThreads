//https://imgur.com/a/QMsCeyh - вывод

package threads.basic;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class sixth {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock reentrantLock = new ReentrantLock();
        var producersThreads = new ArrayList<Thread>();
        var consumersThreads = new ArrayList<Thread>();
        for (int i = 0; i < 10; i++) {
            producersThreads.add(new Thread(new Producers(reentrantLock)));
            consumersThreads.add(new Thread(new Consumers(reentrantLock)));
        }

        producersThreads.forEach(Thread::start);
        consumersThreads.forEach(Thread::start);

//        Thread.sleep(4000);

        producersThreads.forEach(Thread::interrupt);
        consumersThreads.forEach(Thread::interrupt);
    }

}
class BufferFile{
    static int[] SHAREDMEMORY = new int[1];
    void addSmth(){//добавляем в буфер
        SHAREDMEMORY[0]++;
        showSum("add");
    }
    void retrieveSmth(){//забираем из буфера
        SHAREDMEMORY[0]--;
        showSum("retrieve");
    }
    void showSum(String str){
        System.out.println(Arrays.toString(SHAREDMEMORY) + " from " + str);
    }
    int sum(){
        return Arrays.stream(SHAREDMEMORY).sum();
    }
}

class Producers implements Runnable{
    ReentrantLock reentrantLock;
    BufferFile bufferFile = new BufferFile();
    public Producers(ReentrantLock rl){
        reentrantLock=rl;
    }
    @Override
    public void run() {
        reentrantLock.lock();
        try {
            while (bufferFile.sum()<1)
                bufferFile.addSmth();
        }
        finally {
            reentrantLock.unlock();
        }

    }
}
class Consumers implements Runnable{
    ReentrantLock lock;
    BufferFile bufferFile = new BufferFile();
    public Consumers(ReentrantLock reentrantLock){
        lock=reentrantLock;
    }
    @Override
    public void run() {
        lock.lock();
        try {
            while (bufferFile.sum()>0)
                bufferFile.retrieveSmth();
        }
        finally {
            lock.unlock();
        }
    }
}


