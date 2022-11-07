import javax.swing.*;

public class ScreenFrame extends JFrame{
    public void main() {
        JFrame frame = new JFrame();
        frame.setTitle("CPUSim 2 - Screen");
        //frame.setResizable(false);
        frame.setSize(1400,1020);
        frame.setVisible(true);
        frame.add(new Screen());
        frame.setLocationRelativeTo(null);
    }
}
