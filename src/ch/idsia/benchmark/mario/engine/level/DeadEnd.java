package ch.idsia.benchmark.mario.engine.levelBuilderPattern;

import java.util.Random;

import ch.idsia.benchmark.mario.engine.level.Level;
import ch.idsia.tools.RandomCreatureGenerator;

public class DeadEnd implements LevelComponent{
	
	private int x0;
	private int maxLength;
	
	
	public DeadEnd(int x0, int maxLength) {
		super();
		this.x0 = x0;
		this.maxLength = maxLength;
	}


	private int buildDeadEnds(int x0, int maxLength)
	{
		
		SingletonLevelElements i = SingletonLevelElements.getInstance();
	    //first of all build pre dead end zone
	    int floor = i.getHeight() - 2 - SingletonLevelElements.globalRandom.nextInt(2);  //floor of pre dead end zone
	    int length = 0; // total zone length
	    int preDeadEndLength = 7 + SingletonLevelElements.globalRandom.nextInt(10);
	    int rHeight = floor - 1; //rest height
	    int separatorY = 3 + SingletonLevelElements.globalRandom.nextInt(rHeight - 7); //Y coordinate of the top line of the separator

	    Straight s = new Straight(x0, preDeadEndLength, true, floor, SingletonLevelElements.INFINITE_FLOOR_HEIGHT);
	    length += s.build();//buildZone( x0, x0+preDeadEndLength, floor ); //build pre dead end zone

	    if (SingletonLevelElements.globalRandom.nextInt(3) == 0 && i.getIsLadder())
	    {
	        int ladderX = x0 + SingletonLevelElements.globalRandom.nextInt(length - 1) + 1;
	        if (ladderX > x0 + length)
	            ladderX = x0 + length;
	        Ladder.buildLadder(ladderX, floor, floor - separatorY);
	    } else
	        LevelBuilder.buildBlocks(x0, x0 + preDeadEndLength, floor, true, 0, 0, true, true);

	    //correct direction
	    //true - top, false = bottom
	    SingletonLevelElements.globalRandom.nextInt();
	    int k = SingletonLevelElements.globalRandom.nextInt(5);//(globalRandom.nextInt() % (this.levelDifficulty+1));
	    boolean direction = SingletonLevelElements.globalRandom.nextInt(k + 1) != 1;

	    //Y coordinate of the bottom line of the separator is determined as separatorY + separatorHeight
	    int separatorHeight = 2 + SingletonLevelElements.globalRandom.nextInt(2);

	    int nx = x0 + length;
	    int depth = SingletonLevelElements.globalRandom.nextInt(i.getLevelDifficulty() + 1) + 2 * (1 + i.getLevelDifficulty());
	    if (depth + length > maxLength)
	    {
//	        depth = maxLength
	        while (depth + length > maxLength - 1)
	        {
	            depth--;
	        }
	    }

	    int tLength = 0;
	    int bSpace = floor - (separatorY + separatorHeight);
	    if (bSpace < 4)
	    {
	        while (bSpace < 4)
	        {
	            separatorY -= 1;
	            bSpace = floor - (separatorY + separatorHeight);
	        }
	    }

	    int wallWidth = 2 + SingletonLevelElements.globalRandom.nextInt(3);
	    Zone z1 = new Zone(nx + tLength, depth - tLength, separatorY - 1, separatorY, separatorHeight);
	    Zone z2 = new Zone(nx + tLength, depth - tLength, bSpace, floor, SingletonLevelElements.INFINITE_FLOOR_HEIGHT);
	    while (tLength < depth) //top part
	    {
	        tLength += z1.build();
	    }
	    tLength = 0;
	    while (tLength < depth) //bottom part
	    {
	        tLength += z1.build();
	    }

	    boolean wallFromBlocks = false;//globalRandom.nextInt(5) == 2;

	    for (int x = nx; x < nx + depth; x++)
	    {
	        for (int y = 0; y < i.getHeight(); y++)
	        {
	            if (x - nx >= depth - wallWidth)
	            {
	                if (direction) //wall on the top
	                {
	                    if (y <= separatorY)// + separatorHeight )
	                    {
	                        if (wallFromBlocks)
	                            i.getLevel().setBlock(x, y, (byte) (0 + 1 * 16));
	                        else
	                        	i.getLevel().setBlock(x, y, (byte) (1 + 9 * 16));
	                    }
	                } else
	                {
	                    if (y >= separatorY)
	                    {
	                        if (wallFromBlocks)
	                        	i.getLevel().setBlock(x, y, (byte) (0 + 1 * 16));
	                        else
	                        	i.getLevel().setBlock(x, y, (byte) (1 + 9 * 16));
	                    }
	                }
	            }
	        }
	    }

	    return length + tLength;
	}
	
	@Override
	public int build() {
		return buildDeadEnds(x0, maxLength);
	}


}
