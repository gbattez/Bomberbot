package bomberbot.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import bomberbot.Globals;
import bomberbot.screen.ScreenIngame;
import bomberbot.screen.ScreenLoading;

public class Main extends Game
{
    private boolean showControls;

    public Main(boolean showControls)
    {
        this.showControls = showControls;
    }

    @Override
    public void create()
    {
        Globals.assets = new AssetManager();
        Globals.batch = new SpriteBatch();
        Globals.camera.translate(Globals.V_WIDTH/2, Globals.V_HEIGHT/2);
        setScreen(new ScreenLoading(this));
        ScreenIngame.showControls = this.showControls;
    }

    @Override
    public void dispose()
    {
        Globals.batch.dispose();
        Globals.assets.dispose();
        super.dispose();
    }
}