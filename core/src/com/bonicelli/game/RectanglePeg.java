package com.bonicelli.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class RectanglePeg extends Peg {
    final protected Texture lightBlueRec = new Texture(Gdx.files.internal("image/lightBlueRec.png"));
    final protected Texture lightOrangeRec = new Texture(Gdx.files.internal("image/lightOrangeRec.png"));

    public RectanglePeg(Texture rectangleImage, Vector2 position, float rotation, PhysicsManager physicsManager) {
        super(rectangleImage, position, rotation);

        setBody(physicsManager.createBodyRec(getX(), getY(), getWidth(), getHeight(), getRotation(), CollisionCategories.RECTANGLE_BODY));
    }

    public void setLightTexture() {
        if (getType().equals("B")) {
            setTexture(lightBlueRec);
        } else {
            setTexture(lightOrangeRec);
        }
    }
}
