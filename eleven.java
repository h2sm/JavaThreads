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
    static private ReentrantLock rl = new ReentrantLock();
    private int showerRoomCapability = 10;
    static private int manNumber = 0;//счетчик девушек в душевой
    static private int womanNumber = 0;//счетчик мужчин в душевой
     private Condition manWants = rl.newCondition();
    private Condition womanWants = rl.newCondition();

    void womanWantsToEnter(int num) throws InterruptedException {
        rl.lock();
        try {
            boolean x1 = manNumber> 0 ;
            boolean y1 = womanNumber==showerRoomCapability;
            while (manNumber > 0 | womanNumber == showerRoomCapability ){
                boolean x = manNumber> 0 ;
                boolean y = womanNumber==showerRoomCapability;
                boolean z = x|y;
                womanWants.await();//пока есть мужчины или все кабинки заняты женщинами, то ждем
                System.out.println(x + " "+ y+ "Девушка " + num + " попыталась войти. Девушек - " + womanNumber + ", мужчин - " + manNumber);
            }
            womanNumber++;
            System.out.println(x1 + " "+ y1 + "Зашла девушка " + num + ". Количество девушек в душевой - " + womanNumber);
        }
        finally {
            rl.unlock();
        }
    }
    void manWantsToEnter(int num) throws InterruptedException {
        rl.lock();
        try {
            while (womanNumber > 0 | manNumber == showerRoomCapability) {
                boolean x = womanNumber > 0;
                boolean y =manNumber==showerRoomCapability;
                manWants.await();//пока есть женщины или все кабинки заняты мужинами, то ждем
                System.out.println(x + " " + y + " Мужчина " + num + " попытался войти. Девушек - " + womanNumber + ", мужчин - " + manNumber );
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
                //womanWants.signal();//одна девушка может зайти
            }
            if (womanNumber==0){
                System.out.println("Все женщины вышли. Мужчинам можно войти");
                manWants.signalAll();
            }
            else if (womanNumber >=7 && womanNumber<=9){
                //System.out.println("Все женщины вышли");
                manWants.signal();//а вдруг мужик зайдет
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
                //manWants.signal();//пустим мужчину в душевую
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
