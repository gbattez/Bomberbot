package bomberbot.screen;

import bomberbot.Globals;
import bomberbot.button.ButtonBasic;
import bomberbot.main.Main;

public class ScreenMenu extends ScreenBasic
{
    private ButtonBasic buttonChallenge;
    private ButtonBasic buttonQuickGame;
    private ButtonBasic buttonHighscores;
    private ButtonBasic buttonExit;
    private ButtonBasic buttonCredit;

    public ScreenMenu(Main game)
    {
        super(game);
        Globals.music = Globals.getMusic("musicmenu2");
        Globals.music.setVolume(0.4f);
        Globals.music.setLooping(true);
        //Globals.music.play();

        //On initialise les bouttons
        ScreenIngame.font.getData().setScale(3);
        buttonChallenge = new ButtonBasic(Globals.V_WIDTH/2, Globals.BLOCK_HEIGHT*10, 400, Globals.BLOCK_HEIGHT, "Challenge");
        buttonQuickGame = new ButtonBasic(Globals.V_WIDTH/2, Globals.BLOCK_HEIGHT*8f, 400, Globals.BLOCK_HEIGHT, "Quick Game");
        buttonHighscores = new ButtonBasic(Globals.V_WIDTH/2, Globals.BLOCK_HEIGHT*6f, 400, Globals.BLOCK_HEIGHT, "Highscores");
        buttonExit = new ButtonBasic(Globals.V_WIDTH/2, Globals.BLOCK_HEIGHT*4f, 400, Globals.BLOCK_HEIGHT, "Exit");
        buttonCredit = new ButtonBasic(Globals.V_WIDTH/2, Globals.BLOCK_HEIGHT*2f, 400, Globals.BLOCK_HEIGHT, "Credits");
    }

    @Override
    public void render(float delta)
    {
        super.render(delta);
        Globals.batch.begin();

        Globals.draw("splash", 0, 0, Globals.camera.viewportWidth, Globals.camera.viewportHeight);
        buttonChallenge.draw();
        buttonQuickGame.draw();
        buttonHighscores.draw();
        buttonExit.draw();
        buttonCredit.draw();
        Globals.batch.end();
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);
        if(buttonChallenge.onClick(gamePort))
        {
            Globals.music.stop();
            ScreenIngame.isChallengeMode = true;
            game.setScreen(new ScreenIngame(getGame()));
        }

        if(buttonQuickGame.onClick(gamePort))
        {
            game.setScreen(new ScreenQuickGame(getGame()));
        }

        if(buttonHighscores.onClick(gamePort))
        {
            game.setScreen(new ScreenHighscore(getGame()));
        }

        if(buttonExit.onClick(gamePort))
        {
            System.exit(0);
        }

        if(buttonCredit.onClick(gamePort))
        {
            game.setScreen(new ScreenCredit(getGame()));
        }
    }
}
