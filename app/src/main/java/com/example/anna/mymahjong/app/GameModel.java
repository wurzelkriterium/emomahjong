package com.example.anna.mymahjong.app;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class GameModel {
    Context context;
    List<Tile> activeTiles = new ArrayList<Tile>();
    private Stack<Tile> lastMovesStack = new Stack<Tile>();
    Layer layer0;
    Layer layer1;
    Layer layer2;
    Layer layer3;
    private int matches = 0;
    private int maxMatches = 9;
    private int level = 1;
    String[] emotions = null;
    HashMap<String, List<Integer>> emotionsAndActors = new HashMap<String, List<Integer>>();

    private int numberOfEmotions;

    public GameModel(Context context, Tile[][] layer0, Tile[][] layer1, Tile[][] layer2, Tile[][] layer3){
        this.context = context;
        this.layer0 = new Layer(layer0);
        this.layer1 = new Layer(layer1);
        this.layer2 = new Layer(layer2);
        this.layer3 = new Layer(layer3);
        initModel();
    }
    public void loadGameContent()  {
        String [] subdirs = null;
        String [] imageFiles = null;
        AssetManager am=null;
        try {
            am = context.getAssets();
            subdirs = am.list("img");
            emotions = new String [subdirs.length];
            numberOfEmotions = emotions.length;

            for(int i=0; i< numberOfEmotions; i++){
                emotions[i] = subdirs[i];
                imageFiles = am.list("img/"+subdirs[i]);
                ArrayList<Integer> actorList = new ArrayList<Integer>();
                for(int j=0; j<imageFiles.length; j++){
                    String actor = imageFiles[j];
                    String actorNr = actor.substring(emotions[i].length(), actor.length()-4);
                    actorList.add(Integer.valueOf(actorNr));
                }
                emotionsAndActors.put(emotions[i], actorList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initModel() {
        layer0.initNeighborModel();
        layer1.initNeighborModel();
        layer2.initNeighborModel();
        layer3.initNeighborModel();

        layer0.setLayerVisible(false);
        layer1.setLayerVisible(false);
        layer2.setLayerVisible(false);
        layer3.setLayerVisible(false);
    }

    private void activateStartTiles(Tile[][] layer) {
        for(int i=0; i<layer.length; i++){
            activeTiles.add(layer[i][0]);
            activeTiles.add(layer[i][layer[0].length-1]);
        }
    }

    public boolean createLevel() {
        if(level == 6){
            return false;
        }
        if (this.level == 1) {
            maxMatches = 9;
            activateStartTiles(layer3.getTiles());
            activateLayers(layer3, layer2);
            createRandomPairs();
            layer3.initNeighborModel();
            layer2.initNeighborModel();
            layer2.setUpperLayer(layer3);
            activateStartTiles(layer3.getTiles());
            layer3.setLayerVisible(true);
            layer2.setLayerVisible(true);
            layer3.updateLayerView();
            layer2.updateLayerView();
        } else if (level == 2) {
            maxMatches = 16;
            activateStartTiles(layer2.getTiles());
            activateLayers(layer2, layer1);
            createRandomPairs();
            layer2.initNeighborModel();
            layer1.initNeighborModel();
            layer1.setUpperLayer(layer2);
            activateStartTiles(layer2.getTiles());
            layer1.setLayerVisible(true);
            layer2.setLayerVisible(true);
            layer1.updateLayerView();
            layer2.updateLayerView();
        } else if (level == 3) {
            maxMatches = 19;
            activateStartTiles(layer3.getTiles());
            activateLayers(layer3, layer2);
            activateLayers(layer2, layer1);
            createRandomPairs();
            layer3.initNeighborModel();
            layer2.initNeighborModel();
            layer1.initNeighborModel();
            layer2.setUpperLayer(layer3);
            layer1.setUpperLayer(layer2);
            activateStartTiles(layer3.getTiles());
            layer1.setLayerVisible(true);
            layer2.setLayerVisible(true);
            layer3.setLayerVisible(true);
            layer1.updateLayerView();
            layer2.updateLayerView();
            layer3.updateLayerView();
        } else if (level == 4) {
            maxMatches = 25;
            activateStartTiles(layer1.getTiles());
            activateLayers(layer1, layer0);
            createRandomPairs();
            layer1.initNeighborModel();
            layer0.initNeighborModel();
            layer0.setUpperLayer(layer1);
            activateStartTiles(layer1.getTiles());
            layer1.setLayerVisible(true);
            layer0.updateLayerView();
            layer1.updateLayerView();
        } else if (level == 5) {
            maxMatches = 31;
            activateStartTiles(layer2.getTiles());
            activateLayers(layer2, layer1);
            activateLayers(layer1, layer0);
            createRandomPairs();
            layer2.initNeighborModel();
            layer1.initNeighborModel();
            layer0.initNeighborModel();
            layer1.setUpperLayer(layer2);
            layer0.setUpperLayer(layer1);
            activateStartTiles(layer2.getTiles());
            layer1.setLayerVisible(true);
            layer2.setLayerVisible(true);
            layer0.setLayerVisible(true);
            layer0.updateLayerView();
            layer1.updateLayerView();
            layer2.updateLayerView();
        }
        return true;
    }

    public String getHint() {
        String hintEmotion;
        for (int i = 0; i < activeTiles.size(); i++) {
            hintEmotion = activeTiles.get(i).getEmotionText();
            for (int j = 0; j < activeTiles.size(); j++) {
                if (i != j && activeTiles.get(j).getEmotionText() == hintEmotion) {
                    return hintEmotion;
                }
            }
        }
        return null;
    }

    private void activateLayers(Layer upperLayer, Layer lowerLayer) {
        upperLayer.setLayerVisible(true);
        lowerLayer.setLayerVisible(true);
        lowerLayer.setUpperLayer(upperLayer);
        lowerLayer.updateLayerView();
        upperLayer.updateLayerView();
        initModel();
        lowerLayer.initNeighborModel();
        upperLayer.initNeighborModel();
    }

    private void createRandomPairs() {
        for(int i=0; i<maxMatches; i++){
            Random randomGenerator = new Random();

            int randomEmotionNr = randomGenerator.nextInt(numberOfEmotions);
            String emotion = emotions[randomEmotionNr];
            int numberOfActors = emotionsAndActors.get(emotion).size();

            int actorNr1 = randomGenerator.nextInt(numberOfActors);
            int actorNr2 = actorNr1;
            while(actorNr1 == actorNr2){
                actorNr2 = randomGenerator.nextInt(numberOfActors);
            }
            actorNr1=emotionsAndActors.get(emotion).get(actorNr1);
            actorNr2=emotionsAndActors.get(emotion).get(actorNr2);

            String imgName1 = emotion + Integer.toString(actorNr1);
            String imgName2 = emotion + Integer.toString(actorNr2);

            int pos1 = randomGenerator.nextInt(activeTiles.size());
            int pos2 = pos1;
            while (pos2==pos1){
                pos2 = randomGenerator.nextInt(activeTiles.size());
            }

            Tile tile1 = activeTiles.get(pos1);
            Tile tile2 = activeTiles.get(pos2);

            tile1.setValues(context,emotion, actorNr1, imgName1);
            tile2.setValues(context, emotion, actorNr2, imgName2);

            tile1.enableNeighbours();
            tile2.enableNeighbours();

            activateCoveredTiles(tile1);
            activateCoveredTiles(tile2);

            activateNeighbours(tile1);
            activateNeighbours(tile2);

            activeTiles.remove(tile1);
            activeTiles.remove(tile2);
        }
    }

    private void activateNeighbours(Tile tile1) {
        if(tile1.getLeftNeighbor()!=null){
            if(!tile1.getLeftNeighbor().isCovered() && !activeTiles.contains(tile1.getLeftNeighbor())) {
                activeTiles.add(tile1.getLeftNeighbor());
            }
        }
        if(tile1.getRightNeighbor()!=null){
            if(!tile1.getRightNeighbor().isCovered() && !activeTiles.contains(tile1.getRightNeighbor())) {
                activeTiles.add(tile1.getRightNeighbor());
            }
        }
    }

    private void activateCoveredTiles(Tile upperTile) {
        List<Tile> coveredTiles = upperTile.getCoveredTiles();
        for(Tile tile: coveredTiles){
            tile.removeOverlayTile(upperTile);
            if(!tile.isCovered() && !activeTiles.contains(tile) && (tile.getLeftNeighbor()==null || tile.getRightNeighbor()==null )){
                activeTiles.add(tile);
            }
        }
    }

    public void levelFinished() {
        this.matches = 0;
        this.level++;
        this.lastMovesStack.clear();
    }

    public void updateActiveTiles(Tile tile, boolean remove) {
        Tile left = tile.getLeftNeighbor();
        Tile right = tile.getRightNeighbor();

        if(remove){
            for(Tile covered: tile.getCoveredTiles()){
                if(!covered.isCovered() &&(covered.getLeftNeighbor()==null || covered.getRightNeighbor()==null)){
                    if(!activeTiles.contains(covered)){
                        activeTiles.add(covered);
                    }
                }
            }
            if(left!=null){
                if(!left.isCovered() && !activeTiles.contains(left)){
                    activeTiles.add(left);
                }
            }
            if(right!=null){
                if(!right.isCovered() && !activeTiles.contains(right)){
                    activeTiles.add(right);
                }
            }
        }else{
            for(Tile covered: tile.getCoveredTiles()){
                if(covered.isCovered()){
                    if(activeTiles.contains(covered)){
                        activeTiles.remove(covered);
                    }
                }
            }
            if(left!=null){
                if(left.getLeftNeighbor()!=null && activeTiles.contains(left)){
                    activeTiles.remove(left);
                }
            }
            if(right!=null){
                if(right.getRightNeighbor()!=null && activeTiles.contains(right)){
                    activeTiles.remove(right);
                }
            }
        }
    }

    public void addActiveTile(Tile activeTile) {
        if(!activeTiles.contains(activeTile)){
            activeTiles.add(activeTile);
        }
    }

    public Stack<Tile> getLastMovesStack() {
        return lastMovesStack;
    }

    public int getMatchesCount() {
        return matches;
    }

    public int getMaxMatchesCount() {
        return maxMatches;
    }

    public int getLevel() {
        return level;
    }

    public void increaseMatchCount(boolean foundMatch) {
        if(foundMatch)
            matches++;
        else
            matches--;
    }
}