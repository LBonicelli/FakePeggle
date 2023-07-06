package com.bonicelli.game.dynamicSprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;

/**
 * Chest
 * sprite located in the bottom area of the map
 * it moves continuously on the X axis
 * handle a "goal" of the ball
 */
public class Chest extends DynamicSprite {
    final public Sound goalSound = Gdx.audio.newSound(Gdx.files.internal("sound/goal.mp3"));
    public boolean directionLeft = false;
    public boolean goalCheck;
    public float x;

    public Chest() {
        super(new Texture(Gdx.files.internal("image/chest.png")));
        setSize(getTexture().getWidth(), getTexture().getHeight());
        setPosition(0, 0);
    }

    /**
     * update the position of the chest
     *
     * @param camera, the position is based on the dimensions of the camera
     */
    public void updatePosition(Camera camera) {
        //set left movement direction
        if (getX() >= camera.viewportWidth - getTexture().getWidth())
            directionLeft = true;
            //set right movement direction
        else if (getX() <= 0)
            directionLeft = false;
        //change x coordinate
        x = (!directionLeft) ? getX() + 100 * Gdx.graphics.getDeltaTime() : getX() - 100 * Gdx.graphics.getDeltaTime();
        setX(x);
    }

    /**
     * checks if the ball is entered in the chest (touch the bounding rectangle of the sprite)
     *
     * @param ball, the ball in game
     */
    public void isGoal(Ball ball) {
        goalCheck = ball.getBoundingRectangle().overlaps(this.getBoundingRectangle());
    }

    /**
     * play the goal sound
     */
    public void Sound() {
        goalSound.play();
    }
}
