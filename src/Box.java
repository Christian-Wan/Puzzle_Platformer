import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Box {

    private Rectangle collisionBox, onCollisionBox;
    private Engine engine;
    private int x, y, velocity, velocityTimer;
    private BufferedImage visual;
    private boolean inAir;
    public Box(Engine engine, int x, int y) {
        this.engine = engine;
        this.x = x;
        this.y = y;
        velocity = 4;
        inAir = false;
        collisionBox = new Rectangle(x, y, 32, 32);
        try {
            visual = ImageIO.read(new File("image/Level_Assets/Box.png"));
        } catch (IOException e) {}
    }

    public boolean touchingPlatform(ArrayList<Rectangle> rectangles) {
        for (Rectangle rect: rectangles) {
            if ((rect.getY() + rect.getHeight() >= collisionBox.getY() + collisionBox.getHeight()) && (rect.getY() <= collisionBox.getY() + collisionBox.getHeight()) && ((rect.getX() < collisionBox.getX() && rect.getX() + rect.getWidth() > collisionBox.getX()) || (rect.getX() < collisionBox.getX() + collisionBox.getWidth() && rect.getX() + rect.getWidth() > collisionBox.getX() + collisionBox.getWidth()) || (rect.getX() < collisionBox.getCenterX() && rect.getX() + rect.getWidth() > collisionBox.getCenterX()))) {
                onCollisionBox = rect;
                return true;
            }
        }
        return false;
    }
    public boolean wallOnLeft(ArrayList<Rectangle> walls, int x) {
        for (Rectangle rect: walls) {
            if ((rect.getX() + rect.width > x) && (rect.getX() <= x) && ((rect.getY() < collisionBox.getY() && rect.getY() + rect.height > collisionBox.getY()) || (rect.getY() < (collisionBox.getY() + collisionBox.getHeight()) && rect.getY() + rect.height > collisionBox.getY()  + collisionBox.getHeight()) || (rect.getY() < collisionBox.getCenterY() && rect.getY() + rect.getHeight() > collisionBox.getCenterY()))) {
                return true;
            }
        }
        return false;
    }

    public boolean wallOnRight(ArrayList<Rectangle> rectangles, int x) {
        for (Rectangle rect: rectangles) {
            if ((rect.getX() <= x + collisionBox.getWidth()) && (rect.getX() + rect.getWidth() >= x + collisionBox.getWidth()) && ((rect.getY() < collisionBox.getY() && rect.getY() + rect.getHeight() > collisionBox.getY()) || (rect.getY() < collisionBox.getY() + collisionBox.getHeight() && rect.getY() + rect.getHeight() > collisionBox.getY()  + collisionBox.getHeight()) || (rect.getY() < collisionBox.getCenterY() && rect.getY() + rect.getHeight() > collisionBox.getCenterY()))) {
                return true;
            }
        }
        return false;
    }
    public boolean playerBelow(ArrayList<Player> players) {
        for (Player player: players) {
            if ((player.getY() + player.getCollisionBox().height >= collisionBox.getY() + collisionBox.getHeight()) && (player.getY() <= collisionBox.getY() + collisionBox.getHeight()) && ((player.getCollisionBox().getX() < collisionBox.getX() && player.getCollisionBox().getX() + player.getCollisionBox().getWidth() > collisionBox.getX()) || (player.getCollisionBox().getX() < collisionBox.getX() + collisionBox.getWidth() && player.getCollisionBox().getX() + player.getCollisionBox().getWidth() > collisionBox.getX() + collisionBox.getWidth()) || (collisionBox.getX() < player.getCollisionBox().getCenterX() && collisionBox.getX() + 32 > player.getCollisionBox().getCenterX()))) {
                onCollisionBox = player.getCollisionBox();
                return true;
            }
        }
        return false;
    }

    public boolean playerOnLeft(ArrayList<Player> players, int x) {
        for (Player player: players) {
            if ((player.getX() + player.getCollisionBox().width >= x) && (player.getX() <= x) && ((player.getY() < collisionBox.getY() && player.getY() + player.getCollisionBox().height > collisionBox.getY()) || (player.getY() < (collisionBox.getY() + collisionBox.getHeight()) && player.getY() + player.getCollisionBox().height > collisionBox.getY()  + collisionBox.getHeight()))) {
                return true;
            }
        }
        return false;
    }

    public boolean playerOnRight(ArrayList<Player> players, int x) {
        for (Player player: players) {
            if ((player.getCollisionBox().x + engine.getSCALE() <= x + collisionBox.getWidth()) && (player.getCollisionBox().x + engine.getSCALE() + player.getCollisionBox().width >= x + collisionBox.getWidth()) && ((player.getY() < collisionBox.getY() && player.getY() + player.getCollisionBox().height > collisionBox.getY()) || (player.getY() < collisionBox.getY() + collisionBox.getHeight() && player.getY() + player.getCollisionBox().height > collisionBox.getY()  + collisionBox.getHeight()))) {
                return true;
            }
        }
        return false;
    }

    public boolean skeletonBelow(Skeleton skeleton) {
        if ((skeleton.getY() + skeleton.getCollisionBox().height >= collisionBox.getY() + collisionBox.getHeight()) && (skeleton.getY() <= collisionBox.getY() + collisionBox.getHeight()) && ((skeleton.getCollisionBox().getX() < collisionBox.getX() && skeleton.getCollisionBox().getX() + skeleton.getCollisionBox().getWidth() > collisionBox.getX()) || (skeleton.getCollisionBox().getX() < collisionBox.getX() + collisionBox.getWidth() && skeleton.getCollisionBox().getX() + skeleton.getCollisionBox().getWidth() > collisionBox.getX() + collisionBox.getWidth()) || (collisionBox.getX() < skeleton.getCollisionBox().getCenterX() && collisionBox.getX() + 32 > skeleton.getCollisionBox().getCenterX()))) {
            onCollisionBox = skeleton.getCollisionBox();
            return true;
        }
        return false;
    }

    public boolean skeletonOnLeft(Skeleton skeleton, int x) {
        if ((skeleton.getX() + skeleton.getCollisionBox().width >= x) && (skeleton.getX() <= x) && ((skeleton.getY() < collisionBox.getY() && skeleton.getY() + skeleton.getCollisionBox().height > collisionBox.getY()) || (skeleton.getY() < (collisionBox.getY() + collisionBox.getHeight()) && skeleton.getY() + skeleton.getCollisionBox().height > collisionBox.getY()  + collisionBox.getHeight()))) {
            return true;
        }
        return false;
    }

    public boolean skeletonOnRight(Skeleton skeleton, int x) {
        if ((skeleton.getCollisionBox().x + engine.getSCALE() <= x + collisionBox.getWidth()) && (skeleton.getCollisionBox().x + engine.getSCALE() + skeleton.getCollisionBox().width >= x + collisionBox.getWidth()) && ((skeleton.getY() < collisionBox.getY() && skeleton.getY() + skeleton.getCollisionBox().height > collisionBox.getY()) || (skeleton.getY() < collisionBox.getY() + collisionBox.getHeight() && skeleton.getY() + skeleton.getCollisionBox().height > collisionBox.getY()  + collisionBox.getHeight()))) {
            return true;
        }
        return false;
    }
    public void checkChangeX(int change) {
        if (change > 0) {
            if (!wallOnRight(engine.getLevelLayout().getWalls(), x + change) && !playerOnRight(engine.getLevelLayout().getAvailableCharacters(), x + change)) {
                if (engine.getNecromancer() != null && engine.getNecromancer().getSummon() != null) {
                    if (!skeletonOnRight(engine.getNecromancer().getSummon(), x + change)) {
                        x += change;
                    }
                }
                else {
                    x += change;
                }
            }
        }
        if (change < 0) {
            if (!wallOnLeft(engine.getLevelLayout().getWalls(), x + change) && !playerOnLeft(engine.getLevelLayout().getAvailableCharacters(), x + change)) {
                if (engine.getNecromancer() != null && engine.getNecromancer().getSummon() != null) {
                    if (!skeletonOnLeft(engine.getNecromancer().getSummon(), x + change)) {
                        x += change;
                    }
                }
                else {
                    x += change;
                }
            }
        }
    }
    public void update() {
        if (!touchingPlatform(engine.getLevelLayout().getWalls()) && !playerBelow(engine.getLevelLayout().getAvailableCharacters()) && !skeletonBelow(engine.getNecromancer().getSummon())) {
            y += velocity;
            inAir = true;
            velocityTimer++;
        }
        else {
            if (inAir) {
                velocityTimer = 0;
                inAir = false;
                velocity = 4;
                y = (int) onCollisionBox.getY() - 32;
            }
        }
        collisionBox.setLocation(x, y);

        if (velocityTimer == 18) {
            velocityTimer = 0;
            velocity += 1;
        }
    }
    public void draw(Graphics2D g) {
        g.drawImage(visual.getSubimage(16, 16, 32, 32), x, y, 32, 32, null);
        g.drawRect(x, y, 32, 32);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rectangle getCollisionBox() {
        return collisionBox;
    }
}
