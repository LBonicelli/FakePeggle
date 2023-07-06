package com.bonicelli.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

public class CirclePeg extends Peg {
    final protected Texture lightBlueBall = new Texture(Gdx.files.internal("image/lightBlueBall.png"));
    final protected Texture lightOrangeBall = new Texture(Gdx.files.internal("image/lightOrangeBall.png"));

    public CirclePeg(Texture ballImage, Vector2 position, float rotation, PhysicsManager physicsManager) {
        super(ballImage, position, rotation);

        setBody(physicsManager.createBodyCircle(getX() + getWidth() / 2f, getY() + getHeight() / 2f,
                getWidth() / 2f, CollisionCategories.CIRCLE_BODY, BodyDef.BodyType.StaticBody));
    }

    public void setLightTexture() {
        if (getType().equals("B")) {
            setTexture(lightBlueBall);
        } else {
            setTexture(lightOrangeBall);
        }
    }
}
