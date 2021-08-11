import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

// starter

public class EmptyMap
{
    static final int CANVAS_HEIGHT = 600; // always 600


    public static void main(String[] args) throws Exception
    {
        StdDraw.enableDoubleBuffering();
        visualize("GA");
        StdDraw.show();
    }

    private static void visualize(String region) throws Exception
    {
        String str = "./input/" + region + ".txt";
        File f = new File(str);
        Scanner s = new Scanner(f);
        
        String[] minString = s.nextLine().split("   ");
        double[] min = new double[2];
        min[0] = Double.parseDouble(minString[0]);
        min[1] = Double.parseDouble(minString[1]);

        String[] maxString = s.nextLine().split("   ");
        double[] max = new double[2];
        max[0] = Double.parseDouble(maxString[0]);
        max[1] = Double.parseDouble(maxString[1]);
        
        //System.out.println("Long " + max[0] + "   " + min[0]);
        
        double ratio = (min[0] - max[0]) / (min[1] - max[1]);
        //System.out.print( (int) (CANVAS_HEIGHT*ratio));
        
        
        double canvasWidth = (int) (CANVAS_HEIGHT*ratio);
        StdDraw.setCanvasSize( (int) (CANVAS_HEIGHT*ratio), CANVAS_HEIGHT);
        StdDraw.setXscale(0, (int) (CANVAS_HEIGHT*ratio));
        StdDraw.setYscale(0, CANVAS_HEIGHT);
        
        int parts = Integer.parseInt(s.nextLine());
        
        for(int i = 0; i < parts; i++)
        {
            s.nextLine();
            s.nextLine();
            s.nextLine();
            int trials = Integer.parseInt(s.nextLine());
            
            double[] longitude = new double[trials];
            double[] latitude = new double[trials];
            for(int j = 0; j < trials; j++)
            {
                String[] line = s.nextLine().split("   ");
                longitude[j] = Double.parseDouble(line[0]);
                latitude[j] = Double.parseDouble(line[1]);
            }
            
            for(int k = 0; k < trials; k++)
            {
                double z2x = canvasWidth;
                double x1x = longitude[k] - min[0];
                double x2x = max[0] - min[0];
                
                longitude[k] = (x1x*z2x)/(x2x);
                
                double z2y = CANVAS_HEIGHT;
                double y1y = latitude[k] - min[1];
                double y2y = max[1] - min[1];
                
                latitude[k] = (z2y*y1y)/(y2y);
            }
            StdDraw.polygon(longitude, latitude);
        }
    }

}
