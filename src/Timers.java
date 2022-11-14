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


    public void cancelTimer2() {
        if (timer2Running) {
            timer2.cancel();
            timer2Running = false;
        }
    }

    public void setTimer2(int time,Cpu cpu) {
        if (!timer2Running) {
            timer2 = new Timer();
            timer2.schedule(new TimerTask() {
                @Override
                public void run() {
                    cpu.interrupt((byte) 3);
                }
            }, time, time);
            timer2Running = true;
        }
    }


    public void cancelTimer3() {
        if (timer3Running) {
            timer3.cancel();
            timer3Running = false;
        }
    }

    public void setTimer3(int time,Cpu cpu) {
        if (!timer3Running) {
            timer3 = new Timer();
            timer3.schedule(new TimerTask() {
                @Override
                public void run() {
                    cpu.interrupt((byte) 4);
                }
            }, time, time);
            timer3Running = true;
        }
    }


    public void cancelTimer4() {
        if (timer4Running) {
            timer4.cancel();
            timer4Running = false;
        }
    }

    public void setTimer4(int time,Cpu cpu) {
        if (!timer4Running) {
            timer4 = new Timer();
            timer4.schedule(new TimerTask() {
                @Override
                public void run() {
                    cpu.interrupt((byte) 5);
                }
            }, time, time);
            timer4Running = true;
        }
    }

}
