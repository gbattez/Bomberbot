package bomberbot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import bomberbot.screen.ScreenIngame;

/**
 * Created by Guillaume on 06/02/2016.
 */
public class VirtualJoystick
{
    private float pX, pY;
    private float joystickX, joystickY;
    private float centerX, centerY;
    private float scale;

    public VirtualJoystick()
    {
        this.scale = 1.3f;
        this.pX = Globals.BLOCK_WIDTH*1.55f - Globals.getTexture("joystick2").getWidth()/2;
        this.pY  = Globals.BLOCK_HEIGHT;
        this.centerX = pX + (Globals.getTexture("joystick2").getHeight()/2)*scale;
        this.centerY = pY + (Globals.getTexture("joystick2").getHeight()/2)*scale;
        this.joystickX = centerX;
        this.joystickY = centerY;
    }

    public void update(Viewport viewport)
    {
        ScreenIngame.player.move(EnumDirection.NONE);
        Vector2 newPoints = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        newPoints = viewport.unproject(newPoints);

        if(this.getMouseDistance(newPoints.x, newPoints.y) > 700 || !Gdx.input.isTouched(0))
        {
            this.joystickX = centerX;
            this.joystickY = centerY;
            return;
        }

        int angle = ((int)Math.toDegrees(Math.atan2(centerY - newPoints.y, centerX - newPoints.x)));
        int angle2 = Math.abs(180 - angle);
        int angle3 = ((angle2+45)/90)%4;

        float distance = this.getMouseDistance(newPoints.x, newPoints.y);
        if(distance > 50)
        {
            distance = 50;
        }
        this.joystickX = (float) (this.centerX - Math.cos(Math.toRadians(angle))*distance);
        this.joystickY = (float) (this.centerY - Math.sin(Math.toRadians(angle))*distance);

        switch (angle3)
        {
            case 0 :
            {
                if(ScreenIngame.player.getAdjacentNode(EnumDirection.EAST).isBlockedByBlock() && ScreenIngame.player.getDistanceInPixels(ScreenIngame.player.getpX(), ScreenIngame.player.getPbY()*Globals.BLOCK_HEIGHT + Globals.BLOCK_HEIGHT/2, ScreenIngame.player.getAdjacentNode(EnumDirection.EAST).getBlockOn()) <= Globals.BLOCK_WIDTH)
                {
                    if(ScreenIngame.player.getpY() > (ScreenIngame.player.getPbY()*Globals.BLOCK_HEIGHT + Globals.BLOCK_HEIGHT/2))
                    {
                        if(!ScreenIngame.nodes[ScreenIngame.player.getPbX() + 1][ScreenIngame.player.getPbY() + 1].isBlockedByBlock())
                        {
                            ScreenIngame.player.move(EnumDirection.NORTH);
                        }
                    } else
                    {
                        if(!ScreenIngame.nodes[ScreenIngame.player.getPbX() + 1][ScreenIngame.player.getPbY() - 1].isBlockedByBlock())
                        {
                            ScreenIngame.player.move(EnumDirection.SOUTH);
                        }
                    }
                } else
                {
                    ScreenIngame.player.move(EnumDirection.EAST);
                }
            } break;
            case 1 :
            {
                if(ScreenIngame.player.getAdjacentNode(EnumDirection.SOUTH).isBlockedByBlock() && ScreenIngame.player.getDistanceInPixels(ScreenIngame.player.getPbX() * Globals.BLOCK_WIDTH + Globals.BLOCK_WIDTH / 2, ScreenIngame.player.getpY(), ScreenIngame.player.getAdjacentNode(EnumDirection.SOUTH).getBlockOn()) <= Globals.BLOCK_HEIGHT)
                {
                    if(ScreenIngame.player.getpX() > (ScreenIngame.player.getPbX()*Globals.BLOCK_WIDTH + Globals.BLOCK_WIDTH/2))
                    {
                        if(!ScreenIngame.nodes[ScreenIngame.player.getPbX() + 1][ScreenIngame.player.getPbY() - 1].isBlockedByBlock())
                        {
                            ScreenIngame.player.move(EnumDirection.EAST);
                        }
                    } else
                    {
                        if(!ScreenIngame.nodes[ScreenIngame.player.getPbX() - 1][ScreenIngame.player.getPbY() - 1].isBlockedByBlock())
                        {
                            ScreenIngame.player.move(EnumDirection.WEST);
                        }
                    }
                } else
                {
                    ScreenIngame.player.move(EnumDirection.SOUTH);
                }
            } break;
            case 2 :
            {
                if(ScreenIngame.player.getAdjacentNode(EnumDirection.WEST).isBlockedByBlock() && ScreenIngame.player.getDistanceInPixels(ScreenIngame.player.getpX(), ScreenIngame.player.getPbY() * Globals.BLOCK_HEIGHT + Globals.BLOCK_HEIGHT / 2, ScreenIngame.player.getAdjacentNode(EnumDirection.WEST).getBlockOn()) <= Globals.BLOCK_WIDTH)
                {
                    if(ScreenIngame.player.getpY() > (ScreenIngame.player.getPbY()*Globals.BLOCK_HEIGHT + Globals.BLOCK_HEIGHT/2))
                    {
                        if(!ScreenIngame.nodes[ScreenIngame.player.getPbX() - 1][ScreenIngame.player.getPbY() + 1].isBlockedByBlock())
                        {
                            ScreenIngame.player.move(EnumDirection.NORTH);
                        }
                    } else
                    {
                        if(!ScreenIngame.nodes[ScreenIngame.player.getPbX() - 1][ScreenIngame.player.getPbY() - 1].isBlockedByBlock())
                        {
                            ScreenIngame.player.move(EnumDirection.SOUTH);
                        }
                    }
                } else
                {
                    ScreenIngame.player.move(EnumDirection.WEST);
                }
            } break;
            case 3 :
            {
                if(ScreenIngame.player.getAdjacentNode(EnumDirection.NORTH).isBlockedByBlock() && ScreenIngame.player.getDistanceInPixels(ScreenIngame.player.getPbX() * Globals.BLOCK_WIDTH + Globals.BLOCK_WIDTH / 2, ScreenIngame.player.getpY(), ScreenIngame.player.getAdjacentNode(EnumDirection.NORTH).getBlockOn()) <= Globals.BLOCK_HEIGHT)
                {
                    if(ScreenIngame.player.getpX() > (ScreenIngame.player.getPbX()*Globals.BLOCK_WIDTH + Globals.BLOCK_WIDTH/2))
                    {
                        if(!ScreenIngame.nodes[ScreenIngame.player.getPbX() + 1][ScreenIngame.player.getPbY() + 1].isBlockedByBlock())
                        {
                            ScreenIngame.player.move(EnumDirection.EAST);
                        }
                    } else
                    {
                        if(!ScreenIngame.nodes[ScreenIngame.player.getPbX() - 1][ScreenIngame.player.getPbY() + 1].isBlockedByBlock())
                        {
                            ScreenIngame.player.move(EnumDirection.WEST);
                        }
                    }
                } else
                {
                    ScreenIngame.player.move(EnumDirection.NORTH);
                }
            } break;
        }
    }

    public void render(Viewport viewport)
    {
        update(viewport);
        Globals.draw("joystick2", pX, pY, Globals.getTexture("joystick2").getWidth() * scale, Globals.getTexture("joystick2").getHeight() * scale);
        Globals.draw("joystick", joystickX - (Globals.getTexture("joystick").getWidth()/2)*scale, joystickY - (Globals.getTexture("joystick").getHeight()/2)*scale, Globals.getTexture("joystick").getWidth()*scale, Globals.getTexture("joystick").getHeight()*scale);
    }

    public float getMouseDistance(float x, float y)
    {
        float pX2 = pX + (Globals.getTexture("joystick2").getWidth()/2)*scale;
        float pY2 = pY + (Globals.getTexture("joystick2").getHeight()/2)*scale;
        return (float)Math.sqrt(Math.pow(x - pX2, 2) + Math.pow(y - pY2, 2));
    }
}