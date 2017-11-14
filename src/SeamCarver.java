import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {

    private Color[][] picture;
    private double[][] energy;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        this.picture = new Color[picture.height()][picture.width()];//create defensive copy
        this.energy = new double[picture.height()][picture.width()];//create defensive copy
        for (int row = 0; row < picture.height(); row++) {
            this.picture[row] = new Color[picture.width()];

            for (int col = 0; col < picture.width(); col++) {
                this.picture[row][col] = picture.get(col, row);
            }
        }
        for (int row = 0; row < picture.height(); row++) {
            this.energy[row] = new double[picture.width()];

            for (int col = 0; col < picture.width(); col++) {
                this.energy[row][col] = energy(col, row);
            }
        }


    }

    // current picture
    public Picture picture() {
        Picture out = new Picture(width(), height());
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                out.set(col, row, this.picture[row][col]);
            }
        }
        return out;
    }

    // width of current picture
    public int width() {
        return picture[0].length;
    }

    // height of current picture
    public int height() {
        return picture.length;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        checkRange(x, y);
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
            return 1000;
        }

        double deltaX2 = Math.pow(Rx(x, y), 2) + Math.pow(Gx(x, y), 2) + Math.pow(Bx(x, y), 2);
        double deltaY2 = Math.pow(Ry(x, y), 2) + Math.pow(Gy(x, y), 2) + Math.pow(By(x, y), 2);
        return Math.sqrt(deltaX2 + deltaY2);
    }

    private int By(int x, int y) {
        return picture[y + 1][x].getBlue() - picture[y - 1][x].getBlue();
    }

    private int Gy(int x, int y) {
        return picture[y + 1][x].getGreen() - picture[y - 1][x].getGreen();
    }

    private int Ry(int x, int y) {
        return picture[y + 1][x].getRed() - picture[y - 1][x].getRed();
    }

    private int Bx(int x, int y) {
        return picture[y][x + 1].getBlue() - picture[y][x - 1].getBlue();
    }

    private int Gx(int x, int y) {
        return picture[y][x + 1].getGreen() - picture[y][x - 1].getGreen();
    }

    private int Rx(int x, int y) {
        return picture[y][x + 1].getRed() - picture[y][x - 1].getRed();
    }

    private void checkRange(int x, int y) {
        if (x < 0 || x > width() || y < 0 || y > height()) {
            throw new IllegalArgumentException();
        }
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return null;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return null;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        validateImage();
        validateSeam(seam);

    }


    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        validateImage();
        validateSeam(seam);
    }

    private void validateImage() {
        if (height() <= 1 || width() <= 1) {
            throw new IllegalArgumentException();
        }
    }

    private void validateSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        int seamItem = seam[0];
        for (int s : seam) {
            if (Math.abs(s - seamItem) > 1) {
                throw new IllegalArgumentException();
            }
            seamItem = s;
        }
    }

}
