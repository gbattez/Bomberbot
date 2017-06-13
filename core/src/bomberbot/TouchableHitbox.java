package bomberbot;

/**
 * Created by Guillaume on 02/05/2016.
 */
public class TouchableHitbox implements ITouchable
{
    private OnTouchListener onTouchListener;

    public int getX()
    {
        return 0;
    }

    public int getY()
    {
        return 0;
    }

    public int getWidth()
    {
        return 0;
    }

    public int getHeight()
    {
        return 0;
    }



    @Override
    public void setOntouchListener(OnTouchListener onTouchListener)
    {
        this.onTouchListener = onTouchListener;
    }
}