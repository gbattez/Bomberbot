package bomberbot.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import bomberbot.EnumDirection;
import bomberbot.Globals;
import bomberbot.main.Main;
import bomberbot.net.Connexion;
import bomberbot.net.Score;
import bomberbot.screen.ScreenIngame;
import jdk.nashorn.internal.objects.Global;

public class EntityPlayer extends EntityBomberbot
{
    private Score score;
    private boolean dead;

    public Score getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = new Score(score);
    }

    public void setDead(boolean dead)
    {
        this.dead = dead;
    }

    public boolean isDead()
    {
        return dead;
    }

    public EntityPlayer(int pX, int pY, Color color)
    {
        super(pX, pY, color);
        this.setDestructible(true);
        this.score = new Score();
    }

    @Override
    public void onDeath()
    {
        Globals.scoreToUpload = this.getScore();
        Thread thread = new Thread(new Runnable()
        {
            public void run()
            {
            try
            {
                Connexion.doHttpUrlConnectionAction(getScore().getScore(), Globals.user.getUsername());
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            }
        });
        thread.start();
        super.onDeath();
        this.setDead(true);
    }

    @Override
    public void render(float delta, SpriteBatch batch)
    {
        super.render(delta, batch);
    }

    @Override
    public void update(float delta)
    {
        if(!ScreenIngame.showControls)
        {
            this.move(EnumDirection.NONE);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D))
        {
            if(getAdjacentNode(EnumDirection.EAST).isBlockedByBlock() && getDistanceInPixels(pX, getPbY()*Globals.BLOCK_HEIGHT + Globals.BLOCK_HEIGHT/2, getAdjacentNode(EnumDirection.EAST).getBlockOn()) <= Globals.BLOCK_WIDTH)
            {
                if(this.pY > (getPbY()* Globals.BLOCK_HEIGHT + Globals.BLOCK_HEIGHT/2))
                {
                    if(!ScreenIngame.nodes[getPbX() + 1][getPbY() + 1].isBlockedByBlock())
                    {
                        this.move(EnumDirection.NORTH);
                    }
                } else
                {
                    if(!ScreenIngame.nodes[getPbX() + 1][getPbY() - 1].isBlockedByBlock())
                    {
                        this.move(EnumDirection.SOUTH);
                    }
                }
            } else
            {
                this.move(EnumDirection.EAST);
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.Q))
        {
            if(getAdjacentNode(EnumDirection.WEST).isBlockedByBlock() && getDistanceInPixels(pX, getPbY()*Globals.BLOCK_HEIGHT + Globals.BLOCK_HEIGHT/2, getAdjacentNode(EnumDirection.WEST).getBlockOn()) <= Globals.BLOCK_WIDTH)
            {
                if(this.pY > (getPbY()*Globals.BLOCK_HEIGHT + Globals.BLOCK_HEIGHT/2))
                {
                    if(!ScreenIngame.nodes[getPbX() - 1][getPbY() + 1].isBlockedByBlock())
                    {
                        this.move(EnumDirection.NORTH);
                    }
                } else
                {
                    if(!ScreenIngame.nodes[getPbX() - 1][getPbY() - 1].isBlockedByBlock())
                    {
                        this.move(EnumDirection.SOUTH);
                    }
                }
            } else
            {
                this.move(EnumDirection.WEST);
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S))
        {
            if(getAdjacentNode(EnumDirection.SOUTH).isBlockedByBlock() && getDistanceInPixels(getPbX()*Globals.BLOCK_WIDTH + Globals.BLOCK_WIDTH/2, pY, getAdjacentNode(EnumDirection.SOUTH).getBlockOn()) <= Globals.BLOCK_HEIGHT)
            {
                if(this.pX > (getPbX()*Globals.BLOCK_WIDTH + Globals.BLOCK_WIDTH/2))
                {
                    if(!ScreenIngame.nodes[getPbX() + 1][getPbY() - 1].isBlockedByBlock())
                    {
                        this.move(EnumDirection.EAST);
                    }
                } else
                {
                    if(!ScreenIngame.nodes[getPbX() - 1][getPbY() - 1].isBlockedByBlock())
                    {
                        this.move(EnumDirection.WEST);
                    }
                }
            } else
            {
                this.move(EnumDirection.SOUTH);
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Z))
        {
            if(getAdjacentNode(EnumDirection.NORTH).isBlockedByBlock() && getDistanceInPixels(getPbX()*Globals.BLOCK_WIDTH + Globals.BLOCK_WIDTH/2, pY, getAdjacentNode(EnumDirection.NORTH).getBlockOn()) <= Globals.BLOCK_HEIGHT)
            {
                if(this.pX > (getPbX()*Globals.BLOCK_WIDTH + Globals.BLOCK_WIDTH/2))
                {
                    if(!ScreenIngame.nodes[getPbX() + 1][getPbY() + 1].isBlockedByBlock())
                    {
                        this.move(EnumDirection.EAST);
                    }
                } else
                {
                    if(!ScreenIngame.nodes[getPbX() - 1][getPbY() + 1].isBlockedByBlock())
                    {
                        this.move(EnumDirection.WEST);
                    }
                }
            } else
            {
                this.move(EnumDirection.NORTH);
            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_RIGHT))
        {
            this.dropBomb();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
        {
            this.nodeOn.getNodeContent();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))
        {
            this.getAdjacentNode(EnumDirection.EAST).getNodeContent();
        }
        super.update(delta);
    }
}