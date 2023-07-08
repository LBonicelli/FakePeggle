package com.bonicelli.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bonicelli.game.dynamicSprite.Ball;
import com.bonicelli.game.dynamicSprite.Basket;
import com.bonicelli.game.dynamicSprite.Cannon;
import com.bonicelli.game.physics.PhysicsManager;
import com.bonicelli.game.sceneUtilities.GameMap;
import com.bonicelli.game.sceneUtilities.ScoreBoard;

/**
 * GameScreen
 * the game's single screen, where everything that happens is displayed
 */
public class GameScreen implements Screen {
    final FakePeggle game;
    final public Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/backgroundMusic.mp3"));
    public OrthographicCamera camera;
    public TiledMap tiledMap;
    public OrthogonalTiledMapRenderer tiledMapRenderer;
    public MapLayer objectLayer;
    public PhysicsManager physicsManager;
    public GameMap gameMap;
    public ScoreBoard scoreBoard;
    public Cannon cannon;

    public Ball ball;
    public Basket basket;
    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;

    public GameScreen(FakePeggle game) {
        this.game = game;
        //set the camera of the world
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 400, 540);

        //import the map from tmx file
        tiledMap = new TmxMapLoader().load("baseMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        objectLayer = tiledMap.getLayers().get("pegs");

        //set the physics manager
        physicsManager = new PhysicsManager();

        //create all the useful item in the game map
        gameMap = new GameMap(objectLayer, physicsManager, camera);
        scoreBoard = new ScoreBoard();

        cannon = new Cannon(new Texture(Gdx.files.internal("image/cannonBall.png")), camera);
        ball = new Ball(new Texture(Gdx.files.internal("image/ballSprite.png")), cannon);
        basket = new Basket();
        gameMap.upperCircle.setCenter(cannon.getCircle().x, cannon.getCircle().y);

        //instance batch and shapeRender for the rendering of the sprite
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        physicsManager.collisionManager.gameMap = this.gameMap;
        physicsManager.collisionManager.scoreBoard = this.scoreBoard;
        scoreBoard.gameMap = this.gameMap;

        //set the background music
        backgroundMusic.setVolume(0.3f);
        backgroundMusic.play();
        backgroundMusic.setLooping(true);
    }

    /**
     * Renders the game world and updates the screen.
     * This method is called continuously by the game loop.
     * it handles the rendering of game objects, animations,
     * and any other visual elements on the screen.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        //update the mouse position
        Vector3 newPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        //update the world on the base of the new position
        update(delta, newPos);
        // begin a new batch and draw all elements in the game screen
        batch.begin();
        gameMap.circlePegs.values().forEach(c -> c.draw(batch, 1));
        gameMap.rectanglePegs.values().forEach(r -> r.draw(batch, 1));
        gameMap.upperCircle.draw(batch);

        cannon.draw(batch, 1);
        //if the cannon has shot, the ball is drawn in the position of the body associated with the ball
        if (cannon.isFired()) {
            //apply a little force every render iteration, giving the ball more speed
            ball.getBody().applyForceToCenter(ball.getBody().getLinearVelocity().x * 1.5f, ball.getBody().getLinearVelocity().y * 1.5f, true);
            ball.setCenter(ball.getBody().getPosition().x, ball.getBody().getPosition().y);
            ball.draw(batch, 1);
        }

        basket.draw(batch, 1);
        scoreBoard.draw(batch, camera);

        batch.end();

        //if the game is over (win or lose)
        if (scoreBoard.checkScore()) {
            endGame();
        }
    }

    /**
     * update the game screen and call pre or after shot method, based on the status
     *
     * @param ignoredDelta The time in seconds since the last render.
     * @param newPos the updated mouse position
     */
    public void update(float ignoredDelta, Vector3 newPos) {
        // clear the screen
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        // tell the camera to update its matrices.
        camera.update();
        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);
        //increase the world's step
        physicsManager.getWorld().step(1 / 60f, 8, 2);
        //unproject of the given new position
        camera.unproject(newPos);

        basket.updatePosition(camera);

        //check that game's not over yet
        if (!scoreBoard.checkScore()) {
            //status: the cannon hasn't shot
            if (!cannon.isFired()) {
                preShot(newPos.x, newPos.y);
            }
            //status: the cannon has shot
            else {
                afterShot();
            }
        }
    }

    /**
     * in pre shot update the position of the cannon based on the cursor position
     * handle the left button click, so the moment of the shot
     *
     * @param cursorX, mouse x position
     * @param cursorY, mouse y position
     */
    private void preShot(float cursorX, float cursorY) {
        cannon.updatePosition(new Vector2(cursorX, cursorY));
        ball.updatePosition(new Vector2(cursorX, cursorY));
        //when the left button's pressed, the cannon shoots
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            cannon.setFiredTrue();
            ball.applyImpulse(physicsManager);
            scoreBoard.ballsCount--;
        }
    }

    /**
     * in the after shot checks if the ball's entered the basket
     * handle the end of the throw and reset to the initial status
     */
    private void afterShot() {
        basket.isGoal(ball);
        //handle the end of a throw (ball in the basket or out of the map)
        if (basket.goalCheck || ball.getY() <= -ball.getHeight()) {
            cannon.setFiredFalse();
            gameMap.endOfThrow(ball, physicsManager);
            //if the ball hit the basket add a ball
            if (basket.goalCheck) {
                scoreBoard.ballsCount++;
                basket.Sound();
            }

            scoreBoard.score();
        }
    }

    /**
     * cover the screen with a matte black layer and draw the final result
     */
    private void endGame() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.5f);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();
        scoreBoard.drawResult(batch, camera);
        batch.end();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    /**
     * Deallocates the resources used by this object.
     * This method should be called when the object is no longer needed
     * to free up system resources and prevent memory leaks
     */
    public void dispose() {
        tiledMap.dispose();
        tiledMapRenderer.dispose();
        batch.dispose();
        physicsManager.getWorld().dispose();
        scoreBoard.remainingBall.dispose();
        scoreBoard.orangeHit.dispose();
        backgroundMusic.dispose();
        shapeRenderer.dispose();
        cannon.getTexture().dispose();
        ball.getTexture().dispose();
        basket.getTexture().dispose();
        gameMap.UPPER_CIRCLE.dispose();
    }
}

