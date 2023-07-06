package com.bonicelli.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class PhysicsManager {
    protected final float deltaAngle = 33.185f;
    protected World world;
    protected CollisionManager collisionManager;

    public PhysicsManager() {
        setWorld(new World(new Vector2(0, -100f), true));
        collisionManager = new CollisionManager();
        getWorld().setContactListener(collisionManager);
        getWorld().setContinuousPhysics(true);
    }
    public FixtureDef createFixture (Shape shape, short category) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 0.8f;
        fixtureDef.friction = 0.1f;
        fixtureDef.filter.categoryBits = category;

        return fixtureDef;
    }
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

    public Body createBodyRec(float x, float y, float width, float height, float rotation, short category) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        double diagonal = Math.sqrt(height*height + width*width) / 2;

        x += (float)(Math.cos(Math.toRadians(rotation + deltaAngle)) * diagonal);
        y -= (float)(Math.sin(Math.toRadians(rotation + deltaAngle)) * diagonal);
        bodyDef.position.set(x - 12, y + 6);

        Body body = world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(width / 2f, height / 2f, new Vector2(width / 2f, height / 2f), (float) Math.toRadians(360 - rotation));

        FixtureDef fixtureDef = createFixture(polygonShape, category);

        body.createFixture(fixtureDef);
        polygonShape.dispose();

        return body;
    }

    public void createBodyWall(float x, float y, float width, float height, float rotation, short category) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        bodyDef.position.set(x, y);

        Body body = world.createBody(bodyDef);

        float hw = width / 2f;
        float hh = height / 2f;

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(hw, hh, new Vector2(hw, hh), (float) Math.toRadians(360 - rotation));

        FixtureDef fixtureDef = createFixture(polygonShape, category);

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
