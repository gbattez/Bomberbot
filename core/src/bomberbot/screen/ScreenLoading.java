package bomberbot.screen;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

import bomberbot.Globals;
import bomberbot.main.Main;

/**
 * Created by Guillaume on 04/02/2016.
 */
public class ScreenLoading extends ScreenBasic
{
    private ShapeRenderer shapeRenderer;
    private float progress;

    public ScreenLoading(Main game)
    {
        super(game);
        shapeRenderer = new ShapeRenderer();
        this.queueAssets();
        progress = 0;
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);
        progress = MathUtils.lerp(progress, Globals.assets.getProgress(), 0.2f);
        if(Globals.assets.update() && Globals.assets.getProgress() - progress < 0.001f)
        {
            b = true;
            game.setScreen(new ScreenMenu(game));
        }
    }
    boolean b;
    @Override
    public void render(float delta)
    {
        super.render(delta);
        shapeRenderer.setProjectionMatrix(Globals.camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0.2f, 0.7f, 0.8f, 1));
        shapeRenderer.rect(0, 0, Globals.camera.viewportWidth, Globals.camera.viewportHeight);
        shapeRenderer.setColor(Color.ORANGE);
        shapeRenderer.rect(32, Globals.camera.viewportHeight / 2 - 32, (Globals.camera.viewportWidth - 64) * progress, 64);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(32, Globals.camera.viewportHeight / 2 - 32, Globals.camera.viewportWidth - 64, 64);
        shapeRenderer.end();
    }

    @Override
    public void dispose()
    {
        super.dispose();
        this.shapeRenderer.dispose();
    }

    private void queueAssets()
    {
        Globals.assets.load("texture/bomb.png", Texture.class);
        Globals.assets.load("texture/music_on.png", Texture.class);
        Globals.assets.load("texture/music_off.png", Texture.class);
        Globals.assets.load("texture/volume_off.png", Texture.class);
        Globals.assets.load("texture/volume_on.png", Texture.class);
        Globals.assets.load("texture/button_blue.png", Texture.class);
        Globals.assets.load("texture/button_red.png", Texture.class);
        Globals.assets.load("texture/pause_background.png", Texture.class);
        Globals.assets.load("texture/bombskull.png", Texture.class);
        Globals.assets.load("texture/pubombskull.png", Texture.class);
        Globals.assets.load("texture/blockbrick.png", Texture.class);
        Globals.assets.load("texture/blockgrass.png", Texture.class);
        Globals.assets.load("texture/blockmetal.png", Texture.class);
        Globals.assets.load("texture/bombButton.png", Texture.class);
        Globals.assets.load("texture/pukick.png", Texture.class);
        Globals.assets.load("texture/pufire.png", Texture.class);
        Globals.assets.load("texture/pugoldenfire.png", Texture.class);
        Globals.assets.load("texture/puspeed.png", Texture.class);
        Globals.assets.load("texture/firetest.png", Texture.class);
        Globals.assets.load("texture/pubomb.png", Texture.class);
        Globals.assets.load("texture/puchainbomb.png", Texture.class);
        Globals.assets.load("texture/splash.png", Texture.class);
        Globals.assets.load("texture/bbHead.png", Texture.class);
        Globals.assets.load("texture/bbHand.png", Texture.class);
        Globals.assets.load("texture/bbFoot.png", Texture.class);
        Globals.assets.load("texture/joystick.png", Texture.class);
        Globals.assets.load("texture/joystick2.png", Texture.class);
        Globals.assets.load("texture/pauseButton.png", Texture.class);
        Globals.assets.load("texture/score.png", Texture.class);
        Globals.assets.load("texture/timer.png", Texture.class);
        Globals.assets.load("texture/devIcon.png", Texture.class);
        Globals.assets.load("sound/explode.ogg", Sound.class);
        Globals.assets.load("sound/explodeelectric.ogg", Sound.class);
        Globals.assets.load("sound/explodeskull.ogg", Sound.class);
        Globals.assets.load("sound/bombkick.ogg", Sound.class);
        Globals.assets.load("sound/pupickup.ogg", Sound.class);
        Globals.assets.load("sound/music.mp3", Music.class);
        Globals.assets.load("sound/music2.mp3", Music.class);
        Globals.assets.load("sound/musicmenu.mp3", Music.class);
        Globals.assets.load("sound/musicmenu2.mp3", Music.class);
        Globals.assets.load("sound/dropBomb0.ogg", Sound.class);
        Globals.assets.load("sound/dropBomb1.ogg", Sound.class);
        Globals.assets.load("sound/dropBomb2.ogg", Sound.class);
    }
}
