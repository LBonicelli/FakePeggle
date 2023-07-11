package com.bonicelli.game.sceneUtilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * scoreboard in the top left that keeps track of the remaining ball and hit orange pegs
 * moreover checks if the player has won or lost
 */
public class ScoreBoard {
    public Integer orangeCount = 0; //number of hit orange pegs
    public Integer ballsCount = 9; //remaining game balls
    public BitmapFont remainingBall;
    public BitmapFont orangeHit;
    public BitmapFont endFont;
    public Sprite ballImage = new Sprite(new Texture(Gdx.files.internal("images/ballSprite.png")));
    public Sprite orangeImage = new Sprite(new Texture(Gdx.files.internal("images/orangeBall.png")));
    public boolean isWin = false;
    public boolean isLose = false;
    public boolean isPlayed = true; //for the final soundtrack
    public GameMap gameMap;

    public ScoreBoard() {
        remainingBall = new BitmapFont();
        orangeHit = new BitmapFont();

        ballImage.setScale(0.9f, 0.9f);
        orangeImage.setScale(0.8f, 0.8f);
    }

    /**
     * Draw the current results of the active game
     *
     * @param batch, current open batch
     * @param camera, taking its dimensions
     */

    public void draw(Batch batch, Camera camera) {
        //scoreboard of the remaining balls (draws the font and the sprite)
        ballImage.setPosition(camera.viewportWidth / 4 - 30, camera.viewportHeight - 38);
        ballImage.draw(batch);
        remainingBall.draw(batch, ballsCount.toString(), camera.viewportWidth / 4 - 40, camera.viewportHeight - (float) 4 * camera.viewportHeight / 100);
        //scoreboard of the hit orange pegs (draws the font and the sprite)
        orangeImage.setPosition(camera.viewportWidth / 4 + 30, camera.viewportHeight - 36);
        orangeImage.draw(batch);
        orangeHit.draw(batch, orangeCount.toString() + "/" + gameMap.numberOrange, camera.viewportWidth / 4 - 5, camera.viewportHeight - (float) 4 * camera.viewportHeight / 100);
    }

    /**
     * draw the final result, so font Win or Lose and the associated soundtrack
     *
     * @param batch, current open batch
     * @param camera, taking its dimensions
     */
    public void drawResult(Batch batch, Camera camera) {
        endFont = new BitmapFont();
        String resultString = (isWin) ? "You Won!" : "You Lost";
        String soundString = (isWin) ? "sounds/victorySound.mp3" : "sounds/defeatSound.mp3";
        playSound(soundString);
        endFont.getData().setScale(4, 4);
        endFont.draw(batch, resultString, camera.viewportWidth / 2f - 120, camera.viewportHeight / 2f + 50);
    }

    /**
     * Play the right sound at the end of the game
     *
     * @param soundString, name of the file to play
     */
    public void playSound(String soundString) {
        if (isPlayed) {
            isPlayed = false;
            Sound resultSound = Gdx.audio.newSound(Gdx.files.internal(soundString));
            resultSound.play();
        }
    }

    /**
     * update isWin and is Lose flag at the end of each throw
     * the game is over when the hit orange peg is equals to the total number of orange pegs
     * or when the number of remaining ball it's zero@
     */
    public void score() {
        isWin = (orangeCount == gameMap.numberOrange);
        isLose = (ballsCount == 0);
    }

    /**
     * checks if the game is over
     *
     * @return true if the game is over (win or lose)
     */
    public boolean checkScore() {
        return isWin || isLose;
    }
}
