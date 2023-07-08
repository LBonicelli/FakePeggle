package com.bonicelli.game.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * PhysicsManager
 * create the physics world and the physics bodies
 */
public class PhysicsManager {
    public final float deltaAngle = 33.185f;
    public World world;
    public CollisionManager collisionManager;

    public PhysicsManager() {
        //create a new world follow the specifics
        setWorld(new World(new Vector2(0, -104f), true));
        collisionManager = new CollisionManager();
        //set my collisionManager as contact listener of the world
        getWorld().setContactListener(collisionManager);
        getWorld().setContinuousPhysics(true);
    }

    /**
     * create general fixture for every piece in the game board
     *
     * @param shape of the body
     * @param category of the body
     *
     * @return the final fixture to be associated with the body
     */
    public FixtureDef createFixture(Shape shape, short category) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 0.8f;
        fixtureDef.friction = 0.1f;
        fixtureDef.filter.categoryBits = category;

        return fixtureDef;
    }

    /**
     * create the body for circle sprite, so circlePeg and Ball
     *
     * @param x position
     * @param y position
     * @param radius of the ball's sprite
     * @param category of the sprite
     * @param type od the body (Dynamic or Static)
     *
     * @return the final body
     */
    public Body createBodyCircle(float x, float y, float radius, short category, BodyDef.BodyType type) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.position.set(x, y);

        Body body = world.createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);

        FixtureDef fixtureDef = createFixture(circleShape, category);

        body.createFixture(fixtureDef);

        circleShape.dispose();

        return body;
    }

    /**
     * create the body for the rectangle sprite
     *
     * @param x position
     * @param y position
     * @param width of the sprite
     * @param height of the sprite
     * @param rotation of the sprite
     *
     * @return the final body
     */
    public Body createBodyRec(float x, float y, float width, float height, float rotation) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        double diagonal = Math.sqrt(height * height + width * width) / 2;

        x += (float) (Math.cos(Math.toRadians(rotation + deltaAngle)) * diagonal);
        y -= (float) (Math.sin(Math.toRadians(rotation + deltaAngle)) * diagonal);
        bodyDef.position.set(x - 12, y + 6);

        Body body = world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(width / 2f, height / 2f, new Vector2(width / 2f, height / 2f), (float) Math.toRadians(360 - rotation));

        FixtureDef fixtureDef = createFixture(polygonShape, CollisionCategories.RECTANGLE_BODY);

        body.createFixture(fixtureDef);
        polygonShape.dispose();

        return body;
    }

    /**
     * create the body for the walls
     *
     * @param x position
     * @param y position
     * @param width get from the camera dimensions
     * @param height get from the camera dimensions
     * @param rotation always 0
     */
    public void createBodyWall(float x, float y, float width, float height, float rotation) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        bodyDef.position.set(x, y);

        Body body = world.createBody(bodyDef);

        float hw = width / 2f;
        float hh = height / 2f;

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(hw, hh, new Vector2(hw, hh), (float) Math.toRadians(360 - rotation));

        FixtureDef fixtureDef = createFixture(polygonShape, CollisionCategories.WALL);

        body.createFixture(fixtureDef);
        polygonShape.dispose();
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
