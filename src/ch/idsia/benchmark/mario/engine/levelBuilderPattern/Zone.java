package ch.idsia.benchmark.mario.engine.levelBuilderPattern;

import java.util.Random;

import ch.idsia.benchmark.mario.engine.level.*;
import ch.idsia.benchmark.mario.engine.level.Level;
import ch.idsia.tools.RandomCreatureGenerator;

public class Zone implements LevelComponent{
	
	private int x;
	private int maxLength;
	private int maxHeight;
	private int floor;
	private int floorHeight;
	
	public Zone(int x, int maxLength, int maxHeight, int floor, int floorHeight) {
		this.floor = floor;
		this.x = x;
		this.maxHeight = maxHeight;
		this.maxLength = maxLength;
		this.floorHeight = floorHeight;
	}
	
	private int buildZone(int x, int maxLength, int maxHeight, int floor, int floorHeight)
	{
		
		SingletonLevelElements i = SingletonLevelElements.getInstance();
	
//	    System.out.println("buildZone maxLength = " + maxLength);
	    int t = SingletonLevelElements.globalRandom.nextInt(i.getTotalOdds());
	    int type = 0;
	    //calculate what will be built
	    for (int i2 = 0; i2 < i.getOdds().length; i2++)
	    {
	        if (i.getOdds()[i2] <= t)
	        {
	            type = i2;
	        }
	    }

	    int length = 0;
	    
	    Straight s = new Straight(x, maxLength, false, floor, floorHeight);
	    Hill h = new Hill(x, true, maxLength, floor, false);
	    Tubes tu = new Tubes(x, maxLength, maxHeight, floor, floorHeight);
	    Gap g = new Gap(x, maxLength, maxHeight, floor, floorHeight);
	    Cannon c = new Cannon(x, maxLength, maxHeight, floor, floorHeight);
	    DeadEnd d = new DeadEnd(x, maxLength);

	    switch (type)
	    {
	        case SingletonLevelElements.ODDS_STRAIGHT:
	            length = s.build();
	            break;
	        case SingletonLevelElements.ODDS_HILL_STRAIGHT:
	            if (floor == SingletonLevelElements.DEFAULT_FLOOR && SingletonLevelElements.counters.hillStraightCount < SingletonLevelElements.counters.totalHillStraight)
	            {
	            	SingletonLevelElements.counters.hillStraightCount++;
//	                length = buildHillStraight(x, maxLength, floor, false);
	                length = h.build();
	            } else
	                length = 0;
	            break;
	        case SingletonLevelElements.ODDS_TUBES:
	            if (SingletonLevelElements.counters.tubesCount < SingletonLevelElements.counters.totalTubes)
	                length = tu.build();
	            else
	                length = 0;
	            break;
	        case SingletonLevelElements.ODDS_GAPS:
	            if ((floor > 2 || floor == SingletonLevelElements.ANY_HEIGHT) && (SingletonLevelElements.counters.gapsCount < SingletonLevelElements.counters.totalGaps))
	            {
	            	SingletonLevelElements.counters.gapsCount++;
	                length = g.build();
	            } else
	                length = 0;
	            break;
	        case SingletonLevelElements.ODDS_CANNONS:
	            if (SingletonLevelElements.counters.cannonsCount < SingletonLevelElements.counters.totalCannons)
	                length = c.build();
	            else
	                length = 0;
	            break;
	        case SingletonLevelElements.ODDS_DEAD_ENDS:
	        {
	            if (floor == SingletonLevelElements.DEFAULT_FLOOR && SingletonLevelElements.counters.deadEndsCount < SingletonLevelElements.counters.totalDeadEnds) //if method was not called from buildDeadEnds
	            {
	            	SingletonLevelElements.counters.deadEndsCount++;
	                length = d.build();
	            }
	        }
	    }
	        
	        int crCount = 0;
//	      for (int y = level.height - 3; y > levelDifficulty + 1; --y)
//	      {
//	          addEnemy(x, y);
//	          ++crCount;
//	      }
	      for (int yy = i.getLevel().height; yy > 0; yy--)
	          if (i.getLevel().getBlock(x, yy) == 0 &&
	                  SingletonLevelElements.creaturesRandom.nextInt(i.getLevelDifficulty() + 1) + 1 > (i.getLevelDifficulty() + 1) / 2 &&
	                  crCount < i.getLevelDifficulty() + 1 &&
	                  i.getLevel().getSpriteTemplate(x, yy) == null)
	          {
	              //addEnemy(x, yy);
	              ++crCount;
	          }

	      if (i.getLevelType() > 0) {
	  	    Ceiling ce = new Ceiling(x, length);
	          c.build();
	      }

	      return length;
	    
	}
	
	@Override
	public int build() {
		return buildZone( x,  maxLength,  maxHeight,  floor,  floorHeight);
	}
}
	 
	   
	


