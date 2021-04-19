package ch.idsia.benchmark.mario.engine.levelBuilderPattern;

import java.util.Random;

import ch.idsia.benchmark.mario.engine.level.Level;
import ch.idsia.tools.RandomCreatureGenerator;

public class Cannon implements LevelComponent{
	
	private int xo;
	private int maxLength;
	private int maxHeight;
	private int vfloor;
	private int floorHeight;
	
	
	
	public Cannon(int xo, int maxLength, int maxHeight, int vfloor, int floorHeight) {
		super();
		this.xo = xo;
		this.maxLength = maxLength;
		this.maxHeight = maxHeight;
		this.vfloor = vfloor;
		this.floorHeight = floorHeight;
	}



	private int buildCannons(int xo, int maxLength, int maxHeight, int vfloor, int floorHeight)
	{
	    
		SingletonLevelElements i = SingletonLevelElements.getInstance();
		
		int maxCannonHeight = 0;
	    int length = SingletonLevelElements.globalRandom.nextInt(10) + 2;
	    if (length > maxLength) length = maxLength;

	    int floor = vfloor;
	    if (vfloor == SingletonLevelElements.DEFAULT_FLOOR)
	    {
	        floor = i.getHeight() - 1 - SingletonLevelElements.globalRandom.nextInt(4);
	    } else
	    {
	    	SingletonLevelElements.globalRandom.nextInt();
	    }

	    if (floorHeight == SingletonLevelElements.INFINITE_FLOOR_HEIGHT)
	    {
	        floorHeight = i.getHeight() - floor;
	    }

	    int oldXCannon = -1;

	    int xCannon = xo + 1 + SingletonLevelElements.globalRandom.nextInt(4);
	    for (int x = xo; x < xo + length; x++)
	    {
	        if (x > xCannon)
	        {
	            xCannon += 2 + SingletonLevelElements.globalRandom.nextInt(4);
	            SingletonLevelElements.counters.cannonsCount++;
	        }
	        if (xCannon == xo + length - 1)
	        {
	            xCannon += 10;
	        }

	        int cannonHeight = floor - SingletonLevelElements.globalRandom.nextInt(3) - 1; //cannon height is a Y coordinate of top part of the cannon
	        if (maxHeight != SingletonLevelElements.ANY_HEIGHT)
	        {
	            //maxHeight -= 2;
	            if (floor - cannonHeight >= maxHeight)
	            {
	                if (maxHeight > 4)
	                {
	                    maxHeight = 4;
	                }
	                while (floor - cannonHeight > maxHeight)
	                {
	                    cannonHeight++;
	                }
	            }
	            if (cannonHeight > maxCannonHeight)
	                maxCannonHeight = cannonHeight;
	        }

	        for (int y = 0; y < i.getHeight(); y++)
	        {
	            if (y >= floor && y <= floor + floorHeight)
	            {
	                i.getLevel().setBlock(x, y, (byte) (1 + 9 * 16));
	            } else if (SingletonLevelElements.counters.cannonsCount <= SingletonLevelElements.counters.totalCannons)
	            {
	                if (x == xCannon && y >= cannonHeight && y <= floor)// + floorHeight)
	                {
	                    if (y == cannonHeight)
	                    {
	                        if (oldXCannon != -1 && SingletonLevelElements.creaturesRandom.nextInt(35) > i.getLevelDifficulty() + 1)
	                        {
//	                            addEnemiesLine(oldXCannon + 1, xCannon - 1, floor - 1);
	                        }
	                        oldXCannon = x;
	                        i.getLevel().setBlock(x, y, (byte) (14 + 0 * 16));   // cannon barrel
	                    } else if (y == cannonHeight + 1)
	                    {
	                    	i.getLevel().setBlock(x, y, (byte) (14 + 1 * 16));   // base for cannon barrel
	                    } else
	                    {
	                    	i.getLevel().setBlock(x, y, (byte) (14 + 2 * 16));   // cannon pole
	                    }
	                }
	            }
	        }
	    }

	    if (SingletonLevelElements.globalRandom.nextBoolean())
	        LevelBuilder.buildBlocks(xo, xo + length, floor - maxCannonHeight - 2, false, 0, 0, false, false);

	    return length;
	}
	
	@Override 
	public int build() {
		return buildCannons( xo,  maxLength,  maxHeight,  vfloor,  floorHeight);
	}


}
