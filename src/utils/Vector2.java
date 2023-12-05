package utils;

public class Vector2 {
	int x,y;
	public Vector2(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Vector2(Vector2 other)
	{
		this.x = other.x;
		this.y = other.y;
	}
	
	public static final Vector2 Vector0 = new Vector2(0,0);
		
	public int getX() {
		
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	public void add(Vector2 arg) {
		x+=arg.x;
		y+=arg.y;
	}
	public boolean equals(Vector2 arg) {
		return (this.x == arg.x) && (this.y == arg.y);
	}
	
	public void moveUp() {
		this.add(new Vector2(-1,0));
	}
	public void moveDown() {
		this.add(new Vector2(1,0));
	}
	public void moveLeft() {
		this.add(new Vector2(0,-1));
	}
	public void moveRight() {
		this.add(new Vector2(0,1));
	}
	
	public String toString() {
		return String.format("(%d , %d)", this.x, this.y);
	}
}
