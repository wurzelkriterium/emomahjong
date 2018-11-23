package com.example.anna.mymahjong.app;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anna on 25.05.17.
 */


public class Tile extends android.support.v7.widget.AppCompatImageButton {

    private int actor;
    private String emotion;
    private Tile leftNeighbor;
    private Tile rightNeighbor;
    private List<Tile> overlayTiles = new ArrayList<>();
    private List<Tile> coveredTiles = new ArrayList<>();

    public Tile(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public int getActor() {
        return actor;
    }

    public void setActor(int actor) {
        this.actor = actor;
    }
    public void setEmotion(String emotion){
        this.emotion = emotion;
    }
    public String getEmotion(){
        return emotion;
    }
    public String getEmotionText(){
        if(emotion.equals("ueberrascht"))
            return "überrascht";
        else if (emotion.equals("aergerlich"))
            return "ärgerlich";
        else
            return this.emotion;
    }
    public void setLeftNeighbor(Tile leftNeighbor){
        this.leftNeighbor = leftNeighbor;
    }
    public Tile getLeftNeighbor(){
        return leftNeighbor;
    }
    public void setRightNeighbor(Tile rightNeighbor){
        this.rightNeighbor = rightNeighbor;
    }
    public Tile getRightNeighbor(){
        return rightNeighbor;
    }
    public List<Tile> getCoveredTiles() {
        return coveredTiles;
    }
    public void addCoveredTile(Tile coveredTile) {
        this.coveredTiles.add(coveredTile);
    }

    public void addOverlayTile(Tile overlayTile) {
        this.overlayTiles.add(overlayTile);
    }
    public boolean isCovered() {
        return !overlayTiles.isEmpty();
    }

    public void updateNeighbours() {
        if (leftNeighbor != null) {
            leftNeighbor.setRightNeighbor(null);
            leftNeighbor.setSelected(false);
            leftNeighbor.updateView();
        }
        if (rightNeighbor != null) {
            rightNeighbor.setSelected(false);
            rightNeighbor.setLeftNeighbor(null);
            rightNeighbor.updateView();
        }
    }
    public void updateCoveredTiles(boolean remove){
        for(Tile covered: coveredTiles){
            if(remove){
                covered.removeOverlayTile(this);
            }else{
                covered.addOverlayTile(this);
            }
            covered.updateView();
        }
    }
    public void updateView(){
        if(this.isCovered()){
            this.setImageAlpha(125);
            this.setEnabled(false);
            this.setSelected(false);
        }else{
            if(leftNeighbor!=null && rightNeighbor!=null){
                this.setImageAlpha(125);
                this.setEnabled(false);
                this.setSelected(false);
            }else{
                this.setImageAlpha(255);
                this.setEnabled(true);
            }
        }
    }

    public void setImage(Context c, String folder, String imgName){
        try {
            InputStream ims = c.getAssets().open("img/"+folder+"/"+imgName+".png");
            Drawable d = Drawable.createFromStream(ims, null);
            this.setImageDrawable(d);
        }
        catch(IOException ex) {
            ex.printStackTrace();
            return;
        }
    }

    public void removeOverlayTile(Tile tile) {
        overlayTiles.remove(tile);
    }

    public void putBack() {
        this.setVisibility(View.VISIBLE);
        this.setSelected(false);
        this.updateCoveredTiles(false);
        this.setEnabled(true);

        if(leftNeighbor !=null){
            leftNeighbor.setRightNeighbor(this);
            leftNeighbor.updateView();
        }
        if(rightNeighbor !=null){
            rightNeighbor.setLeftNeighbor(this);
            rightNeighbor.updateView();
        }
    }
    public void setValues(Context c, String emotion, int actorNr1, String imgName1) {
        this.setEmotion(emotion);
        this.setActor(actorNr1);
        this.setImage(c, emotion, imgName1);
    }

    public boolean matches(Tile tile){
        return (this.getEmotion()==tile.getEmotion());
    }
    public void enableNeighbours() {
        if (leftNeighbor != null) {
            leftNeighbor.setRightNeighbor(null);
            leftNeighbor.setEnabled(true);
        }
        if (rightNeighbor != null) {
            rightNeighbor.setLeftNeighbor(null);
            rightNeighbor.setEnabled(true);
        }
    }

}
