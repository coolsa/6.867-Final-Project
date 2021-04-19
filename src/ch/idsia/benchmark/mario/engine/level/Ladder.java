package ch.idsia.benchmark.mario.engine.levelBuilderPattern;

import java.util.Random;

import ch.idsia.benchmark.mario.engine.level.Level;
import ch.idsia.tools.RandomCreatureGenerator;

public class Ladder implements LevelComponent{
	
	int x0;
	int floor;
	int maxHeight;
	
	
	
	public Ladder(int x0, int floor, int maxHeight) {
		super();
		this.x0 = x0;
		this.floor = floor;
		this.maxHeight = maxHeight;
	}



	public static void buildLadder(int x0, int floor, int maxHeight)
	{
		
		SingletonLevelElements i = SingletonLevelElements.getInstance();
	    int ladderHeight = SingletonLevelElements.globalRandom.nextInt(i.getHeight());
	    if (ladderHeight > maxHeight && maxHeight != SingletonLevelElements.ANY_HEIGHT)
	    {
	        ladderHeight = maxHeight;
	    }

	    if (ladderHeight < 4)
	        return;

	    for (int y = floor, i2 = 0; i2 < ladderHeight - 1; y--, i2++)
	        i.getLevel().setBlock(x0, y - 1, (byte) (13 + 3 * 16));

	    i.getLevel().setBlock(x0, floor - ladderHeight, (byte) (13 + 5 * 16));
	}
	
	@Override
	public int build() {
		buildLadder(x0, floor, maxHeight);
		return 1;
	}


}
