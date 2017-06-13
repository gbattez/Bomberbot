package bomberbot.button;

import com.badlogic.gdx.Gdx;

import bomberbot.Globals;
import bomberbot.screen.ScreenIngame;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.Color;

import java.awt.event.MouseListener;

/**
 * Created by Guillaume on 18/02/2016.
 */
public class ButtonBasic
{
    private float pX, pY;
    private float width, height;
    private String textureSrc;
    private GlyphLayout layout;
    Vector2 newPoints;
    String desc;
    private boolean isButtonPressed;
    private boolean isActive;

    public void setpY(float pY)
    {
        this.pY = pY;
    }

    public float getHeight()
    {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public ButtonBasic(float pX, float pY)
    {
        this.textureSrc = "button_blue";
        this.desc = "";
        width = 440;
        height = 115;
        this.pX = pX - width/2;
        this.pY = pY - height/2;
        layout = new GlyphLayout();
        layout.setText(ScreenIngame.font, desc);
    }

    public ButtonBasic(float pX, float pY, String desc)
    {
        this.textureSrc = "button_blue";
        this.desc = desc;
        width = 440;
        height = 115;
        this.pX = pX - width/2;
        this.pY = pY - height/2;
        layout = new GlyphLayout();
        layout.setText(ScreenIngame.font, desc);
    }

    public ButtonBasic(float pX, float pY, float width, float height)
    {
        this.pX = pX - width/2;
        this.pY = pY - width/2;
        this.textureSrc = "button_blue";
        this.desc = "";
        this.width = width;
        this.height = height;
        layout = new GlyphLayout();
        layout.setText(ScreenIngame.font, desc);
    }

    public ButtonBasic(float pX, float pY, float width, float height, String desc)
    {
        this.pX = pX - width/2;
        this.pY = pY - height/2;
        this.textureSrc = "button_blue";
        this.desc = desc;
        this.width = width;
        this.height = height;
        layout = new GlyphLayout();
        layout.setText(ScreenIngame.font, desc);
    }

    public void setTextureSrc(String textureSrc, boolean useTextureSize)
    {
        this.textureSrc = textureSrc;
        if(useTextureSize)
        {
            this.width = Globals.getTexture(textureSrc).getWidth();
            this.height = Globals.getTexture(textureSrc).getHeight();
        }
    }

    public boolean mouseHovered(Viewport viewport)
    {
        if(!isActive)
            return false;

        newPoints = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        newPoints = viewport.unproject(newPoints);
        return newPoints.x >= pX && newPoints.x <= pX + width && newPoints.y >= pY && newPoints.y <= pY + height;
    }

    public void setWidth(float width)
    {
        this.width = width;
    }

    public void setHeight(float height)
    {
        this.height = height;
    }

    public boolean mouseClicked(Viewport viewport)
    {
        return mouseHovered(viewport) && Gdx.input.justTouched();
    }

    public boolean onClick(Viewport viewport)
    {
        if(this.mouseHovered(viewport) && Gdx.input.justTouched())
        {
            this.isButtonPressed = true;
        }
        if(this.isButtonPressed && this.mouseHovered(viewport) && !Gdx.input.isTouched(0))
        {
            this.isButtonPressed = false;
            return true;
        }
        isActive = false;
        return false;
    }

    public void draw()
    {
        isActive = true;
        Globals.batch.setColor(this.isButtonPressed ? Color.LIGHT_GRAY : Color.WHITE);
        Globals.draw(this.textureSrc, pX, pY, width, height);
        Globals.batch.setColor(Color.WHITE);
        layout.setText(ScreenIngame.font, desc);
        if(layout != null)
        {
            float textSizeX = layout.width;
            float textSizeY = layout.height;

            ScreenIngame.font.draw(Globals.batch, desc, pX + width / 2 - textSizeX / 2, pY + height / 2 + textSizeY / 2);
        }
    }
}