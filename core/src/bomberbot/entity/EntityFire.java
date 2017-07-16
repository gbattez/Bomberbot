package bomberbot.entity;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;

import bomberbot.*;
import bomberbot.screen.ScreenIngame;


public class EntityFire extends Entity
{
    protected byte fireStrength;
    private boolean propagated;
    //TODO corriger
    public boolean looped;
    private EnumDirection directionToPropagate;
    private PooledEffect fireEffect;
    private EntityBomberbot owner;

    public boolean hasPropagated()
    {
        return propagated;
    }

    public EntityFire(int pbX, int pbY, byte fireStrength, EnumDirection directionToPropagate, EntityBomberbot owner)
    {
        super(pbX, pbY);
        this.owner = owner;
        this.fireStrength = fireStrength;
        this.directionToPropagate = directionToPropagate;
        setDestructible(false);
        EntityFire fireToDelete = this.nodeOn.getFire();
        if(fireToDelete != null && fireToDelete != this)
        {
            if(fireToDelete.propagated)
            {
                fireToDelete.deleteEntity();
            }
        } else
        {
            fireEffect = ScreenIngame.fireEffectPool.obtain();
            fireEffect.setPosition(pX, pY);
            if (directionToPropagate == EnumDirection.EAST || directionToPropagate == EnumDirection.WEST)
            {
                fireEffect.getEmitters().get(0).getXOffsetValue().setLow(-45, 45);
                fireEffect.getEmitters().get(0).getYOffsetValue().setLow(-10, 10);
            } else if (directionToPropagate == EnumDirection.SOUTH || directionToPropagate == EnumDirection.NORTH)
            {
                fireEffect.getEmitters().get(0).getXOffsetValue().setLow(-10, 10);
                fireEffect.getEmitters().get(0).getYOffsetValue().setLow(-45, 45);
            } else
            {
                fireEffect.getEmitters().get(0).getXOffsetValue().setLow(-50, 50);
                fireEffect.getEmitters().get(0).getYOffsetValue().setLow(-50, 50);
            }

            ScreenIngame.effects.add(fireEffect);
        }
        //TODO corriger
        this.enableTicks = this.directionToPropagate == EnumDirection.ALL;
    }

    public void update(float delta)
    {
        super.update(delta);
        if(this.ticks > 0.013f && fireStrength > 0 && !propagated)
        {
            propagate(directionToPropagate);
            propagated = true;
        }

        if(this.ticks > 0.14f)
        {
            this.deleteEntity();
        }

        if(this.directionToPropagate == EnumDirection.NONE)
        {
            if(this.nodeOn.getBlockOn().getMaterial() == bomberbot.EnumBlockMaterial.BRICK)
            {
                this.nodeOn.getBlockOn().destroyBrick(getOwner());
                this.deleteEntity();
            }
        }

        //TODO a corriger
        if(this.directionToPropagate == EnumDirection.WEST || this.directionToPropagate == EnumDirection.SOUTH || this.directionToPropagate == EnumDirection.NONE)
        {
            this.enableTicks = true;
        }
        if(this.directionToPropagate == EnumDirection.EAST && looped)
        {
            this.enableTicks = true;
        }
        if(this.directionToPropagate == EnumDirection.NORTH && looped)
        {
            this.enableTicks = true;
        }
        looped = true;
    }

    public void propagate(EnumDirection direction)
    {
        if(direction == EnumDirection.ALL)
        {
            for(EnumDirection directions : EnumDirection.values())
            {
                if(directions != EnumDirection.ALL && directions != EnumDirection.NONE)
                {
                    reactWithAdjacentBlock(this.getAdjacentNode(directions).getBlockOn(), directions);
                }
            }
        } else if(direction != EnumDirection.NONE)
        {
            reactWithAdjacentBlock(this.getAdjacentNode(direction).getBlockOn(), direction);
        }
    }

    public EntityBomberbot getOwner()
    {
        return owner;
    }

    @Override
    public void spawnEntity()
    {
        super.spawnEntity();
    }

    public void reactWithAdjacentBlock(EntityBlock adjacentBlock, EnumDirection direction)
    {
        if(adjacentBlock == null)
        {
            return;
        }
        switch (adjacentBlock.getMaterial())
        {
            case GRASS:
                new EntityFire(adjacentBlock.getPbX(), adjacentBlock.getPbY(), (byte) (this.fireStrength - 1), direction, owner).spawnEntity();
                break;
            case BRICK:
            {
                adjacentBlock.destroyBrick(owner);
            }
            break;
        }
    }
}