package com.awsite;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class EyeCenter {

    int x, y, colorInd;
    Page p;

    public EyeCenter(Page p, int colorInd){
        this.colorInd = colorInd;
        this.p = p;}

    // adds the eye to the page 
    public void set(){

        // pick a random point
        int x = ThreadLocalRandom.current().nextInt(3,p.width-3);
        int y = ThreadLocalRandom.current().nextInt(3,p.height-3);
        int[][] filledPoints = new int[49][2];
        int ind = 0;

        // verify point is not blocked
        for (int i=0; i<7; i++){
            for (int j=0; j<7; j++){
                int[] cords = {x-3+i, y-3+j};

                // re-try if cords are not full or out of bounds
                if ((cords[0]<0)||(cords[1]<0)||(cords[0]>=p.width)||(cords[1]>=p.height)){set(); return;}
                if(((Page)p).blockedCells[cords[0]][cords[1]]){set(); return;}

                // record filled points
                filledPoints[ind] = cords.clone();
                ind ++;
            }}

        // mark points as filled
        this.x = x;
        this.y = y;
        for (int[] point: filledPoints){
            p.blockedCells[point[0]][point[1]] = true;}
    }

}
