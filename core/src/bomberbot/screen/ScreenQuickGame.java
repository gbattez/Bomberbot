package bomberbot.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import bomberbot.Globals;
import bomberbot.button.ButtonBasic;
import bomberbot.main.Main;

public class ScreenQuickGame extends ScreenBasic
{
    private ButtonBasic backButton;
    private ButtonBasic[] aiButtons = new ButtonBasic[4];
    private ShapeRenderer shapeRenderer;

    public ScreenQuickGame(Main game)
    {
        super(game);
        backButton = new ButtonBasic(Globals.V_WIDTH/2, 120, "Back");
        for(int i = 0; i < aiButtons.length; i++)
        {
            aiButtons[i] = new ButtonBasic(Globals.V_WIDTH/2, Globals.BLOCK_HEIGHT*10 - Globals.BLOCK_HEIGHT*2*i, 400, Globals.BLOCK_HEIGHT, (i+1) + " bots");
        }
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta)
    {
        super.render(delta);
        shapeRenderer.setProjectionMatrix(Globals.camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0.2f, 0.7f, 0.8f, 1));
        shapeRenderer.rect(0, 0, Globals.camera.viewportWidth, Globals.camera.viewportHeight);
        shapeRenderer.end();
        Globals.batch.begin();
        backButton.draw();
        for (ButtonBasic aiButton : aiButtons)
        {
            aiButton.draw();
        }
        Globals.batch.end();
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);
        if(backButton.onClick(gamePort))
        {
            game.setScreen(new ScreenMenu(getGame()));
        }
        for(int i = 0; i < aiButtons.length; i++)
        {
            if(aiButtons[i].onClick(gamePort))
            {
                Globals.music.stop();
                ScreenIngame.isChallengeMode = false;
                game.setScreen(new ScreenIngame(getGame(), (byte)(i + 1)));
            }
        }
    }
}
