package bomberbot.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import bomberbot.*;
import bomberbot.button.BombButton;
import bomberbot.button.ButtonBasic;
import bomberbot.entity.EntityBlock;
import bomberbot.entity.EntityBomb;
import bomberbot.entity.EntityBomberbot;
import bomberbot.entity.EntityAI;
import bomberbot.entity.EntityGiftBomb;
import bomberbot.entity.EntityPlayer;
import bomberbot.EnumBlockMaterial;
import bomberbot.main.Main;
import jdk.nashorn.internal.objects.Global;

public class ScreenIngame extends ScreenBasic
{
    public static ParticleEffect fireEffect;
    public static ParticleEffectPool fireEffectPool;
    public static ParticleEffect sparkeEffect;
    public static ParticleEffectPool sparkeEffectPool;
    public static ParticleEffect popEffect;
    public static ParticleEffectPool popEffectPool;
    public static ParticleEffect giftExplodeEffect;
    public static ParticleEffectPool giftExplodeEffectPool;
    public static Array<PooledEffect> effects;
    public static Node[][] nodes = new Node[20][11];
    public static EntityPlayer player;
    public static Stage ingameStage;
    public static Table ingameTable;
    public static boolean showControls;
    public static boolean isChallengeMode;
    private boolean paused;
    private VirtualJoystick virtualJoystick;
    private BombButton bombButton;
    private ShapeRenderer shapeRenderer;
    public static BitmapFont font = new BitmapFont();
    private float timer;
    private float gameFinishedTimer;
    private float bombTimer;
    private float flashOverlayAlpha;
    private float giftBombTimer;
    public static byte aiNumber;
    private byte aiNumberToSpawn;
    public static int bricks;
    private Random rand = new Random();
    private ButtonBasic buttonPause;
    private ButtonBasic buttonResume;
    private ButtonBasic buttonMusic;
    private ButtonBasic buttonSound;
    private ButtonBasic buttonExit;
    private ButtonBasic buttonNextLevel;
    private ButtonBasic buttonRetry;
    private ButtonBasic buttonReplay;

    public ScreenIngame(Main game)
    {
        super(game);
        aiNumberToSpawn = aiNumber = 1;
        init();
    }

    public ScreenIngame(Main game, byte aiNumberToSpawn)
    {
        super(game);
        this.aiNumberToSpawn = aiNumber = aiNumberToSpawn;
        init();
    }

    public void init()
    {
        font.getData().setScale(3);
        ingameTable = new Table();
        ingameStage = new Stage(this.gamePort, Globals.batch);
        Gdx.input.setInputProcessor(ingameStage);
        Globals.music = Globals.getMusic("music");
        Globals.music.setLooping(true);
        initLevel();

        fireEffect = new ParticleEffect();
        fireEffect.load(Gdx.files.internal("fire.p"), Gdx.files.internal(""));
        fireEffect.start();
        fireEffectPool = new ParticleEffectPool(fireEffect, 0, 100);
        sparkeEffect = new ParticleEffect();
        sparkeEffect.load(Gdx.files.internal("sparkles.p"), Gdx.files.internal(""));
        sparkeEffect.start();
        sparkeEffectPool = new ParticleEffectPool(sparkeEffect, 0, 100);
        popEffect = new ParticleEffect();
        popEffect.load(Gdx.files.internal("pop.p"), Gdx.files.internal(""));
        popEffect.start();
        popEffectPool = new ParticleEffectPool(popEffect, 0, 100);
        giftExplodeEffect = new ParticleEffect();
        giftExplodeEffect.load(Gdx.files.internal("giftExplode.p"), Gdx.files.internal(""));
        giftExplodeEffect.start();
        giftExplodeEffectPool = new ParticleEffectPool(giftExplodeEffect, 0, 100);


        effects = new Array<PooledEffect>();

        virtualJoystick = new VirtualJoystick();
        bombButton = new BombButton();
        shapeRenderer = new ShapeRenderer();
        buttonPause = new ButtonBasic(Globals.BLOCK_WIDTH*19, Globals.BLOCK_HEIGHT*10, Globals.BLOCK_WIDTH, Globals.BLOCK_HEIGHT);
        buttonPause.setTextureSrc("pauseButton", false);
        buttonResume = new ButtonBasic(Globals.camera.viewportWidth/2 + Globals.BLOCK_WIDTH, Globals.BLOCK_HEIGHT*8, "Resume");
        buttonNextLevel = new ButtonBasic(Globals.camera.viewportWidth/2 + Globals.BLOCK_WIDTH, Globals.BLOCK_HEIGHT*8, "Next Level");
        buttonRetry = new ButtonBasic(Globals.camera.viewportWidth/2 + Globals.BLOCK_WIDTH, 0, "Retry");
        buttonReplay = new ButtonBasic(Globals.camera.viewportWidth/2 + Globals.BLOCK_WIDTH, 0, "Replay");
        buttonMusic = new ButtonBasic(Globals.camera.viewportWidth/2 - 20 + 510/2 - 120, 0);
        buttonMusic.setTextureSrc("music_on", true);
        buttonSound = new ButtonBasic(Globals.camera.viewportWidth/2 - 20 + 510/2 + 120, 0);
        buttonSound.setTextureSrc("volume_on", true);
        buttonExit = new ButtonBasic(Globals.camera.viewportWidth/2 + Globals.BLOCK_WIDTH, 0, "Exit");
        buttonExit.setTextureSrc("button_red", false);
    }

    public boolean isGameFinished()
    {
        return player.isDead() || aiNumber == 0;
    }

    public boolean isTimeOut()
    {
        return this.timer <= 0;
    }

    public void initLevel()
    {
        this.bricks = 0;
        this.giftBombTimer = -1;
        this.gameFinishedTimer = 0;
        if(Globals.musicEnabled)
        {
            Globals.music.setVolume(0.2f);
            Globals.music.setLooping(true);
            if (!Globals.music.isPlaying())
            {
                Globals.music.play();
            }

            if (this.aiNumberToSpawn < 4 && Globals.music == Globals.getMusic("music2"))
            {
                Globals.music.stop();
                Globals.music = Globals.getMusic("music");
                Globals.music.setVolume(0.2f);
                Globals.music.setLooping(true);
                Globals.music.play();
            }
            if (this.aiNumberToSpawn == 4 && Globals.music == Globals.getMusic("music"))
            {
                Globals.music.stop();
                Globals.music = Globals.getMusic("music2");
                Globals.music.setVolume(0.2f);
                Globals.music.setLooping(true);
                Globals.music.play();
            }
        }
        this.createTerrain();
        this.spawnBomberbot((byte) (aiNumberToSpawn + 1));
        this.timer = 90;
    }

    public void createTerrain()
    {
        this.bricks = 0;
        for(int i = 0; i < 20; i++)
        {
            for (int j = 0; j < 11; j++)
            {
                nodes[i][j] = new Node(i, j);
                Random rand = new Random();
                EnumBlockMaterial material;
                if (j == 0 || j == 10 || i < 4 || i == 19 || i % 2 == 1 && j % 2 == 0)
                {
                    material = EnumBlockMaterial.METAL;
                } else
                {
                    if (rand.nextInt(3) == 0)
                    {
                        material = EnumBlockMaterial.GRASS;
                    } else
                    {
                        material = EnumBlockMaterial.BRICK;
                        this.bricks ++;
                    }
                }
                new EntityBlock(material, i, j).spawnEntity();
            }
        }
    }

    public void spawnBomberbot(byte number)
    {
        if(number == 5)
        {
            spawnPlayer(4);
        } else
        {
            spawnPlayer(0);
        }
        for(byte i = 1; i < number; i++)
        {
            if(i == 4)
            {
                spawnAI(0);
            } else
            {
                spawnAI(i);
            }
        }
        aiNumber = aiNumberToSpawn;
    }

    public void spawnAI(int playerNumber)
    {
        int spawnX = 4 + (playerNumber % 2) * 14;
        int spawnY = 9 - (playerNumber / 2) * 8;

        EntityAI entityAI = null;
        switch (playerNumber)
        {
            case 0 : entityAI = new EntityAI(spawnX, spawnY, Color.PINK); break;
            case 1 : entityAI = new EntityAI(spawnX, spawnY, Color.ORANGE); break;
            case 2 : entityAI = new EntityAI(spawnX, spawnY, Color.SKY); break;
            case 3 : entityAI = new EntityAI(spawnX, spawnY, Color.GOLD); break;
        }

        if(entityAI != null)
        {
            prepareSpawnPoint(entityAI);
            entityAI.spawnEntity();
        }
    }

    public void spawnPlayer(int playerNumber)
    {
        int spawnX = 4 + (playerNumber % 2) * 14;
        int spawnY = 9 - (playerNumber / 2) * 8;

        if(playerNumber == 4)
        {
            spawnX = 10;
            spawnY = 5;
        }
        int scoretmp = 0;
        if(player != null)
        {
            scoretmp = Integer.parseInt(player.getScore().toString());
        }
        player = new EntityPlayer(spawnX, spawnY, Color.WHITE);
        player.setScore(scoretmp);
        prepareSpawnPoint(player);
        player.spawnEntity();
    }

    //Enlève les briques qui peuvent blocker les mouvements au spawn
    public void prepareSpawnPoint(EntityBomberbot bbot)
    {
        new EntityBlock(EnumBlockMaterial.GRASS, bbot.getPbX(), bbot.getPbY()).spawnEntity();
        bricks --;
        for(bomberbot.Node n : bbot.getAdjacentNodes())
        {
            if(n.getBlockOn().getMaterial() == EnumBlockMaterial.BRICK)
            {
                new EntityBlock(EnumBlockMaterial.GRASS, n.getnX(), n.getnY()).spawnEntity();
                bricks --;
            }
        }
    }

    @Override
    public void render(float delta)
    {
        super.render(delta);
        Globals.batch.begin();

        for(int i = 0; i < 20; i++)
        {
            for (int j = 0; j < 11; j++)
            {
                nodes[i][j].renderBlock(delta);
            }
        }

        for(int i = 0; i < 20; i++)
        {
            for(int j = 0; j < 11; j++)
            {
                nodes[i][j].renderNodeContent(delta);
            }
        }

        for(PooledEffect effect : effects)
        {
            effect.draw(Globals.batch, delta);
            if(effect.isComplete())
            {
                effects.removeValue(effect, true);
                effect.free();
            }
        }
        if(isChallengeMode)
        {
            Globals.draw("score", 0, Globals.BLOCK_HEIGHT * 7, Globals.BLOCK_WIDTH * 3 + 30, Globals.BLOCK_HEIGHT);
            font.draw(Globals.batch, "score : " + ScreenIngame.player.getScore(), 10, Globals.BLOCK_HEIGHT * 7 + 65);
        }
        Globals.drawCentered("timer", Globals.BLOCK_WIDTH * 11, Globals.BLOCK_HEIGHT * 10.5f, Globals.BLOCK_WIDTH * 2.5f, Globals.BLOCK_HEIGHT * 0.8f);
        font.draw(Globals.batch, Integer.toString((int) timer), Globals.BLOCK_WIDTH * 11, Globals.BLOCK_HEIGHT * 10.5f + 16);

        if(!player.isDead() && showControls)
        {
            virtualJoystick.render(gamePort);
        }
        Globals.batch.end();
        if(gameFinishedTimer > 1.3f || paused)
        {
            renderOverlay();
        }
        Globals.batch.begin();
        if(showControls)
        {
            buttonPause.draw();
        }
        if(paused)
        {
            renderPausePopup();
        }
        Globals.batch.end();

        if(!(isGameFinished() || paused) && showControls)
        {
            bombButton.draw(delta);
        }
    }

    public void renderOverlay()
    {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(Globals.camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.5f);
        shapeRenderer.rect(0, 0, Globals.camera.viewportWidth, Globals.camera.viewportHeight);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void renderFlashOverlay(boolean won, float delta)
    {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(Globals.camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if(won)
        {
            shapeRenderer.setColor(0.5f, 1, 0.5f, flashOverlayAlpha);
        } else
        {
            shapeRenderer.setColor(1f, 0.2f, 0.2f, flashOverlayAlpha);
        }
        shapeRenderer.rect(0, 0, Globals.camera.viewportWidth, Globals.camera.viewportHeight);
        shapeRenderer.end();
        this.flashOverlayAlpha -= this.flashOverlayAlpha > 0 ? delta : 0;
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    float popupY = Globals.camera.viewportHeight;

    public void renderPausePopup()
    {
        int width = 510;
        int height = 610;
        float x = Globals.camera.viewportWidth / 2 - width/2 + Globals.BLOCK_WIDTH;
        popupY = MathUtils.lerp(popupY, Globals.camera.viewportHeight - height, 0.2f);
        Globals.draw("pause_background", x, popupY, width, height);

        if(this.isGameFinished())
        {
            font.draw(Globals.batch, "Score : " + player.getScore(), x + 100,popupY + 540);
            if(player.isDead())
            {
                if(isChallengeMode)
                {
                    this.buttonRetry.setpY(popupY + 250);
                    this.buttonRetry.draw();
                } else
                {
                    this.buttonReplay.setpY(popupY + 250);
                    this.buttonReplay.draw();
                }
            } else
            {
                if(isChallengeMode)
                {
                    this.buttonNextLevel.setpY(popupY + 250);
                    this.buttonNextLevel.draw();
                } else
                {
                    this.buttonReplay.setpY(popupY + 250);
                    this.buttonReplay.draw();
                }
            }
        } else
        {
            this.buttonResume.setpY(popupY + 250);
            this.buttonResume.draw();
            this.buttonSound.setpY(popupY + 450);
            this.buttonMusic.setpY(popupY + 450);
            this.buttonMusic.draw();
            this.buttonSound.draw();
        }
        this.buttonExit.setpY(popupY + 70);
        this.buttonExit.draw();
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);

        Gdx.input.setCursorCatched(!(this.isGameFinished() || this.paused));

        if(buttonPause.onClick(gamePort) || this.gameFinishedTimer > 1.3f)
        {
            paused = true;
        }

        if(!isTimeOut() && this.bricks <= 0)
        {
            if(this.giftBombTimer == -1)
            {
                this.giftBombTimer = 19 + rand.nextFloat()*8;
            } else if (this.giftBombTimer < 0)
            {
                new EntityGiftBomb(11, 5).spawnEntity();
                this.giftBombTimer = 19 + rand.nextFloat()*8;
            } else if(!paused)
            {
                this.giftBombTimer -= delta;
            }
        }
        if(buttonMusic.onClick(gamePort))
        {
            Globals.musicEnabled = !Globals.musicEnabled;
            if(Globals.musicEnabled)
            {
                Globals.music.play();
                buttonMusic.setTextureSrc("music_on", true);
            } else
            {
                Globals.music.stop();
                buttonMusic.setTextureSrc("music_off", true);
            }
        }
        if(buttonSound.onClick(gamePort))
        {
            Globals.soundEnabled = !Globals.soundEnabled;
            buttonSound.setTextureSrc(Globals.soundEnabled ? "volume_on" : "volume_off", true);
        }
        if(buttonExit.onClick(gamePort))
        {
            player.getScore().resetScore();
            Globals.music.stop();
            game.setScreen(new ScreenMenu(getGame()));
        }
        if(buttonResume.onClick(gamePort))
        {
            paused = false;
        }
        if(buttonRetry.onClick(gamePort) || (this.isGameFinished() && isChallengeMode && player.isDead() && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)))
        {
            aiNumberToSpawn = 1;
            this.initLevel();
            player.getScore().resetScore();
            paused = false;
        }
        if(buttonNextLevel.onClick(gamePort) || (this.isGameFinished() && isChallengeMode && !player.isDead() && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)))
        {
            if(aiNumberToSpawn < 4)
            {
                aiNumberToSpawn++;
            }
            this.initLevel();
            paused = false;
        }
        if(buttonReplay.onClick(gamePort) || this.isGameFinished() && !isChallengeMode && Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        {
            player.getScore().resetScore();
            this.initLevel();
            paused = false;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.P))
        {
            this.paused = !paused;
        }
        if(paused)
        {
            return;
        }
        popupY = MathUtils.lerp(popupY, Globals.camera.viewportHeight, 0.2f);
        for(int i = 0; i < 20; i++)
        {
            for (int j = 0; j < 11; j++)
            {
                nodes[i][j].setDangerous(false);
            }
        }
        for(int i = 0; i < 20; i++)
        {
            for (int j = 0; j < 11; j++)
            {
                nodes[i][j].updateNodeContent(delta);
            }
        }
        for(int i = 0; i < 20; i++)
        {
            for (int j = 0; j < 11; j++)
            {
                nodes[i][j].updateAI(delta);
            }
        }

        if(!this.isTimeOut())
        {
            this.timer -= delta;
        } else
        {
            this.bombTimer -= delta;
            int pX = rand.nextInt(15) + 4;
            int pY = rand.nextInt(9) + 1;
            Node node = nodes[pX][pY];
            if(!node.isBlocked() && node.getBomb() == null && bombTimer <= 0)
            {
                new EntityBomb(pX, pY, (byte) 3).spawnEntity();

              //  if(ScreenIngame.popEffectPool!= null) {
                    ParticleEffectPool.PooledEffect popEffect = ScreenIngame.popEffectPool.obtain();
                    popEffect.setPosition(pX * Globals.BLOCK_WIDTH + Globals.BLOCK_WIDTH / 2, pY * Globals.BLOCK_HEIGHT + Globals.BLOCK_HEIGHT / 2);
                    ScreenIngame.effects.add(popEffect);
                    Globals.playSound("pop", 0.65f, 1f + rand.nextFloat() * 0.1f);
               // }
                bombTimer = 0.4f;
            }
        }

        if(this.isGameFinished())
        {
            this.gameFinishedTimer += delta;
        }
    }

    @Override
    public void dispose()
    {
        super.dispose();
        fireEffect.dispose();
        sparkeEffect.dispose();
        popEffect.dispose();
        giftExplodeEffect.dispose();
    }
}