package bomberbot.entity;

import java.util.ArrayList;
import java.util.List;

import bomberbot.Globals;

public class EntityPowerup extends Entity
{
    public enum EnumPowerup
    {
        FIRE(1), BOMB(1), SPEED(1),
        KICK(2), CHAINBOMB(2),
        GOLDENFIRE(3), BOMBSKULL(3);

        private int rarity;

        public int getRarity()
        {
            return rarity;
        }

        public static final List<EnumPowerup> COMMONPOWERUPS = new ArrayList<EnumPowerup>();
        public static final List<EnumPowerup> RAREPOWERUPS = new ArrayList<EnumPowerup>();
        public static final List<EnumPowerup> EPICPOWERUPS = new ArrayList<EnumPowerup>();

        static
        {
            for(EnumPowerup value : EnumPowerup.values())
            {
                switch(value.rarity)
                {
                    case 2 : RAREPOWERUPS.add(value); break;
                    case 3 : EPICPOWERUPS.add(value); break;
                    default : COMMONPOWERUPS.add(value); break;
                }
            }
        }

        EnumPowerup(int rarity)
        {
            this.rarity = rarity;
        }

        public String getTextureSrc()
        {
            String src = "pu" + super.toString();
            src = src.toLowerCase();
            return src;
        }

        public void runEffect(EnumPowerup powerType, EntityBomberbot bomberbot)
        {
            switch (powerType)
            {
                case FIRE : bomberbot.addFirePower(); break;
                case BOMB : bomberbot.addBomb(); bomberbot.addMaxBomb(); break;
                case SPEED: bomberbot.addMoveSpeed(); break;
                case KICK: bomberbot.setCanKickBomb(true); break;
                case GOLDENFIRE: bomberbot.setMaxFirePower(); break;
                case CHAINBOMB: bomberbot.setCanChainBomb(true); break;
                case BOMBSKULL: bomberbot.setHasSkullBomb(true); break;
            }
        }
    }

    private EnumPowerup powerType;

    public EntityPowerup(int pX, int pY, EnumPowerup powerType)
    {
        super(pX, pY);
        this.powerType = powerType;
        this.setDestructible(true);
        this.textureSrc = powerType.getTextureSrc();
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);

        for(Entity e : this.nodeOn.getEntityList())
        {
            if(e instanceof EntityBomberbot)
            {
                Globals.playSound("pupickup", 0.25f, 1);
                this.powerType.runEffect(this.powerType, (EntityBomberbot) e);
                this.deleteEntity();
            }
        }
    }
}