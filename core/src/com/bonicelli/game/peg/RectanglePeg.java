package com.bonicelli.game.peg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.bonicelli.game.physics.PhysicsManager;

/**
 * static sprite which represent a rectangle peg
 */
public class RectanglePeg extends Peg {
    final public Texture lightBlueRec = new Texture(Gdx.files.internal("images/lightBlueRec.png"));
    final public Texture lightOrangeRec = new Texture(Gdx.files.internal("images/lightOrangeRec.png"));

    public RectanglePeg(Texture rectangleImage, Vector2 position, float rotation, PhysicsManager physicsManager) {
        super(rectangleImage, position, rotation);

        setBody(physicsManager.createBodyRec(getX(), getY(), getWidth(), getHeight(), getRotation()));
    }

    public void setLightTexture() {
        if (getType().equals("B")) {
            setTexture(lightBlueRec);
        } else {
            setTexture(lightOrangeRec);
        }
    }
}
