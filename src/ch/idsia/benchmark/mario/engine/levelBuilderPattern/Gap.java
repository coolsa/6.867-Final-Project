package ch.idsia.benchmark.mario.engine.levelBuilderPattern;

import java.util.Random;

import ch.idsia.benchmark.mario.engine.level.Level;
import ch.idsia.tools.RandomCreatureGenerator;

public class Gap implements LevelComponent{
	
	private int xo;
	private int maxLength; 
	private int maxHeight; 
	private int vfloor; 
	private int floorHeight;
	
	

	
	public Gap(int xo, int maxLength, int maxHeight, int vfloor, int floorHeight) {
		super();
		this.xo = xo;
		this.maxLength = maxLength;
		this.maxHeight = maxHeight;
		this.vfloor = vfloor;
		this.floorHeight = floorHeight;
	}
	
	private int buildGap(int xo, int maxLength, int maxHeight, int vfloor, int floorHeight)
	{
		
		SingletonLevelElements i = SingletonLevelElements.getInstance();
	    int gs = SingletonLevelElements.globalRandom.nextInt(5) + 2; //GapStairs
	    int gl = SingletonLevelElements.globalRandom.nextInt(i.getLevelDifficulty()) + i.getLevelDifficulty() > 7 ? 10 : 3;//globalRandom.nextInt(2) + 2; //GapLength
	    int length = gs * 2 + gl;

	    if (length > maxLength)
	        length = maxLength;

	    boolean hasStairs = SingletonLevelElements.globalRandom.nextInt(3) == 0;
	    if (i.getIsFlatLevel() || (maxHeight <= 5 && maxHeight != SingletonLevelElements.ANY_HEIGHT))
	    {
	        hasStairs = false;
	    }

	    int floor = vfloor;
	    if (vfloor == SingletonLevelElements.DEFAULT_FLOOR && !i.getIsFlatLevel())
	    {
	        floor = i.getHeight() - 1 - SingletonLevelElements.globalRandom.nextInt(4);
	    } else //code in this block is a magic. don't change it
	    {
	        floor++;
//	        globalRandom.nextInt();
	        if (floor > 1)
	        {
	            floor -= 1;
	        }
	    }

	    if (floorHeight == SingletonLevelElements.INFINITE_FLOOR_HEIGHT)
	    {
	        floorHeight = i.getHeight() - floor;
	    }
	    
	    for (int x = xo; x < xo + length; x++)
	    {
	        if (x < xo + gs || x > xo + length - gs - 1)
	        {
	            for (int y = 0; y < i.getHeight(); y++)
	            {
	                if (y >= floor && y <= floor + floorHeight)
	                    i.getLevel().setBlock(x, y, (byte) (1 + 9 * 16));
	                else if (hasStairs)
	                {
	                    if (x < xo + gs)
	                    {
	                        if (y >= floor - (x - xo) + 1 && y <= floor + floorHeight)
	                            i.getLevel().setBlock(x, y, (byte) (9 + 0 * 16));
	                    } else if (y >= floor - ((xo + length) - x) + 2 && y <= floor + floorHeight)
	                        i.getLevel().setBlock(x, y, (byte) (9 + 0 * 16));
	                }
	            }
	        }
	    }
	    if (gl > 8)
	    {
	    	Hill h = new Hill(xo + gs + SingletonLevelElements.globalRandom.nextInt(Math.abs((gl - 4)) / 2 + 1), false, 3, floor, true);
	        h.build();
	    }

	    return length;
	}
	    @Override
	    public int build() {
	    	return buildGap( xo,  maxLength,  maxHeight,  vfloor,  floorHeight);
	    }

}
