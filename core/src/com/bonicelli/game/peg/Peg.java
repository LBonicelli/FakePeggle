package com.bonicelli.game.peg;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Peg
 * a peg is every static sprite that have to be hit by the active game ball
 * there are circle and rectangle peg, blue and orange
 * every peg is associated with a physic body
 */
public abstract class Peg extends Sprite {

    public Body body;
    public TextureRegion textureRegion;
    public String type; //type of the peg, a peg can be blue or orange

    public Peg(Texture image, Vector2 position, float rotation) {
        super(image);
        setPosition(position.x, position.y);
        setRotation(rotation);
        setOrigin(0, getHeight());
    }

    /**
     * Draw the sprite on the game board
     *
     * @param batch current open batch
     * @param alpha The parent alpha, to be multiplied with this actor's alpha, allowing the parent's alpha to affect all
     * children.
     */
    @Override
    public void draw(Batch batch, float alpha) {
        textureRegion = new TextureRegion(getTexture());

        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), textureRegion.getRegionWidth(), textureRegion.getRegionHeight(), 1, 1, 360 - getRotation());
    }

    /**
     * Set the "light texture" when a body is hit by the active game ball
     */
    public abstract void setLightTexture();

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
