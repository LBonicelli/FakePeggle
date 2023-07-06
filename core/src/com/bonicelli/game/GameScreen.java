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
import com.bonicelli.game.dynamicSprite.Cannon;
import com.bonicelli.game.dynamicSprite.Chest;
import com.bonicelli.game.physics.PhysicsManager;

public class GameScreen implements Screen {
    final FakePeggle game;
    final public Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/backgroundMusic.mp3"));
    public OrthographicCamera camera;
    public TiledMap tiledMap;
    public OrthogonalTiledMapRenderer tiledMapRenderer;
    public MapLayer objectLayer;
    public PhysicsManager physicsManager;
    public GameMap gameMap;
    public Cannon cannon;
    public Ball ball;
    public Chest chest;
    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    public ScoreBoard scoreBoard;

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

        cannon = new Cannon(new Texture(Gdx.files.internal("image/cannonBall.png")), camera);
        ball = new Ball(new Texture(Gdx.files.internal("image/ballSprite.png")), cannon);
        chest = new Chest();
        gameMap.upperCircle.setCenter(cannon.getCircle().x, cannon.getCircle().y);
        scoreBoard = new ScoreBoard();

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

    @Override
    public void render(float delta) {
        //update the mouse's position
        Vector3 newPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        //update the world on the base of the new position
        update(delta, newPos);
        // begin a new batch and draw all elements in the game screen
        batch.begin();
        gameMap.circlePegs.values().forEach(c -> c.draw(batch, 1));
        gameMap.rectanglePegs.values().forEach(r -> r.draw(batch, 1));
        gameMap.upperCircle.draw(batch);

        cannon.draw(batch, 1);
        //if the cannon has shot, the ball is drawn at the position of the body
        if (cannon.isFired()) {
            ball.getBody().applyForceToCenter(ball.getBody().getLinearVelocity().x * 2, ball.getBody().getLinearVelocity().y * 1.5f, true);
            ball.setCenter(ball.getBody().getPosition().x, ball.getBody().getPosition().y);
            ball.draw(batch, 1);
        }

        chest.draw(batch, 1);
        scoreBoard.draw(batch, camera);

        batch.end();

        //if the game is over (win or lose)
        if (scoreBoard.checkScore()) {
            endGame();
        }
    }

    public void update(float delta, Vector3 newPos) {
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

        chest.updatePosition(camera);

        //check that game's not over yet
        if (!scoreBoard.checkScore()) {
            //status: the cannon hasn't shot
            if (!cannon.isFired()) {
                cannon.updatePosition(new Vector2(newPos.x, newPos.y));
                ball.updatePosition(new Vector2(newPos.x, newPos.y));
                //when the left button's pressed, the cannon shoots
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    cannon.setFiredTrue();
                    ball.applyImpulse(physicsManager);
                    scoreBoard.ballsCount--;
                }
            }

            //status: the cannon has shot
            if (cannon.isFired()) {
                chest.isGoal(ball);
                //handle the end of a throw
                if (chest.goalCheck || ball.getY() <= -ball.getHeight()) {
                    cannon.setFiredFalse();
                    gameMap.endOfThrow(ball, physicsManager);
                    //if the ball hit the chest it doesn't decrease the balls count
                    if (chest.goalCheck) {
                        scoreBoard.ballsCount++;
                        chest.Sound();
                    }

                    scoreBoard.score();
                }
            }
        }
    }

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

    public void dispose() {
        tiledMap.dispose();
        tiledMapRenderer.dispose();
        batch.dispose();
        physicsManager.getWorld().dispose();
        scoreBoard.remainingBall.dispose();
        scoreBoard.orangeHit.dispose();
        backgroundMusic.dispose();
        shapeRenderer.dispose();
    }
}

