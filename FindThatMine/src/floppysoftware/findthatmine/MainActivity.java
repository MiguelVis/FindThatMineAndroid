/**
 * MainActivity.java
 */
package floppysoftware.findthatmine;

import java.util.Random;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

	//ImageView [][] arrayImageView = new ImageView[9][9];
	
	final static int SKIN_MINES = 0;
	final static int SKIN_FLOWERS = 1;
	
	int boardRows;
	int boardCols;
	int boardMines; // Mines according to level
	int boardSkin;
	//int boardSkinMines;
	//int boardSkinFlowers;
	Square [][] board;
	int gameFlags;
	int gameMines;
	TextView tvTimer;
	TextView tvFlagsCounter;
	TextView tvGameOver;
	ImageView ivFace;
	boolean gameOver;
	int gameVisibleSquares;
	int [] imgNumber = new int[9];
	int rowOffset[] = new int[8];
	int colOffset[] = new int[8];
	
	int secondsPassed;
	private boolean isTimerStarted; // check if timer already started or not
	private Handler timer = new Handler();

	public void startTimer()
	{
	  if (secondsPassed == 0) 
	  {
	    timer.removeCallbacks(updateTimeElasped);
	    // tell timer to run call back after 1 second
	    timer.postDelayed(updateTimeElasped, 1000);
	  }
	}

	public void stopTimer()
	{
	  // disable call backs
	  timer.removeCallbacks(updateTimeElasped);
	}

	// timer call back when timer is ticked
	private Runnable updateTimeElasped = new Runnable()
	{
	  public void run()
	  {
	    long currentMilliseconds = System.currentTimeMillis();
	    ++secondsPassed;
	    updateTimer();

	    // add notification
	    timer.postAtTime(this, currentMilliseconds);
	    // notify to call back after 1 seconds
	    // basically to remain in the timer loop
	    timer.postDelayed(updateTimeElasped, 1000);
	  }
	};
	
	private void updateTimer() {
		tvTimer.setText(String.format("%03d", secondsPassed));
		
	}
	
	private void checkIfTimerRunning() {
		
		// start timer on first click
	    if (!isTimerStarted)
	    {
	      startTimer();
	      isTimerStarted = true;
	    }

		
	}
	
	private void createBoard() {
		
		boardRows = 9;
		boardCols = 9;
		boardMines = 8;
		
		//boardSkinMines = 0;
		//boardSkinFlowers = 1;
		
		//boardSkin = boardSkinMines;
		boardSkin = SKIN_MINES;
		
		board = new Square[boardRows][boardCols];
		
		for(int r = 0; r < boardRows; ++r) {
			for(int c = 0; c < boardCols; ++c) {
				
				board[r][c] = new Square();
				
				ImageView iv = (ImageView) findViewById(getResources().getIdentifier("imageViewR" + r + "C" + c, "id", getPackageName()));
				
				iv.setOnClickListener(onClickListenerForSquares);
				iv.setOnLongClickListener(onLongClickListenerForSquares);
				
				board[r][c].setImageView(iv);
				
				
				
				//arrayImageView[r][c] = findViewById(R.id.***);
				
				//arrayImageView[r][c] = (ImageView) findViewById(getResources().getIdentifier("imageViewR" + r + "C" + c, "id", getPackageName()));
			}
		}
		
		imgNumber[0] = R.drawable.sq_ground;
		imgNumber[1] = R.drawable.sq_one;
		imgNumber[2] = R.drawable.sq_two;
		imgNumber[3] = R.drawable.sq_three;
		imgNumber[4] = R.drawable.sq_four;
		imgNumber[5] = R.drawable.sq_five;
		imgNumber[6] = R.drawable.sq_six;
		imgNumber[7] = R.drawable.sq_seven;
		imgNumber[8] = R.drawable.sq_eight;
		
		rowOffset[0]=-1; colOffset[0]= 0; /* Up */
		rowOffset[1]=-1; colOffset[1]=+1; /* Upper right */
		rowOffset[2]= 0; colOffset[2]=+1; /* Right */
		rowOffset[3]=+1; colOffset[3]=+1; /* Lower right */
		rowOffset[4]=+1; colOffset[4]= 0; /* Down */
		rowOffset[5]=+1; colOffset[5]=-1; /* Lower left */
		rowOffset[6]= 0; colOffset[6]=-1; /* Left */
		rowOffset[7]=-1; colOffset[7]=-1; /* Upper left */
		
	}
	
	OnClickListener onClickListenerForSquares = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			if(gameOver == false) {
				
				checkIfTimerRunning();
				
				//ImageView iv = (ImageView) v;
				
				//Square sq = findSquareById(iv.getId());
				
				Square sq = findSquareById(v.getId());
				
				if(sq.getVisible() == false && sq.getFlag() == false) {
					
					if(sq.getMine() == true) {
					
						gameOver = true;
						
						showMines();
						
						stopTimer();
						
						setFaceLosser();
						
						seeGameOver(true);

					} else {
						
						sq.setVisible(true);
						sq.setImageResource(imgNumber[sq.getCount()]);
						++gameVisibleSquares;
						
						
						//iv.setImageResource(imgNumber[sq.getCount()]);
						
						if(sq.getCount() == 0) {
						
							setVisAdj(sq.getRow(), sq.getCol());
						}
						
						checkIfWin();
						
					}
					
				}
			}

		}

	};
	
	OnLongClickListener onLongClickListenerForSquares = new OnLongClickListener() {
		
		@Override
		public boolean onLongClick(View v) {
			
			if(gameOver == false) {
				
				checkIfTimerRunning();
				
				//ImageView iv = (ImageView) v;
				
				//Square sq = findSquareById(iv.getId());
				
				Square sq = findSquareById(v.getId());
				
				if(sq.getVisible() == false) {
					
					if(sq.getFlag() == true) {
						//iv.setImageResource(R.drawable.sq_unknown);
						sq.setImageResource(R.drawable.sq_unknown);
						sq.setFlag(false);
						--gameFlags;
					} else {
						//iv.setImageResource(R.drawable.sq_flag);
						sq.setImageResource(R.drawable.sq_flag);
						sq.setFlag(true);
						++gameFlags;
					}
					
				}
				
				updateFlagsCounter();
				
				checkIfWin();
			}
			
			return true;
			
		}
	};
	
	private void setupBoard() {
		
		seeGameOver(false);
		
		for(int r = 0; r < boardRows; ++r) {
			for(int c = 0; c < boardCols; ++c) {
				
				//board[r][c].getImageView().setImageResource(R.drawable.sq_unknown);
				board[r][c].setImageResource(R.drawable.sq_unknown);
				board[r][c].setVisible(false);
				board[r][c].setFlag(false);
				board[r][c].setMine(false);
				board[r][c].setRow(r);
				board[r][c].setCol(c);
				//board[r][c].setCount(0);
			}
		}
		
		
		gameFlags = 0;
		gameMines = boardMines;
		gameVisibleSquares = 0;
		gameOver = false;
		
		// Place the mines randomly
		
		Random rnd = new Random();
		int i = 0;
		
		while(i < gameMines) {
			
			int r = rnd.nextInt(boardRows);
			int c = rnd.nextInt(boardCols);
			
			if(board[r][c].getMine() == true)
				continue;
			
			board[r][c].setMine(true);
			
			++i;			
		}
		
		for(int r = 0; r < boardRows; ++r) {
			for(int c = 0; c < boardCols; ++c) {
				
				board[r][c].setCount(findAdjMines(r, c));
			}
		}
		
		secondsPassed = 0;
		isTimerStarted = false;
		
		stopTimer();
		updateTimer();
		updateFlagsCounter();
		setFaceNormal();

	}
	
	private void showMines() {
		
		for(int r = 0; r < boardRows; ++r) {
			for(int c = 0; c < boardCols; ++c) {
				
				if(board[r][c].getMine() == true) {
					if(board[r][c].getFlag() == false) {
						//board[r][c].getImageView().setImageResource(R.drawable.sq_mine);
						if(boardSkin == SKIN_MINES)
						//if(boardSkin == boardSkinMines)
							board[r][c].setImageResource(R.drawable.sq_mine);
						else
							board[r][c].setImageResource(R.drawable.sq_flower);
					}
				} else if(board[r][c].getFlag() == true) {
					//board[r][c].getImageView().setImageResource(R.drawable.sq_flag_err);
					board[r][c].setImageResource(R.drawable.sq_flag_err);
				}
			}
		}

	}
	
	private void updateFlagsCounter() {
		
		tvFlagsCounter.setText(String.format("%03d", gameMines - gameFlags));
	}
	
	private Square findSquareById(int id) {
		
		for(int r = 0; r < boardRows; ++r) {
			for(int c = 0; c < boardCols; ++c) {
				
				if(board[r][c].getImageView().getId() == id)
						return board[r][c];
			}
		}
		return null;
	}

	private int findAdjMines(int row, int col) {
		
		int minesFound = 0;
		
		for(int i = 0; i < 8; ++i) {
			
			int rn = row + rowOffset[i];
			int cn = col + colOffset[i];
			
			if(rn >= 0 && rn < boardRows && cn >= 0 && cn < boardCols) {
				if(board[rn][cn].getMine() == true)
					++minesFound;
			}
		}
		
		return minesFound;

	}
	
	
	private void setVisAdj(int row, int col) {
		
		for(int i = 0; i < 8; ++i)	{
			
			int rn = row + rowOffset[i];
			int cn = col + colOffset[i];

			if(rn >= 0 && rn < boardRows && cn >= 0 && cn < boardCols) {
				
				if(board[rn][cn].getVisible() == false && board[rn][cn].getFlag() == false)	{
					
					board[rn][cn].setVisible(true);
					++gameVisibleSquares;
					
					//board[rn][cn].getImageView().setImageResource(imgNumber[board[rn][cn].getCount()]);
					board[rn][cn].setImageResource(imgNumber[board[rn][cn].getCount()]);
					
					if(board[rn][cn].getCount() == 0)
						setVisAdj(rn, cn);

				}
			}
		}
	}

	private boolean checkIfWin()
	{
		if(gameFlags == gameMines && gameFlags + gameVisibleSquares == boardRows * boardCols) {

			gameOver = true;
			
			setFaceWinner();
			
			stopTimer();
			
			seeGameOver(true);
			
		}

		return gameOver;
	}
	
	private void seeGameOver(boolean yes) {

			tvGameOver.setVisibility((yes == true) ? View.VISIBLE : View.INVISIBLE);
	}
	
	private void setFaceNormal() {
		ivFace.setImageResource(R.drawable.face_normal);
	}
	
	private void setFaceLosser() {
		ivFace.setImageResource(R.drawable.face_losser);
	}
	
	private void setFaceWinner() {
		ivFace.setImageResource(R.drawable.face_winner);
	}
	
	OnClickListener onClickListenerForFace = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			setupBoard();
		}
		
	};
	
	private void seeAboutDialog() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.about_of_title))
			   .setMessage(getResources().getString(R.string.about_of_text))
		       .setCancelable(false)
		       .setPositiveButton(getResources().getString(R.string.about_of_button), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                //do things
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tvTimer = (TextView) findViewById(R.id.textViewTimer);
        tvFlagsCounter = (TextView) findViewById(R.id.textViewFlags);
        tvGameOver = (TextView) findViewById(R.id.textViewGameOver);
        ivFace = (ImageView) findViewById(R.id.imageViewFace);
        
        ivFace.setOnClickListener(onClickListenerForFace);
       
        createBoard();
        setupBoard();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
    	
    	getMenuInflater().inflate(R.menu.top_menu, menu);
    	
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /**************
    	int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        ***************/
    	
    	switch(item.getItemId()) {
    		case R.id.opt_reset :
    			setupBoard();
    			break;    		
    		case R.id.opt_8_mines :
    			boardMines = 8;
    			setupBoard();
    			break;
    		case R.id.opt_12_mines :
    			boardMines = 12;
    			setupBoard();
    			break;
    		case R.id.opt_16_mines :
    			boardMines = 16;
    			setupBoard();
    			break;
    		case R.id.opt_skin_mines :
    			boardSkin = SKIN_MINES;
    			//boardSkin = boardSkinMines;
    			//setupBoard();
    			break;
    		case R.id.opt_skin_flowers :
    			boardSkin = SKIN_FLOWERS;
    			//boardSkin = boardSkinFlowers;
    			//setupBoard();
    			break;
    		case R.id.opt_about :
    			seeAboutDialog();
    			break;
    		default :
    			return super.onOptionsItemSelected(item);
    	}
        
        return true;
    }
}
