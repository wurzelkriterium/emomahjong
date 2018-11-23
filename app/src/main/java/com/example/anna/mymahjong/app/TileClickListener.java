package com.example.anna.mymahjong.app;

import android.view.View;

/**
 * Created by anna on 19.10.17.
 */

abstract class TileClickListener implements View.OnClickListener {

//    @Override
//    public void onClick(View v) {
//        if (!tiles[i][j].isSelected()) {
//            tiles[i][j].setSelected(true);
//        } else if (tiles[i][j].isSelected()) {
//            tiles[i][j].setSelected(false);
//        }
//        if (increaseMatchCount[0] == null) {
//            increaseMatchCount[0] = tiles[i][j];
//        } else {
//            if (increaseMatchCount[0].matches(tiles[i][j]) && (increaseMatchCount[0] != tiles[i][j])) {
//                increaseMatchCount[1]=tiles[i][j];
//
//                lastMovesStack.push(increaseMatchCount[0]);
//                lastMovesStack.push(increaseMatchCount[1]);
//
//                String imgName1 = increaseMatchCount[0].getEmotion() + Integer.toString(increaseMatchCount[0].getActor());
//                String imgName2 = tiles[i][j].getEmotion() + Integer.toString(tiles[i][j].getActor());
//
//                setTileImage(lastPair1,imgName1);
//                setTileImage(lastPair2,imgName2);
//
//                lastEmotion.setText(increaseMatchCount[0].getEmotion());
//                increaseMatchCount[0].setVisibility(View.INVISIBLE);
//                tiles[i][j].setVisibility(View.INVISIBLE);
//                matchesView.setText(String.valueOf(++matches)+ " von "+String.valueOf(maxMatches));
//
//                increaseMatchCount[0].updateNeighbours();
//                tiles[i][j].updateNeighbours();
//                increaseMatchCount[0].updateCoveredTiles(true);
//                tiles[i][j].updateCoveredTiles(true);
//                //updateNeighbours(increaseMatchCount[0], tiles[i][j]);
//
//                List<Tile> coveredTiles = tiles[i][j].getCoveredTiles();
//                for(Tile tile: coveredTiles){
//                    tile.removeOverlayTile(tiles[i][j]);
//                }
//                coveredTiles=increaseMatchCount[0].getCoveredTiles();
//                for(Tile tile: coveredTiles){
//                    tile.removeOverlayTile(increaseMatchCount[0]);
//                }
//                updateActiveTiles(increaseMatchCount[0], true);
//                updateActiveTiles(increaseMatchCount[1], true);
//                activeTiles.remove(increaseMatchCount[0]);
//                activeTiles.remove(increaseMatchCount[1]);
//                increaseMatchCount[0] =null;
//                increaseMatchCount[1]=null;
//                //refreshAllLayerView();
//
//                hint = getHint();
//                if (hint==null){
//                    Toast.makeText(PlayActivity.this,"Es gibt keine möglichen Spielzüge mehr. Klicke Rückgängig und finde einen anderen Weg.",Toast.LENGTH_LONG).show();
//                }
//                if (matches == maxMatches) {
//                    initModel();
//                    resetLevelAndMatchesInfoView();
//                    createLevel();
//                }
//
//
//            } else {
//                increaseMatchCount[0].setSelected(false);
//                increaseMatchCount[0] = tiles[i][j];
//            }
//        }
//
//
//    }
}
