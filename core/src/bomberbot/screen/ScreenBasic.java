package bomberbot.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import bomberbot.Globals;
import bomberbot.main.Main;

public abstract class ScreenBasic implements Screen
{
    protected Viewport gamePort;
    protected Main game;

    public Main getGame() {
        return game;
    }

    public ScreenBasic(Main game)
    {
        this.game = game;
        this.gamePort = new FitViewport(Globals.V_WIDTH, Globals.V_HEIGHT, Globals.camera);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0.4f, 0.9f, 1.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        Globals.camera.update();
        Globals.batch.setProjectionMatrix(Globals.camera.combined);
    }

    public void update(float delta)
    {

    }

    @Override
    public void show()
    {

    }

    @Override
    public void resize(int width, int height)
    {
        gamePort.update(width, height);
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {

    }
}