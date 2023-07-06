package com.bonicelli.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Cannon extends DynamicSprite {
    final protected Sound fire = Gdx.audio.newSound(Gdx.files.internal("sound/fire.mp3"));
    final protected Texture imageFired = new Texture(Gdx.files.internal("image/cannon.png"));
    protected  Texture imageBall;
    boolean isFired = false;

    public Cannon(Texture imageBall, Camera camera) {
        super(imageBall, camera.viewportWidth, camera.viewportHeight);
        setImageBall(getTexture());
    }
    public boolean isFired() {
        return isFired;
    }
    public void setFiredTrue() {
        fire.play();
        isFired = true;
        setTexture(imageFired);
        setSize(imageFired.getWidth(), imageFired.getHeight());
        setOrigin(getWidth() / 2f, getHeight());
    }
    public void setFiredFalse() {
        isFired = false;
        setTexture(getImageBall());
        setSize(imageBall.getWidth(), imageBall.getHeight());
        setOrigin(getWidth() / 2f, getHeight());
    }

    public Texture getImageBall() {
        return imageBall;
    }

    public void setImageBall(Texture imageBall) {
        this.imageBall = imageBall;
    }

    public void updatePosition(Vector2 lineStart) {
        Vector2 intersection = genPos(lineStart);
        float bound = getCircle().y;

        if(!getCircle().contains(lineStart) && lineStart.y < bound) {
            setCenter(intersection.x, Math.min(intersection.y ,bound));

        if(intersection.y < bound) {
            float slope = (float) Math.toDegrees(Math.atan((getCircle().y - lineStart.y) / (getCircle().x - lineStart.x)));
            setRotation((slope > 0) ? 90 - slope : 270 - slope);
        }}
    }
}