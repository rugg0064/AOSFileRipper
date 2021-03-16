public class Color
{
    public int r,g,b,a;
    public Color()
    {
        r = g = b = a = 0;
    }
    public String toString()
    {
        return String.format("[%03d,%03d,%03d,%03d]", r, g, b, a);
    }
    public boolean isAir()
    {
        return r == -1 && g == -1 && b == -1 && a == -1;
    }
    public boolean isColor()
    {
        return r >= 0 && g >= 0 && b >= 0 && a >= 0;
    }
    public boolean isSolid()
    {
        return r == -2 && g == -2 && b == -2 && a == -2;
    }
}