package com.bonicelli.game.dynamicSprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * a sprite which have to move when the cursor does
 * handle the moment when the ball is shot
 */
public class Cannon extends DynamicSprite {
    final public Sound fire = Gdx.audio.newSound(Gdx.files.internal("sounds/fire.mp3")); //sound made when the left
    // button's pressed
    final public Texture imageFired = new Texture(Gdx.files.internal("images/cannon.png"));
    public Texture imageBall;
    boolean isFired = false;

    public Cannon(Texture imageBall, Camera camera) {
        super(imageBall, camera.viewportWidth, camera.viewportHeight);
        setImageBall(getTexture());
    }

    /**
     * checks if the cannon has fired
     *
     * @return boolean isFired
     */
    public boolean isFired() {
        return isFired;
    }

    /**
     * Handle the moment when the cannon shot
     */
    public void setFiredTrue() {
        //play sound
        fire.play();
        isFired = true;
        //change the image
        setTexture(imageFired);
        setSize(imageFired.getWidth(), imageFired.getHeight());
        setOrigin(getWidth() / 2f, getHeight());
    }

    /**
     * Handle the moment when the cannon is prepared to shot
     */
    public void setFiredFalse() {
        isFired = false;
        //change image
        setTexture(getImageBall());
        setSize(imageBall.getWidth(), imageBall.getHeight());
        setOrigin(getWidth() / 2f, getHeight());
    }

    /**
     * update the cannon position based on the cursor position
     *
     * @param cursor, the cursor position
     */
    public void updatePosition(Vector2 cursor) {
        Vector2 intersection = getCircleLineIntersectionPoint(cursor, new Vector2(getCircle().x, getCircle().y), getCircle());
        float bound = getCircle().y;

        if (!getCircle().contains(cursor) && cursor.y < bound) {
            setCenter(intersection.x, Math.min(intersection.y, bound));

            if (intersection.y < bound) {
                float slope = (float) Math.toDegrees(Math.atan((getCircle().y - cursor.y) / (getCircle().x - cursor.x)));
                setRotation((slope > 0) ? 90 - slope : 270 - slope);
            }
        }
    }

    public Texture getImageBall() {
        return imageBall;
    }

    public void setImageBall(Texture imageBall) {
        this.imageBall = imageBall;
    }
}