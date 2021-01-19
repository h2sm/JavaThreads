package threads.basic;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class tenth {
    public static void main(String[] args) {
        DB db = new DB();
        Random random = new Random();
        db.fillDB();
        var writerThreads = new ArrayList<Thread>();
        var readerThreads = new ArrayList<Thread>();

        for (int i = 0; i <10 ; i++) {
            if (i<5) writerThreads.add(new Thread(new Writer(db, random)));
            readerThreads.add(new Thread(new Reader(db, random)));
        }
        writerThreads.forEach(Thread::start);
        readerThreads.forEach(Thread::start);

    }
}
class DB{
    private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private Lock readLock = rwl.readLock();
    private Lock writeLock = rwl.writeLock();
    private ArrayList<Integer> dataBase = new ArrayList<>();//условная база данных с какими-то числами

    void writeToDatabase(int num) throws InterruptedException {
        writeLock.lock();
        try {
            dataBase.add(num);
            Thread.sleep(1000);
        }
        finally {
            System.out.println("Записано число " + num + " Массив: " + dataBase);
            writeLock.unlock();
        }
    }
    void readFromDatabase(int num){
        readLock.lock();
        try {
            System.out.println("Считано число " + dataBase.get(num) + " Массив: " + dataBase);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
    }
    void fillDB(){
        dataBase.add(0);
        dataBase.add(1);
        dataBase.add(2);
        dataBase.add(3);
        dataBase.add(4);
        dataBase.add(5);
    }
    int getSize(){
        return dataBase.size();
    }
}
class Reader implements Runnable{
    DB data;
    int size;
    Random rand;
    public Reader(DB db, Random random){
        this.data=db;
        rand = random;
    }
    @Override
    public void run() {
        size = data.getSize();
        while (!Thread.currentThread().isInterrupted()) {
            data.readFromDatabase(rand.nextInt(10));
            System.out.println(Thread.currentThread().getName() + " READER");
        }
    }
}
class Writer implements Runnable{
    DB data;
    Random rand;
    int[] someArray = {6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};

    public Writer(DB db, Random random){
        this.data=db;
        rand = random;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                int x = rand.nextInt(5);
                data.writeToDatabase(someArray[x]);
                System.out.println(Thread.currentThread().getName() + " WRITER");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
