import java.awt.Image;

public class TrashCan extends DisplayObject {
	Image trashcanSprite;
	Game theGame;
    public TrashCan(int x, int y, Game game, Image theImage) {
    	super(game,x,y);
		setSprite(theImage);
		theGame=game;
		trashcanSprite=theImage;
    }
    public void onCollision() {
    	// implement seperate collision test? cuz you have to come up behind the trashcan, not bump
    	theGame.getPlayer().setCrouch(true);
    }
}
