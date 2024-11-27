public class FPS {
    private int fps;
    private int frames;
    private long lastFpsTime;

    public FPS() {
        this.fps = 0;
        this.frames = 0;
        this.lastFpsTime = System.currentTimeMillis();
    }

    public void update() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFpsTime > 1000) { // Mise à jour chaque seconde
            fps = frames;
            frames = 0;
            lastFpsTime = currentTime;
        }
        frames++;
    }

    public int getFPS() {
        return fps;
    }
}