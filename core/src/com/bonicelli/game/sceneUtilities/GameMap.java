package com.bonicelli.game.sceneUtilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.bonicelli.game.dynamicSprite.Ball;
import com.bonicelli.game.peg.CirclePeg;
import com.bonicelli.game.peg.Peg;
import com.bonicelli.game.peg.RectanglePeg;
import com.bonicelli.game.physics.PhysicsManager;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * generate the game map(so the disposition of the pegs) based on the map passed by Tiled
 * moreover handle the random position of the orange pieces
 */
public class GameMap {
    public final float ORANGE_PERCENTAGE = 0.4f; /* percentage of orange pegs in the map */
    public final Texture BLUE_BALL = new Texture(Gdx.files.internal("images/blueBall.png"));
    public final Texture BLUE_REC = new Texture(Gdx.files.internal("images/blueRec.png"));
    public final Texture OR_BALL = new Texture(Gdx.files.internal("images/orangeBall.png"));
    public final Texture O_REC = new Texture(Gdx.files.internal("images/orangeRec.png"));
    public final Texture UPPER_CIRCLE = new Texture(Gdx.files.internal("images/circleImage.png"));
    public Sprite upperCircle;
    public HashMap<Body, CirclePeg> circlePegs;
    public HashMap<Body, RectanglePeg> rectanglePegs;
    public HashMap<Body, Peg> hitPeg;
    public int numberOrange; /* exact integer number of orange Pegs */

    /**
     * Create the map with the appropriate Design and Texture
     *
     * @param objectLayer the layer with the object create in Tiled
     * @param physicsManager for the creation of the body for each block
     * @param camera needed for the creation of the wall
     */
    public GameMap(MapLayer objectLayer, PhysicsManager physicsManager, Camera camera) {
        circlePegs = new HashMap<>();
        rectanglePegs = new HashMap<>();
        hitPeg = new HashMap<>();

        for (MapObject object : objectLayer.getObjects()) {
            float x = object.getProperties().get("x", float.class);
            float y = object.getProperties().get("y", float.class);
            float rotation = object.getProperties().get("rotation", 0.00f, float.class);

            if (object.getProperties().get("type").equals("CirclePeg")) {
                CirclePeg circlePeg = new CirclePeg(BLUE_BALL, new Vector2(x, y), rotation, physicsManager);
                circlePegs.put(circlePeg.getBody(), circlePeg);
            } else if (object.getProperties().get("type").equals("RectanglePeg")) {
                RectanglePeg rectanglePeg = new RectanglePeg(BLUE_REC, new Vector2(x, y), rotation, physicsManager);
                rectanglePegs.put(rectanglePeg.getBody(), rectanglePeg);
            }
        }
        //set the type of the pegs
        circlePegs.forEach((b, c) -> c.setType("B"));
        rectanglePegs.forEach((b, r) -> r.setType("B"));

        setOrangePeg(circlePegs, rectanglePegs);

        upperCircle = new Sprite(UPPER_CIRCLE);
        //creation of the right and left wall bodies
        physicsManager.createBodyWall(0, 0, 1, camera.viewportHeight, 0);
        physicsManager.createBodyWall(camera.viewportWidth, 0, 1, camera.viewportHeight, 0);
    }

    /**
     * Set the orange Peg randomly in the map,considering all the Peg without distinction
     *
     * @param circlePegs list of circle Pegs
     * @param rectanglePegs list of rectangle Pegs
     * @param <T> generic type of Peg, circle or rectangle
     */
    public <T extends Peg> void setOrangePeg(Map<Body, ? extends T> circlePegs, Map<Body, ? extends T> rectanglePegs) {
        List<T> totalPeg = Stream.concat(circlePegs.values().stream(), rectanglePegs.values().stream()).collect(Collectors.toList());
        Collections.shuffle(totalPeg);
        numberOrange = (int) (ORANGE_PERCENTAGE * totalPeg.size());
        totalPeg.stream().limit(numberOrange).forEach(p -> p.setType("O"));
        totalPeg.stream().limit(numberOrange).filter(p -> p.getTexture() == BLUE_BALL).forEach(p -> p.setTexture(OR_BALL));
        totalPeg.stream().limit(numberOrange).filter(p -> p.getTexture() == BLUE_REC).forEach(p -> p.setTexture(O_REC));
    }

    /**
     * create a map with all the pegs in the map
     * use in the collision manager
     *
     * @return the total map
     */
    public Map<Body, Peg> getTotalMap() {
        HashMap<Body, Peg> totalMap = new HashMap<>(circlePegs);
        totalMap.putAll(rectanglePegs);
        return totalMap;
    }

    /**
     * handle the operations at the end of each throw
     * destroy all the bodies that have been hitten and the active ball body, move the sprites
     *
     * @param ball the active game ball
     * @param physicsManager current physics manage who contain the World instance
     */
    public void endOfThrow(Ball ball, PhysicsManager physicsManager) {
        //destroy all the useless bodies (hit and the current ball)
        //move all the useless sprites
        physicsManager.getWorld().destroyBody(ball.getBody());
        hitPeg.forEach((b, p) -> {
            physicsManager.getWorld().destroyBody(b);
            p.setPosition(-100, -100);
            p.getTexture().dispose();
        });
        //clear the list of hit peg
        hitPeg.clear();
    }
}
