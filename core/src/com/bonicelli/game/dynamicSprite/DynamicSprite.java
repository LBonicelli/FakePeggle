package com.bonicelli.game.dynamicSprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

/**
 * DynamicSprite
 * represents generically a moving sprite in the map
 */
public abstract class DynamicSprite extends Sprite {
    Circle circle;
    public TextureRegion textureRegion;

    /**
     * Cannon constructor
     *
     * @param image Texture of the Sprite
     * @param cameraWidth X position of the circle
     * @param cameraHeight Y position for the circle
     */
    public DynamicSprite(Texture image, float cameraWidth, float cameraHeight) {
        super(image);
        setCircle(cameraWidth / 2f, cameraHeight - 50, 35);
        setCenter(getCircle().x, getCircle().y - getCircle().radius);
        setOrigin(getWidth() / 2f, getHeight());
    }

    /**
     * Ball constructor
     *
     * @param image Texture of the Sprite
     * @param cannon the ball set its position based on the position of the cannon
     */
    public DynamicSprite(Texture image, Cannon cannon) {
        super(image);
        setPosition(cannon.getX(), cannon.getY());
        setCircle(cannon.getCircle().x, cannon.getCircle().y, cannon.getCircle().radius + cannon.getHeight());
        setOrigin(getWidth() / 2f, getHeight());
    }

    public DynamicSprite(Texture texture) {
        super(texture);
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
        batch.draw(textureRegion, getX(), getY(), this.getOriginX(), this.getOriginY(), textureRegion.getRegionWidth(), textureRegion.getRegionHeight(), 1, 1, 360 - getRotation());
    }

    /**
     * Generates the new position of the cannon, and consequently of the ball
     *
     * @param pointA, first point of the line, represents the cursor position
     * @param pointB, second point of the line, represent the center of the circle
     * @param circle, the circle associated with di DynamicSprite
     *
     * @return the nearest intersection point to the cursor
     */
    public Vector2 getCircleLineIntersectionPoint(Vector2 pointA, Vector2 pointB, Circle circle) {
        float baX = pointB.x - pointA.x;
        float baY = pointB.y - pointA.y;
        float caX = circle.x - pointA.x;
        float caY = circle.y - pointA.y;

        float a = baX * baX + baY * baY;
        float bBy2 = baX * caX + baY * caY;
        float c = caX * caX + caY * caY - circle.radius * circle.radius;

        float pBy2 = bBy2 / a;
        float q = c / a;

        float disc = pBy2 * pBy2 - q;
        if (disc < 0) {
            return Vector2.Zero;
        }

        float tmpSqrt = (float) Math.sqrt(disc);
        float abScalingFactor1 = -pBy2 + tmpSqrt;
        float abScalingFactor2 = -pBy2 - tmpSqrt;

        Vector2 p1 = new Vector2(pointA.x - baX * abScalingFactor1, pointA.y - baY * abScalingFactor1);
        if (disc == 0) {
            return p1;
        }
        Vector2 p2 = new Vector2(pointA.x - baX * abScalingFactor2, pointA.y - baY * abScalingFactor2);

        p1.set(p1.x, Math.min(p1.y, circle.y));
        p2.set(p2.x, Math.min(p2.y, circle.y));

        return (pointA.dst(p1) < pointA.dst(p2)) ? p1 : p2;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(float x, float y, float radius) {
        this.circle = new Circle(x, y, radius);
    }
}
