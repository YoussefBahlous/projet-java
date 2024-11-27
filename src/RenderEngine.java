import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RenderEngine extends JPanel implements Engine {
    private ArrayList<Displayable> renderList;
    private FPS fps;
    private boolean showFPS;
    private Color fpsColor;
    private final int TARGET_FPS = 60;

    public RenderEngine(JFrame jFrame) {
        renderList = new ArrayList<>();
        fps = new FPS();
        showFPS = true;
        fpsColor = Color.GREEN;

        this.setPreferredSize(new Dimension(800, 600));
        this.setBackground(Color.BLACK);
        jFrame.add(this);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public void addToRenderList(Displayable displayable) {
        if (!renderList.contains(displayable)) {
            renderList.add(displayable);
        }
    }

    public void addToRenderList(ArrayList<Displayable> displayables) {
        for (Displayable d : displayables) {
            if (!renderList.contains(d)) {
                renderList.add(d);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        for (Displayable renderObject : renderList) {
            renderObject.draw(g2d);
        }

        if (showFPS) {
            drawFPS(g2d);
        }
    }

    private void drawFPS(Graphics2D g) {
        g.setColor(fpsColor);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        String fpsText = String.format("FPS: %d", fps.getFPS());
        g.drawString(fpsText, 10, 20);
    }

    @Override
    public void update() {
        fps.update();
        repaint();
    }

    public void setShowFPS(boolean show) {
        this.showFPS = show;
    }

    public void setFPSColor(Color color) {
        this.fpsColor = color;
    }

    public int getCurrentFPS() {
        return fps.getFPS();
    }
}
