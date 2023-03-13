import java.util.*;
import java.awt.*;
import java.awt.geom.*;

public class OncomingStudent extends DisplayObject{
	private ArrayList<Image> students;
	private Game game;
	private int velocity, positionX, positionY, index;
	private Player player;
	//private Path path;
	private ArrayList<OncomingStudent> currentStudents;
	private int offset;
	private double heading;
	private int goal;
	private boolean set;
	private double distOnPath;
	private int dimensionX, dimensionY;
	private int GRAZE_HEALTH, HEAD_ON_HEALTH, AVOID_DISX, AVOID_V; //these will equal something later, placeholder
	//AVOID_V what velocity students change to when splitting
	//AVOID_DIS distance they separate by 
	
	public OncomingStudent(Game g, int posX, int posY, double heading, int dimensionX, int dimensionY) {
		super(g, posX, posY, heading, dimensionX, dimensionY);
		game = g;
		positionX = posX;
		positionY = posY;
		player = g.getPlayer();
		index = (int) (Math.random()*students.size());
		//path = g.getMap().getPath();
		offset = 0;
		
		generateList();
	}
	
	public double getDistOnPath() {
		return distOnPath;
	}
	
	public void setDistOnPath(double dist) {
		distOnPath = dist;
	}
	
	private Point getPoint() {
		return new Point(this.getPositionX(), this.getPositionY());
	}
	
	public void setHeading(double head) {
		heading = head;
	}
	
	public double getHeading() {
		return heading;
	}
	
	public Hitbox getLHitbox() {
		//hitbox for grazes
		return new Hitbox(getPoint(), dimensionX+10, dimensionY+10, heading);
	}
	
	public Hitbox getSepHitbox() {
		//hitbox for detecting whether or not to separate
		return new Hitbox(getPoint(), dimensionX+10, dimensionY, heading);
	}
	
	public void setOffset(int strafe) {
		Map map = game.getMap();
		offset = Math.max(-1*map.getMaxStrafe()*map.getScale(), Math.min(map.getMaxStrafe()*map.getScale(), strafe));
	}
	
	public int getOffset() {
		return offset;
	}
	
	public void setVelocity(int vel) {
		velocity = vel;
	}
	
	public int getVelocity() {
		return velocity;
	}
	
	public int getPositionX() {
		return positionX;
	}
	
	public int getPositionY() {
		return positionY;
	}
	
	public void setPositionX(int x) {
		positionX = x;
	}
	
	public void setPositionY(int y) {
		positionY = y;
	}
	
	//specialize on collision
	
	public void onCollision() {
		super.onCollision();
		if(this.getLHitbox().isColliding(player.getHitbox())) {
			if(this.getHitbox().isColliding(player.getHitbox())){
				player.setHealth(player.getHealth()-HEAD_ON_HEALTH);
			} else {
				player.setHealth(player.getHealth()-GRAZE_HEALTH);
			}
		}	
	}
	
	private void generateList() {
		int i = 0;
		while(game.getOncomingStudents(i)!=null) {
			currentStudents.add(game.getOncomingStudents(i));
		}
	}
	
	public void checkProximity() {
		Hitbox studentHB;
		double playerDist = player.getDistOnPath();
		int playerOff = player.getOffset();
		Hitbox thisHB = this.getSepHitbox();
		double target;
		if(currentStudents.size()>=1) {
			for(OncomingStudent student:currentStudents) {
				studentHB = student.getSepHitbox();
				if(studentHB.isColliding(thisHB)) {
					target = ((student.getOffset()+getOffset())/2);
					if(Math.abs(target-playerOff)<=5&&distOnPath-playerDist<=dimensionY+5) {
						set = false;
						student.setSet(false);
						separate(student);
					}
				}
			}
		}
	}
	
	public int getGoal() {
		return goal;
	}
	
	public void setGoal(int x) {
		goal = x;
	}
	
	private void setSet(boolean tOrFalse) {
		set = tOrFalse;
	}
	
	private void separate(OncomingStudent student) {
		int studentOff = student.getOffset();
		int studentGoal;
		//goal is ideally set to half of player's width
		if(set == false) {
			goal = Math.abs(studentOff+AVOID_DISX);
			studentGoal = Math.abs(getOffset()+AVOID_DISX);
			student.setGoal(studentGoal);
			student.setSet(true);
			set = true;
		} else {
			studentGoal = student.getGoal();
		}
		
		if(studentOff>getOffset()) {
			if(Math.abs(studentOff)!=studentGoal&&Math.abs(offset)!=goal) {
				student.setVelocity(AVOID_V);
				setVelocity(-AVOID_V);
			} else {
				student.setVelocity(0);
				setVelocity(0);
			}
		} else {
			student.setVelocity(-AVOID_V);
			setVelocity(AVOID_V);
			if(Math.abs(studentOff)!=studentGoal&&Math.abs(offset)!=goal) {
				student.setVelocity(AVOID_V);
				setVelocity(-AVOID_V);
			} else {
				student.setVelocity(0);
				setVelocity(0);
			}
		}
	}
	
	public Image getImage() {
		return students.get(index);
	}
	
	public static void main(String[]args) {
		
	}
}

