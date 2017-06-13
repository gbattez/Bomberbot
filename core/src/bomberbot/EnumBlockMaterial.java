package bomberbot;

public enum EnumBlockMaterial
{
    GRASS(false),
    BRICK(true),
    METAL(true);

    private boolean movementBlocker;

    EnumBlockMaterial(boolean movementBlocker)
    {
        this.movementBlocker = movementBlocker;
    }

    public String getTextureSrc()
    {
        String src = "block" + super.toString();
        src = src.toLowerCase();

        return src;
    }

    public boolean isMovementBlocker()
    {
        return this.movementBlocker;
    }
}