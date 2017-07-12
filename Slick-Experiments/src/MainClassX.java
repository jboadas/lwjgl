	

    import org.lwjgl.input.Keyboard;
    import org.lwjgl.input.Mouse;
    import org.newdawn.slick.*;
    import java.util.*;
     
     
    public class MainClassX extends BasicGame {
     
            public MainClassX(String title) {
                    super(title);
                    // TODO Auto-generated constructor stub
            }
     
            /**
             * @param args
             */
           
            public List<Wall> walls = new ArrayList<Wall>();
           
            float rayPointX = 0F;
            float rayPointY = 0F;
            float alpha = 96F;
           
            class Wall {
                    float x;
                    float y;
                    float w;
                    float h;
                   
                    public Wall(float x, float y, float w, float h) {
                            this.x = x;
                            this.y = y;
                            this.w = w;
                            this.h = h;
                    }
                   
            }
           
            public static void main(String[] args) throws SlickException {
                    AppGameContainer appgc = new AppGameContainer(new MainClassX("2d RayCaster"));
                    appgc.setDisplayMode(1280, 720, false);
                    appgc.start();
            }
           
            class PointF {
                    public float x;
                    public float y;
                   
                    public PointF(float x, float y) {
                            this.x = x;
                            this.y = y;
                    }
                   
            }
           
            public boolean bounceOff = false;
            public float resol = 0.5F;
           
            public PointF rayCast(float startx, float starty, float diffx, float diffy, Graphics g, int steplimit, GameContainer gc) {
                    float dx = startx;
                    float dy = starty;
                    float latx = startx;
                    float laty = starty;
                    boolean dox = true;
                    int step = 0;
                    for (Wall w : walls) {
                            if (dx > w.x && dx < w.x + w.w && dy > w.y && dy < w.y + w.h) {
                                    dox = false;
                                    break;
                            }
                    }
                    if (dox) {
                            while (true) {
                                    step++;
                                    if (step > steplimit) {
                                            if (!(infiniteBounce)) break;
                                    }
                                    float lx = dx;
                                    dx += diffx;
                                    if (bounceOff) {
                                            for (Wall w : walls) {
                                                    if (dx > w.x && dx < w.x + w.w && dy > w.y && dy < w.y + w.h) {
                                                            dx = lx;
                                                            diffx = diffx * -1;
                                                            if (gradient) {
                                                                    g.drawGradientLine(latx, laty, new Color(255, 0, 0, 255), dx, dy, new Color(255, 0, 0, 0));
                                                            }else{
                                                                    g.drawLine(latx, laty, dx, dy);
                                                            }
                                                            latx = dx;
                                                            laty = dy;
                                                            break;
                                                    }
                                            }
                                    }
                                   
                                    float ly = dy;
                                    dy += diffy;
                                    if (bounceOff) {
                                            for (Wall w : walls) {
                                                    if (dx > w.x && dx < w.x + w.w && dy > w.y && dy < w.y + w.h) {
                                                            dy = ly;
                                                            diffy = diffy * -1;
                                                            if (gradient) {
                                                                    g.drawGradientLine(latx, laty, new Color(255, 0, 0, 255), dx, dy, new Color(255, 0, 0, 0));
                                                            }else{
                                                                    g.drawLine(latx, laty, dx, dy);
                                                            }
                                                            latx = dx;
                                                            laty = dy;
                                                            break;
                                                    }
                                            }
                                    }
                                    if (!(bounceOff)) {
                                            boolean outerbreak = false;
                                            for (Wall w : walls) {
                                                    if (dx > w.x && dx < w.x + w.w && dy > w.y && dy < w.y + w.h) {
                                                            outerbreak = true;
                                                            break;
                                                    }
                                            }
                                            if (outerbreak) break;
                                    }
                                   
                                   
                                    if (dx > gc.getWidth()) break;
                                    if (dx < 0) break;
                                    if (dy > gc.getHeight()) break;
                                    if (dy < 0) break;
                            }
                            if (gradient) {
                                    g.drawGradientLine(latx, laty, new Color(255, 0, 0, 255), dx, dy, new Color(255, 0, 0, 0));
                            }else{
                                    g.drawLine(latx, laty, dx, dy);
                            }
                    }
                   
                    return new PointF(dx, dy);
            }
           
            boolean renderPlat = true;
           
            boolean dragging = false;
            int startx = 0;
            int starty = 0;
            int endx = 0;
            int endy = 0;
           
            public boolean gradient = false;
           
            public int stepLimit = 10000;
           
            public boolean infiniteBounce = false;
           
            public boolean singleBar = true;
           
            public float goX = 0F;
            public float goY = 0F;
           
            @Override
            public void render(GameContainer gc, Graphics g) throws SlickException {
                   
                   
                    g.setColor(new Color(255, 0, 0, (int)alpha));
                    g.setLineWidth(1f);
                    if (singleBar) {
                            rayCast(rayPointX, rayPointY, goX, goY, g, stepLimit, gc);
                    }else{
                            for (float diffx = -1F; diffx < 1F; diffx += resol) {
                                    for (float diffy = -1; diffy < 1F; diffy += resol) {
                                            if (diffx < -0.9F || diffx > 0.9F || diffy < -0.9F || diffy > 0.9F) {
                                                    rayCast(rayPointX, rayPointY, diffx, diffy, g, stepLimit, gc);
                                                    /*if (gradient) {
                                                            g.drawGradientLine(rayPointX, rayPointY, new Color(255, 245, 25, 255), fx.x, fx.y, new Color(255, 245, 25, 0));
                                                    }else{
                                                            g.drawLine(rayPointX, rayPointY, fx.x, fx.y);
                                                    }*/
                                            }
                                    }
                            }
                    }
                   
                    if (renderPlat) {
                            g.setColor(Color.white);
                            for (Wall w : walls) {
                                    g.fillRect(w.x, w.y, w.w, w.h);
                            }
                    }
                   
                    g.setColor(Color.blue);
                    g.fillRect(rayPointX - 4, rayPointY - 4, 8, 8);
                    g.setColor(Color.red);
                    float goXa = goX * 8F;
                    float goYa = goY * 8F;
                   
                    g.fillRect(rayPointX - 4 + goXa, rayPointY - 4 + goYa, 8, 8);
                    if (dragging) {
                            g.setLineWidth(2f);
                            g.setColor(Color.green);
                            int sx = startx;
                            int sy = starty;
                            int ex = endx;
                            int ey = endy;
                            if (sx > ex) {
                                    int j1 = sx;
                                    int j2 = ex;
                                    ex = j1;
                                    sx = j2;
                            }
                            if (sy > ey) {
                                    int j1 = sy;
                                    int j2 = ey;
                                    ey = j1;
                                    sy = j2;
                                   
                            }
                            g.drawRect(sx, sy, ex - sx, ey - sy);
                    }
                    g.setColor(Color.green);
                    g.drawString("Resolution: " + resol + "\nstepLimit: " + stepLimit + "\nGradient Rendering[H]: " +
                    String.valueOf(gradient) + "\nbounceOff[J]: " + String.valueOf(bounceOff) + "\ninfiniteBounceOff[K]: " + String.valueOf(infiniteBounce) +
                    "\nSingle Bar[L]: " + String.valueOf(singleBar) + "\nAlpha[E/D]: " + alpha + "\nRender Plat[U]: " + renderPlat, 50, 50);
            }
     
            @Override
            public void init(GameContainer g) throws SlickException {
                   
            }
           
            public void keyPressed(int key, char c) {
                    if (c == 'h' || c == 'H') {
                            gradient = !gradient;
                    }
                    if (c == 'j' || c == 'J') {
                            bounceOff = !bounceOff;
                    }
                    if (c == 'k' || c == 'K') {
                            infiniteBounce = !infiniteBounce;
                    }
                    if (c == 'l' || c == 'L') {
                            singleBar = !singleBar;
                    }
                    if (c == 'u' || c == 'U') {
                            renderPlat = !renderPlat;
                    }
            }
           
            public boolean reactLeft = true;
            public boolean reactRight = true;
           
            public boolean GetCollision(float playerX, float playerY, float playerWidth,
                float playerHeight, float EnemyX, float EnemyY, float EnemyWidth,
                float EnemyHeight)
            {
                if ((playerX + playerWidth) > EnemyX &&
                                        (playerX) < EnemyX + EnemyWidth &&
                                        (playerY + playerHeight) > EnemyY &&
                                        (playerY) < EnemyY + EnemyHeight)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
           
            @Override
            public void update(GameContainer gc, int delta) throws SlickException {
                    if (Mouse.isButtonDown(0)) {
                            if (reactLeft) {
                                    //MOUSE DOWN
                                    startx = Mouse.getX();
                                    starty = gc.getHeight() - Mouse.getY();
                                    endx = Mouse.getX();
                                    endy = gc.getHeight() - Mouse.getY();
                                    dragging = true;
                                    reactLeft = false;
                            }
                    }else{
                            if (!(reactLeft)) {
                                    //MOUSE UP
                                    int sx = startx;
                                    int sy = starty;
                                    int ex = endx;
                                    int ey = endy;
                                    if (sx > ex) {
                                            int j1 = sx;
                                            int j2 = ex;
                                            ex = j1;
                                            sx = j2;
                                    }
                                    if (sy > ey) {
                                            int j1 = sy;
                                            int j2 = ey;
                                            ey = j1;
                                            sy = j2;
                                    }
                                    walls.add(new Wall(sx, sy, ex - sx, ey - sy));
                                    dragging = false;
                                    reactLeft = true;
                            }
                    }
                    if (Mouse.isButtonDown(1)) {
                            if (reactRight) {
                                    //MOUSE DOWN
                                    startx = Mouse.getX();
                                    starty = gc.getHeight() - Mouse.getY();
                                    endx = Mouse.getX();
                                    endy = gc.getHeight() - Mouse.getY();
                                    dragging = true;
                                    reactRight = false;
                            }
                    }else{
                            if (!(reactRight)) {
                                    //MOUSE UP
                                    int sx = startx;
                                    int sy = starty;
                                    int ex = endx;
                                    int ey = endy;
                                    if (sx > ex) {
                                            int j1 = sx;
                                            int j2 = ex;
                                            ex = j1;
                                            sx = j2;
                                    }
                                    if (sy > ey) {
                                            int j1 = sy;
                                            int j2 = ey;
                                            ey = j1;
                                            sy = j2;
                                    }
                                    List<Wall> remwall = new ArrayList<Wall>();
                                    for (Wall w : walls) if (GetCollision(w.x, w.y, w.w, w.h, sx, sy, ex - sx, ey - sy)) remwall.add(w);
                                    for (Wall w : remwall) walls.remove(w);
                                    dragging = false;
                                    reactRight = true;
                            }
                    }
                    if (dragging) {
                            endx = Mouse.getX();
                            endy = gc.getHeight() - Mouse.getY();
                    }
                    rayPointX = Mouse.getX();
                    rayPointY = gc.getHeight() - Mouse.getY();
                    if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                            goX -= delta * 0.001F;
                    }
                    if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                            goX += delta * 0.001F; 
                    }
                    if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                            goY -= delta * 0.001F;
                    }
                    if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                            goY += delta * 0.001F;
                    }
                    if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
                            resol += delta * .0005F;
                    }
                    if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
                            if (resol > 0.01F && delta * 0.0005F < 0.01F) resol -= delta * .0005F;
                    }
                    if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
                            stepLimit += 5;
                    }
                    if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
                            if (stepLimit > 0) stepLimit -= 5;
                    }
                    if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
                            if (alpha < 256) alpha += 0.1F;
                    }
                    if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
                            if (alpha > 0) alpha -= 0.1F;
                    }
                   
                   
                   
            }
     
    }

