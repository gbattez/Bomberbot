package bomberbot.entity;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;

import bomberbot.screen.ScreenIngame;
import bomberbot.entity.EntityPowerup.EnumPowerup;

public class EntityBlock extends Entity
{
    private bomberbot.EnumBlockMaterial material;

    public EntityBlock(bomberbot.EnumBlockMaterial material, int pX, int pY)
    {
        super(pX, pY);
        this.material = material;
        this.textureSrc = material.getTextureSrc();
        this.setMovementBlocker(material.isMovementBlocker());
    }

    public bomberbot.EnumBlockMaterial getMaterial()
    {
        return material;
    }

    public void setMaterial(bomberbot.EnumBlockMaterial material)
    {
        this.material = material;
        this.textureSrc = material.getTextureSrc();
        this.setMovementBlocker(material.isMovementBlocker());
    }

    public void spawnEntity()
    {
        this.nodeOn.setBlockOn(this);
    }

    public void destroyBrick(EntityBomberbot breaker)
    {
        if(this.textureSrc.contains("blockgrass"))
        {
            return;
        }
        if(breaker instanceof EntityPlayer)
        {
            ScreenIngame.player.getScore().addDestroyBlockScore();
        }
        onDeath();
    }

    @Override
    public void onDeath()
    {
        this.enableTicks = true;
        if(Math.random() < 0.7f)
        {
            new EntityPowerup(getPbX(), getPbY()).spawnEntity();
        }

        ParticleEffectPool.PooledEffect effect = ScreenIngame.fireEffectPool.obtain();
        effect.getEmitters().get(0).getXOffsetValue().setLow(-50, 50);
        effect.getEmitters().get(0).getYOffsetValue().setLow(-50, 50);
        effect.setPosition(this.pX, this.pY);
        ScreenIngame.effects.add(effect);
        this.textureSrc = "blockgrass";
        ScreenIngame.bricks --;
    }

    @Override
    public void deleteEntity()
    {
    }

    @Override
    public void update(float delta)
    {
        if(this.enableTicks)
        {
            ticks += delta;
        }
        if(this.ticks > 0.5f)
        {
            new EntityBlock(bomberbot.EnumBlockMaterial.GRASS, getPbX(), getPbY()).spawnEntity();
            super.onDeath();
        }
    }
}