package ch.idsia.benchmark.mario.engine.level;

import java.util.Random;

import ch.idsia.benchmark.mario.engine.level.Level;
import ch.idsia.tools.RandomCreatureGenerator;

public class Hill implements LevelComponent{
	
	private int x0;
	private boolean withStraight;
	private int maxLength;
	private int vfloor;
	private boolean isInGap;
	
	
	
	public Hill(int x0, boolean withStraight, int maxLength, int vfloor, boolean isInGap) {
		super();
		this.x0 = x0;
		this.withStraight = withStraight;
		this.maxLength = maxLength;
		this.vfloor = vfloor;
		this.isInGap = isInGap;
	}



	private int buildHill(int x0, boolean withStraight, int maxLength, int vfloor, boolean isInGap)
	{
		
		SingletonLevelElements i = SingletonLevelElements.getInstance();
		
	    int length = SingletonLevelElements.globalRandom.nextInt(10) + 10;
	    if (length > maxLength)
	    {
	        length = maxLength;
	    }

	    int floor = vfloor;
	    if (vfloor == SingletonLevelElements.DEFAULT_FLOOR)
	    {
	        floor = i.getHeight() - 1 - SingletonLevelElements.globalRandom.nextInt(4);
	    }

	    if (withStraight)
	    {
	        for (int x = x0; x < x0 + length; x++)
	        {
	            for (int y = 0; y < i.getHeight(); y++)
	            {
	                if (y >= floor)
	                {
	                	i.getLevel().setBlock(x, y, (byte) (1 + 9 * 16));
	                }
	            }
	        }
	    }

	    boolean canBuild = true;

	    int top = floor;
	    if (isInGap)
	        floor = i.getLevel().height;
	    while (canBuild)
	    {
	        top -= i.getIsFlatLevel() ? 0 : (SingletonLevelElements.globalRandom.nextInt(2) + 2);
	        if (top < 0)
	            canBuild = false;
	        else
	        {
	            int l = SingletonLevelElements.globalRandom.nextInt(length / 2) + 1;
	            int xx0 = SingletonLevelElements.globalRandom.nextInt(l + 1) + x0;

	            if (SingletonLevelElements.globalRandom.nextInt(4) == 0)
	            {
	                //decorate(xx0 - 1, xx0 + l + 1, top);
	                canBuild = false;
	            }
	            for (int x = xx0; x < xx0 + l; x++)
	            {
	                for (int y = top; y < floor; y++)
	                {
	                    int xx = 5;
	                    if (x == xx0) xx = 4;
	                    if (x == xx0 + l - 1) xx = 6;
	                    int yy = 9;
	                    if (y == top) yy = 8;

	                    if (i.getLevel().getBlock(x, y) == 0)
	                    {
	                    	i.getLevel().setBlock(x, y, (byte) (xx + yy * 16));
	                    } else
	                    {
	                        if (i.getLevel().getBlock(x, y) == (byte) (4 + 8 * 16))
	                        	i.getLevel().setBlock(x, y, (byte) (4 + 11 * 16));
	                        if (i.getLevel().getBlock(x, y) == (byte) (6 + 8 * 16))
	                        	i.getLevel().setBlock(x, y, (byte) (6 + 11 * 16));
	                    }
	                }
	            }
	            //addEnemy(xx0, top - 1);
	        }
	    }

	    return length;
	}
	
	@Override
	public int build() {
		return buildHill(x0,  withStraight,  maxLength,  vfloor,  isInGap);
	}


}
