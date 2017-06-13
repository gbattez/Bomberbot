package bomberbot.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import bomberbot.EnumDirection;
import bomberbot.Globals;
import bomberbot.main.Main;
import bomberbot.Node;
import bomberbot.screen.ScreenIngame;

public abstract class Entity
{
    protected float pX, pY;
    private int pbX, pbY;
    protected float vX, vY;
    protected float ticks;
    protected Node nodeOn, prevNodeOn;
    protected String textureSrc;
    protected boolean enableTicks;
    private boolean destructible;
    protected float moveSpeed;
    private boolean movementBlocker;
    private EnumDirection movingDirection = EnumDirection.NONE;
    public Random rand = new Random();
    public boolean drawDebug;
    private EnumDirection lastMovingDirection = EnumDirection.NONE;

    public Entity(int pbX, int pbY)
    {
        this.setPbX(pbX);
        this.setPbY(pbY);
        this.nodeOn = ScreenIngame.nodes[pbX][pbY];
    }

    public void setMovementBlocker(boolean movementBlocker)
    {
        this.movementBlocker = movementBlocker;
    }

    public void setLastMovingDirection(EnumDirection lastMovingDirection)
    {
        this.lastMovingDirection = lastMovingDirection;
    }

    public boolean isMovementBlocker()
    {
        return movementBlocker;
    }

    public boolean isDestructible()
    {
        return destructible;
    }

    public void setDestructible(boolean destructible)
    {
        this.destructible = destructible;
    }

    public int getPbX()
    {
        return pbX;
    }

    public int getPbY()
    {
        return pbY;
    }

    public void setPbX(int pbX)
    {
        this.pbX = pbX;
        this.pX = pbX * Globals.BLOCK_WIDTH + Globals.BLOCK_WIDTH/2;
    }

    public void setPbY(int pbY)
    {
        this.pbY = pbY;
        this.pY = pbY*Globals.BLOCK_HEIGHT + Globals.BLOCK_HEIGHT/2;
    }

    public void setpX(float pX)
    {
        this.pX = pX + Globals.BLOCK_WIDTH/2;
    }

    public void setpY(float pY)
    {
        this.pY = pY + Globals.BLOCK_HEIGHT/2;
    }

    public void render(float delta, SpriteBatch batch)
    {
        if (textureSrc != null)
        {
            Globals.draw(textureSrc, pX - Globals.BLOCK_WIDTH / 2, pY - Globals.BLOCK_HEIGHT / 2);
        }

        if(drawDebug)
        {
            Globals.draw("firetest", pX, pY);
        }
    }

    public void move(EnumDirection direction)
    {
        if(direction != EnumDirection.NONE)
        {
            lastMovingDirection = direction;
        }
        this.movingDirection = direction;
    }

    public EnumDirection getLastMovingDirection()
    {
        return lastMovingDirection;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public float getpY() {
        return pY;
    }

    public float getpX() {
        return pX;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void addMoveSpeed()
    {
        this.moveSpeed += 0.03f;
    }

    public boolean movementBlocked(EnumDirection direction)
    {
        Node adjNode = this.getAdjacentNode(direction);
        return adjNode.isBlocked() && this.getDistanceInPixels(adjNode.getpX(), adjNode.getpY()) < Globals.BLOCK_HEIGHT;
    }

    public List<Node> getAdjacentNodes()
    {
        List<Node> adjacentNodes = new CopyOnWriteArrayList<Node>();
        for(EnumDirection direction1 : EnumDirection.values())
        {
            if(direction1 == EnumDirection.NONE || direction1 == EnumDirection.ALL)
                continue;
            adjacentNodes.add(getAdjacentNode(direction1));
        }
        return adjacentNodes;
    }

    public Node getAdjacentNode(EnumDirection direction, byte distance)
    {
        switch (direction)
        {
            case NORTH : if(this.getPbY() + distance < 11) return ScreenIngame.nodes[this.getPbX()][this.getPbY() + distance];
            case EAST : if(this.getPbX() + distance < 20) return ScreenIngame.nodes[this.getPbX() + distance][this.getPbY()];
            case SOUTH : if(this.getPbY() - distance >= 0) return ScreenIngame.nodes[this.getPbX()][this.getPbY() - distance];
            case WEST : if(this.getPbX() - distance >= 0) return ScreenIngame.nodes[this.getPbX() - distance][this.getPbY()];
        }
        return null;
    }

    public Node getAdjacentNode(EnumDirection direction)
    {
        switch (direction)
        {
            case NORTH : if(this.getPbY() + 1 < 11) return ScreenIngame.nodes[this.getPbX()][this.getPbY() + 1];
            case EAST : if(this.getPbX() + 1 < 20) return ScreenIngame.nodes[this.getPbX() + 1][this.getPbY()];
            case SOUTH : if(this.getPbY() - 1 >= 0) return ScreenIngame.nodes[this.getPbX()][this.getPbY() - 1];
            case WEST : if(this.getPbX() - 1 >= 0) return ScreenIngame.nodes[this.getPbX() - 1][this.getPbY()];
        }
        return null;
    }

    public void onNodeChange(Node prevNode, Node newNode)
    {
        if(prevNode != null)
        {
            prevNode.removeEntity(this);
        }
        if(!newNode.getEntityList().contains(this))
        {
            newNode.addEntity(this);
        }
        this.prevNodeOn = this.nodeOn;
    }

    public void update(float delta)
    {
        this.nodeOn = ScreenIngame.nodes[this.getPbX()][this.getPbY()];

        if(this.prevNodeOn != this.nodeOn)
        {
            this.onNodeChange(prevNodeOn, nodeOn);
        }

        if(this.enableTicks)
        {
            this.ticks += delta;
        }

        if(this.isDestructible() && this.nodeOn.getFire() != null)
        {
            this.onDeath();
        }

        switch (movingDirection)
        {
            case NORTH :

                vX = 0;
                vY = 1;
                pX = MathUtils.lerp(pX, pbX*Globals.BLOCK_WIDTH + Globals.BLOCK_WIDTH/2, 0.2f);
                break;
            case EAST :
                vX = 1;
                vY = 0;
                pY = MathUtils.lerp(pY, pbY*Globals.BLOCK_HEIGHT + Globals.BLOCK_HEIGHT/2, 0.2f);
                break;
            case SOUTH :
                vX = 0;
                vY = -1;
                pX = MathUtils.lerp(pX, pbX*Globals.BLOCK_WIDTH + Globals.BLOCK_WIDTH/2, 0.2f);
                break;
            case WEST :
                vX = -1;
                vY = 0;
                pY = MathUtils.lerp(pY, pbY*Globals.BLOCK_HEIGHT + Globals.BLOCK_HEIGHT/2, 0.2f);
                break;
            case NONE :
            {
                vX = MathUtils.lerp(vX, 0, 0.4f);
                vY = MathUtils.lerp(vY, 0, 0.4f);
            } break;
        }
        if(vX > 0 && movementBlocked(EnumDirection.EAST))
        {
            vX = 0;
        }
        if(vX < 0 && movementBlocked(EnumDirection.WEST))
        {
            vX = 0;
        }
        if(vY > 0 && movementBlocked(EnumDirection.NORTH))
        {
            vY = 0;
        }
        if(vY < 0 && movementBlocked(EnumDirection.SOUTH))
        {
            vY = 0;
        }

        this.pbX = (int) (pX / 96);
        this.pbY = (int) (pY / 98);

        this.pX += vX * (delta * 1000) * moveSpeed;
        this.pY += vY * (delta * 1000) * moveSpeed;
    }

    public boolean isMoving()
    {
        return movingDirection != EnumDirection.NONE && movingDirection != EnumDirection.ALL;
    }

    public EnumDirection getMovingDirection()
    {
        return movingDirection;
    }

    public void onDeath()
    {
        deleteEntity();
    }

    public float getDistanceInPixels(float pX, float pY, Entity entity)
    {
        return (float)Math.sqrt(Math.pow(entity.pX - pX, 2) + Math.pow(entity.pY - pY, 2));
    }

    public float getDistanceInPixels(float pX, float pY)
    {
        return (float)Math.sqrt(Math.pow(pX - this.pX, 2) + Math.pow(pY - this.pY, 2));
    }

    public float getDistanceInPixels(Entity entity)
    {
        return (float)Math.sqrt(Math.pow(entity.pX - this.pX, 2) + Math.pow(entity.pY - this.pY, 2));
    }

    public float getDistanceInBlocks(Entity entity)
    {
        return (float)Math.sqrt(Math.pow(entity.pbX - this.pbX, 2) + Math.pow(entity.pbY - this.pbY, 2));
    }

    public void spawnEntity()
    {
        this.nodeOn.addEntity(this);
    }

    public void deleteEntity()
    {
        ScreenIngame.nodes[this.getPbX()][this.getPbY()].removeEntity(this);
    }
}