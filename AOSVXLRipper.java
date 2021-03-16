import java.io.IOException;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.util.ArrayList;

import java.io.ObjectOutputStream;
import java.io.FileOutputStream;

public class AOSVXLRipper
{
    public static void main(String[] args)
    {
        if(args.length > 0)
        {
            System.out.println(args[0]);
            File f = new File(args[0]);
            System.out.println(f);
            ArrayList<ArrayList<ArrayList<Span>>> spans = fileToSpanArray(f);
            Color[][][] blocks = spanArrayToColorArray(spans);
            
            try {
                File f2 = new File("out.javaobj");
                FileOutputStream fos = new FileOutputStream(f2);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(spans);
            } catch (Exception e) {
                //TODO: handle exception
            }
            
            ArrayList<Span>[][] newSpans = colorArrayToSpanArray(blocks);

        }
        else
        {
            System.out.println("No file supplied");
        }
    }

    public static ArrayList<Span>[][] colorArrayToSpanArray(Color[][][] colors)
    {
        ArrayList<Span>[][] spans = new ArrayList[512][512];
        for(int x = 0; x < 512; x++)
        {
            for(int y = 0; y < 512; y++)
            {
                spans[x][y] = new ArrayList<Span>();
                ArrayList<Color> colorList = new ArrayList<Color>();
                int z = 0;

                int a = 0;
                int s = 0;
                int e = 0;

                while(z < 64)
                {
                    System.out.printf("%n (%d, %d) %n", x, y);
                    System.out.print(0);

                    a = z;

                    while(colors[x][y][z].isAir())
                    {
                        System.out.print(1);
                        z++;
                    }

                    //Just entered the first colored block
                    s = z;
                    //colorList.add(colors[x][y][z]);
                    while(colors[x][y][z].isColor())
                    {
                        colorList.add(colors[x][y][z]);
                        System.out.print(3);
                        z++;
                    }
                    e = z-1;
                    while(!colors[x][y][z].isColor())
                    {
                        System.out.print(4);
                        z++;
                    }
                    
                    while(colors[x][y][z].isColor())
                    {
                        System.out.print(5);
                        colorList.add(colors[x][y][z]);
                        z++;
                    }
                }
                Span sp = new Span();
                sp.s = s;
                sp.a = a;
                sp.e = e;
                sp.colors = colorList;
                sp.n = (1 + sp.colors.size());
            }
        }

        return spans;
    }

    public static Color[][][] spanArrayToColorArray(ArrayList<ArrayList<ArrayList<Span>>> spans)
    {
        Color[][][] blocks = new Color[512][512][64];

        for(int x = 0; x < 512; x++)
        {
            for(int y = 0; y < 512; y++)
            {
                int z = 0;
                for(int i = 0 ; i < spans.get(x).get(y).size(); i++)
                {
                    Span s = spans.get(x).get(y).get(i);
                    while(z < s.s)
                    {
                        Color c = new Color();
                        c.r = c.g = c.b = c.a = -1;
                        blocks[x][y][z] = c;
                        z++;
                    } 
                    //now Z is on S
                    int colorCounter = 0;
                    for(z = s.s; z <= s.e; z++)
                    {
                        blocks[x][y][z] = s.colors.get(colorCounter);
                        colorCounter++;
                    }
                    //now Z is on E
                    int m = 0; 
                    int z1 = 0;
                    if(s.n == 0)
                    { // M = 64
                        m = 64;
                    }
                    else
                    {
                        m = spans.get(x).get(y).get(i+1).a;
                        int k = s.e - s.s + 1;
                        z1 = s.n - 1 - k;
                    }
                    for(z = s.e+1; z <= m-z1-1; z++)
                    {
                        Color c = new Color();
                        c.r = c.g = c.b = c.a = -2;
                        blocks[x][y][z] = c;
                    }

                    for(z = m-z1; z <= m-1; z++)
                    {
                        blocks[x][y][z] = s.colors.get(colorCounter);
                        colorCounter++;
                    }
                }
            }
        }

        return blocks;
    }

    public static ArrayList<ArrayList<ArrayList<Span>>> fileToSpanArray(File f)
    {
        try
        {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
            
            //ArrayList<Span> spans = new ArrayList<Span>();
            ArrayList<ArrayList<ArrayList<Span>>> spans = new ArrayList<ArrayList<ArrayList<Span>>>();
            for(int x = 0; x < 512; x++)
            {
                spans.add(new ArrayList<ArrayList<Span>>());
                for(int y = 0; y < 512; y++)
                {
                    spans.get(x).add(new ArrayList<Span>());
                }
            }

            int x = 0;
            int y = 0;

            int nextByte = 0;
            while( (nextByte = bis.read()) != -1)
            {
                Span s = new Span();
                s.n = nextByte;
                s.s = bis.read();
                s.e = bis.read();
                s.a = bis.read();

                ArrayList<Color> colors = s.colors;
                boolean mustIncr = false;
                if(s.n == 0)
                {
                    for(int i = 0; i < (s.e - s.s) + 1; i++)
                    {
                        Color color = new Color();
                        color.r = bis.read();
                        color.g = bis.read();
                        color.b = bis.read();
                        color.a = bis.read();
                        colors.add(color);
                    }

                    mustIncr = true;
                }
                else
                {
                    for(int i = 0; i < s.n-1; i++)
                    {
                        Color color = new Color();
                        color.r = bis.read();
                        color.g = bis.read();
                        color.b = bis.read();
                        color.a = bis.read();
                        colors.add(color);
                    }
                }
                spans.get(x).get(y).add(s);

                if(mustIncr)
                {
                    x++;
                    if(x >= 512)
                    {
                        x -= 512;
                        y++;
                    }
                }
            }

            return spans;
        }
        catch(IOException e)
        {
            System.out.printf("Error: %s%n", e);
        }
        return null;
    }
}