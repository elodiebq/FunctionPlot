package functionEvaluator;

import javax.swing.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainWindow extends JFrame {
    JTextField txtFunction;
    JButton btnDraw;
    JLabel text;
    FunctionSurface surface;

    class DrawButtonAction implements ActionListener {
        MainWindow frame;

        DrawButtonAction(MainWindow window) {
            frame = window;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                frame.surface.setEquation(txtFunction.getText());
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            frame.invalidate();
            frame.repaint();
        }

    }

    public MainWindow() {
        this.setLayout(null);
        txtFunction = new JTextField();
        this.add(txtFunction);
        txtFunction.setLocation(10, 10);
        txtFunction.setSize(200, 25);
        btnDraw = new JButton();
        this.add(btnDraw);
        btnDraw.addActionListener(new DrawButtonAction(this));
        btnDraw.setLocation(220, 10);
        btnDraw.setSize(60, 28);
        btnDraw.setText("Draw");
        surface = new FunctionSurface();
        surface.setLocation(10, 40);
        surface.setSize(480, 450);
        add(surface);
    }

    public static void main(String[] args) {

        MainWindow frame = new MainWindow();
        frame.setSize(500, 500);

        // 2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 5. Show it.
        frame.setVisible(true);

        // try {
        // String input = "sqrt(x^2) ";

        // System.out.println(sb1.toString());
        // vm.excute(sb1.toString(), 5);
        // System.out.println("");
        // vm.excute(sb2.toString(), 5);
        //
        // } catch (Exception e) {
        // System.out.println(e.getMessage());
        // }

    }
}

class FunctionSurface extends JPanel {
    VirtualMachine vm = new VirtualMachine();
    String code;
    String driveCode;

    public void setEquation(String equation) throws Exception {
        // System.out.println(equation);
        Lexer testLexer = new Lexer();
        Parser parser = new Parser();
        List<Token> tokens = testLexer.parse(equation);
        Node syntaxTree = parser.parseProgram(tokens);
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        Node derivative = syntaxTree.derive();
        System.out.println(derivative.tostring());
        syntaxTree.compile(sb1);
        code = sb1.toString();
        derivative.compile(sb2);
        driveCode = sb2.toString();
        this.invalidate();
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        float w = this.getWidth();
        float h = this.getHeight();
        float xMin = -10.0f;
        float xMax = 10.0f;
        float yMin = -10.0f;
        float yMax = 10.0f;
        float xDifference = xMax - xMin;
        float yDifference = yMax - yMin;
        int lastX = 0;
        g.setColor(Color.black);
        g.drawLine(0, (int)h/2, (int)w, (int)(h/2));
        g.drawLine((int)(w/2), 0, (int)(w/2), (int)h);
        if (code == null)
            return;
        try {
            int lastY = (int) ((yMax - vm.excute(code,xMin)) * h / yDifference);
            g.setColor(Color.red);
          
            for (int i = 1; i < w; i++) {
                int curX = i;
                float realX = xDifference * (i / w) + xMin;
                float realY = vm.excute(code, realX);
                int curY = (int) ((yMax - realY) / yDifference * h);

                if(!Float.isNaN(realY) && Math.abs(curY - lastY) <= 50 ) {
                    g.drawLine(lastX, lastY, curX, curY);
                }
                lastX = curX;
                lastY = curY;
            }
            
            g.setColor(Color.blue);
            lastX = 0;
            lastY = (int) ((yMax - vm.excute(driveCode,xMin)) * h / yDifference);
            for (int i = 1; i < w; i++) {
                int curX = i;
                float realX = xDifference * (i / w) + xMin;
                float realY = vm.excute(driveCode, realX);
                int curY = (int) ((yMax - realY) / yDifference * h);

                if(!Float.isNaN(realY) && Math.abs(curY - lastY) <= 50 ) {
                    g.drawLine(lastX, lastY, curX, curY);
                }
                lastX = curX;
                lastY = curY;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        g.setColor(Color.white);
        g.fillRect(10, (int)(h - 75), 70, 55);
        g.setColor(Color.red);
        g.drawLine(15,(int)(h - 60),30, (int)(h - 60));
        g.drawString("f(x)", 45, (int)(h - 55));
        g.setColor(Color.blue);
        g.drawLine(15,(int)(h - 40),30, (int)(h - 40));
        g.drawString("f'(x)", 45, (int)(h - 35));

    }

}
