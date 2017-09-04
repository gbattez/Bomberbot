package bomberbot;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import bomberbot.entity.Entity;
import bomberbot.entity.EntityBlock;
import bomberbot.entity.EntityBomb;
import bomberbot.entity.EntityBomberbot;
import bomberbot.entity.EntityFire;
import bomberbot.entity.EntityAI;
import bomberbot.main.Main;

/**
 * Created by Guillaume on 16/02/2016.
 */
public class Node
{
    private EntityBlock blockOn;
    private List<Entity> entityList = new CopyOnWriteArrayList<Entity>();
    private int nX, nY;
    private int pX, pY;
    private int hCost, gCost;
    private Node parent;
    public boolean drawDebug;

    public int getfCost()
    {
        return hCost + gCost;
    }

    public int gethCost()
    {
        return hCost;
    }

    public void sethCost(int hCost)
    {
        this.hCost = hCost;
    }

    public void setEntityList(List<Entity> entityList)
    {
        this.entityList = entityList;
    }

    public int getgCost()
    {
        return gCost;
    }

    public void setgCost(int gCost)
    {
        this.gCost = gCost;
    }

    public Node getParent()
    {
        return parent;
    }

    public void setParent(Node parent)
    {
        this.parent = parent;
    }

    public int getpX()
    {
        return nX*Globals.BLOCK_WIDTH + Globals.BLOCK_WIDTH/2;
    }

    public int getpY()
    {
        return nY*Globals.BLOCK_HEIGHT + Globals.BLOCK_HEIGHT/2;
    }

    public int getnX()
    {
        return nX;
    }

    public int getnY()
    {
        return nY;
    }

    public EntityBlock getBlockOn()
    {
        return blockOn;
    }

    public List<Entity> getEntityList()
    {
        return entityList;
    }

    public void setBlockOn(EntityBlock blockOn)
    {
        this.blockOn = blockOn;
    }

    public boolean isBurning()
    {
        for(Entity e : this.getEntityList())
        {
            if(e instanceof EntityFire)
            {
                return true;
            }
        }
        return false;
    }

    public EntityBomberbot getBomberbot()
    {
        for(Entity e : this.getEntityList())
        {
            if(e instanceof EntityBomberbot)
            {
                return (EntityBomberbot)e;
            }
        }
        return null;
    }

    public void getNodeContent()
    {
        System.out.println(this.isBurning());
        for(Entity e : entityList)
        {
            System.out.println(entityList.size() + " " + e);
        }
    }

    public EntityBomb getBomb()
    {
        for(Entity e : this.getEntityList())
        {
            if(e instanceof EntityBomb)
            {
                return (EntityBomb)e;
            }
        }
        return null;
    }

    public EntityFire getFire()
    {
        for(Entity e : this.getEntityList())
        {
            if(e instanceof EntityFire)
            {
                return (EntityFire)e;
            }
        }
        return null;
    }

    private boolean dangerous;

    public void setDangerous(boolean b)
    {
        this.dangerous = b;
    }

    public boolean isDangerous()
    {
        return dangerous;
    }

    public void renderBlock(float delta)
    {
        this.blockOn.update(delta);
        this.blockOn.render(delta, Globals.batch);
    }

    public void renderNodeContent(float delta)
    {
        for(Entity e : entityList)
        {
            e.render(delta, Globals.batch);
        }
        if(drawDebug)
        {
            Globals.draw("bbFoot", this.getpX(), this.getpY());
        }
    }

    public void updateNodeContent(float delta)
    {
        for(Entity e : entityList)
        {
            if(!(e instanceof EntityAI))
                e.update(delta);
        }
    }

    public void updateAI(float delta)
    {
        for(Entity e : entityList)
        {
            if(e instanceof EntityAI)
                e.update(delta);
        }
    }

    public boolean isBlockedByBlock()
    {
        return this.blockOn.isMovementBlocker();
    }

    public boolean isBlocked()
    {
        if(this.blockOn.isMovementBlocker())
        {
            return true;
        }
        for(Entity entity : entityList)
        {
            if(entity.isMovementBlocker())
            {
                return true;
            }
        }
        return false;
    }

    public Entity getBlockingEntity()
    {
        if(this.blockOn.isMovementBlocker())
        {
            return this.blockOn;
        }
        for(Entity entity : entityList)
        {
            if(entity.isMovementBlocker())
            {
                return entity;
            }
        }
        return null;
    }

    public Node(int nX, int nY)
    {
        this.nX = nX;
        this.nY = nY;
        this.pX = nX*Globals.BLOCK_WIDTH + Globals.BLOCK_WIDTH/2;
        this.pY = nY*Globals.BLOCK_HEIGHT + Globals.BLOCK_HEIGHT/2;
    }

    public void removeEntity(Entity entity)
    {
        this.entityList.remove(entity);
    }

    public void addEntity(Entity entity)
    {
        this.entityList.add(entity);
    }
}
