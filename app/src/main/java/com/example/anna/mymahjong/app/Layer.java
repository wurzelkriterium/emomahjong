package com.example.anna.mymahjong.app;


import android.view.View;

public class Layer {
    Tile[][] tiles;

    public Layer(Tile[][] layer){
        this.tiles = layer;
    }
    public void initNeighborModel() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length - 1; j++) {
                tiles[i][j].setRightNeighbor(tiles[i][j + 1]);
            }
            for (int j = 1; j < tiles[0].length; j++) {
                tiles[i][j].setLeftNeighbor(tiles[i][j - 1]);
            }
        }
    }
    public void setLayerVisible(boolean visible) {
        for (Tile[] tiles : tiles) {
            for (Tile tile : tiles) {
                if (tile != null) {
                    tile.setVisibility(visible? View.VISIBLE : View.INVISIBLE);
                    tile.setSelected(false);
                }
            }
        }
    }

    public void updateLayerView() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] != null) {
                    if(tiles[i][j].isCovered()){
                        tiles[i][j].setImageAlpha(125);
                        tiles[i][j].setEnabled(false);

                    }else{
                        if (tiles[i][j].getLeftNeighbor() != null && tiles[i][j].getRightNeighbor() != null) {
                            tiles[i][j].setImageAlpha(125);
                            tiles[i][j].setEnabled(false);

                        } else {
                            tiles[i][j].setImageAlpha(255);
                            tiles[i][j].setEnabled(true);
                        }
                    }
                }
            }
        }
    }
    public void setTiles(Tile[][] tiles){
        this.tiles=tiles;

    }

    public Tile[][] getTiles() {
        return tiles;

    }
    public void setUpperLayer(Layer upLayer){
        Tile[][] upperLayer =  upLayer.getTiles();
        for (int i = 0; i < upperLayer.length; i++) {
            for (int j = 0; j < upperLayer[0].length; j++) {
                upperLayer[i][j].addCoveredTile(tiles[i][j]);
                upperLayer[i][j].addCoveredTile(tiles[i][j+1]);
                upperLayer[i][j].addCoveredTile(tiles[i+1][j]);
                upperLayer[i][j].addCoveredTile(tiles[i+1][j+1]);
                tiles[i][j].addOverlayTile(upperLayer[i][j]);
                tiles[i][j+1].addOverlayTile(upperLayer[i][j]);
                tiles[i+1][j].addOverlayTile(upperLayer[i][j]);
                tiles[i+1][j+1].addOverlayTile(upperLayer[i][j]);
            }
        }
        upLayer.setTiles(upperLayer);
    }
}
