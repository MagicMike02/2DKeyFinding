package tiles;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp){
        this.gp = gp;
        tile = new Tile[50]; // number of different tiles in the game

        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap();
    }

    public void getTileImage(){
        //PlaceHolders
        setup(0, "grass00", false);
        setup(1, "grass00", false);
        setup(2, "grass00", false);
        setup(3, "grass00", false);
        setup(4, "grass00", false);
        setup(5, "grass00", false);
        setup(6, "grass00", false);
        setup(7, "grass00", false);
        setup(8, "grass00", false);
        setup(9, "grass00", false);

        //
        setup(10, "grass00", false);
        setup(11, "grass01", false);
        setup(12, "water00", true);
        setup(13, "water01", true);
        setup(14, "water02", true);
        setup(15, "water03", true);
        setup(16, "water04", true);
        setup(17, "water05", true);
        setup(18, "water06", true);
        setup(19, "water07", true);
        setup(20, "water08", true);
        setup(21, "water09", true);
        setup(22, "water10", true);
        setup(23, "water11", true);
        setup(24, "water12", true);
        setup(25, "water13", true);

        setup(26, "road00", false);
        setup(27, "road01", false);
        setup(28, "road02", false);
        setup(29, "road03", false);
        setup(30, "road04", false);
        setup(31, "road05", false);
        setup(32, "road06", false);
        setup(33, "road07", false);
        setup(34, "road08", false);
        setup(35, "road09", false);
        setup(36, "road10", false);
        setup(37, "road11", false);
        setup(38, "road12", false);

        setup(39, "earth", false);
        setup(40, "wall", true);
        setup(41, "tree", true);
    }

    public void setup(int index, String imageName, boolean collision) {
        UtilityTool uTool = new UtilityTool();

        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Tiles/New version/" + imageName + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
// LOADMAP CHAT GPT
    public void loadMap() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("Map/worldV2.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            int col = 0;
            int row = 0;
            String line;

            while ((line = br.readLine()) != null && row < gp.maxWorldRow) {
                List<Integer> numbers = parseLine(line);

                for (int num : numbers) {
                    if (col < gp.maxWorldCol) {
                        mapTileNum[col][row] = num;
                        col++;
                    }
                }

                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*
    public void loadMap(){
        try{
            InputStream is = getClass().getClassLoader().getResourceAsStream("Map/world01.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int col = 0;
            int row = 0;

            while(col < gp.maxWorldCol && row < gp.maxWorldRow)   {
                String line = br.readLine();

                while(col < gp.maxWorldCol){
                    List<Integer> numbers = parseLine(line);

                    for (int num : numbers) {
                        if (col < gp.maxWorldCol) {
                            mapTileNum[col][row] = num;
                            col++;
                        }
                    }
                }
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
*/
    private List<Integer> parseLine(String line) {
        String[] numStrings = line.split(" ");
        List<Integer> numbers = new ArrayList<>();

        for (String numStr : numStrings) {
            try {
                int num = Integer.parseInt(numStr);
                numbers.add(num);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return numbers;
    }

    public void draw(Graphics2D g2){
        int worldCol = 0;
        int worldRow = 0;

        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow){
           int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;

            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            //boundaries of the camera to just render the pixel seen
            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY){

                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }

            worldCol++;

            if(worldCol == gp.maxWorldCol){
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
