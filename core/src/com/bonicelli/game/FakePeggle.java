package com.bonicelli.game;

import com.badlogic.gdx.Game;

public class FakePeggle extends Game {
    @Override
    public void create() {
        this.setScreen(new GameScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
    }
}
