/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Tuyu
 */
public class ImageColor {

    class Point {

        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    Random rand = new Random();
    int id = 0;
    int warna = 0;
    int tres = 0;

    ArrayList<ArrayList> daftar;
    ArrayList<ArrayList> akhir;

    int checked[][];
    Vector<Point> listcoordinate;

    public BufferedImage getImage(int tres, String location) {
        this.tres = tres;
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(location));
        } catch (IOException ex) {
            Logger.getLogger(ImageColor.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedImage hasil = colorImage(img, tres);
        return hasil;
    }

    public BufferedImage colorImage(BufferedImage img, int treshold) {
        BufferedImage temp = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        checked = new int[img.getWidth()][img.getHeight()];
        listcoordinate = new Vector();

        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                checked[i][j] = 0;
            }
        }

        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                if (checked[x][y] == 0) {
                    Color color = new Color(img.getRGB(x, y));
                    listcoordinate.add(new Point(x, y));
                    searchNeighbour(img, temp, color, treshold);
                }
            }
        }

        return temp;
    }

    public void searchNeighbour(BufferedImage img, BufferedImage temp, Color color, int treshold) {
        while (listcoordinate.size() > 0) {
            Point p = listcoordinate.remove(0);
            if ((p.x >= 0) && (p.y >= 0) && (p.x < img.getWidth()) && (p.y < img.getHeight())) {
                if (checked[p.x][p.y] == 0) {
                    Color c = new Color(img.getRGB(p.x, p.y));
                    if (Math.abs(c.getRed() - color.getRed()) <= treshold
                            && Math.abs(c.getGreen() - color.getGreen()) <= treshold
                            && Math.abs(c.getBlue() - color.getBlue()) <= treshold) {
                        temp.setRGB(p.x, p.y, color.getRGB());
                        checked[p.x][p.y] = 1;

                        listcoordinate.add(new Point(p.x - 1, p.y - 1));
                        listcoordinate.add(new Point(p.x, p.y - 1));
                        listcoordinate.add(new Point(p.x + 1, p.y - 1));
                        listcoordinate.add(new Point(p.x - 1, p.y));
                        listcoordinate.add(new Point(p.x + 1, p.y));
                        listcoordinate.add(new Point(p.x - 1, p.y + 1));
                        listcoordinate.add(new Point(p.x, p.y + 1));
                        listcoordinate.add(new Point(p.x + 1, p.y + 1));
                    }
                }
            }
        }
    }
}
