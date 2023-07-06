package com.bonicelli.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class ScoreBoard {
    protected Integer orangeCount = 0;
    protected Integer ballsCount = 9;
    protected BitmapFont remainingBall;
    protected BitmapFont orangeHit;
    protected BitmapFont endFont;
    protected Sprite ballImage = new Sprite(new Texture(Gdx.files.internal("image/ballSprite.png")));
    protected Sprite orangeImage = new Sprite(new Texture(Gdx.files.internal("image/orangeBall.png")));
    protected boolean isWin = false;
    protected boolean isLose = false;
    protected boolean isPlayed = true;
    protected GameMap gameMap;

    public ScoreBoard() {
        remainingBall = new BitmapFont();
        orangeHit = new BitmapFont();

        ballImage.setScale(0.9f, 0.9f);
        orangeImage.setScale(0.8f, 0.8f);
    }

    public void draw(Batch batch, Camera camera) {
        ballImage.setPosition(camera.viewportWidth / 4 - 30, camera.viewportHeight - 38);
        ballImage.draw(batch);
        remainingBall.draw(batch, ballsCount.toString(), camera.viewportWidth / 4 - 40,
                camera.viewportHeight - (float) 4 * camera.viewportHeight / 100);

        orangeImage.setPosition(camera.viewportWidth / 4 + 30, camera.viewportHeight - 36);
        orangeImage.draw(batch);
        orangeHit.draw(batch, orangeCount.toString() + "/" + gameMap.numberOrange, camera.viewportWidth / 4 - 5,
                camera.viewportHeight - (float) 4 * camera.viewportHeight / 100);
    }

    public void drawResult(Batch batch, Camera camera) {
        endFont = new BitmapFont();
        String resultString = (isWin) ? "You Won!" : "You Lost";
        String soundString = (isWin) ? "sound/victorySound.mp3" : "sound/defeatSound.mp3";
        playSound(soundString);
        endFont.getData().setScale(4, 4);
        endFont.draw(batch, resultString, camera.viewportWidth / 2f - 120, camera.viewportHeight / 2f + 50);
    }

    public void playSound(String soundString) {
        if(isPlayed) {
            isPlayed = false;
            Sound resultSound = Gdx.audio.newSound(Gdx.files.internal(soundString));
            resultSound.play();
        }
    }

    public boolean checkScore() {
        return isWin || isLose;
    }

    public void score() {
        isWin = (orangeCount == gameMap.numberOrange);
        isLose = (ballsCount == 0);
    }
}
