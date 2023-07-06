package com.bonicelli.game.dynamicSprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.bonicelli.game.physics.CollisionCategories;
import com.bonicelli.game.physics.PhysicsManager;

/**
 * Ball
 * the most important sprite in the map
 * represent an active ball in game
 */
public class Ball extends DynamicSprite {
    final public float RADIUS = 6.5f; //radius of the ball
    public Cannon cannon;
    public Body body;

    public Ball(Texture image, Cannon cannon) {
        super(image, cannon);
        setCannon(cannon);
    }

    /**
     * Update the position of the ball when the cannon hasn't fired
     *
     * @param cursor, the cursor position
     */
    public void updatePosition(Vector2 cursor) {
        Vector2 intersection = getCircleLineIntersectionPoint(cursor, new Vector2(getCircle().x, getCircle().y), getCircle());
        float bound = getCircle().y;

        if (!getCannon().getCircle().contains(cursor) && cursor.y < bound) {
            setPosition(intersection.x - getTexture().getWidth() / 2f, Math.min(intersection.y, bound) + RADIUS / 2);
        }
    }

    /**
     * Apply the initial impulse to the ball when the left button's pressed
     *
     * @param physicsManager handle the bodies collisions
     */
    public void applyImpulse(PhysicsManager physicsManager) {
        //create the body for the ball
        setBody(physicsManager.createBodyCircle(getX() + getWidth() / 2f, getY() + getHeight() / 2f, RADIUS, CollisionCategories.BALL, BodyDef.BodyType.DynamicBody));
        //set the body's mass
        MassData massData = new MassData();
        massData.mass = 50;
        getBody().setMassData(massData);
        //apply a linear impulse to the ball
        Vector2 direction = new Vector2(getBody().getPosition().x - getCircle().x, getBody().getPosition().y - getCircle().y);
        getBody().applyLinearImpulse(direction.scl(Integer.MAX_VALUE), getBody().getWorldCenter(), true);
    }

    public Cannon getCannon() {
        return cannon;
    }

    public void setCannon(Cannon cannon) {
        this.cannon = cannon;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
