package com.bonicelli.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.MassData;

public class Ball extends DynamicSprite {
    final protected float RADIUS = 6.5f;
    protected Cannon cannon;
    protected Body body;
    public Ball(Texture image, Cannon cannon) {
        super(image, cannon);
        setCannon(cannon);
    }
    public void updatePosition(Vector2 lineStart) {
        Vector2 intersection = genPos(lineStart);
        float bound = getCircle().y;

        if(!getCannon().getCircle().contains(lineStart) && lineStart.y < bound) {
            setPosition(intersection.x - getTexture().getWidth() / 2f,
                    Math.min(intersection.y,bound) + RADIUS / 2);
        }
    }

    public void applyImpulse(PhysicsManager physicsManager) {
        setBody(physicsManager.createBodyCircle(getX() + getWidth() / 2f, getY() + getHeight() / 2f, RADIUS, CollisionCategories.BALL, BodyDef.BodyType.DynamicBody));
        MassData massData = new MassData();
        massData.mass = 50;
        getBody().setMassData(massData);
        //apply an impulse to the ball
        Vector2 direction = new Vector2(getBody().getPosition().x - getCircle().x,
                getBody().getPosition().y - getCircle().y);
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
