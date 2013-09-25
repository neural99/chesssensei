package se.lannstrom.chesssensei;

import java.util.HashMap;

import se.lannstrom.chesssensei.model.Board;
import se.lannstrom.chesssensei.model.Board.ChessPiece;
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

	private int selectedY = -1;
	private int selectedX = -1;

	private Paint selectedPaint;
	
	private HashMap<ChessPiece, Bitmap> bigBitmaps = new HashMap<Board.ChessPiece, Bitmap>();
	private HashMap<ChessPiece, Bitmap> scaledBitmaps = new HashMap<Board.ChessPiece, Bitmap>();

	public BoardView(Context context, AttributeSet as) {
		super(context, as);
		
		darkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		darkPaint.setColor(Color.GREEN);
		lightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		lightPaint.setColor(Color.GRAY);
		selectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		selectedPaint.setColor(Color.RED);
		
		rect = new Rect();
		
		board = Board.createStartingBoard();
		
		loadChessPieceImages();
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
	   selectedX = (int) (getRelativeX(e) / squareDiag);
	   selectedY = (int) (getRelativeY(e) / squareDiag);
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
		
		Paint p = null;
		if (j == selectedY && i == selectedX) {
			p = selectedPaint;
		} else if ((j + i) % 2 == 0) {
			p = darkPaint;
		} else {
			p = lightPaint;
		}
		
		canvas.drawRect(rect, p);
		
		ChessPiece cp = board.getPieceAt(i, j);
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

}
