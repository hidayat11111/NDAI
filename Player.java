import org.nfunk.jep.JEP;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Objects;

class DataPoint {
    public double x;
    public double y;

    DataPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
}

@SuppressWarnings("ALL")
class Player extends JFrame implements ActionListener {
    final int width = 800;
    final int height = 500;
    Canvas canvas1, canvas2;
    JTextField entFunction;
    JComboBox<String> intChoice;
    //
    float xA = (float) -Math.PI;
    float xB = (float) Math.PI;
    float dx = 0.01f;

    Player() {
        canvas1 = new Canvas() {
            @Override
            public void paint(Graphics g) {
            }
        };

        canvas2 = new Canvas() {
            @Override
            public void paint(Graphics g) {
            }
        };

        canvas1.setBackground(Color.black);
        canvas2.setBackground(Color.black);

        // Grouping canvas 1 and canvas 2
        JPanel panelGraphs = new JPanel(new GridLayout(1, 2, 2, 0));
        panelGraphs.setBackground(Color.darkGray);
        panelGraphs.add(canvas1);
        panelGraphs.add(canvas2);

        // Defining the controls' pane, this
        // contains 2 rows 1 -> textField, and comboBox
        JPanel panelControls = new JPanel(new GridLayout(1, 2, 2, 2));
        panelControls.setBorder(new EmptyBorder(5, 5, 5, 5));
        panelControls.setBackground(Color.darkGray);

        // Defining the custom text field for focus and placeholder text
        entFunction = new JTextField(25);
        entFunction.setMargin(new Insets(3, 3, 3, 3));
        entFunction.setForeground(Color.GRAY);
        entFunction.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (entFunction.getText().equals("Enter function")) {
                    entFunction.setText("");
                    entFunction.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (entFunction.getText().isEmpty()) {
                    entFunction.setForeground(Color.GRAY);
                    entFunction.setText("Enter function");
                }
            }
        });

        // Label for "enter expression" text field
        JLabel labelEntFunc = new JLabel("Expression");
        labelEntFunc.setVerticalAlignment(SwingConstants.CENTER);
        labelEntFunc.setHorizontalAlignment(SwingConstants.CENTER);
        labelEntFunc.setFont(new Font("Sans Serif", Font.BOLD, 20));
        labelEntFunc.setForeground(Color.WHITE);
        panelControls.add(labelEntFunc);
        panelControls.add(entFunction);

        intChoice = new JComboBox<>();
        intChoice.addItem("Euler");
        intChoice.addItem("Simsons");

        // Label for "integrator" combobox
        JLabel labelIntChoice = new JLabel("Integrator");
        labelIntChoice.setVerticalAlignment(SwingConstants.CENTER);
        labelIntChoice.setHorizontalAlignment(SwingConstants.CENTER);
        labelIntChoice.setFont(new Font("Sans Serif", Font.BOLD, 20));
        labelIntChoice.setForeground(Color.WHITE);

        // grouping label1 and combobox for row 1.
        panelControls.add(labelIntChoice);
        panelControls.add(intChoice);

        // container for row 2, 3 buttons
        JPanel panelControlsButtons = new JPanel(new GridLayout(1, 3, 2, 2));
        panelControls.setBorder(new EmptyBorder(5, 5, 5, 5));

        JButton btnDiff = new JButton("Differentiate");
        JButton btnInt = new JButton("Integrate");
        JButton btnPlot = new JButton("Plot");

        // Adding listeners
        btnDiff.addActionListener(this);
        btnInt.addActionListener(this);
        btnPlot.addActionListener(this);

        panelControlsButtons.add(btnDiff);
        panelControlsButtons.add(btnInt);
        panelControlsButtons.add(btnPlot);
        panelControlsButtons.setBackground(Color.darkGray);

        // Main container for containing 2 rows.
        JPanel controlsContainer = new JPanel(new GridLayout(2, 1));
        controlsContainer.add(panelControls);
        controlsContainer.add(panelControlsButtons);

        // Main panel for containing Canvases and Controls
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panelGraphs);
        mainPanel.add(controlsContainer, BorderLayout.SOUTH);

        this.add(mainPanel);
        this.setTitle("NIAD V1");
        this.setIconImage(new ImageIcon("images/icon.png").getImage());
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(this.width, this.height);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Player();
    }

    float map(float value, float iStart, float iStop, float oStart, float oStop) {
        return oStart + (oStop - oStart) * ((value - iStart) / (iStop - iStart));
    }

    void initCanvas1() {
        Graphics g = canvas1.getGraphics();
        g.fillRect(50, 50, 100, 200);
    }

    void canvas1Plot() {
        // Get the graphics
        Graphics g = canvas1.getGraphics();
        int w = canvas1.getWidth();
        int h = canvas1.getHeight();
        g.clearRect(0, 0, w, h);
        g.translate(w / 2, h / 2);
        g.setColor(Color.DARK_GRAY);
        g.drawLine(-w / 2, 0, w / 2, 0);
        g.drawLine(0, -h / 2, 0, h / 2);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 15));
        g.drawString("Original", -25, -h / 2 + 20);

        // ticks
        int divs = 5;
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 10));
        for (float x = -w / 2.0f; x <= w / 2.0f; x++) {
            if ((int) x % (int) ((w / 2.0f) / divs) == 0 && x != -0.5) {
                float xLab = map(x, -w / 2.0f, w / 2.0f, xA, xB);
                g.drawString(String.format("%.1f", xLab), (int) x, 0);
            }
        }
        for (float y = -h / 2.0f; y <= w / 2.0f; y++) {
            if ((int) y % (int) ((w / 2.0f) / divs) == 0 && y != -0.5) {
                float yLab = map(y, -w / 2.0f, w / 2.0f, xA, xB);
                g.drawString(String.format("%.1f", yLab), 0, (int) -y);
            }
        }

        // get the expression and create the parser ...
        String expression = entFunction.getText();

        g.setColor(Color.magenta);
        g.setFont(new Font("Consolas", Font.PLAIN, 17));
        g.drawString("y=" + expression, -w / 2 + 20, -h / 2 + 50);
        g.setColor(Color.blue);

        if (expression.equals("Enter function")) {
            System.out.println("Invalid expression !");
            return;
        }

        JEP parser1 = new JEP();
        parser1.addStandardFunctions();
        parser1.addStandardConstants();

        double min = Double.MAX_VALUE, max = Double.MIN_VALUE;
        ArrayList<DataPoint> values = new ArrayList<>();
        for (float t = 0; t <= w; t += 0.01) {
            float x = map(t, 0, w, -w / 2.0f, w / 2.0f);
            float xx = map(x, -w / 2.0f, w / 2.0f, xA, xB);
            parser1.addVariable("x", xx);
            parser1.parseExpression(expression);
            double Y = -(parser1.getValue() * h * 0.30);
            values.add(new DataPoint(x, Y));
            if (Y < min) min = Y;
            if (Y > max) max = Y;
        }

        for (DataPoint d : values) {
            float y = map((float) d.y, (float) min, (float) max, -h / 2.0f, h / 2.0f) * 0.60f;
            g.fillOval((int) d.x, (int) y, 2, 2);
        }
    }

    // Integrators ...
    void euler() {
        Graphics g = canvas2.getGraphics();

        int w = canvas2.getWidth();
        int h = canvas2.getHeight();
        g.clearRect(0, 0, w, h);
        g.translate(w / 2, h / 2);
        g.setColor(Color.DARK_GRAY);
        g.drawLine(-w / 2, 0, w / 2, 0);
        g.drawLine(0, -h / 2, 0, h / 2);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 15));
        g.drawString("Result[Euler]", -35, -h / 2 + 20);

        int divs = 5;
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 10));
        for (float x = -w / 2.0f; x <= w / 2.0f; x++) {
            if ((int) x % (int) ((w / 2.0f) / divs) == 0 && x != -0.5) {
                float xLab = map(x, -w / 2.0f, w / 2.0f, xA, xB);
                g.drawString(String.format("%.1f", xLab), (int) x, 0);
            }
        }
        for (float y = -h / 2.0f; y <= w / 2.0f; y++) {
            if ((int) y % (int) ((w / 2.0f) / divs) == 0 && y != -0.5) {
                float yLab = map(y, -w / 2.0f, w / 2.0f, xA, xB);
                g.drawString(String.format("%.1f", yLab), 0, (int) -y);
            }
        }

        String expression = entFunction.getText();
        g.setColor(Color.green);

        if (expression.equals("Enter function")) {
            System.out.println("Invalid expression !");
            return;
        }

        JEP parserInt = new JEP();
        parserInt.addStandardFunctions();
        parserInt.addStandardConstants();

        double I = 0.0, min = Double.MAX_VALUE, max = Double.MIN_VALUE;
        ArrayList<DataPoint> values = new ArrayList<>();

        for (float t = xA; t <= xB; t += dx) {
            float x = map(t, xA, xB, -w / 2.0f, w / 2.0f);
            parserInt.addVariable("x", t);
            parserInt.parseExpression(expression);

            double k = I + parserInt.getValue() * dx * 1;
            I = k;
            if (k < min) min = k;
            if (k > max) max = k;

            values.add(new DataPoint(x, k));
        }

        for (DataPoint d : values) {
            float y = map((float) d.y, (float) min, (float) max, -h / 2.0f, h / 2.0f) * 0.60f;
            g.fillOval((int) d.x, (int) y, 2, 2);
        }

        g.setColor(Color.magenta);
        g.setFont(new Font("Consolas", Font.PLAIN, 17));
        g.drawString(String.format("Area=%.3f", Math.abs(I)), -w / 2 + 20, -h / 2 + 50);
    }

    void simsons() {
        Graphics g = canvas2.getGraphics();
        int N = (int) ((xB - xA) / dx) + 1;
        int w = canvas2.getWidth();
        int h = canvas2.getHeight();
        g.clearRect(0, 0, w, h);
        g.translate(w / 2, h / 2);
        g.setColor(Color.DARK_GRAY);
        g.drawLine(-w / 2, 0, w / 2, 0);
        g.drawLine(0, -h / 2, 0, h / 2);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 15));
        g.drawString("Result[Simsons]", -35, -h / 2 + 20);

        int divs = 5;
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 10));
        for (float x = -w / 2.0f; x <= w / 2.0f; x++) {
            if ((int) x % (int) ((w / 2.0f) / divs) == 0 && x != -0.5) {
                float xLab = map(x, -w / 2.0f, w / 2.0f, xA, xB);
                g.drawString(String.format("%.1f", xLab), (int) x, 0);
            }
        }
        for (float y = -h / 2.0f; y <= w / 2.0f; y++) {
            if ((int) y % (int) ((w / 2.0f) / divs) == 0 && y != -0.5) {
                float yLab = map(y, -w / 2.0f, w / 2.0f, xA, xB);
                g.drawString(String.format("%.1f", yLab), 0, (int) -y);
            }
        }

        g.setColor(Color.yellow);
        String expression = entFunction.getText();

        if (expression.equals("Enter function")) {
            System.out.println("Invalid expression !");
            return;
        }

        JEP parserInt = new JEP();
        parserInt.addStandardFunctions();
        parserInt.addStandardConstants();

        double I = 0.0, sum = 0.0, min = Double.MAX_VALUE, max = Double.MIN_VALUE;
        ArrayList<DataPoint> values = new ArrayList<>();

        for (float t = 1; t < N; t++) {
            float x = map(t, 1, N, -w / 2.0f, w / 2.0f);
            float j = (xA + t * dx);

            parserInt.addVariable("x", j);
            parserInt.parseExpression(expression);
            if (t % 2 == 0) {
                sum += 2.0f * parserInt.getValue();
            } else {
                sum += 4.0f * parserInt.getValue();
            }

            parserInt.addVariable("x", xA);
            parserInt.parseExpression(expression);
            double eA = parserInt.getValue();

            parserInt.addVariable("x", xB);
            parserInt.parseExpression(expression);
            double eB = parserInt.getValue();

            double k = (dx / 3.0f) * (eA + eB + sum);
            I = k;

            if (k < min) min = k;
            if (k > max) max = k;

            values.add(new DataPoint(x, k));
        }

        for (DataPoint d : values) {
            float y = map((float) d.y, (float) min, (float) max, -h / 2.0f, h / 2.0f) * 0.60f;
            g.fillOval((int) d.x, (int) y, 2, 2);
        }

        g.setColor(Color.magenta);
        g.setFont(new Font("Consolas", Font.PLAIN, 17));
        g.drawString(String.format("Area=%.3f", Math.abs(I)), -w / 2 + 20, -h / 2 + 50);
    }

    void diff() {
        Graphics g = canvas2.getGraphics();
        int w = canvas2.getWidth();
        int h = canvas2.getHeight();
        g.clearRect(0, 0, w, h);
        g.translate(w / 2, h / 2);
        g.setColor(Color.DARK_GRAY);
        g.drawLine(-w / 2, 0, w / 2, 0);
        g.drawLine(0, -h / 2, 0, h / 2);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 15));
        g.drawString("Result[Derivative]", -35, -h / 2 + 20);
        int divs = 5;
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 10));
        for (float x = -w / 2.0f; x <= w / 2.0f; x++) {
            if ((int) x % (int) ((w / 2.0f) / divs) == 0 && x != -0.5) {
                float xLab = map(x, -w / 2.0f, w / 2.0f, xA, xB);
                g.drawString(String.format("%.1f", xLab), (int) x, 0);
            }
        }
        for (float y = -h / 2.0f; y <= w / 2.0f; y++) {
            if ((int) y % (int) ((w / 2.0f) / divs) == 0 && y != -0.5) {
                float yLab = map(y, -w / 2.0f, w / 2.0f, xA, xB);
                g.drawString(String.format("%.1f", yLab), 0, (int) -y);
            }
        }

        String expression = entFunction.getText();
        g.setColor(Color.cyan);

        if (expression.equals("Enter function")) {
            System.out.println("Invalid expression !");
            return;
        }

        JEP parserDiff = new JEP();
        parserDiff.addStandardFunctions();
        parserDiff.addStandardConstants();

        double min = Double.MAX_VALUE, max = Double.MIN_VALUE;
        ArrayList<DataPoint> values = new ArrayList<>();

        for (float t = xA; t <= xB; t += dx) {
            float x = map(t, xA, xB, -w / 2.0f, w / 2.0f);
            parserDiff.addVariable("x", t);
            parserDiff.parseExpression(expression);
            double current = parserDiff.getValue();

            parserDiff.addVariable("x", t + dx);
            parserDiff.parseExpression(expression);
            double next = parserDiff.getValue();

            double k = (next - current) / dx;
            if (k < min) min = k;
            if (k > max) max = k;

            values.add(new DataPoint(x, k));
        }

        for (DataPoint d : values) {
            float y = map((float) d.y, (float) min, (float) max, -h / 2.0f, h / 2.0f) * 0.60f;
            g.fillOval((int) d.x, (int) y, 2, 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Plot")) {
            this.canvas1Plot();
        } else if (e.getActionCommand().equals("Differentiate")) {
            this.diff();
        } else if (e.getActionCommand().equals("Integrate")) {
            if (Objects.equals(intChoice.getSelectedItem(), "Euler")) {
                this.euler();
            } else {
                this.simsons();
            }
        }
    }
}
