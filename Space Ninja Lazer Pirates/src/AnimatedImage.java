

import java.io.Serializable;

import javafx.scene.image.Image;

public class AnimatedImage implements Serializable
{
    public Image[] frames;
    public double duration;

    public Image getFrame(double time)
    {
        int index = (int)((time % (frames.length * duration)) / duration);
        return frames[index];
    }
}
