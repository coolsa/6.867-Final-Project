package ch.idsia.benchmark.mario.engine.level;

import java.util.Random;

import ch.idsia.benchmark.mario.engine.level.Level;
import ch.idsia.tools.RandomCreatureGenerator;

public class Straight implements LevelComponent{
	
	int xo; 
	int maxLength;
	boolean safe;
	int vfloor;
	int floorHeight;
	
	
	
	public Straight(int xo, int maxLength, boolean safe, int vfloor, int floorHeight) {
		super();
		this.xo = xo;
		this.maxLength = maxLength;
		this.safe = safe;
		this.vfloor = vfloor;
		this.floorHeight = floorHeight;
	}



	private int buildStraight(int xo, int maxLength, boolean safe, int vfloor, int floorHeight)
	{
		
		SingletonLevelElements i = SingletonLevelElements.getInstance();
		
	    int length;
	    if (floorHeight != SingletonLevelElements.INFINITE_FLOOR_HEIGHT)
	    {
	        length = maxLength;
	    } else
	    {
	        length = SingletonLevelElements.globalRandom.nextInt(8) + 2;//globalRandom.nextInt(50)+1) + 2;
	        if (safe) length = 10 + SingletonLevelElements.globalRandom.nextInt(5);
	        if (length > maxLength) length = maxLength;
	    }


	    int floor = vfloor;
	    if (vfloor == SingletonLevelElements.DEFAULT_FLOOR)
	    {
	        floor = i.getHeight() - 1 - SingletonLevelElements.globalRandom.nextInt(4);
	    } else
	    {
	    	SingletonLevelElements.globalRandom.nextInt();
	    }

	    int y1 = i.getHeight();
	    if (floorHeight != SingletonLevelElements.INFINITE_FLOOR_HEIGHT)
	    {
	        y1 = floor + floorHeight;
	    }

	    for (int x = xo; x < xo + length; x++)
	        for (int y = floor; y < y1; y++)
	            if (y >= floor)
	            	i.getLevel().setBlock(x, y, (byte) (1 + 9 * 16));

	    if (!safe)
	    {
	        if (length > 5)
	        {
	            //decorate(xo, xo + length, floor);
	        }
	    }

	    return length;
	}
	
	@Override
	public int build() {
		return buildStraight( xo,  maxLength,  safe,  vfloor,  floorHeight);
	}


}
