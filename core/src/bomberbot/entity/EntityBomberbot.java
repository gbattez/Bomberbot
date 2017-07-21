package bomberbot.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import bomberbot.EnumDirection;
import bomberbot.Globals;
import bomberbot.Node;
import bomberbot.screen.ScreenIngame;

public abstract class EntityBomberbot extends Entity {
    private byte firePower;
    private byte bombs, skullBombs, maxBombCapacity;
    private boolean canKickBomb;
    private boolean canChainBomb;
    private boolean hasSkullBomb;
    private float angle;
    private Sprite headSprite, rightHandSprite, leftHandSprite, rightFootSprite, leftFootSprite;
    private float animTicks, targetTicks;
    private float bodyAngle;

    public EntityBomberbot(int pX, int pY, Color color) {
        super(pX, pY);
        this.textureSrc = "bbHead";
        moveSpeed = 0.4f;
        this.firePower = 2;
        this.bombs = 1;
        this.maxBombCapacity = 1;
        this.canKickBomb = false;
        this.setDestructible(true);

        this.headSprite = new Sprite(Globals.getTexture(textureSrc));
        this.headSprite.setOriginCenter();
        this.rightHandSprite = new Sprite(Globals.getTexture("bbHand"));
        this.leftHandSprite = new Sprite(Globals.getTexture("bbHand"));
        this.rightFootSprite = new Sprite(Globals.getTexture("bbFoot"));
        this.leftFootSprite = new Sprite(Globals.getTexture("bbFoot"));
        this.headSprite.setColor(color);
        this.rightHandSprite.setColor(color);
        this.leftHandSprite.setColor(color);
        this.rightFootSprite.setColor(color);
        this.leftFootSprite.setColor(color);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (this.isMoving()) {
            this.animTicks += delta;
            if (this.animTicks > Math.PI / 5) {
                this.animTicks = 0;
            }
            this.bodyAngle = (float) Math.sin(animTicks * 10) * 16;
            if (this.canKickBomb) {
                EntityBomb bomb = this.getAdjacentNode(this.getMovingDirection()).getBomb();
                if (bomb != null && getDistanceInPixels(bomb) < Globals.BLOCK_HEIGHT && bomb.getMovingDirection() != this.getMovingDirection() && !bomb.isExploded()) {
                    Globals.playSound("bombkick", 1f, 0.8f + rand.nextFloat() * 0.2f);
                    bomb.move(this.getMovingDirection());
                }
            }
            if (this.animTicks < Math.PI / 20) {
                this.targetTicks = 0;
            }
            if (this.animTicks > Math.PI / 20 && this.animTicks < Math.PI / 7.5f) {
                this.targetTicks = (float) Math.PI / 10;
            }
            if (this.animTicks > Math.PI / 7.5f) {
                this.targetTicks = (float) Math.PI / 5;
            }
        } else {
            this.bodyAngle = MathUtils.lerpAngle(bodyAngle, 0, 0.2f);
            this.animTicks = MathUtils.lerp(animTicks, targetTicks, 0.2f);
        }
        switch (this.getMovingDirection()) {
            case NORTH:
                angle = MathUtils.lerpAngleDeg(angle, 0, 0.2f);
                break;
            case WEST:
                angle = MathUtils.lerpAngleDeg(angle, 90, 0.2f);
                break;
            case SOUTH:
                angle = MathUtils.lerpAngleDeg(angle, 180, 0.2f);
                break;
            case EAST:
                angle = MathUtils.lerpAngleDeg(angle, 270, 0.2f);
                break;
        }
    }

    @Override
    public void render(float delta, SpriteBatch batch) {
        rightFootSprite.setPosition(pX + 10, (float) (pY - 25 + Math.sin(animTicks * 10) * 35));
        rightFootSprite.setOrigin(-10, -(float) (-15 + Math.sin(animTicks * 10) * 35));
        rightFootSprite.setRotation(angle);
        rightFootSprite.draw(Globals.batch);

        leftFootSprite.setPosition(pX - 28, (float) (pY - 25 - Math.sin(animTicks * 10) * 35));
        leftFootSprite.setOrigin(28, -(float) (-15 - Math.sin(animTicks * 10) * 35));
        leftFootSprite.setRotation(angle);
        leftFootSprite.draw(Globals.batch);

        rightHandSprite.setPosition(pX + 27, (float) (pY - 18 - Math.sin(animTicks * 10) * 15));
        rightHandSprite.setOrigin(-27, -(float) (-7 - Math.sin(animTicks * 10) * 15));
        rightHandSprite.setRotation(angle - bodyAngle);
        rightHandSprite.draw(Globals.batch);

        leftHandSprite.setPosition(pX - 47, (float) (pY - 17 + Math.sin(animTicks * 10) * 15));
        leftHandSprite.setOrigin(47, -(float) (-7 + Math.sin(animTicks * 10) * 15));
        leftHandSprite.setRotation(angle - bodyAngle);
        leftHandSprite.draw(Globals.batch);

        headSprite.setRotation(angle - bodyAngle);
        headSprite.setPosition(pX - Globals.BLOCK_WIDTH / 2, pY - Globals.BLOCK_HEIGHT / 2);
        headSprite.draw(Globals.batch);
    }

    public void addSkullBomb()
    {
        this.skullBombs++;
    }

    public void addBomb()
    {
        this.bombs++;
    }

    public void addMaxBomb()
    {
        this.maxBombCapacity++;
    }

    @Override
    public void onDeath()
    {
        Globals.playSound("explodeelectric", 1.5f, 0.8f);
        ParticleEffectPool.PooledEffect fireEffect;
        for(int i = -1; i < 2; i++)
        {
            for(int j = -1; j < 2; j++)
            {
                fireEffect = ScreenIngame.fireEffectPool.obtain();
                fireEffect.setPosition((getPbX() + i) * Globals.BLOCK_WIDTH + Globals.BLOCK_WIDTH / 2, (getPbY() + j) * Globals.BLOCK_HEIGHT + Globals.BLOCK_HEIGHT/2);
                fireEffect.getEmitters().get(0).getXOffsetValue().setLow(-40, 40);
                fireEffect.getEmitters().get(0).getYOffsetValue().setLow(-40, 40);

                ScreenIngame.effects.add(fireEffect);
            }
        }
        super.onDeath();
    }

    public void addFirePower()
    {
        this.firePower++;
    }

    public void addGoldenFirePower()
    {
        this.firePower += 15;
    }

    public void dropBomb()
    {
        if(bombs > 0)
        {
            if(!this.nodeOn.isBlocked())
            {
                EntityBomb bomb = new EntityBomb(getPbX(), getPbY(), this);
                if(skullBombs > 0)
                {
                    bomb.setSkullBomb(true);
                    this.skullBombs --;
                }
                bomb.spawnEntity();
                bombs--;
                Globals.playSound("dropBomb" + rand.nextInt(3), 0.5f, 1);
            } else if(canChainBomb)
            {
                byte bombsDroped = 0;
                for(byte i = 1; i < bombs + 1; i++)
                {
                    Node adjacentNode = this.getAdjacentNode(getLastMovingDirection(), i);
                    if(adjacentNode.isBlocked() || adjacentNode.getBomberbot() != null)
                    {
                        break;
                    }
                    dropBomb(getLastMovingDirection(), i);
                    bombsDroped ++;
                }
                if(bombsDroped > 0)
                {
                    Globals.playSound("dropBomb" + rand.nextInt(3), 0.5f, 1);
                    bombs -= bombsDroped;
                }
            }
        }
    }

    public void dropBomb(EnumDirection direction, byte distance)
    {
        Node node = this.getAdjacentNode(direction, distance);
        EntityBomb bomb = new EntityBomb(node.getnX(), node.getnY(), this);
        bomb.spawnEntity();
    }

    public byte getFirePower()
    {
        return firePower;
    }

    @Override
    public float getMoveSpeed()
    {
        return super.getMoveSpeed();
    }

    public void setCanChainBomb(boolean canChainBomb)
    {
        this.canChainBomb = canChainBomb;
    }

    public void setCanKickBomb(boolean b)
    {
        this.canKickBomb = b;
    }
}