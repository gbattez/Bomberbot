package bomberbot.button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import bomberbot.Globals;
import bomberbot.entity.EntityPlayer;
import bomberbot.main.Main;
import bomberbot.screen.ScreenBasic;
import bomberbot.screen.ScreenIngame;

/**
 * Created by Guillaume on 07/02/2016.
 */
public class BombButton
{
    public BombButton()
    {
        ScreenIngame.ingameTable.left().bottom();

        Image bombImage = new Image(Globals.getTexture("bombButton"));

        bombImage.setSize(220, 220);
        bombImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                super.clicked(event, x, y);
                if(ScreenIngame.player != null)
                    ScreenIngame.player.dropBomb();
            }
        });
        ScreenIngame.ingameTable.add(bombImage).size(bombImage.getWidth(), bombImage.getHeight()).padLeft(Globals.V_WIDTH - 220).padBottom(120);
        ScreenIngame.ingameStage.addActor(ScreenIngame.ingameTable);
    }

    public void draw(float delta)
    {
        ScreenIngame.ingameStage.act(delta);
        ScreenIngame.ingameStage.draw();
    }
}