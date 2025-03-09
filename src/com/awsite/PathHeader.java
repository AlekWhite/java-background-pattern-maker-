package com.awsite;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class PathHeader {

    int x, y, dir;
    ArrayList<PathNode> pathStreak;
    Page p;
    boolean done = false;

    public PathHeader(int x, int y, int d, Page p){
        this.x = x;
        this.y = y;
        dir = d;
        this.p = p;
        pathStreak = new ArrayList<>();
        pathStreak.add(new PathNode(x, y));}

    // expands if possible
    public boolean expandPath(){
        if (done) return false;

        // build potential directions and magnitudes
        ArrayList<Integer> potentialDirs = new ArrayList<>();
        int invalidDir = Math.floorMod(dir-2, 4);
        for (int i=0; i<4; i++){
            if (i != invalidDir) potentialDirs.add(i);}
        ArrayList<Integer> potentialMags = new ArrayList<>();
        for (int i=0; i<5; i++) potentialMags.add(i);

        // run through available directions
        while (potentialDirs.size()>0){
            int selectedDir = potentialDirs.remove(ThreadLocalRandom.current().nextInt(0, potentialDirs.size()));
            ArrayList<Integer> workingMags = new ArrayList<>(potentialMags);

            // run through available mags
            while (workingMags.size()>0){
                int selectedMag = workingMags.remove(ThreadLocalRandom.current().nextInt(0, workingMags.size()));

                // check if expansion is valid
                if (tryExpansion(selectedDir, selectedMag)) {return true;}}}

        done = true;
        return false;}

    // attempt to expand this path
    private boolean tryExpansion(int dir, int mag){

        // verify that the expansion fits on the page
        int[] delta = new int[][]{{0,-1},{-1,0},{0,1},{1,0}}[dir];
        ArrayList<int[]> path = new ArrayList<>();
        for (int i=0; i<mag+1; i++){
            int[] cord = new int[]{x+delta[0], y+delta[1]};
            if ((cord[0]<0)||(cord[1]<0)||(cord[0]>=p.width)||(cord[1]>=p.height)||(p.blockedCells[cord[0]][cord[1]])){
                return false;}
            path.add(cord.clone());}

        // record new blocked cells
        for(int[] pathPart: path){
            if (path.indexOf(pathPart)==path.size()-1) break;
            p.blockedCells[pathPart[0]][pathPart[1]] = true;
            if ((dir==1)||(dir==3)){
                if (((pathPart[1]+1)>=p.height)||((pathPart[1]-1)<0)) continue;
                p.blockedCells[pathPart[0]][pathPart[1]+1] = true;
                p.blockedCells[pathPart[0]][pathPart[1]-1] = true;
            } else {
                if (((pathPart[0]+1)>=p.height)||((pathPart[0]-1)<0)) continue;
                p.blockedCells[pathPart[0]+1][pathPart[1]] = true;
                p.blockedCells[pathPart[0]-1][pathPart[1]] = true;}}

        // do the expansion
        for(int[] p: path) pathStreak.add(new PathNode(p[0], p[1]));

        this.dir = dir;
        x = path.get(path.size()-1)[0];
        y = path.get(path.size()-1)[1];
        return true;}
}
