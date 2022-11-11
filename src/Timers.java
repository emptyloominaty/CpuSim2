import java.util.Timer;
import java.util.TimerTask;

public class Timers {
    Timer timer1 = new Timer();
    Timer timer2 = new Timer();
    Timer timer3 = new Timer();
    Timer timer4 = new Timer();

    boolean timer1Running = false;
    boolean timer2Running = false;
    boolean timer3Running = false;
    boolean timer4Running = false;

    public void cancelTimer1() {
        if (timer1Running) {
            timer1.cancel();
            timer1Running = false;
        }
    }

    public void setTimer1(int time,Cpu cpu) {
        if (!timer1Running) {
            timer1 = new Timer();
            timer1.schedule(new TimerTask() {
                @Override
                public void run() {
                    cpu.interrupt((byte) 2);
                }
            }, time, time);
            timer1Running = true;
        }
    }


}
