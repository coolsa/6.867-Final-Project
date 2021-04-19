package ch.idsia.benchmark.mario.engine.sprites;

public class SpriteFactory {
    public Sprite getSprite(String name, LevelScene levelScene, int x, int y, int dir, int type, boolean winged, 
                            int mapX, int mapY, float xa, float ya, int xPic, int yPic, int timeSpan) {
        if(name == null) {
            return null;
        } else if(name.equalsIgnoreCase("SPARKLE")) {
            return new Sparkle(x, y, xa, ya, xPic, yPic, timeSpan);
        } else if(name.equalsIgnoreCase("BULLETBILL")) {
            return new BulletBill(levelScene, x, y, dir);
        } else if(name.equalsIgnoreCase("GREENMUSHROOM")) {
            return new GreenMushroom(levelScene, x, y);
        } else if(name.equalsIgnoreCase("MUSHROOM")) {
            return new Mushroom(levelScene, x, y);
        } else if(name.equalsIgnoreCase("FIREFLOWER")) {
            return new FireFlower(levelScene, x, y);
        } else if(name.equalsIgnoreCase("COINANIM")) {
            return new CoinAnim(x, y);
        } else if(name.equalsIgnoreCase("PARTICLE")) {
            return new Particle(x, y, xa, ya);
        } else if(name.equalsIgnoreCase("ENEMY")) {
            return new Enemy(levelScene, x, y, dir, type, winged, mapX, mapY);
        } else if(name.equalsIgnoreCase("FLOWERENEMY")) {
            return null;
        } else if(name.equalsIgnoreCase("PRINCESS")) {
            return null;
        } else if(name.equalsIgnoreCase("WAVEGOOMBA")) {
            return new WaveGoomba(levelScene, x, y, dir, mapX, mapY);
        }
    }
}