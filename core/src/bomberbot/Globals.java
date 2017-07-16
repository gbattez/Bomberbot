package bomberbot;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import bomberbot.net.Score;
import bomberbot.net.User;

public class Globals
{
    public static boolean soundEnabled = true;
    public static boolean musicEnabled = true;
    public static Music music = null;
    public static OrthographicCamera camera = new OrthographicCamera();
    public static AssetManager assets;
    public static SpriteBatch batch;
    public static final int V_WIDTH = 1920;
    public static final int V_HEIGHT = 1080;
    public static final int BLOCK_WIDTH = 96;
    public static final int BLOCK_HEIGHT = 98;
    public static User user;
    public static Score scoreToUpload;

    public static Texture getTexture(String name)
    {
        try
        {
            return assets.get("texture/" + name + ".png", Texture.class);
        } catch (Exception e)
        {
            return assets.get("texture/noTexture.png", Texture.class);
        }
    }

    public static void draw(String name, float pX, float pY)
    {
        Globals.batch.draw(getTexture(name), pX, pY);
    }

    public static void drawCentered(String name, float pX, float pY, float width, float height)
    {
        Globals.batch.draw(getTexture(name), pX - width/2, pY - height/2, width, height);
    }

    public static void draw(String name, float pX, float pY, float width, float height)
    {
        Globals.batch.draw(getTexture(name), pX, pY, width, height);
    }

    public static Music getMusic(String name)
    {
        return assets.get("sound/" + name + ".mp3", Music.class);
    }

    public static void playSound(String name, float volume, float pitch)
    {
        if(Globals.soundEnabled)
        {
            assets.get("sound/" + name + ".ogg", Sound.class).play(volume, pitch, 1);
        }
    }
}