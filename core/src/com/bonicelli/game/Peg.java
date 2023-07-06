package com.bonicelli.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Peg extends Sprite {

    protected Body body;
    protected TextureRegion textureRegion;
    protected String type;

    /**
     * Create an object Hittable, link it with an image
     *
     * @param image to link
     * @param position by Tiled
     * @param rotation by Tiled
     */
    public Peg(Texture image, Vector2 position, float rotation) {
        super(image);
        setPosition(position.x, position.y);
        setRotation(rotation);
        setOrigin(0, getHeight());
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public abstract void setLightTexture();

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
}
