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
                this.energy[row][col] = energyCalculate(col, row);
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
        return energy[y][x];
    }

    // energy of pixel at column x and row y
    private double energyCalculate(int x, int y) {
        // checkRange(x, y);
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
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }

    private void transpose() {
        Color[][] tpicture = new Color[width()][height()];
        double[][] tenergy = new double[width()][height()];
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                tpicture[col][row] = picture[row][col];
                tenergy[col][row] = energy[row][col];
            }
        }
        picture = tpicture;
        energy = tenergy;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        // run DFS to find topological order:
        // https://www.coursera.org/learn/algorithms-part2/lecture/RAMNS/topological-sort  (6:43)
        double[][] distTo = new double[height()][width()];
        int[][] edgeTo = new int[height()][width()];

        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                distTo[row][col] = Double.POSITIVE_INFINITY;
                edgeTo[row][col] = -1;
            }
        }
        for (int col = 0; col < width(); col++) {
            distTo[0][col] = energy(col, 0);// first row
        }
        for (int row = 0; row < height() - 1; row++) {
            for (int col = 0; col < width(); col++) {
                //distTo[row][col] = energy(col, row);
                //double energy = energy(col, row);
                double dist = distTo[row][col];
                double minEnergy = Double.POSITIVE_INFINITY;
                for (int v = Math.max(col - 1, 0); v <= Math.min(col + 1, width() - 1); v++) {
                    // relax edge:
                    double currDist = dist + energy(v, row + 1);
                    if (currDist < distTo[row + 1][v]) {
                        distTo[row + 1][v] = currDist;
                        edgeTo[row + 1][v] = col;// edge we came from
                    }
                }
            }
        }
        // then consider vertices in topological order:
        // https://www.coursera.org/learn/algorithms-part2/lecture/6rxSt/edge-weighted-dags  (1:50)
        // note: edges are three neighbour pixels under the current one
        // relax all edges pointing from a vertex saving distTo(aka total energy)
        // store 'edgeTo'- x-coordinate of previous row that took us there

        // find minimal total energy in the last row and follow back through using 'edgeTo'
        double[] lastRow = distTo[height() - 1];
        int minIdx = -1;
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < lastRow.length; i++) {
            if (lastRow[i] < min) {
                min = lastRow[i];
                minIdx = i;
            }
        }
        int[] out = new int[height()];
        out[height() - 1] = minIdx;
        for (int i = height() - 1; i > 0; i--) {
            out[i - 1] = edgeTo[i][minIdx];
            minIdx = out[i - 1];
        }
        return out;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        validateImage();
        validateSeam(seam);
        transpose();
        removeVerticalSeam(seam);
        transpose();
    }


    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        validateImage();
        validateSeam(seam);
        int newWidth = width() - 1;
        for (int row = 0; row < height(); row++) {
            // remove seam in energy

            int intactPos = seam[row];
            int intactLen = seam[row] + 1;

            double[] newEnergy = new double[newWidth];
            System.arraycopy(energy[row], 0, newEnergy, 0, intactPos);
            System.arraycopy(energy[row], intactLen, newEnergy, intactPos, newWidth - intactPos);
            energy[row] = newEnergy;

            // remove seam in picture by shifting row members using System.arraycopy()
            Color[] newPicture = new Color[newWidth];
            System.arraycopy(picture[row], 0, newPicture, 0, intactPos);
            System.arraycopy(picture[row], intactLen, newPicture, intactPos, newWidth - intactPos);
            picture[row] = newPicture;
        }

        // update energies for all neighbours of the removed seam
        for (int row = 0; row < height(); row++) {
            if (seam[row] - 1 >= 0 && seam[row] - 1 < width()) {
                energy[row][seam[row] - 1] = energyCalculate(seam[row] - 1, row);
            }
            if (seam[row] >= 0 && seam[row] < width()) {
                energy[row][seam[row]] = energyCalculate(seam[row], row);
            }
        }
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
