package ch.idsia.benchmark.mario.engine.level;

import java.util.Random;

import ch.idsia.benchmark.mario.engine.level.Level;
import ch.idsia.tools.RandomCreatureGenerator;

public class SingletonLevelElements {
	
	private static SingletonLevelElements instance = new SingletonLevelElements();
	
	private SingletonLevelElements() {
		
	}
	
	public static SingletonLevelElements getInstance(){
		return instance;
	}
	
	public static final int TYPE_OVERGROUND = 0;
	public static final int TYPE_UNDERGROUND = 1;
	public static final int TYPE_CASTLE = 2;

	public static final int DEFAULT_FLOOR = -1;

	public static final int LevelLengthMinThreshold = 50; // minimal length of the level. used in ToolsConfigurator
	private boolean isFlatLevel;

	private int length;
	
	private int height;
	private Level level;


	public static Random globalRandom = new Random(0);
	public static Random ceilingRandom = new Random(0);
	public static RandomCreatureGenerator creaturesRandom = new RandomCreatureGenerator(0, "", 0);
	public static Random dxRnd = new Random(0); //used in addEnemy to compute dx

	public static final int ODDS_STRAIGHT = 0;
	public static final int ODDS_HILL_STRAIGHT = 1;
	public static final int ODDS_TUBES = 2;
	public static final int ODDS_GAPS = 3;
	public static final int ODDS_CANNONS = 4;
	public static final int ODDS_DEAD_ENDS = 5;
	private int[] odds = new int[6];
	private int totalOdds;
	private int levelDifficulty;
	private int levelType;
	private int levelSeed;

	private boolean isLadder = false;

	public static final int ANY_HEIGHT = -1;
	public static final int INFINITE_FLOOR_HEIGHT = Integer.MAX_VALUE;
	
	static Level.objCounters counters = new Level.objCounters();
	
	
	public boolean isFlatLevel() {
		return isFlatLevel;
	}

	public void setFlatLevel(boolean isFlatLevel) {
		this.isFlatLevel = isFlatLevel;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public int getTotalOdds() {
		return totalOdds;
	}

	public void setTotalOdds(int totalOdds) {
		this.totalOdds = totalOdds;
	}

	public int getLevelDifficulty() {
		return levelDifficulty;
	}

	public void setLevelDifficulty(int levelDifficulty) {
		this.levelDifficulty = levelDifficulty;
	}

	public int getLevelType() {
		return levelType;
	}

	public void setLevelType(int levelType) {
		this.levelType = levelType;
	}

	public int getLevelSeed() {
		return levelSeed;
	}

	public void setLevelSeed(int levelSeed) {
		this.levelSeed = levelSeed;
	}
	
	public void setisLadder(boolean isLadder) {
		this.isLadder = isLadder;
	}
	
	public boolean getIsLadder() {
		return isLadder;
	}

	
	public void setIsFlatLevel(boolean isFlatLevel) {
		this.isFlatLevel = isFlatLevel;
	}
	
	public boolean getIsFlatLevel(){
		return this.isFlatLevel;
	}

	public int[] getOdds() {
		return odds;
	}

	public void setOdds(int[] odds) {
		this.odds = odds;
	}
	
	
	
}
