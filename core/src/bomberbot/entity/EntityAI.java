package bomberbot.entity;

import com.badlogic.gdx.graphics.Color;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import bomberbot.*;
import bomberbot.screen.ScreenIngame;

public class EntityAI extends EntityBomberbot
{
    private int randMove;
    private float waitBeforeMove;
    private Node targetNode;
    private Node[][] nodesCopy;
    private List<Node> open = new CopyOnWriteArrayList<Node>();
    private List<Node> closed = new CopyOnWriteArrayList<Node>();
    private List<Node> path = new CopyOnWriteArrayList<Node>();
    private EnumDirection directionToPath;
    private boolean pathNotFound;

    public EntityAI(int pX, int pY, Color color)
    {
        super(pX, pY, color);
        this.directionToPath = EnumDirection.NONE;
        this.nodesCopy = ScreenIngame.nodes;
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);
        this.move(EnumDirection.NONE);

        for(int i = 0; i < 20; i++)
        {
            for (int j = 0; j < 11; j++)
            {
                this.nodesCopy[i][j].setBlockOn(ScreenIngame.nodes[i][j].getBlockOn());
                this.nodesCopy[i][j].setEntityList(ScreenIngame.nodes[i][j].getEntityList());
            }
        }

        //MOVE
        if(!this.isInDanger())
        {
            if(pathNotFound)
            {
               pathNotFound = false;
            }
            this.waitBeforeMove -= delta;
            if(this.waitBeforeMove > 0)
            {
                return;
            }

            this.targetNode = null;
            this.path.clear();
            if (rand.nextInt(200) == 0)
            {
                randMove = rand.nextInt(4);
            }
            switch (randMove)
            {
                case 0:
                {
                    if (shouldChangeDirection(EnumDirection.EAST))
                    {
                        randMove = (rand.nextInt(3) + 1) % 4;
                    } else
                    {
                        this.move(EnumDirection.EAST);
                    }
                }
                break;
                case 1:
                {
                    if (shouldChangeDirection(EnumDirection.WEST))
                    {
                        randMove = (rand.nextInt(3) + 2) % 4;
                    } else
                    {
                        this.move(EnumDirection.WEST);
                    }
                }
                break;
                case 2:
                {
                    if (shouldChangeDirection(EnumDirection.NORTH))
                    {
                        randMove = (rand.nextInt(3) + 3) % 4;
                    } else
                    {
                        this.move(EnumDirection.NORTH);
                    }
                }
                break;
                case 3:
                {
                    if (shouldChangeDirection(EnumDirection.SOUTH))
                    {
                        randMove = (rand.nextInt(3) + 4) % 4;
                    } else
                    {
                        this.move(EnumDirection.SOUTH);
                    }
                }
                break;
            }

            //DROP BOMB
            if (this.isBomberBotNearby())
            {
                this.dropBomb();
            }

            for (EnumDirection directions : EnumDirection.values())
            {
                if (directions != EnumDirection.NONE && directions != EnumDirection.ALL)
                {
                    EntityBlock block = this.getAdjacentNode(directions).getBlockOn();
                    if (block != null && block.getMaterial() == bomberbot.EnumBlockMaterial.BRICK)
                    {
                        for(EnumDirection curDirection : EnumDirection.values())
                        {
                            for(byte i = 0; i < this.getFirePower()+1; i++)
                            {
                                Node adjacentNode = this.getAdjacentNode(curDirection, i);

                                if(adjacentNode != null)
                                {
                                    if(adjacentNode.isBlocked())
                                    {
                                        break;
                                    }
                                    if(adjacentNode.isDangerous())
                                    {
                                        return;
                                    }
                                }
                            }
                        }
                        this.dropBomb();
                    }
                }
            }
        }
        //DANGER
        else
        {
            if(targetNode == null)
            {
                if(pathNotFound)
                {
                    if(rand.nextInt(40) == 0)
                    {
                        searchSecureNode();
                    }
                } else
                {
                    searchSecureNode();
                }
            } else
            {
                if(targetNode.isDangerous())
                {
                    targetNode = null;
                }
                for(Node p : path)
                {
                    if(p.isBlocked())
                    {
                        targetNode = null;
                        break;
                    }
                }
            }
            if(path.size() > 0)
            {
                Node nodeToGo = path.get(path.size() - 1);
                if(this.getPbX() > nodeToGo.getnX())
                {
                    directionToPath = EnumDirection.WEST;
                }
                if(this.getPbX() < nodeToGo.getnX())
                {
                    directionToPath = EnumDirection.EAST;
                }
                if(this.getPbY() > nodeToGo.getnY())
                {
                    directionToPath = EnumDirection.SOUTH;
                }
                if(this.getPbY() < nodeToGo.getnY())
                {
                    directionToPath = EnumDirection.NORTH;
                }
                move(directionToPath);
                if(this.nodeOn == nodeToGo)
                {
                    path.remove(nodeToGo);
                }

            }
            this.waitBeforeMove = 0.1f;
        }
    }

    public void searchSecureNode()
    {
        byte searchDistance = 1;
        boolean foundSecureNode = false;
        while(searchDistance < 14 && !foundSecureNode)
        {
            System.out.println(System.currentTimeMillis() + "Searching..." + searchDistance);
            for(int i = -searchDistance; i <= searchDistance; i++)
            {
                for(int j = -searchDistance; j <= searchDistance; j++)
                {
                    if(i+getPbX() < 0 || i+getPbX() >= 20 || j+getPbY() < 0 || j+getPbY() >= 11)
                    {
                        continue;
                    }
                    if(!ScreenIngame.nodes[i+getPbX()][j+getPbY()].isBlocked() && !ScreenIngame.nodes[i+getPbX()][j+getPbY()].isDangerous())
                    {
                        Node nodeToTest = ScreenIngame.nodes[i+getPbX()][j+getPbY()];
                        doAStar(nodeToTest);
                        if(path.size() > 0)
                        {
                            foundSecureNode = true;
                            this.targetNode = nodeToTest;
                            break;
                        }
                    }
                }
                if(foundSecureNode)
                {
                    break;
                }
            }
            searchDistance++;
        }
        if(!foundSecureNode)
        {
            pathNotFound = true;
        }
    }

    public Node openNodeWithLowestFCost()
    {
        Node node = open.get(0);

        for(Node n : open)
        {
            if(n.getfCost() < node.getfCost() || n.getfCost() == node.getfCost() && n.gethCost() < node.gethCost())
            {
                node = n;
            }
        }
        return node;
    }

    public void retracePath(Node startNode, Node endNode)
    {
        List<Node> path = new CopyOnWriteArrayList<Node>();
        Node currentNode = endNode;
        while(currentNode != startNode)
        {
            path.add(currentNode);
            currentNode = currentNode.getParent();
        }
        this.path = path;
    }

    public void doAStar(Node targetNode)
    {
        open.clear();
        closed.clear();
        path.clear();
        this.open.add(nodeOn);
        while(open.size() > 0)
        {
            Node current = openNodeWithLowestFCost();
            open.remove(current);
            closed.add(current);
            if(current == targetNode)
            {
                retracePath(nodeOn, targetNode);
                return;
            }

            List<Node> neighbours = new CopyOnWriteArrayList<Node>();
            if(current.getnY() + 1 < 20)
            {
                neighbours.add(ScreenIngame.nodes[current.getnX()][current.getnY() + 1]);
            }
            if(current.getnY() - 1 >= 0)
            {
                neighbours.add(ScreenIngame.nodes[current.getnX()][current.getnY() - 1]);
            }
            if(current.getnX() + 1 < 20)
            {
                neighbours.add(ScreenIngame.nodes[current.getnX() + 1][current.getnY()]);
            }
            if(current.getnX() - 1 >= 0)
            {
                neighbours.add(ScreenIngame.nodes[current.getnX() - 1][current.getnY()]);
            }

            for (Node n : neighbours)
            {
                if(n.isBlocked() || closed.contains(n))
                {
                    continue;
                }

                int newMovementCostToNeighbour = current.getgCost() + this.getDistanceBetweenNodes(current, n);
                if(newMovementCostToNeighbour < n.getgCost() || !closed.contains(n))
                {
                    n.setgCost(newMovementCostToNeighbour);
                    n.sethCost(this.getDistanceBetweenNodes(n, targetNode));
                    n.setParent(current);

                    if(!open.contains(n))
                    {
                        open.add(n);
                    }
                }
            }
        }
    }

    public int getDistanceBetweenNodes(Node nodeA, Node nodeB)
    {
        return Math.abs(nodeA.getnX() - nodeB.getnX()) + Math.abs(nodeA.getnY() - nodeB.getnY());
    }

    public boolean shouldChangeDirection(EnumDirection direction)
    {
        EntityBlock block = this.getAdjacentNode(direction).getBlockOn();
        return block != null && block.getMaterial().isMovementBlocker() || this.getAdjacentNode(direction).isDangerous();
    }

    @Override
    public void onDeath()
    {
        if(this.nodeOn.getFire().getOwner() instanceof EntityPlayer)
        {
            ScreenIngame.player.getScore().addKillScore();
        }
        ScreenIngame.aiNumber --;
        super.onDeath();
    }

    public boolean isInDanger()
    {
        return this.nodeOn.isDangerous();
    }

    public boolean isBomberBotNearby()
    {
        for (EnumDirection directions : EnumDirection.values())
        {
            if(directions != EnumDirection.ALL && directions != EnumDirection.NONE)
            {
                for(byte distance = 1; distance < this.getFirePower() + 1; distance++)
                {
                    Node adjacentNode = this.getAdjacentNode(directions, distance);

                    if(adjacentNode.isBlocked() || adjacentNode.isDangerous())
                    {
                        break;
                    }
                    if(adjacentNode.getBomberbot() != null && adjacentNode.getBomberbot() != this)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}