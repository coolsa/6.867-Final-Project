package ch.idsia.benchmark.mario.engine.level;
import java.util.Random;

import ch.idsia.benchmark.mario.engine.level.Level;
import ch.idsia.tools.RandomCreatureGenerator;

public class Ceiling implements LevelComponent{
	
	private int x0;
	private int length;
	
	
	public Ceiling(int x0, int length) {
		super();
		this.x0 = x0;
		this.length = length;
	}


	private void buildCeiling(int x0, int length)
	{
		
		SingletonLevelElements i = SingletonLevelElements.getInstance();
	    int maxCeilingHeight = 3;
	    int ceilingLength = length;

	    if (ceilingLength < 2)
	        return;
//	    len:
//	    for (int i = x0; i < x1; i++)
//	        for (int j = 0; j < height; j++)
//	            if (level.getBlock(i, j) != 0)
//	            {
//	                maxCeilingHeight = j;
//	                break len;
//	            }
	    int len = 0;

	    while (len < ceilingLength)
	    {
	        int sectionLength = SingletonLevelElements.ceilingRandom.nextInt(2) + 2;
	        if (sectionLength > ceilingLength)
	            sectionLength = ceilingLength;

//	        if (maxCeilingHeight > 0)
//	            maxCeilingHeight--;
//	        if (maxCeilingHeight == 0)
//	            maxCeilingHeight = 1;
//	        if (maxCeilingHeight > 5)
//	            maxCeilingHeight = 5;

	        int height = SingletonLevelElements.ceilingRandom.nextInt(maxCeilingHeight) + 1;
	        for (int i2 = 0; i2 < sectionLength; i2++)
	        {
	            for (int j = 0; j < height; j++)
	                i.getLevel().setBlock(x0 + len + i2, j, (byte) (1 + 9 * 16));
	        }

	        len += sectionLength;
	    }
	}
	
	@Override
	public int build() {
		buildCeiling(x0, length);
		return 1;
	}
}
