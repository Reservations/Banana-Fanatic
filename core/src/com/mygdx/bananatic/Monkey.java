package com.mygdx.bananatic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Kha on 4/13/2015.
 */
public class Monkey {
    public static final int ASCENDING = 1;
    public static final int DESCENDING = -1;
    public static final int FALLING = -2;
    public static final int STANDING = 0;

    private boolean grounded;

    private Texture monkeyTexture;
    private Sprite monkeySprite;


    private float xVelocity;
    private float yVelocity;
    private float originalVelocity; // used in jump method to reset to after the jump is completed

    private float originalYPosition;

    private int state;

    private int numberOfBananasCollected;

    public Monkey(){
        monkeyTexture = new Texture(Gdx.files.internal("monkey_faceforward.png"));
        monkeySprite = new Sprite(monkeyTexture);
        xVelocity = 450f;
        yVelocity = 900;
        originalVelocity = 900;
        numberOfBananasCollected = 0;
        state = STANDING;
        grounded = true;
    }

    public Monkey(Texture t, int x, int y, float xv, float yv, int bananas){
        monkeySprite = new Sprite(t);
        monkeySprite.setPosition(x, y);
        xVelocity = xv;
        yVelocity = yv;
        originalVelocity = yv;
        numberOfBananasCollected = bananas;
        state = STANDING;
        grounded = true;
    }

    public void addToBananaCounter(int i){
        numberOfBananasCollected+= i;
    }

    public void removeFromBananaCounter(int i){
        numberOfBananasCollected -= i;
    }

    public void setXPosition(float x){
        monkeySprite.setX(x);
    }

    public float getXPosition(){
        return monkeySprite.getX();
    }

    public void setYPosition(float y){
        monkeySprite.setY(y);
    }

    public float getYPosition(){
        return monkeySprite.getY();
    }

    public float getWidth(){
        return monkeySprite.getWidth();
    }

    public float getHeight(){
        return monkeySprite.getHeight();
    }


    public void setPosition(float x, float y){
        monkeySprite.setPosition(x, y);
    }

    public void moveRight() {
        monkeySprite.translateX(450f * Gdx.graphics.getDeltaTime());
    }

    public void moveLeft() {
        monkeySprite.translateX(-450 * Gdx.graphics.getDeltaTime());
    }

    public void moveUp(float f) {
        monkeySprite.translateY(f * Gdx.graphics.getDeltaTime());
    }

    public void moveDown(float f) {
        monkeySprite.translateY(-f * Gdx.graphics.getDeltaTime());
    }




    public float getYVelocity(){
        return yVelocity;
    }

    //return what the monkey is currently doing
    public int returnState(){
        return state;
    }

    public void setState(int s){
        state = s;
    }

    public boolean isAscending(){
       if(state == ASCENDING){
           return true;
       }
       else{
           return false;
       }
    }

    public boolean isDescending(){
        if(state == DESCENDING){
            return true;
        }
        else{
            return false;
        }
    }

    public void ascend(){
        //System.out.println("yVelocity: " + getYVelocity());
        //System.out.println("YPosition =   " + getYPosition());
        yVelocity -= 150; // as the monkey ascends, he will start fast then slow down near the peak of the jump
        setYPosition(getYPosition() + yVelocity * Gdx.graphics.getDeltaTime());
        if(yVelocity <= 0) { // has reached the top of the jump
            yVelocity = 0;
            setState(DESCENDING);
        }

    }

    public void descend(){
        //System.out.println("yVelocity: " + getYVelocity());
        //System.out.println("YPosition =   " + getYPosition());
        yVelocity += 150; // determines how fast the jump takes;
        setYPosition(getYPosition() - yVelocity * Gdx.graphics.getDeltaTime());
        if(yVelocity >= originalVelocity){
            setState(STANDING);
        }

    }

    public boolean isGrounded(){
        return grounded;
    }

    public void setGrounded(boolean b){
        grounded = b;
    }

    public void resetVelocity(){
        yVelocity = originalVelocity;
    }


    public int getNumberOfBananasCollected(){
        return numberOfBananasCollected;
    }

    /**
     * checks if the monkey is overlapping the banana by using cases in which
     * the monkey is not overlapping the banana as this is easier than checking when it is
     *
     * the monkey is overlapping when:
     *  its left edge is NOT to the right edge of the banana,
     *  its right edge is NOT to the left of the banana
     *  its bottom is NOT above the top of the banana,
     *  its top is NOT below the bottom of the banana
     *
     * as an example, if the monkey's left edge is to the right of the banana, then this would
     * cause the if-check to return true (ultimately false) and it would not fit the requirements
     * to be overlapping
     *
     *
     * @param b
     * @return true if any part of the monkeySprite overlaps any part of the banana
     */
    public boolean overlapsBanana(Banana b){
        return !(getXPosition() > b.getXPosition() + b.getWidth() ||
                 getXPosition() + getWidth() < b.getXPosition() ||
                 getYPosition() > b.getYPosition() + b.getHeight() ||
                 getYPosition() + getHeight() < b.getYPosition());

    }

    public boolean overlapsPlatform(Platform p){
        return !(getXPosition() > p.getXPosition() + p.getWidth() ||
                getXPosition() + getWidth() < p.getXPosition() ||
                getYPosition() > p.getYPosition() + p.getHeight() ||
                getYPosition() + getHeight() < p.getYPosition());

    }


    public void draw(SpriteBatch batch){
        batch.draw(monkeySprite, getXPosition(), getYPosition());
    }

    public void drawBananaCount(SpriteBatch batch){
        String stringOfBananasCollected = Integer.toString(numberOfBananasCollected);
        Bananatic.font.draw(batch, stringOfBananasCollected, getXPosition() + getWidth() / 2 - 10, getYPosition() + getHeight() + 10);
    }

    public void dispose(){
        monkeySprite.getTexture().dispose();
    }

    // prevent monkey from going past the edges of the screen by
    // res
    public void keepWithinScreenBounds(){
        if(getXPosition() + getWidth() >= Gdx.graphics.getWidth()){
            setXPosition(Gdx.graphics.getWidth() - getWidth());
        }
        else if(getXPosition() <= 0){
            setXPosition(0);
        }

        if(getYPosition() <= 0){
            setYPosition(0);
            setGrounded(true);
            setState(STANDING);
        }
    }

}
