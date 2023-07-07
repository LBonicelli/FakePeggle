package com.bonicelli.game.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.bonicelli.game.sceneUtilities.GameMap;
import com.bonicelli.game.sceneUtilities.ScoreBoard;
import com.bonicelli.game.peg.Peg;

/**
 * Collision Manager
 * get trace of every collision in the game map between two bodies
 * handle the contact with sound and texture
 * a contact could be only between the active game ball
 */
public class CollisionManager implements ContactListener {
    final public Sound boing = Gdx.audio.newSound(Gdx.files.internal("sound/boing.mp3"));
    public GameMap gameMap;
    public ScoreBoard scoreBoard;

    /**
     * Handle the beginning part of a contact between two bodies
     *
     * @param contact reference
     */
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        Body ballBody;
        Body otherBody;

        if (checkContact(fixtureA, fixtureB)) {
            if (fixtureA.getFilterData().categoryBits == 1) {
                ballBody = fixtureA.getBody();
                otherBody = fixtureB.getBody();
            } else {
                ballBody = fixtureB.getBody();
                otherBody = fixtureA.getBody();
            }

            //apply a force to the active game ball to simulate a bounce
            Vector2 contactDirection = ballBody.getPosition().sub(otherBody.getPosition()).nor();
            ballBody.applyForceToCenter(contactDirection.scl(1000), true);

            //checks if the otherBody is a circle or rectangle peg
            if (otherBody.getFixtureList().first().getFilterData().categoryBits == 2 || otherBody.getFixtureList().first().getFilterData().categoryBits == 3) {
                contactFeatureHandler(otherBody);
            }
        }
    }

    /**
     * Handle some features when a peg has been hit
     * for instance: change the texture, play the boing sound,
     * get trace of every hit peg, especially if its orange
     *
     * @param otherBody the body of the peg that has been hit
     */
    private void contactFeatureHandler(Body otherBody) {
        Peg hitPeg = gameMap.getTotalMap().get(otherBody);
        if (hitPeg != null) {
            //if a body's hit, changes the texture
            hitPeg.setLightTexture();
            //check if it's already been hit
            if (gameMap.hitPeg.get(otherBody) == null) {
                //if it's orange increase the scoreBoard.orangeCount value and play the sound
                if (hitPeg.getType().equals("O")) {
                    scoreBoard.orangeCount++;
                    boing.setPitch(boing.play(), 1.3f);
                } else if (otherBody.getFixtureList().first().getFilterData().categoryBits == 3) {
                    boing.setPitch(boing.play(), 1.2f);
                } else {
                    boing.setPitch(boing.play(), 1.1f);
                }
            }
            //add the hit body in the list
            gameMap.hitPeg.put(otherBody, hitPeg);
        }
    }

    /**
     * checks the nature of the two bodies that made contact
     *
     * @param fixtureA, first fixture's body
     * @param fixtureB, second fixture's body
     *
     * @return boolean value
     */
    public boolean checkContact(Fixture fixtureA, Fixture fixtureB) {
        return fixtureA.getFilterData().categoryBits == 1 && fixtureB.getFilterData().categoryBits == 2 || fixtureB.getFilterData().categoryBits == 1 && fixtureA.getFilterData().categoryBits == 2 || fixtureA.getFilterData().categoryBits == 1 && fixtureB.getFilterData().categoryBits == 3 || fixtureB.getFilterData().categoryBits == 1 && fixtureA.getFilterData().categoryBits == 3 || fixtureA.getFilterData().categoryBits == 1 && fixtureB.getFilterData().categoryBits == 4 || fixtureB.getFilterData().categoryBits == 1 && fixtureA.getFilterData().categoryBits == 4;
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
