package bomberbot;

/**
 * Created by Guillaume on 02/05/2016.
 */
public interface ITouchable
{
    void setOntouchListener(OnTouchListener onTouchListener);

    interface OnTouchListener
    {
        void onTouch();
    }
}