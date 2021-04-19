package ch.idsia.benchmark.mario.engine.levelBuilderPattern;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Random;

import ch.idsia.benchmark.mario.engine.level.Level;
import ch.idsia.benchmark.mario.engine.level.LevelGenerator;
import ch.idsia.tools.MarioAIOptions;
import ch.idsia.tools.RandomCreatureGenerator;
import ch.idsia.utils.ErrorCodes;

public class LevelBuilder {


	
	public static Level createLevel(MarioAIOptions args)
	{
		
		SingletonLevelElements i = SingletonLevelElements.getInstance();
	    // -ls option can also loadAgent level from file if filename instead of a number provided
	    i.setLevelType(args.getLevelType());
	    try
	    {
	        i.setLevelSeed(args.getLevelRandSeed() + i.getLevelType());
	    } catch (Exception e)
	    {
	        loadLevel(args.getParameterValue("-ls"));
	        return i.getLevel();
	    }
	    i.setLength(args.getLevelLength());
	    i.setHeight(args.getLevelHeight());
	    if (i.getHeight() < 15)
	    {
	        System.err.println("[Mario AI WARNING] : Level height changed to minimal allowed value 15");
	        i.setHeight(15);
	    }
	    i.setFlatLevel(args.isFlatLevel());

	    SingletonLevelElements.counters.reset(args);
	    
	    int[] odds = new int[6];
	    
	    i.setLevelDifficulty(args.getLevelDifficulty());
	    odds[SingletonLevelElements.ODDS_STRAIGHT] = 20;
	    odds[SingletonLevelElements.ODDS_HILL_STRAIGHT] = 1;
	    odds[SingletonLevelElements.ODDS_TUBES] = 2 + 1 * i.getLevelDifficulty();
	    odds[SingletonLevelElements.ODDS_GAPS] = 3 * i.getLevelDifficulty();
	    odds[SingletonLevelElements.ODDS_CANNONS] = -10 + 5 * i.getLevelDifficulty();
	    odds[SingletonLevelElements.ODDS_DEAD_ENDS] = 2 + 2 * i.getLevelDifficulty();

	    if (i.getLevelType() != LevelGenerator.TYPE_OVERGROUND)
	        odds[SingletonLevelElements.ODDS_HILL_STRAIGHT] = 0; //no hill straight in TYPE_OVERGROUND level

	     int totalOdds = 0;
	    for (int i2 = 0; i2 < odds.length; i2++)
	    {
	        if (odds[i2] < 0) odds[i2] = 0;
	        totalOdds += odds[i2];
	        odds[i2] = totalOdds - odds[i2];
	    }
	    if (totalOdds <= 0)
	    {
	        System.err.println("[Mario AI SURPRISE] : UNEXPECTED level will be generated");
	        totalOdds = 1;
	    }
	    
	    i.setTotalOdds(totalOdds);

	    i.setLevel(new Level(i.getLength(), i.getHeight()));
//	    levelSeed = args.getLevelRandSeed();// + levelType; // TODO:TASK:[M] ensure the difference of underground, castle
	    SingletonLevelElements.globalRandom.setSeed(i.getLevelSeed());
	    SingletonLevelElements.creaturesRandom.setSeed(i.getLevelSeed(), args.getEnemies(), i.getLevelDifficulty());
	    SingletonLevelElements.ceilingRandom.setSeed(i.getLevelSeed());
	    SingletonLevelElements.dxRnd.setSeed(i.getLevelSeed());

	    i.setisLadder(args.isLevelLadder());

	    int currentLength = 0; //total level currentLength so far
	    
	    //by default mario supposed to start on a straight surface
	    int floor = SingletonLevelElements.DEFAULT_FLOOR;
	    if (i.getIsFlatLevel())
	        floor = i.getHeight() - 1 - SingletonLevelElements.globalRandom.nextInt(4);
	    Straight s = new Straight(0, i.getLevel().length, true, floor, SingletonLevelElements.INFINITE_FLOOR_HEIGHT);
	    Zone z = new Zone(currentLength, i.getLevel().length - currentLength, SingletonLevelElements.ANY_HEIGHT, floor, SingletonLevelElements.INFINITE_FLOOR_HEIGHT);

	    currentLength += s.build();
	    while (currentLength < i.getLevel().length - 10)
	    {
//	        System.out.println("level.currentLength - currentLength = " + (level.currentLength - currentLength));
	        currentLength += z.build();
	    }

	    if (!i.getIsFlatLevel())  //NOT flat level
	        floor = i.getHeight() - 1 - SingletonLevelElements.globalRandom.nextInt(4); //floor of the exit line

	    //coordinates of the exit
	    i.getLevel().xExit = args.getExitX();
	    i.getLevel().yExit = args.getExitY();

	    if (i.getLevel().xExit == 0)
	    	i.getLevel().xExit = i.getLevel().length - 1;

	    if (i.getLevel().yExit == 0)
	    	i.getLevel().yExit = floor - 1;

	    i.getLevel().randomSeed = i.getLevelSeed();
	    i.getLevel().type = i.getLevelType();
	    i.getLevel().difficulty = i.getLevelDifficulty();


	    //level zone where exit is located
	    for (int x = currentLength; x < i.getLevel().length; x++)
	    {
	        for (int y = 0; y < i.getHeight(); y++)
	        {
	            if (y >= floor)
	            {
	            	i.getLevel().setBlock(x, y, (byte) (1 + 9 * 16));
	            }
	        }
	    }
	    return i.getLevel();
	    
	}
	
	private static void loadLevel(String filePath)
	{
		SingletonLevelElements i = SingletonLevelElements.getInstance();
	    try
	    {
	        if (filePath.equals("")) //This must never happen
	        {
	            System.err.println("[MarioAI ERROR] : level file path is empty; exiting...");
	            System.exit(ErrorCodes.FILE_NAME_OR_LOAD_PROBLEM);
	        }

	        i.setLevel(Level.load(new ObjectInputStream(new FileInputStream(filePath))));
	    } catch (IOException e)
	    {
	        System.err.println("[MarioAI EXCEPTION] : failed while trying to loadAgent " + filePath);
	        System.exit(ErrorCodes.FILE_NAME_OR_LOAD_PROBLEM);
	    } catch (ClassNotFoundException e)
	    {
	        System.err.println("[MarioAI EXCEPTION] : class not found in " + filePath);
	        System.exit(ErrorCodes.FILE_NAME_OR_LOAD_PROBLEM);
	    }
	}
	
	public static boolean buildBlocks(int x0, int x1, int floor, boolean pHB, int pS, int pE, boolean onlyHB, boolean isDistance)
	{
		
		SingletonLevelElements i = SingletonLevelElements.getInstance();
		
	    boolean result = false;
	    if (SingletonLevelElements.counters.blocksCount > SingletonLevelElements.counters.totalBlocks)
	    {
	        return false;
	    }
	    int s = pS; //Start
	    int e = pE; //End
	    boolean hb = pHB;

	    if (onlyHB)
	        hb = onlyHB;

	    --floor;
	    while (floor > 0) //minimal distance between the bricks line and floor is 4
	    {
	        if ((x1 - 1 - e) - (x0 + 1 + s) > 0) //minimal number of bricks in the line is positive value
	        {
	            for (int x = x0 + s; x < x1 - e; x++)
	            {
	                if (hb && SingletonLevelElements.counters.totalHiddenBlocks != 0) //if hidden blocks to be built
	                {
	                    boolean isBlock = SingletonLevelElements.globalRandom.nextInt(2) == 1;
	                    if (isBlock && canBuildBlocks(x, floor - 4, true))
	                    {
	                        i.getLevel().setBlock(x, floor - 4, (byte) (1)); //a hidden block with a coin
	                        SingletonLevelElements.counters.hiddenBlocksCount++;
	                        ++SingletonLevelElements.counters.coinsCount;
	                    }
	                } else
	                {
	                    boolean canDeco = false; //can add enemy line and coins
	                    //decorate( x0, x1, floor, true );
	                    if (x != x0 + 1 && x != x1 - 2 && SingletonLevelElements.globalRandom.nextInt(3) == 0)
	                    {
	                        if (canBuildBlocks(x, floor - 4, false))
	                        {
	                        	SingletonLevelElements.counters.blocksCount++;
	                            int rnd = SingletonLevelElements.globalRandom.nextInt(6);
	                            if (rnd >= 0 && rnd < 2)
	                            {
	                                if (i.getLevel().getBlock(x, floor) == 0)
	                                	i.getLevel().setBlock(x, floor, (byte) (4 + 2 + 1 * 16)); //a brick with animated question symbol with power up. when broken becomes a rock
	                            } else if (rnd >= 2 && rnd < 4)
	                            {
	                                if (i.getLevel().getBlock(x, floor) == 0)
	                                {
	                                	i.getLevel().setBlock(x, floor, (byte) (4 + 1 + 1 * 16)); //a brick with animated question symbol with coin. when broken becomes a rock
	                                    ++SingletonLevelElements.counters.coinsCount;
	                                }
	                            } else if (rnd >= 4 && rnd < 6)
	                            {
	                                int coinsNumber = SingletonLevelElements.globalRandom.nextInt(9) + 1;
	                                i.getLevel().setBlock(x, floor, (byte) (4 + 3 + 1 * 16)); //a brick with animated question symbol with N coins inside. when broken becomes a rock
	                                i.getLevel().setBlockData(x, floor, (byte) -coinsNumber);
	                                SingletonLevelElements.counters.coinsCount += coinsNumber;
	                            }
	                            canDeco = true;
	                        }
	                    } else if (SingletonLevelElements.globalRandom.nextInt(4) == 0)
	                    {
	                        if (canBuildBlocks(x, floor - 4, false))
	                        {
	                        	SingletonLevelElements.counters.blocksCount++;
	                            if (SingletonLevelElements.globalRandom.nextInt(4) == 0)
	                            {
	                                if (i.getLevel().getBlock(x, floor) == 0)
	                                	i.getLevel().setBlock(x, floor, (byte) (2 + 1 * 16)); //a brick with a power up. when broken becomes a rock
	                            } else
	                            {
	                                if (i.getLevel().getBlock(x, floor) == 0)
	                                {
	                                	i.getLevel().setBlock(x, floor, (byte) (1 + 1 * 16)); //a brick with a coin. when broken becomes a rock
	                                    ++SingletonLevelElements.counters.coinsCount;
	                                }
	                            }
	                            canDeco = true;
	                        }
	                    } else if (SingletonLevelElements.globalRandom.nextInt(2) == 1 && canBuildBlocks(x, floor - 4, false))
	                    {
	                        if (i.getLevel().getBlock(x, floor) == 0)
	                        {
	                        	SingletonLevelElements.counters.blocksCount++; //TODO:TASK:!H! move it in to the Level.setBlock
	                            i.getLevel().setBlock(x, floor, (byte) (0 + 1 * 16)); //a break brick
	                            canDeco = true;
	                        }
	                    }
	                    if (canDeco)
	                    {
	                        //if (creaturesRandom.nextInt(35) > levelDifficulty + 1) addEnemiesLine(x0 + 1, x1 - 1, floor - 1);
	                        buildCoins(x0, x1, floor, s, e);
	                    }
	                }
	            }
	            if (onlyHB)
	            {
	                hb = true;
	            } else
	            {
	                hb = SingletonLevelElements.globalRandom.nextInt(4) == 0;//globalRandom.nextInt(3) == globalRandom.nextInt(3);
	            }
	        }

//	        if (creaturesRandom.nextInt(35) > levelDifficulty + 1)
//	            addEnemiesLine(x0 + 1, x1 - 1, floor - 1);

	        int delta = isDistance ? 4 : SingletonLevelElements.globalRandom.nextInt(6) + 3;
	        if (delta > 4)
	            result = true;
	        floor -= delta;
	        s = SingletonLevelElements.globalRandom.nextInt(4);
	        e = SingletonLevelElements.globalRandom.nextInt(4);
	    }
	    SingletonLevelElements.globalRandom.nextBoolean();
	    return result;
	}
	
	private static boolean canBuildBlocks(int x0, int floor, boolean isHB)
	{
	    if ((SingletonLevelElements.counters.blocksCount >= SingletonLevelElements.counters.totalBlocks && !isHB))
	    {
	        return false;
	    }

	    boolean res = true;

//	    if (floor < 1)
//	    {
//	        return false;
//	    }

//	    for (int y = 0; y < 1; y++)
//	    {
//	        if (level.getBlock(x0, floor - y) != 0)
//	        {
//	            res = false;
//	            break;
//	        }
//	    }

	    return res;
	}
}
