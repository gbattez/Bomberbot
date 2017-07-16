package bomberbot.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import bomberbot.*;
import bomberbot.screen.ScreenIngame;

public class EntityBomb extends Entity
{
    private EntityBomberbot owner;
    private boolean exploded;
    private boolean skullBomb;
    private float texAddWidth = 0;
    private float texAddHeight = 0;
    private byte power;

    public EntityBomb(int pX, int pY, EntityBomberbot owner)
    {
        super(pX, pY);
        this.textureSrc = "bomb";
        this.enableTicks = true;
        this.owner = owner;
        this.setMoveSpeed(0.7f);
        this.setMovementBlocker(true);
        this.setDestructible(true);
        this.power = owner.getFirePower();
    }

    public EntityBomb(int pX, int pY, byte power)
    {
        super(pX, pY);
        this.textureSrc = "bomb";
        this.enableTicks = true;
        this.owner = null;
        this.setMoveSpeed(0.7f);
        this.setMovementBlocker(true);
        this.setDestructible(true);
        this.power = power;
    }

    public void setSkullBomb(boolean skullBomb)
    {
        this.textureSrc = "bombskull";
        this.skullBomb = skullBomb;
    }

    public void explode()
    {
        if(skullBomb)
        {
            int firePower = 2 + owner.getFirePower()/15;

            for(int i = -firePower; i < firePower + 1; i++)
            {
                for(int j = -firePower; j < firePower + 1; j++)
                {
                    if(getPbX() + i < 20 && getPbX() + i > 0 && getPbY() + j > 0 && getPbY() + j < 11)
                    {
                        new EntityFire(getPbX() + i, getPbY() + j, (byte) 1, EnumDirection.NONE, owner).spawnEntity();
                    }
                }
            }
        } else
        {
            new EntityFire(getPbX(), getPbY(), power, EnumDirection.ALL, owner).spawnEntity();
        }
        if(this.owner != null)
        {
            this.owner.addBomb();
            if(this.skullBomb)
                this.owner.addSkullBomb();
        }
        Globals.playSound(this.skullBomb ? "explodeskull" : "explode", 0.8f, 0.8f + rand.nextFloat() * 0.4f);
   }

    @Override
    public void update(float delta)
    {
        super.update(delta);
        if(this.ticks > 2)
        {
            this.onDeath();
        }

        if(this.isMoving() && this.getAdjacentNode(this.getMovingDirection()).getBomberbot() != null)
        {
            this.move(EnumDirection.NONE);
        }

        if(this.ticks > 2 + 0.1f*power)
        {
            this.deleteEntity();
        }

        for(EnumDirection directions : EnumDirection.values())
        {
            if(directions != EnumDirection.ALL && directions != EnumDirection.NONE && !skullBomb)
            {
                for (byte i = 0; i < power + 1; i++)
                {
                    EntityBlock block = this.getAdjacentNode(directions, i).getBlockOn();
                    if(block != null)
                    {
                        if(block.getMaterial() != bomberbot.EnumBlockMaterial.GRASS)
                        {
                            break;
                        } else
                        {
                            this.getAdjacentNode(directions, i).setDangerous(true);
                        }
                    }
                }
            }
            if(skullBomb)
            {
                int firePower = owner.getFirePower() >= 15 ? 3 : 2;

                for(int i = -firePower; i < firePower + 1; i++)
                {
                    for(int j = -firePower; j < firePower + 1; j++)
                    {
                        if(getPbX() + i < 20 && getPbX() + i > 0 && getPbY() + j > 0 && getPbY() + j < 11 && ScreenIngame.nodes[getPbX() + i][getPbY() + j].getBlockOn().getMaterial() != bomberbot.EnumBlockMaterial.METAL)
                        {
                            ScreenIngame.nodes[this.getPbX() + i][this.getPbY() + j].setDangerous(true);
                        }
                    }
                }
            }
        }
    }

    public boolean isExploded()
    {
        return exploded;
    }

    public void onDeath()
    {
        if(!exploded)
        {
            explode();
            exploded = true;
        }
        this.textureSrc = null;
        this.setMovementBlocker(false);
        this.setMoveSpeed(0);
    }

    @Override
    public void render(float delta, SpriteBatch batch)
    {
        float shakeX = 0;
        float shakeY = 0;
        if(ticks < 1.85f)
        {
            texAddWidth = (float)Math.sin(this.ticks*6)*10;
            texAddHeight = (float)Math.sin(this.ticks*6)*10;
        } else
        {
            texAddHeight = MathUtils.lerp(texAddHeight, -30, 0.25f);
            texAddWidth = MathUtils.lerp(texAddWidth, 35, 0.25f);
            float shakeExtent = ticks*50 - 92.5f;
            shakeX = rand.nextFloat()*shakeExtent - rand.nextFloat()*shakeExtent;
            shakeY = rand.nextFloat()*shakeExtent - rand.nextFloat()*shakeExtent;
        }

        float texWidth = 88 + texAddWidth;
        float texHeight = 91 + texAddHeight;

        if(textureSrc != null)
        {
            Globals.draw(this.textureSrc, pX - texWidth / 2 + shakeX, pY - texHeight / 2 + shakeY, texWidth, texHeight);
        }
    }
}
