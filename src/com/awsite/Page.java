package com.awsite;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Page extends JPanel {

    public final int width, height, count, scale;
    private final EyeCenter[] centers;
    private final PathHeader[] pathHeaders;
    public boolean[][] blockedCells;
    private final BufferedImage image;
    private ArrayList<Color[]> gradient;

    public Page(int w, int h, int s, int  c){

        scale = s;
        count = c;
        width= w;
        height = h;
        image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        colorSetUp();

        // build empty array of blocked cells
        blockedCells = new boolean[w][h];
        for (int i=0; i<w; i++){
            for (int j=0; j<h; j++){
                blockedCells[i][j] = false;}}


        // populate page with eye-centers
        centers = new EyeCenter[count];
        for(int i=0; i<count; i++){
            centers[i] = new EyeCenter(this, ThreadLocalRandom.current().nextInt(0, gradient.size()));
            centers[i].set();}

        // populate page with path headers
        int ind = 0;
        pathHeaders = new PathHeader[count*4];
        for (EyeCenter cen: centers){
            pathHeaders[ind] = new PathHeader(cen.x-3, cen.y, 1, this);
            pathHeaders[ind+1] = new PathHeader(cen.x+3, cen.y, 3, this);
            pathHeaders[ind+2] = new PathHeader(cen.x, cen.y-3, 0, this);
            pathHeaders[ind+3] = new PathHeader(cen.x, cen.y+3, 2, this);
            ind=ind+4;}

        // expand the paths
        for(int i=0; i<100; i++){
            int doneCount = 0;
            for(PathHeader p: pathHeaders) {
                if (p.expandPath()) doneCount++;}
            if (doneCount == pathHeaders.length) break;}

    }

    public void colorSetUp() {
        int steps = 100;
        gradient = new ArrayList<>();
        /*
        Color[][] colors = new Color[][]{
                {new Color(3, 74, 252), new Color(224, 4, 103)},
                {new Color(3, 74, 252), new Color(66, 245, 75)},
                {new Color(252, 190, 3), new Color(66, 245, 75)},
                {new Color(252, 190, 3), new Color(224, 4, 103)}};

         */


        Color[][] colors = new Color[][]{
                {new Color(224, 4, 103), new Color(3, 74, 252)},
                {new Color(66, 245, 75), new Color(3, 74, 252)},
                {new Color(66, 245, 75), new Color(252, 190, 3)},
                {new Color(224, 4, 103), new Color(252, 190, 3)}};








        for(Color[] c: colors){
            Color start = c[0];
            Color end = c[1];
            Color[] gr = new Color[steps];

            for (int i = 0; i < steps; i++) {
                float ratio = (float) i / (steps - 1);
                int r = (int) (start.getRed() + ratio * (end.getRed() - start.getRed()));
                int g = (int) (start.getGreen() + ratio * (end.getGreen() - start.getGreen()));
                int b = (int) (start.getBlue() + ratio * (end.getBlue() - start.getBlue()));
                gr[i] = new Color(r, g, b);}
            gradient.add(gr);}}

    // draw the final image
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // add bg-color
        g.setColor(new Color(21, 21, 21, 255));
        g.fillRect(0, 0, width*scale, height*scale);

        // draw eye-centers
        for (EyeCenter c: centers){

            // dot
            g.setColor(new Color(159, 156, 156, 255));
            g.fillRect(c.x*scale, c.y*scale, scale, scale);

            // outer ring
            g.setColor(new Color(94, 94, 94, 255));
            for (int i=0; i<5; i++){
                if ((i!=0) && (i!=4)){
                    g.fillRect((c.x-2+i)*scale, (c.y+2)*scale, scale, scale);
                    g.fillRect((c.x-2+i)*scale, (c.y-2)*scale, scale, scale);}
                else{
                    g.fillRect((c.x-2+i)*scale, (c.y+1)*scale, scale, scale);
                    g.fillRect((c.x-2+i)*scale, (c.y)*scale, scale, scale);
                    g.fillRect((c.x-2+i)*scale, (c.y-1)*scale, scale, scale);}}}

        // for each path-part
        double maxMinDis = -1;
        for (PathHeader p: pathHeaders){
            for (PathNode cord: p.pathStreak){

                // find the nearest center point
                int ind = -1;
                double minDis = Double.MAX_VALUE;
                for(EyeCenter cen: centers){
                    double dis = Math.sqrt(Math.pow(cen.x-cord.x,2)+Math.pow(cen.y-cord.y,2));
                    if (dis < minDis) {
                        minDis = dis;
                        ind = cen.colorInd;
                    }}

                // record distance
                cord.minDis = minDis;
                cord.colorInd = ind;
                if (minDis>maxMinDis) maxMinDis = minDis;}}

        // draw paths
        maxMinDis = maxMinDis*(0.9);
        for (PathHeader p: pathHeaders){
            for (PathNode cord: p.pathStreak){

                // use distance to determine color
                int colorInd = (int)Math.round(((cord.minDis)/maxMinDis)*100);
                if (colorInd >= 100) colorInd =99;
                System.out.println(colorInd);
                g.setColor(gradient.get(cord.colorInd)[colorInd]);
                g.fillRect(cord.x*scale, cord.y*scale, scale, scale);}}

        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);}


}
