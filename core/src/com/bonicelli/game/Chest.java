package com.bonicelli.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Chest extends Sprite {
    final protected Sound goalSound = Gdx.audio.newSound(Gdx.files.internal("sound/goal.mp3"));
    protected boolean directionLeft = false;
    protected boolean goalCheck;
    protected float x;

    public Chest() {
        super(new Texture(Gdx.files.internal("image/chest.png")));
        setSize(getTexture().getWidth(), getTexture().getHeight());
        setPosition(0, 0);
    }

    public void updatePosition(Camera camera) {
        if (getX() >= camera.viewportWidth - getTexture().getWidth())
            directionLeft = true;
        else if (getX() <= 0)
            directionLeft = false;

        x = (!directionLeft) ? getX() + 100 * Gdx.graphics.getDeltaTime() : getX() - 100 * Gdx.graphics.getDeltaTime();
        setX(x);
    }

    public void isGoal(Ball ball) {
        goalCheck = ball.getBoundingRectangle().overlaps(this.getBoundingRectangle());
    }

    public void Sound() {
        goalSound.play();
    }
}
