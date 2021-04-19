package ch.idsia.benchmark.mario.engine.level;

import java.util.Random;

import ch.idsia.benchmark.mario.engine.level.Level;
import ch.idsia.benchmark.mario.engine.level.SpriteTemplate;
import ch.idsia.benchmark.mario.engine.sprites.Sprite;
import ch.idsia.tools.RandomCreatureGenerator;

public class Tubes implements LevelComponent{
	
	int xo;
	int maxLength;
	int maxHeight;
	int vfloor;
	int floorHeight;
	
	
	
	public Tubes(int xo, int maxLength, int maxHeight, int vfloor, int floorHeight) {
		super();
		this.xo = xo;
		this.maxLength = maxLength;
		this.maxHeight = maxHeight;
		this.vfloor = vfloor;
		this.floorHeight = floorHeight;
	}



	private int buildTubes(int xo, int maxLength, int maxHeight, int vfloor, int floorHeight)
	{
		
		SingletonLevelElements i = SingletonLevelElements.getInstance();
		
	    int maxTubeHeight = 0;
	    int length = SingletonLevelElements.globalRandom.nextInt(10) + 5;
	    if (length > maxLength) length = maxLength;

	    int floor = vfloor;
	    if (vfloor == SingletonLevelElements.DEFAULT_FLOOR)
	    {
	        floor = i.getHeight() - 1 - SingletonLevelElements.globalRandom.nextInt(4);
	    } else
	    {
	    	SingletonLevelElements.globalRandom.nextInt();
	    }
	    int xTube = xo + 1 + SingletonLevelElements.globalRandom.nextInt(4);

	    int tubeHeight = floor - SingletonLevelElements.globalRandom.nextInt(3) - 1;

	    if (maxHeight != SingletonLevelElements.ANY_HEIGHT)
	    {
	        //maxHeight -= 2;
	        if (floor - tubeHeight > maxHeight)
	        {
	            if (maxHeight > 4)
	            {
	                maxHeight = 4;
	            }
	            while (floor - tubeHeight > maxHeight)
	            {
	                tubeHeight++;
	            }
	        }
	    }

	    if (floorHeight == SingletonLevelElements.INFINITE_FLOOR_HEIGHT)
	    {
	        floorHeight = i.getHeight() - floor;
	    }

	    int oldXTube = -1;

	    for (int x = xo; x < xo + length; x++)
	    {
	        if (x > xTube + 1)
	        {
	            xTube += 3 + SingletonLevelElements.globalRandom.nextInt(4);
	            tubeHeight = floor - SingletonLevelElements.globalRandom.nextInt(2) - 2;
	            if (maxHeight != SingletonLevelElements.ANY_HEIGHT)
	            {
	                while (floor - tubeHeight > maxHeight - 1)
	                {
	                    tubeHeight++;
	                }
	            }

	            if (tubeHeight > maxTubeHeight)
	                maxTubeHeight = tubeHeight;
	        }
	        if (xTube >= xo + length - 2)
	        {
	            xTube += 10;
	        }

	        if (x == xTube && SingletonLevelElements.globalRandom.nextInt(11) < i.getLevelDifficulty() + 1 && SingletonLevelElements.creaturesRandom.isCreatureEnabled("f"))
	        {
	        	i.getLevel().setSpriteTemplate(x, tubeHeight, new SpriteTemplate(Sprite.KIND_ENEMY_FLOWER));
	            ++SingletonLevelElements.counters.creatures;
	        }

	        for (int y = 0; y < floor + floorHeight; y++)
	        {
	            if (y >= floor && y <= floor + floorHeight)
	            	i.getLevel().setBlock(x, y, (byte) (1 + 9 * 16));
	            else
	            {
	                if ((x == xTube || x == xTube + 1) && y >= tubeHeight)
	                {

	                    int xPic = 10 + x - xTube;
	                    if (y == tubeHeight)
	                    {
	                    	i.getLevel().setBlock(x, y, (byte) (xPic + 0 * 16));
	                        if (x == xTube)
	                        {
	                            if (oldXTube != -1 && SingletonLevelElements.creaturesRandom.nextInt(35) > i.getLevelDifficulty() + 1)
	                            {
//	                                addEnemiesLine(oldXTube + 2, xTube - 1, floor - 1);
	                            }
	                            oldXTube = x;
	                            SingletonLevelElements.counters.tubesCount++;
	                        }
	                    } else
	                    {
	                        i.getLevel().setBlock(x, y, (byte) (xPic + 1 * 16));
	                    }
	                }
	            }
	        }
	    }

	    if (SingletonLevelElements.globalRandom.nextBoolean())
	        LevelBuilder.buildBlocks(xo, xo + length, floor - maxTubeHeight - 2, false, 0, 0, false, false);

	    return length;
	}
	
	@Override
	public int build() {
		return buildTubes( xo,  maxLength,  maxHeight,  vfloor,  floorHeight);
	}


}
