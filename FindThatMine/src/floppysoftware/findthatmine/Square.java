/**
 * Square.java
 */
package floppysoftware.findthatmine;

import android.widget.ImageView;

/**
 * Implements a square for the board game FindThatMine!
 * 
 * @author   Miguel I. García López
 * @version 1.00
 */
public class Square {
	
	private ImageView imgView = null;
	private boolean isVisible = true;
	private boolean hasFlag = false;
	private boolean hasMine = false;
	private int count = 0;
	private int row = 0;
	private int col = 0;
	
	public Square() {
		//
	}
	
	public void setImageView(ImageView imgView) {
		this.imgView = imgView;
	}
	
	public void setImageResource(int img) {
		this.imgView.setImageResource(img);
	}
	
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	public void setFlag(boolean hasFlag) {
		this.hasFlag = hasFlag;
	}
	
	public void setMine(boolean hasMine) {
		this.hasMine = hasMine;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public void setCol(int col) {
		this.col = col;
	}
	
	public ImageView getImageView() {
		return this.imgView;
	}
	
	public boolean getFlag() {
		return this.hasFlag;
	}
	
	public boolean getMine() {
		return this.hasMine;
	}
	
	public boolean getVisible() {
		return this.isVisible;
	}
	
	public int getCount() {
		return this.count;
	}
	
	public int getRow() {
		return this.row;
	}
	
	public int getCol() {
		return this.col;
		
	}

}
