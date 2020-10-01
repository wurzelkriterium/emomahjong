package com.example.anna.mymahjong.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by anna on 25.05.17.
 */

public class PlayActivity extends AppCompatActivity {
    GameModel model;
    Button hintButton, undoButton, menuButton;
    TextView matchesView, lastEmotion, levelView;
    Tile lastPair1;
    Tile lastPair2;
    Tile[] match = new Tile[2];
    String hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_activity);

        lastPair1 =  findViewById(R.id.lastTile1);
        lastPair1.setEnabled(false);
        lastPair2 =  findViewById(R.id.lastTile2);
        lastPair2.setEnabled(false);
        lastEmotion =  findViewById(R.id.lastEmotion);
        levelView =  findViewById(R.id.levelCount);
        matchesView =  findViewById(R.id.numberOfMatches);
        hintButton =  findViewById(R.id.hintButton);
        undoButton =  findViewById(R.id.undoButton);
        menuButton =  findViewById(R.id.menuButton);

        initModel();

        setTileClickListeners(model.layer0.getTiles());
        setTileClickListeners(model.layer1.getTiles());
        setTileClickListeners(model.layer2.getTiles());
        setTileClickListeners(model.layer3.getTiles());

        model.createLevel();
        updateLevelView();
        resetLevelAndMatchesInfoView();
        hint = model.getHint();

        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHint();
            }
        });

        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undo();
            }
        });
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuActivity();
            }
        });
    }

    public void showHint() {
        hint = model.getHint();
        if(hint!=null){
            Toast.makeText(PlayActivity.this, "Finde zwei Gesichter, die "+hint+" aussehen.", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(PlayActivity.this, R.string.no_possible_turn , Toast.LENGTH_LONG).show();
        }
    }

    public void openMenuActivity() {
        Intent intent = new Intent(PlayActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void undo() {
        if(!model.getLastMovesStack().isEmpty()){
            Tile last2 = model.getLastMovesStack().pop();
            Tile last1 = model.getLastMovesStack().pop();
            last1.putBack();
            last2.putBack();
            model.updateActiveTiles(last1, false);
            model.updateActiveTiles(last2, false);
            model.addActiveTile(last1);
            model.addActiveTile(last2);

            hint = model.getHint();

            if(hint==null)
                Toast.makeText(PlayActivity.this, R.string.no_possible_turn , Toast.LENGTH_LONG).show();
            model.increaseMatchCount(false);
            matchesView.setText(String.valueOf(model.getMatchesCount())+ " von " + String.valueOf(model.getMaxMatchesCount()));

            if(!model.getLastMovesStack().isEmpty()){
                Tile nextLast2 = model.getLastMovesStack().pop();
                Tile nextLast1 = model.getLastMovesStack().peek();
                model.getLastMovesStack().push(nextLast2);
                String imgName1 = nextLast1.getEmotion() + Integer.toString(nextLast1.getActor());
                String imgName2 = nextLast2.getEmotion() + Integer.toString(nextLast2.getActor());

                //VIEW
                lastPair1.setImage(getBaseContext(), nextLast1.getEmotion(), imgName1);
                lastPair2.setImage(getBaseContext(), nextLast2.getEmotion(), imgName2);
                lastEmotion.setText(nextLast2.getEmotionText());
            }
            else {
                //VIEW
                lastPair1.setImageResource(android.R.color.transparent);
                lastPair2.setImageResource(android.R.color.transparent);
                lastEmotion.setText("");
            }

        }
    }
    public void setTileClickListeners(Tile[][] tlayer) {
        final Tile[][] layer = tlayer;
        for (int x = 0; x < layer.length; x++) {
            final int i = x;
            for (int y = 0; y < layer[0].length; y++) {
                final int j = y;
                layer[i][j].setOnClickListener(new TileClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!layer[i][j].isSelected()) {
                            layer[i][j].setSelected(true);
                        } else if (layer[i][j].isSelected()) {
                            layer[i][j].setSelected(false);
                            match[0]=null;
                        }
                        if ((match[0] == null)) {
                            match[0] = layer[i][j];
                        } else {
                            if (match[0].isSelected() && match[0].matches(layer[i][j]) && (match[0] != layer[i][j])) {
                                match[1]=layer[i][j];

                                model.getLastMovesStack().push(match[0]);
                                model.getLastMovesStack().push(match[1]);

                               String imgName1 = match[0].getEmotion() + Integer.toString(match[0].getActor());
                               String imgName2 = layer[i][j].getEmotion() + Integer.toString(layer[i][j].getActor());

                                //setTileImage(lastPair1,imgName1);
                               // setTileImage(lastPair2,imgName2);
                                lastPair1.setImage(getBaseContext(), match[0].getEmotion(), imgName1);
                                lastPair2.setImage(getBaseContext(), match[1].getEmotion(), imgName2);

                                lastPair1.setEnabled(false);
                                lastPair2.setEnabled(false);


                                lastEmotion.setText(match[0].getEmotionText());
                                match[0].setVisibility(View.INVISIBLE);
                                match[1].setVisibility(View.INVISIBLE);
                                model.increaseMatchCount(true);
                                matchesView.setText(String.valueOf(model.getMatchesCount())+ " von "+String.valueOf(model.getMaxMatchesCount()));

                                match[0].updateNeighbours();
                                match[1].updateNeighbours();
                                match[0].updateCoveredTiles(true);
                                match[1].updateCoveredTiles(true);

                                for(Tile tile: match[1].getCoveredTiles()){
                                    tile.removeOverlayTile(match[1]);
                                }
                                for(Tile tile: match[0].getCoveredTiles()){
                                    tile.removeOverlayTile(match[0]);
                                }
                                model.updateActiveTiles(match[0], true);
                                model.updateActiveTiles(match[1], true);
                                model.activeTiles.remove(match[0]);
                                model.activeTiles.remove(match[1]);
                                match[0] =null;
                                match[1]=null;
                                if (model.getMatchesCount() == model.getMaxMatchesCount()) {
                                    Toast.makeText(PlayActivity.this, "Level beendet!", Toast.LENGTH_LONG).show();
                                    model.initModel();
                                    model.levelFinished();
                                    if(model.createLevel()){
                                    updateLevelView();
                                    resetLevelAndMatchesInfoView();
                                    }else{
                                        startGameEndActivity();
                                    }

                                }else{
                                    hint = model.getHint();
                                    if (hint==null){
                                        Toast.makeText(PlayActivity.this, R.string.no_possible_turn ,Toast.LENGTH_LONG).show();
                                    }
                                }
                            } else {
                                if(match[0]!= layer[i][j])
                                    match[0].setSelected(false);
                                match[0] = layer[i][j];
                            }
                        }
                    }
                });
            }
        }
    }
    public void initModel() {

        Tile[][] layer0 = new Tile[5][6];
        Tile[][] layer1 = new Tile[4][5];
         Tile[][] layer2 = new Tile[3][4];
         Tile[][] layer3 = new Tile[2][3];
        //tiles 0
        layer0[0][0] =  findViewById(R.id.tile000);
        layer0[0][1] =  findViewById(R.id.tile010);
        layer0[0][2] =  findViewById(R.id.tile020);
        layer0[0][3] =  findViewById(R.id.tile030);
        layer0[0][4] =  findViewById(R.id.tile040);
        layer0[0][5] =  findViewById(R.id.tile050);

        layer0[1][0] =  findViewById(R.id.tile100);
        layer0[1][1] =  findViewById(R.id.tile110);
        layer0[1][2] =  findViewById(R.id.tile120);
        layer0[1][3] =  findViewById(R.id.tile130);
        layer0[1][4] =  findViewById(R.id.tile140);
        layer0[1][5] =  findViewById(R.id.tile150);

        layer0[2][0] =  findViewById(R.id.tile200);
        layer0[2][1] =  findViewById(R.id.tile210);
        layer0[2][2] =  findViewById(R.id.tile220);
        layer0[2][3] =  findViewById(R.id.tile230);
        layer0[2][4] =  findViewById(R.id.tile240);
        layer0[2][5] =  findViewById(R.id.tile250);

        layer0[3][0] =  findViewById(R.id.tile300);
        layer0[3][1] =  findViewById(R.id.tile310);
        layer0[3][2] =  findViewById(R.id.tile320);
        layer0[3][3] =  findViewById(R.id.tile330);
        layer0[3][4] =  findViewById(R.id.tile340);
        layer0[3][5] =  findViewById(R.id.tile350);

        layer0[4][0] =  findViewById(R.id.tile400);
        layer0[4][1] =  findViewById(R.id.tile410);
        layer0[4][2] =  findViewById(R.id.tile420);
        layer0[4][3] =  findViewById(R.id.tile430);
        layer0[4][4] =  findViewById(R.id.tile440);
        layer0[4][5] =  findViewById(R.id.tile450);

        //tiles 1
        layer1[0][0] =  findViewById(R.id.tile001);
        layer1[0][1] =  findViewById(R.id.tile011);
        layer1[0][2] =  findViewById(R.id.tile021);
        layer1[0][3] =  findViewById(R.id.tile031);
        layer1[0][4] =  findViewById(R.id.tile041);

        layer1[1][0] =  findViewById(R.id.tile101);
        layer1[1][1] =  findViewById(R.id.tile111);
        layer1[1][2] =  findViewById(R.id.tile121);
        layer1[1][3] =  findViewById(R.id.tile131);
        layer1[1][4] =  findViewById(R.id.tile141);

        layer1[2][0] =  findViewById(R.id.tile201);
        layer1[2][1] =  findViewById(R.id.tile211);
        layer1[2][2] =  findViewById(R.id.tile221);
        layer1[2][3] =  findViewById(R.id.tile231);
        layer1[2][4] =  findViewById(R.id.tile241);

        layer1[3][0] =  findViewById(R.id.tile301);
        layer1[3][1] =  findViewById(R.id.tile311);
        layer1[3][2] =  findViewById(R.id.tile321);
        layer1[3][3] =  findViewById(R.id.tile331);
        layer1[3][4] =  findViewById(R.id.tile341);

        //tiles 2
        layer2[0][0] =  findViewById(R.id.tile002);
        layer2[0][1] =  findViewById(R.id.tile012);
        layer2[0][2] =  findViewById(R.id.tile022);
        layer2[0][3] =  findViewById(R.id.tile032);

        layer2[1][0] =  findViewById(R.id.tile102);
        layer2[1][1] =  findViewById(R.id.tile112);
        layer2[1][2] =  findViewById(R.id.tile122);
        layer2[1][3] =  findViewById(R.id.tile132);

        layer2[2][0] =  findViewById(R.id.tile202);
        layer2[2][1] =  findViewById(R.id.tile212);
        layer2[2][2] =  findViewById(R.id.tile222);
        layer2[2][3] =  findViewById(R.id.tile232);

        //tiles 3
        layer3[0][0] =  findViewById(R.id.tile003);
        layer3[0][1] =  findViewById(R.id.tile013);
        layer3[0][2] =  findViewById(R.id.tile023);

        layer3[1][0] =  findViewById(R.id.tile103);
        layer3[1][1] =  findViewById(R.id.tile113);
        layer3[1][2] =  findViewById(R.id.tile123);

        model = new GameModel(getBaseContext(),layer0, layer1, layer2, layer3);
        model.loadGameContent();
    }
    public void resetLevelAndMatchesInfoView() {
        lastPair1.setImageResource(android.R.color.transparent);
        lastPair2.setImageResource(android.R.color.transparent);
        lastEmotion.setText("");
        matchesView.setText(String.valueOf(model.getMatchesCount())+ " von " + String.valueOf(model.getMaxMatchesCount()));
    }
    public void updateLevelView(){
        levelView.setText(String.valueOf(model.getLevel()));
    }

    public void startGameEndActivity() {
        Intent intent = new Intent(PlayActivity.this, EndActivity.class);
        startActivity(intent);
    }
}
