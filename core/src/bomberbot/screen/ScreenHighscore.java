package bomberbot.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import bomberbot.Globals;
import bomberbot.button.ButtonBasic;
import bomberbot.main.Main;
import bomberbot.net.Connexion;

public class ScreenHighscore extends ScreenBasic
{
    private final int LINESPACEING = 55;
    private ButtonBasic backButton;
    private ShapeRenderer shapeRenderer;
    private String rank = "Rank", name = "Name", score = "Score";
    private GlyphLayout rankLayout, nameLayout, scoreLayout;
    private String htmlPageSrc;
    private String[][] highscoreArray = new String[3][10];
    private GlyphLayout[][] layoutArray = new GlyphLayout[3][10];
    private String[] data;
    private boolean finishedFetching;

    public ScreenHighscore(Main game)
    {
        super(game);
        this.shapeRenderer = new ShapeRenderer();
        ScreenIngame.font.getData().setScale(3);
        backButton = new ButtonBasic(Globals.V_WIDTH/2, 120, "Back");
        rankLayout = new GlyphLayout();
        nameLayout = new GlyphLayout();
        scoreLayout = new GlyphLayout();
        rankLayout.setText(ScreenIngame.font, rank);
        nameLayout.setText(ScreenIngame.font, name);
        scoreLayout.setText(ScreenIngame.font, score);

        Thread thread = new Thread(new Runnable()
        {

            public void run()
            {
                htmlPageSrc = null;
                try
                {
                    htmlPageSrc = Connexion.doHttpUrlConnectionAction();
                    String[] line = htmlPageSrc.split("<br/>");
                    for(int i = 0; i < line.length - 1; i++)
                    {
                        data = line[i].split(" ");
                        for(int j = 0; j < data.length; j++)
                        {
                            highscoreArray[j][i] = data[j];

                            layoutArray[j][i] = new GlyphLayout();
                            layoutArray[j][i].setText(ScreenIngame.font, data[0]);
                        }
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                for(int i = 0; i < 3; i++)
                {
                    for(int j = 0; j < 10; j++)
                    {
                        System.out.println(i + j +highscoreArray[i][j]);
                    }
                }
                finishedFetching = true;
                //fillScoreArray();
            }
        });
        thread.start();
    }

    public void drawHighscores()
    {
        float pX = 0;
        for(int i = 0; i < 3 ; i++)
        {
            for (int j = 0; j < 10; j++)
            {
                if(highscoreArray[i][j] == null)
                {
                    continue;
                }

                if (i == 0)
                    pX = Globals.V_WIDTH / 2 - layoutArray[i][j].width / 2 - Globals.V_WIDTH / 4;
                if (i == 1)
                    pX = Globals.V_WIDTH / 2 - layoutArray[i][j].width / 2;
                if (i == 2)
                    pX = Globals.V_WIDTH / 2 - layoutArray[i][j].width / 2 + Globals.V_WIDTH / 4;

                if(this.highscoreArray[i][j].equals("Ironfists") || this.highscoreArray[i][j].equals("Draenir") || this.highscoreArray[i][j].equals("Hjolfrin") || this.highscoreArray[i][j].equals("Ashmore"))
                {
                    Globals.draw("devIcon", pX - 62, Globals.V_HEIGHT - LINESPACEING * 4 - (LINESPACEING * j) - 30, 60, 35);
                    ScreenIngame.font.draw(Globals.batch, this.highscoreArray[i][j], pX, Globals.V_HEIGHT - LINESPACEING * 4 - (LINESPACEING * j));
                } else
                {
                    ScreenIngame.font.draw(Globals.batch, this.highscoreArray[i][j], pX - 50, Globals.V_HEIGHT - LINESPACEING * 4 - (LINESPACEING * j));
                }
             }
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        shapeRenderer.setProjectionMatrix(Globals.camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0.2f, 0.7f, 0.8f, 1));
        shapeRenderer.rect(0, 0, Globals.camera.viewportWidth, Globals.camera.viewportHeight);
        shapeRenderer.end();

        Globals.batch.begin();

        ScreenIngame.font.draw(Globals.batch, rank, Globals.V_WIDTH / 2 - rankLayout.width / 2 - Globals.V_WIDTH / 4, Globals.V_HEIGHT - LINESPACEING * 2);
        ScreenIngame.font.draw(Globals.batch, name, Globals.V_WIDTH / 2 - nameLayout.width / 2, Globals.V_HEIGHT - LINESPACEING * 2);
        ScreenIngame.font.draw(Globals.batch, score, Globals.V_WIDTH / 2 - scoreLayout.width / 2 + Globals.V_WIDTH / 4, Globals.V_HEIGHT - LINESPACEING * 2);

        if(finishedFetching)
        {
            drawHighscores();
        }

        //BDD
        //  System.out.print(co.selectAllHighscores().get(1));
        /*for(String score : co.selectAllHighscores())
        {
            System.out.println(score);
            ScreenIngame.font.draw(Main.batch, score, 20, 50 + pos);
            pos += 50;
        }*/
        backButton.draw();
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
    }
}
