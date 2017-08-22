package bomberbot.entity;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import bomberbot.EnumBlockMaterial;
import bomberbot.EnumDirection;
import bomberbot.Globals;
import bomberbot.screen.ScreenIngame;

public class EntityGiftBomb extends Entity
{
    private EntityBomberbot owner;
    private boolean exploded;
    private float texAddWidth = 0;
    private float texAddHeight = 0;

    public EntityGiftBomb(int pbX, int pbY)
    {
        super(pbX, pbY);
        this.textureSrc = "giftBomb";
        this.enableTicks = true;
        this.setMovementBlocker(true);

        ParticleEffectPool.PooledEffect sparkleEffect = ScreenIngame.sparkeEffectPool.obtain();
        sparkleEffect.setPosition(getpX(), getpY());
        ParticleEffectPool.PooledEffect popEffect = ScreenIngame.popEffectPool.obtain();
        popEffect.setPosition(getpX(), getpY());

        ScreenIngame.effects.add(sparkleEffect);
        ScreenIngame.effects.add(popEffect);
        Globals.playSound("pop", 0.65f, 1f + rand.nextFloat() * 0.1f);
    }


    public void explode()
    {
        for(int i = -4; i < 4; i++)
        {
            for(int j = -3; j < 3; j++)
            {
                int powerupX = this.getPbX() + i;
                int powerupY = this.getPbY() + j;

                if(ScreenIngame.nodes[powerupX][powerupY].isBlocked() || rand.nextFloat() < 0.2f)
                    continue;

                new EntityPowerup(powerupX, powerupY).spawnEntity();
            }
        }
        ParticleEffectPool.PooledEffect giftExplodeEffect = ScreenIngame.giftExplodeEffectPool.obtain();
        giftExplodeEffect.setPosition(getpX(), getpY());
        ScreenIngame.effects.add(giftExplodeEffect);
        Globals.playSound("giftExplode", 1, 1);
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);
        if(this.ticks > 3.5f)
        {
            this.onDeath();
        }
    }

    public void onDeath()
    {
        if(!exploded)
        {
            explode();
            this.deleteEntity();
            exploded = true;
        }
    }

    @Override
    public void render(float delta, SpriteBatch batch)
    {
        if(ticks < 3.35f)
        {
            texAddWidth = (float)Math.sin(this.ticks*6)*10;
            texAddHeight = (float)Math.sin(this.ticks*6)*10;
        } else
        {
            texAddHeight = MathUtils.lerp(texAddHeight, -30, 0.25f);
            texAddWidth = MathUtils.lerp(texAddWidth, 35, 0.25f);
        }

        float texWidth = 88 + texAddWidth;
        float texHeight = 91 + texAddHeight;

        if(textureSrc != null)
        {
            Globals.draw(this.textureSrc, pX - texWidth / 2, pY - texHeight / 2, texWidth, texHeight);
        }
    }
}
