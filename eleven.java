package threads.basic;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class eleven {
    public static void main(String[] args) {
        ShowerRoom showerRoom = new ShowerRoom();
        var womanThreads = new ArrayList<Thread>();
        var manThreads = new ArrayList<Thread>();
        for (int i = 0; i < 20; i++) {
            womanThreads.add(new Thread(new Woman(showerRoom, i)));
            manThreads.add(new Thread(new Man(showerRoom, i)));
        }

        womanThreads.forEach(Thread::start);
        manThreads.forEach(Thread::start);
    }
}
class ShowerRoom{
    private ReentrantLock rl = new ReentrantLock();
    private int showerRoomCapability = 10;
    private int manNumber = 0;//счетчик девушек в душевой
    private int womanNumber = 0;//счетчик мужчин в душевой
    private Condition manWants = rl.newCondition();
    private Condition womanWants = rl.newCondition();

    void womanWantsToEnter(int num) throws InterruptedException {
        rl.lock();
        try {
            while (manNumber != 0 || womanNumber >= showerRoomCapability ){
                System.out.println("Девушка " + num + " попыталась войти. Девушек - " + womanNumber + ", мужчин - " + manNumber);
                womanWants.await();//пока есть мужчины или все кабинки заняты женщинами, то ждем
            }
            womanNumber++;
            System.out.println("Зашла девушка " + num + ". Количество девушек в душевой - " + womanNumber);
        }
        finally {
            rl.unlock();
        }
    }
    void manWantsToEnter(int num) throws InterruptedException {
        rl.lock();
        try {
            while (womanNumber != 0 || manNumber >= showerRoomCapability) {
                System.out.println("Мужчина " + num + " попытался войти. Девушек - " + womanNumber + ", мужчин - " + manNumber );
                manWants.await();//пока есть женщины(кол-во женщин !=0) или все кабинки заняты мужинами, то ждем
            }
            manNumber++;
            System.out.println("Зашел мужчина " + num+ ". Количество мужчин в душевой - " + manNumber);
        }
        finally {
            rl.unlock();
        }
    }
    void womanLeaves(int num){
        rl.lock();
        try {
            if (womanNumber != 0) {
                womanNumber--;
                System.out.println("Вышла девушка " + num + ". Количество девушек - " + womanNumber);
            }
            if (womanNumber==0){
                System.out.println("Все женщины вышли. Мужчинам можно войти");
                manWants.signalAll();
            }
            else if (womanNumber >=7 && womanNumber<=9){
                manWants.signal();//попытка симуляции ситуации, если вдруг мужчина решит зайти
                womanWants.signal();//женщина хочет зайти
            }
        }
        finally {
            rl.unlock();
        }
    }
    void manLeaves(int num){
        rl.lock();
        try {
            if (manNumber!=0){
                manNumber--;
                System.out.println("Мужчина " + num + " вышел. Количество людей - " + manNumber);
            }
            if (manNumber==0){
                System.out.println("Все мужчины вышли. Девушкам можно зайти");
                womanWants.signalAll();
            }
            else if (manNumber>=7 && manNumber <= 9){
                womanWants.signal();//а вдруг женщина зайдет
                manWants.signal();//мужчина хочет зайти
            }
        }
        finally {
            rl.unlock();
        }
    }

}
class Man implements Runnable{
    private ShowerRoom showerRoomMan;
    int num;
    public Man(ShowerRoom sr, int i){
        this.showerRoomMan = sr;
        this.num = i;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            try {
                showerRoomMan.manWantsToEnter(num);
                Thread.sleep(1000);
                showerRoomMan.manLeaves(num);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
class Woman implements Runnable{
    private ShowerRoom showerRoomWoman;
    int num;
    public Woman(ShowerRoom sr, int i){
        this.showerRoomWoman = sr;
        this.num = i;
    }
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            try {
                showerRoomWoman.womanWantsToEnter(num);
                Thread.sleep(1000);
                showerRoomWoman.womanLeaves(num);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
