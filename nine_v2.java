package threads.basic;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class nine_v2 {
    public static void main(String[] args) {
        new Eat().starterPack();
        var array = new ArrayList<Thread>();
        for (int i = 0; i < 7; i++) {
            array.add(new Thread(new Philosopher(i)));
        }
        array.forEach(Thread::start);
    }
}
class Eat {
    static ArrayList <Boolean> fork = new ArrayList<>();//доступные вилки, true - свободны, false - заняты
    static ReentrantLock rlLock;
    static Condition cond;
    Eat(){
        rlLock = new ReentrantLock();
        cond = rlLock.newCondition();
    }
    void startEating(int id, int leftForkID, int rightForkID) throws InterruptedException {
            while (true) {
                rlLock.lock();
                if (fork.get(leftForkID) == true && fork.get(rightForkID) == true) {
                    fork.set(leftForkID, false);
                    fork.set(rightForkID, false);
                    System.out.println("Философ " + id + " взял " + leftForkID + " и" + rightForkID + " вилки. Состояние:" + fork);
                    rlLock.unlock();
                    Thread.sleep(3000);
                    rlLock.lock();
                    fork.set(leftForkID, true);
                    fork.set(rightForkID, true);
                    System.out.println("Философ " + id + " положил " + leftForkID + " и" + rightForkID + " вилки " + " Состояние:" + fork);
                    rlLock.unlock();
                } else {
                    rlLock.unlock();
                }
            }
    }
    void starterPack(){
        for (int i = 0; i <7 ; i++) {
            fork.add(true);
        }
    }
}

class Philosopher implements Runnable{
    Eat eat = new Eat();//хачю кушотб
    int id, leftForkID, rightForkID;
    public Philosopher(int num){
       this.id=num;
    }
    @Override
    public void run() {
        if (id == 0){
            leftForkID = 6;
            rightForkID = 0;
        }
        else {
            leftForkID = id - 1;
            rightForkID = id;
        }
        try {
            eat.startEating(id, leftForkID, rightForkID);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
