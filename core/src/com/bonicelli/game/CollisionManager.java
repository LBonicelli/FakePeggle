package com.bonicelli.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class CollisionManager implements ContactListener {
    final protected Sound boing = Gdx.audio.newSound(Gdx.files.internal("sound/boing.mp3"));
    protected GameMap gameMap;
    protected ScoreBoard scoreBoard;
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        Body ballBody;
        Body otherBody;

        if (checkContact(fixtureA, fixtureB)) {
            if(fixtureA.getFilterData().categoryBits == 1) {
                ballBody = fixtureA.getBody();
                otherBody = fixtureB.getBody();
            }
            else {
                ballBody = fixtureB.getBody();
                otherBody = fixtureA.getBody();
            }

            Vector2 contactDirection = ballBody.getPosition().sub(otherBody.getPosition()).nor();
            ballBody.applyForceToCenter(contactDirection.scl(1000), true);


            if(otherBody.getFixtureList().first().getFilterData().categoryBits==2 ||
                    otherBody.getFixtureList().first().getFilterData().categoryBits==3) {
                Peg hitPeg = gameMap.getTotalMap().get(otherBody);
                //if a body's hit, changes the texture
                if(hitPeg != null) {
                    hitPeg.setLightTexture();
                    //check if it's already been hitten
                    if (gameMap.hitPeg.get(otherBody) == null) {
                        //if it's orange increase the scoreBoard.orangeCount value and play the sound
                        if (hitPeg.getType().equals("O")) {
                            scoreBoard.orangeCount++;
                            boing.setPitch(boing.play(), 1.3f);
                        } else if (otherBody.getFixtureList().first().getFilterData().categoryBits==3){
                            boing.setPitch(boing.play(), 1.2f);
                        } else {
                            boing.setPitch(boing.play(), 1.1f);
                        }
                    }
                    //add the hit body in the list
                    gameMap.hitPeg.put(otherBody, hitPeg);
                }
            }
        }
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
    public boolean checkContact (Fixture fixtureA, Fixture fixtureB ) {
        return fixtureA.getFilterData().categoryBits == 1 && fixtureB.getFilterData().categoryBits == 2 ||
                fixtureB.getFilterData().categoryBits == 1 && fixtureA.getFilterData().categoryBits == 2 ||
                fixtureA.getFilterData().categoryBits == 1 && fixtureB.getFilterData().categoryBits == 3||
                fixtureB.getFilterData().categoryBits == 1 && fixtureA.getFilterData().categoryBits == 3 ||
                fixtureA.getFilterData().categoryBits == 1 && fixtureB.getFilterData().categoryBits == 4||
                fixtureB.getFilterData().categoryBits == 1 && fixtureA.getFilterData().categoryBits == 4;
    }
}
