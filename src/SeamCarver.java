import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);//create defensive copy
    }

    // current picture
    public Picture picture() {
        return this.picture;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        checkRange(x, y);
        if (x == 0 || x == picture.width() - 1 || y == 0 || y == picture.height() - 1) {
            return 1000;
        }

        double deltaX2 = Math.pow(Rx(x, y), 2) + Math.pow(Gx(x, y), 2) + Math.pow(Bx(x, y), 2);
        double deltaY2 = Math.pow(Ry(x, y), 2) + Math.pow(Gy(x, y), 2) + Math.pow(By(x, y), 2);
        return Math.sqrt(deltaX2 + deltaY2);
    }

    private int By(int x, int y) {
        return picture.get(x, y + 1).getBlue() - picture.get(x, y - 1).getBlue();
    }

    private int Gy(int x, int y) {
        return picture.get(x, y + 1).getGreen() - picture.get(x, y - 1).getGreen();
    }

    private int Ry(int x, int y) {
        return picture.get(x, y + 1).getRed() - picture.get(x, y - 1).getRed();
    }

    private int Bx(int x, int y) {
        return picture.get(x + 1, y).getBlue() - picture.get(x - 1, y).getBlue();
    }

    private int Gx(int x, int y) {
        return picture.get(x + 1, y).getGreen() - picture.get(x - 1, y).getGreen();
    }

    private int Rx(int x, int y) {
        return picture.get(x + 1, y).getRed() - picture.get(x - 1, y).getRed();
    }

    private void checkRange(int x, int y) {
        if (x < 0 || x > picture.width() || y < 0 || y > picture.height()) {
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
        if (picture.height() <= 1 || picture.width() <= 1) {
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
