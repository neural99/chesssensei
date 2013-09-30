package se.lannstrom.chesssensei;

import java.util.HashMap;

import se.lannstrom.chesssensei.SelectionManager.SelectionState;
import se.lannstrom.chesssensei.model.Board;
import se.lannstrom.chesssensei.model.Board.ChessColor;
import se.lannstrom.chesssensei.model.Board.ChessPiece;
import se.lannstrom.chesssensei.model.rules.ChessRuleStrategy;
import se.lannstrom.chesssensei.model.BoardPosition;
import se.lannstrom.chesssensei.model.ChessMove;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class BoardView extends View {
	private class GuestureDect extends GestureDetector.SimpleOnGestureListener {
	   @Override
	   public boolean onDown(MotionEvent e) {
		   tapped(e);
		   return true;
	   }
	   
	}
	private GestureDetector detector = new GestureDetector(BoardView.this.getContext(), new GuestureDect());

	private Board board;
	private Paint darkPaint;
	private Paint lightPaint;
	private Rect rect;
	private int squareDiag;
	private boolean inverted;
	
	private SelectionManager selectionManager;
	
	private Paint fromSelectedPaint;
	
	private Paint movesPaint;
	
	private HashMap<ChessPiece, Bitmap> bigBitmaps = new HashMap<Board.ChessPiece, Bitmap>();
	private HashMap<ChessPiece, Bitmap> scaledBitmaps = new HashMap<Board.ChessPiece, Bitmap>();

	private ChessRuleStrategy chessRuleStrategy;


	public BoardView(Context context, AttributeSet as) {
		super(context, as);
		
		darkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		darkPaint.setColor(Color.GREEN);
		lightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		lightPaint.setColor(Color.GRAY);
		fromSelectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		fromSelectedPaint.setColor(Color.RED);
		movesPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		movesPaint.setColor(Color.BLUE);
		
		rect = new Rect();
		board = Board.createStartingBoard();
		
		selectionManager = new SelectionManager(ChessColor.WHITE, board);
		selectionManager.setDoneCallback(new SelectionDoneCallback() {
			
			@Override
			public void selectionDone() {
				doMove();
			}
		});
		selectionManager.setSelectionState(SelectionState.FROM);
		
		chessRuleStrategy = new ChessRuleStrategy();
		
		loadChessPieceImages();
	}


	protected void doMove() {
		ChessMove move = selectionManager.buildMove();
		chessRuleStrategy.doMove(board, move);
		selectionManager.reset();
		selectionManager.setColor(board.getActive());
		
		invalidate();
	}


	private void loadChessPieceImages() {
		Bitmap tmp = null;
		
		tmp = BitmapFactory.decodeResource(getResources(), R.raw.bking);
		bigBitmaps.put(ChessPiece.B_KING, tmp);
		
		tmp = BitmapFactory.decodeResource(getResources(), R.raw.wking);
		bigBitmaps.put(ChessPiece.W_KING, tmp);
		
		tmp = BitmapFactory.decodeResource(getResources(), R.raw.bqueen);
		bigBitmaps.put(ChessPiece.B_QUEEN, tmp);
		
		tmp = BitmapFactory.decodeResource(getResources(), R.raw.wqueen);
		bigBitmaps.put(ChessPiece.W_QUEEN, tmp);
		
		tmp = BitmapFactory.decodeResource(getResources(), R.raw.brook);
		bigBitmaps.put(ChessPiece.B_ROOK, tmp);
		
		tmp = BitmapFactory.decodeResource(getResources(), R.raw.wrook);
		bigBitmaps.put(ChessPiece.W_ROOK, tmp);
		
		tmp = BitmapFactory.decodeResource(getResources(), R.raw.bbishop);
		bigBitmaps.put(ChessPiece.B_BISHOP, tmp);
		
		tmp = BitmapFactory.decodeResource(getResources(), R.raw.wbishop);
		bigBitmaps.put(ChessPiece.W_BISHOP, tmp);
		
		tmp = BitmapFactory.decodeResource(getResources(), R.raw.bknight);
		bigBitmaps.put(ChessPiece.B_KNIGHT, tmp);
		
		tmp = BitmapFactory.decodeResource(getResources(), R.raw.wknight);
		bigBitmaps.put(ChessPiece.W_KNIGHT, tmp);
		
		tmp = BitmapFactory.decodeResource(getResources(), R.raw.bpawn);
		bigBitmaps.put(ChessPiece.B_PAWN, tmp);
		
		tmp = BitmapFactory.decodeResource(getResources(), R.raw.wpawn);
		bigBitmaps.put(ChessPiece.W_PAWN, tmp);
	}

	public double getRelativeX(MotionEvent e) {
		return e.getX() - getLeft();
	}
	
	public double getRelativeY(MotionEvent e) {
		return e.getY() - getTop();
	}
	
	public void tapped(MotionEvent e) {
	   int selectedX = (int) (getRelativeX(e) / squareDiag);
	   int selectedY = (int) (getRelativeY(e) / squareDiag);
	   if (selectedX > 7) selectedX = 7;
	   if (selectedY > 7) selectedY = 7;
	   
	   if (inverted) {
		   selectedY = 7 - selectedY;
	   }
	   
	   selectionManager.select(selectedX, selectedY);
	   
	   invalidate();
	}

	public Board getBoard() {
		return board;
	}
	
	public void setBoard(Board b) {
		board = b;
		invalidate();
	}
	
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		int squareWidth = w / 8;
		int squareHeight = h / 8;
		
		squareDiag = Math.min(squareWidth, squareHeight);
		scaleBitmaps(squareDiag);
	}

	private void scaleBitmaps(int dstDiag) {
		for (ChessPiece cp : bigBitmaps.keySet()) {
			Bitmap big = bigBitmaps.get(cp);
			Bitmap scaled = Bitmap.createScaledBitmap(big, dstDiag, dstDiag, true);
			scaledBitmaps.put(cp, scaled);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j ++) {
				drawSquare(canvas, i, j);
			}
		}
	}

	private void drawSquare(Canvas canvas, int i, int j) {
		rect.left = getLeft() + i * squareDiag;
		rect.right = rect.left + squareDiag;
		rect.top = getTop() + j * squareDiag;
		rect.bottom = rect.top + squareDiag;
		
		if (inverted) {
			j = 7 - j;
		}
		
		Paint p = null;
		BoardPosition from = selectionManager.getFrom();
		if (from != null && 
				from.getX() == i && 
				from.getY() == j) {
			p = fromSelectedPaint;
		} else if (selectionManager.isValidMove(i, j)) {
			p = movesPaint;
		} else if ((j + i) % 2 == 0) {
			p = darkPaint;
		} else {
			p = lightPaint;
		}
		
		canvas.drawRect(rect, p);
		
		ChessPiece cp = null;
		if (inverted) {
			cp = board.getPieceAt(i, j);
		} else {
			cp = board.getPieceAt(i, j);
		}
		if (cp != null) {
			Bitmap map = scaledBitmaps.get(cp);
			if (map != null) {
				canvas.drawBitmap(map, null, rect, p);
			} else {
				Log.d("", cp.toString());
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean result = detector.onTouchEvent(event);
		return result;
	}


	public void flip() {
		if (inverted) {
			inverted = false;
		} else {
			inverted = true;
		}
		invalidate();
	}

}
