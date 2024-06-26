import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;

public class LevelLayout {

    private BufferedImage topLeftCorner, topRightCorner, bottomLeftCorner, bottomRightCorner, upWall, leftWall, rightWall, downWall, darkness, topLeftDarkness, topRightDarkness, bottomLeftDarkness, bottomRightDarkness, spike, arrow, oneWayLeft, oneWayMiddle, oneWayRight, halfCornerDown, halfCornerRight, halfCornerUp, halfCornerLeft, halfCornerHalfWallDown, halfCornerHalfWallRight, halfCornerHalfWallUp, halfCornerHalfWallLeft, allCorner, allWall, doubleMiddleUpDown, doubleMiddleRightLeft, doubleRight, doubleLeft, doubleDown, doubleUp;
    private BufferedImage combined;
    private ArrayList<Rectangle> walls, oneWayPlatforms;
    private String[][] levelData;
    private Engine engine;
    private ArrayList<Player> availableCharacters;
    private boolean levelDone, swapped, resetting;
    private int characterInControl, arrowCounter;
    private String levelNumber;
    private ArrayList<Box> boxes;
    private HashMap<Opener, Door[]> openersAndDoors;
    private ArrayList<Opener> openers;
    private ArrayList<Door> doors;
    private ArrayList<Rectangle> spikes;

    //Have like a list of all the characters that the player can control and when a character reaches the portal remove them from the list

    public LevelLayout(Engine engine, String fileName) {
        this.engine = engine;
        characterInControl = 0;
        arrowCounter = 0;
        availableCharacters = new ArrayList<Player>();
        levelData = getLevelData(fileName);
        levelDone = false;
        characterInControl = 0;
        levelNumber = fileName.substring(5);
        swapped = false;
        boxes = new ArrayList<Box>();
        openersAndDoors = new HashMap<Opener, Door[]>();
        openers = new ArrayList<Opener>();
        doors = new ArrayList<Door>();
        spikes = new ArrayList<Rectangle>();
        oneWayPlatforms = new ArrayList<Rectangle>();
        combined = new BufferedImage(1500, 900, BufferedImage.TYPE_INT_ARGB);
        setTileSet();
        setWalls();
    }

    public LevelLayout(Engine engine) {
        this.engine = engine;
    }

    private void setTileSet() {
        BufferedImage tileset = null;
        try {
            tileset = ImageIO.read(new File("image/Level_Assets/Walls_Tileset.png"));
        } catch (IOException e) {}
        topLeftCorner = tileset.getSubimage(16, 16, 32, 32);
        upWall = tileset.getSubimage(64, 16, 32, 32);
        topRightCorner = tileset.getSubimage(112, 16, 32, 32);
        leftWall = tileset.getSubimage(16, 64, 32, 32);
        darkness = tileset.getSubimage(64, 64, 32, 32);
        rightWall = tileset.getSubimage(112, 64, 32, 32);
        bottomLeftCorner = tileset.getSubimage(16, 112, 32, 32);
        downWall = tileset.getSubimage(64, 112, 32, 32);
        bottomRightCorner = tileset.getSubimage(112, 112, 32, 32);
        topLeftDarkness = tileset.getSubimage(160, 16, 32, 32);
        topRightDarkness = tileset.getSubimage(208, 16, 32, 32);
        bottomLeftDarkness = tileset.getSubimage(160, 64, 32, 32);
        bottomRightDarkness = tileset.getSubimage(208, 64, 32, 32);
        oneWayLeft = tileset.getSubimage(496, 16, 32, 6);
        oneWayMiddle = tileset.getSubimage(496, 48, 32, 6);
        oneWayRight = tileset.getSubimage(496, 80, 32, 6);
        halfCornerDown = tileset.getSubimage(256, 16, 32, 32);
        halfCornerRight = tileset.getSubimage(256, 64, 32, 32);
        halfCornerUp = tileset.getSubimage(304, 64, 32, 32);
        halfCornerLeft = tileset.getSubimage(304, 16, 32, 32);
        halfCornerHalfWallDown = tileset.getSubimage(352, 16, 32, 32);
        halfCornerHalfWallRight = tileset.getSubimage(352, 64, 32, 32);
        halfCornerHalfWallUp = tileset.getSubimage(400, 64, 32, 32);
        halfCornerHalfWallLeft = tileset.getSubimage(400, 16, 32, 32);
        allCorner = tileset.getSubimage(448, 16, 32, 32);
        allWall = tileset.getSubimage(448, 64, 32, 32);
        doubleMiddleUpDown = tileset.getSubimage(256, 112, 32, 32);
        doubleMiddleRightLeft = tileset.getSubimage(304, 112, 32, 32);
        doubleRight = tileset.getSubimage(352, 112, 32, 32);
        doubleLeft = tileset.getSubimage(400, 112, 32, 32);
        doubleDown = tileset.getSubimage(448, 112, 32, 32);
        doubleUp = tileset.getSubimage(496, 112, 32, 32);
        try {
            spike = ImageIO.read(new File("image/Level_Assets/Spike.png")).getSubimage(16, 41,32, 7);
            arrow = ImageIO.read(new File("image/Level_Assets/Arrow.png")).getSubimage(25, 19,14, 23);
        } catch (IOException e) {}
    }

    public void setWalls() {
        Graphics g = combined.getGraphics();
        walls = new ArrayList<Rectangle>();
        //make sure to put the things that won't be counted as walls here
        String nonWallTiles = "pzekb/[{}n^<>-";
        for (int r = 0; r < 27; r++) {
            for (int c = 0; c < 47; c++) {
                if (!nonWallTiles.contains(levelData[r][c]) && !levelData[r][c].contains("[") && !levelData[r][c].contains("/") && !levelData[r][c].contains("}") && !levelData[r][c].contains("{")) {
                    //make sure that this has the right values
                    walls.add(new Rectangle(c * 32, r * 32, 32, 32));
                }
                switch (levelData[r][c]) {
                    case "0":
                        g.drawImage(topLeftCorner, c * 32, r * 32, 32, 32, null);
                        break;
                    case "1":
                        g.drawImage(upWall, c * 32, r * 32, 32, 32, null);
                        break;
                    case "2":
                        g.drawImage(topRightCorner, c * 32, r * 32, 32, 32, null);
                        break;
                    case "3":
                        g.drawImage(leftWall, c * 32, r * 32, 32, 32, null);
                        break;
                    case "4":
                        g.drawImage(darkness, c * 32, r * 32, 32, 32, null);
                        break;
                    case "5":
                        g.drawImage(rightWall, c * 32, r * 32, 32, 32, null);
                        break;
                    case "6":
                        g.drawImage(bottomLeftCorner, c * 32, r * 32, 32, 32, null);
                        break;
                    case "7":
                        g.drawImage(downWall, c * 32, r * 32, 32, 32, null);
                        break;
                    case "8":
                        g.drawImage(bottomRightCorner, c * 32, r * 32, 32, 32, null);
                        break;
                    case "q":
                        g.drawImage(topLeftDarkness, c * 32, r * 32, 32, 32, null);
                        break;
                    case "w":
                        g.drawImage(topRightDarkness, c * 32, r * 32, 32, 32, null);
                        break;
                    case "a":
                        g.drawImage(bottomLeftDarkness, c * 32, r * 32, 32, 32, null);
                        break;
                    case "s":
                        g.drawImage(bottomRightDarkness, c * 32, r * 32, 32, 32, null);
                        break;
                    case "Q":
                        g.drawImage(halfCornerDown, c * 32, r * 32, 32, 32, null);
                        break;
                    case "W":
                        g.drawImage(halfCornerRight, c * 32, r * 32, 32, 32, null);
                        break;
                    case "E":
                        g.drawImage(halfCornerUp, c * 32, r * 32, 32, 32, null);
                        break;
                    case "R":
                        g.drawImage(halfCornerLeft, c * 32, r * 32, 32, 32, null);
                        break;
                    case "T":
                        g.drawImage(halfCornerHalfWallDown, c * 32, r * 32, 32, 32, null);
                        break;
                    case "Y":
                        g.drawImage(halfCornerHalfWallRight, c * 32, r * 32, 32, 32, null);
                        break;
                    case "U":
                        g.drawImage(halfCornerHalfWallUp, c * 32, r * 32, 32, 32, null);
                        break;
                    case "I":
                        g.drawImage(halfCornerHalfWallLeft, c * 32, r * 32, 32, 32, null);
                        break;
                    case "O":
                        g.drawImage(allCorner, c * 32, r * 32, 32, 32, null);
                        break;
                    case "P":
                        g.drawImage(allWall, c * 32, r * 32, 32, 32, null);
                        break;
                    case "A":
                        g.drawImage(doubleMiddleUpDown, c * 32, r * 32, 32, 32, null);
                        break;
                    case "S":
                        g.drawImage(doubleMiddleRightLeft, c * 32, r * 32, 32, 32, null);
                        break;
                    case "D":
                        g.drawImage(doubleRight, c * 32, r * 32, 32, 32, null);
                        break;
                    case "F":
                        g.drawImage(doubleLeft, c * 32, r * 32, 32, 32, null);
                        break;
                    case "G":
                        g.drawImage(doubleDown, c * 32, r * 32, 32, 32, null);
                        break;
                    case "H":
                        g.drawImage(doubleUp, c * 32, r * 32, 32, 32, null);
                        break;
                    case "p":
                        engine.newWizard(c * 32 + 2, r * 32 + 20);
                        availableCharacters.add(engine.getWizard());
                        break;
                    case "e":
                        engine.getPortal().setX(c * 32 + 16);
                        engine.getPortal().setY(r * 32 - 5);
                        break;
                    case "k":
                        engine.newKnight(c * 32 + 2, r * 32 + 20);
                        availableCharacters.add(engine.getKnight());
                        break;
                    case "b":
                        boxes.add(new Box(engine, c * 32, r * 32));
                        System.out.println(boxes);
                        break;
                    case "n":
                        engine.newNecromancer(c * 32 + 2, r * 32 + 20);
                        availableCharacters.add(engine.getNecromancer());
                        break;
                    case "^":
                        spikes.add(new Rectangle(c * 32, r * 32 + 16, 32, 5));
                        g.drawImage(spike, c * 32, r * 32 + 25, 32 ,7, null);
                        break;
                    case "<":
                        oneWayPlatforms.add(new Rectangle(c * 32, r * 32 + 16, 32, 5));
                        g.drawImage(oneWayLeft, c * 32, r * 32 + 16, 32, 12, null);
                        break;
                    case "-":
                        oneWayPlatforms.add(new Rectangle(c * 32, r * 32 + 16, 32, 5));
                        g.drawImage(oneWayMiddle, c * 32, r * 32 + 16, 32, 12, null);
                        break;
                    case ">":
                        oneWayPlatforms.add(new Rectangle(c * 32, r * 32 + 16, 32, 5));
                        g.drawImage(oneWayRight, c * 32, r * 32 + 16, 32, 12, null);
                        break;
                }
                if (levelData[r][c].contains("/")) {
                    openers.add(new Key(engine, levelData[r][c], c * 32 + 8, r * 32 + 9));
                }
                else if (levelData[r][c].contains("[")) {
                    openers.add(new Button(engine, levelData[r][c], c * 32 + 8, r * 32 + 24));
                }
                else if (levelData[r][c].contains("}")) {
                    doors.add(new Door(engine, levelData[r][c],c * 32, r * 32, true));
                }
                else if (levelData[r][c].contains("{")) {
                    doors.add(new Door(engine, levelData[r][c], c * 32, r * 32, false));
                }
            }
        }
        for (Opener opener: openers) {
            ArrayList<Door> subDoors = new ArrayList<Door>();
            for (int i = 0; i < doors.size(); i++) {
                if (doors.get(i).getNumber() == opener.getNumber()) {
                    subDoors.add(doors.get(i));
                }
            }
            Door[] doorArray = subDoors.toArray(new Door[subDoors.size()]);
            openersAndDoors.put(opener, doorArray);
        }
        System.out.println(walls.size());
        try {
            ImageIO.write(combined, "PNG", new File("image/Level.png"));
        } catch (IOException e) {
            System.out.println("fail");
        }
        availableCharacters.get(0).setActive(true);
    }
    private String[][] getLevelData(String fileName) {
        String[][] data = new String[27][47];
        File f = new File("level_data/" + fileName);
        Scanner s = null;
        try {
            s = new Scanner(f);
        } catch (FileNotFoundException e) {}
        for (int c = 0; c < 27; c++) {
            String[] tiles = s.nextLine().split(" ");
            for (int r = 0; r < 47; r++) {
                data[c][r] = tiles[r];
            }
        }
        return data;
    }

    public void resetStage() {
        boxes.clear();
        int counter = 0;
        String characters = "pkn";
        for (int r = 0; r < 27; r++) {
            for (int c = 0; c < 47; c++) {
                if (characters.contains(levelData[r][c])) {
                    availableCharacters.get(counter).setX(c * 32 + 2);
                    availableCharacters.get(counter).setY(r * 32 + 20);
                    availableCharacters.get(counter).setAvailable(true);
                    resetting = true;
                    if (availableCharacters.get(counter) instanceof Necromancer) {
                        ((Necromancer) availableCharacters.get(counter)).nullSummon();
                    }
                    counter++;
                }
                else if (levelData[r][c].equals("b")) {
                    boxes.add(new Box(engine, c * 32, r * 32));
                }
                for (Opener opener: openers) {
                    opener.setOpening(false);
                }
                for (Door door: doors) {
                    door.setKeyOpen(false);
                }
            }
        }
    }

    public void checkLevelDone() {
        boolean check = true;
        for (Player character: availableCharacters) {
            if (character.isAvailable()) {
                check = false;
            }
        }
        levelDone = check;
    }

    public void changeActive() {
        if (!swapped && !levelDone) {
            int current = characterInControl + 1;
            while (current != characterInControl) {
                if (current == availableCharacters.size()) {
                    current = -1;
                }
                else if(availableCharacters.get(current).isAvailable()) {
                    availableCharacters.get(characterInControl).setActive(false);
                    availableCharacters.get(current).setActive(true);
                    characterInControl = current;
                    swapped = true;
                    break;
                }
                current++;
                System.out.println("current: " + current);
                System.out.println(characterInControl);
            }
        }
    }

    public void draw(Graphics2D g) {
        engine.getPlayBackground().draw(g);
        arrowCounter++;
        g.drawImage(combined, 0, 0, null);
        //Draws all boxes
        for (Box box: boxes) {
            box.draw(g);
        }
        //Draws all keys/buttons
        for (Opener opener: openers) {
            opener.draw(g);
        }
        //Draws all doors
        for (Door door: doors) {
            door.draw(g);
        }
        engine.getPortal().draw(g); //Draws the end portal
        //Draws all players that haven't entered the portal yet
        for (Player character: availableCharacters) {
            if (character.isAvailable()) {
                character.draw(g);
            }
        }
        Player current = availableCharacters.get(characterInControl);
        if (arrowCounter < 20) {
            g.drawImage(arrow, current.getX() + 13, current.getY() - 15, 7, 12, null);
        }
        else if (arrowCounter <= 40) {
            g.drawImage(arrow, current.getX() + 13, current.getY() - 17, 7, 12, null);
            if (arrowCounter >= 40) {
                arrowCounter = 0;
            }
        }

//        System.out.println("QWE");
    }

    public void update() {
        if (!levelDone) {
//            System.out.println(openers.get(1).getNumber());
            for (Player character : availableCharacters) {
                if (character.isAvailable()) {
                    character.update();
                }
            }
            for (Box box: boxes) {
                box.update();
            }
            //not really sure why this is needed. For some reason it needs two separate updates to actually bring back the player
            if (resetting) {
                resetStage();
                resetting = false;
            }
            for (Opener opener: openers) {
                opener.update();
            }
            for (Door door: doors) {
                door.update();
            }
            engine.getPortal().update();
//        System.out.println("LKJ");
        }
        else {
            try {
                String save = "," + levelNumber;
                Files.write(Paths.get("level_data/save"), save.getBytes(), StandardOpenOption.APPEND);
                engine.getLevelSelectionPanel().updateCompleatedLevels(Integer.parseInt(levelNumber));
            } catch (IOException e) {}
            engine.getTransitions().setDesiredLocation("Level Select");
            engine.getTransitions().setIn(true);
        }
        swapped = false;
    }



    public ArrayList<Rectangle> getWalls() {
        return walls;
    }

    public ArrayList<Player> getAvailableCharacters() {
        return availableCharacters;
    }

    public ArrayList<Box> getBoxes() {
        return boxes;
    }

    public HashMap<Opener, Door[]> getOpenersAndDoors() {
        return openersAndDoors;
    }

    public ArrayList<Door> getDoors() {
        return doors;
    }

    public ArrayList<Rectangle> getSpikes() {
        return spikes;
    }


    public ArrayList<Rectangle> getOneWayPlatforms() {
        return oneWayPlatforms;
    }
}
