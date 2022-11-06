import javax.swing.*;

public class ScreenFrame extends JFrame{
    public void main() {
        JFrame frame = new JFrame();
        frame.setTitle("CPUSim 2 - Screen");
        frame.setResizable(false);
        frame.setSize(1000,700);
        frame.setVisible(true);
        frame.add(new Screen());
        frame.setLocationRelativeTo(null);
    }
}
