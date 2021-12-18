package pt.isel.pdm.chess4android.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.GridLayout
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import pt.isel.pdm.chess4android.models.Army
import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.models.*
import pt.isel.pdm.chess4android.views.TileView.Type
import kotlin.reflect.KClass



typealias TileTouchListener = (tile: TileView, row: Int, column: Int) -> Unit

/**
 * Custom view that implements a chess board.
 */
@SuppressLint("ClickableViewAccessibility")
class BoardView(private val ctx: Context, attrs: AttributeSet?) : GridLayout(ctx, attrs) {
    private var board: Board? = null
    private val tiles: Array<Array<TileView?>>
    private var highlightedTiles: List<TileView>? = null
    private val side = 8

    private val brush = Paint().apply {
        ctx.resources.getColor(R.color.chess_board_black, null)
        style = Paint.Style.STROKE
        strokeWidth = 10F
    }


    private fun createImageEntry(army: Army, piece: KClass<*>, imageId: Int) =
        Pair(Pair(army, piece), VectorDrawableCompat.create(ctx.resources, imageId, null))

    private val piecesImages = mapOf(
        createImageEntry(Army.WHITE, Pawn::class, R.drawable.ic_white_pawn),
        createImageEntry(Army.WHITE, Knight::class, R.drawable.ic_white_knight),
        createImageEntry(Army.WHITE, Bishop::class, R.drawable.ic_white_bishop),
        createImageEntry(Army.WHITE, Rook::class, R.drawable.ic_white_rook),
        createImageEntry(Army.WHITE, Queen::class, R.drawable.ic_white_queen),
        createImageEntry(Army.WHITE, King::class, R.drawable.ic_white_king),
        createImageEntry(Army.BLACK, Pawn::class, R.drawable.ic_black_pawn),
        createImageEntry(Army.BLACK, Knight::class, R.drawable.ic_black_knight),
        createImageEntry(Army.BLACK, Bishop::class, R.drawable.ic_black_bishop),
        createImageEntry(Army.BLACK, Rook::class, R.drawable.ic_black_rook),
        createImageEntry(Army.BLACK, Queen::class, R.drawable.ic_black_queen),
        createImageEntry(Army.BLACK, King::class, R.drawable.ic_black_king),
    )

    init {
        tiles = Array(side) { Array(8) { null } }
        rowCount = side
        columnCount = side
        repeat(side * side) {
            val row = it / side
            val column = it % side
            val tile = TileView(
                ctx,
                if((row + column) % 2 == 0) Type.WHITE else Type.BLACK,
                side,
                piecesImages
            )
            tile.setOnClickListener { onTileClickedListener?.invoke(tile, 7-row, column) }
            addView(tile)
            tiles[7-row][column] = tile
        }
    }

    fun setBoard(board: Board){
        this.board = board
        repeat(side*side){
            val row = it / side
            val column = it % side
            val piece = board.getPieceAtTile(Tile(row,column))
            if(piece != null) tiles[row][column]!!.piece = piece
        }

    }

    fun drawMove(){
        if(highlightedTiles != null) removeHighlights()
        val lastMove: Pair<Tile, ChessPiece> = board!!.lastMoves[board!!.lastMoves.size-1]!!
        if(lastMove.second is King){
            val secondLastMove = board!!.lastMoves[board!!.lastMoves.size-2]
            if(secondLastMove!!.second is Rook && secondLastMove.second.army == lastMove.second.army){
                board!!.lastMoves.removeLast()
                drawMove()
                board!!.lastMoves.add(lastMove)
            }
        }
        tiles[lastMove.first.row][lastMove.first.column]!!.piece = null
        tiles[lastMove.second.row][lastMove.second.column]!!.piece = lastMove.second

    }
    fun highlightMoves(moves: List<Tile>, tile: TileView){
        if(highlightedTiles != null) removeHighlights()
        if(moves.isEmpty()) return
        val highlights = mutableListOf<TileView>();
        for(tile in moves){
            val temp = tiles[tile.row][tile.column]!!;
            temp.highlightTile()
            highlights.add(temp)
        }
        highlightedTiles = highlights;
    }

    private fun removeHighlights(){
        for(tile in highlightedTiles!!){
            tile.invalidate()
        }
        highlightedTiles = null
    }

    var onTileClickedListener: TileTouchListener? = null

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        canvas.drawLine(0f, 0f, width.toFloat(), 0f, brush)
        canvas.drawLine(0f, height.toFloat(), width.toFloat(), height.toFloat(), brush)
        canvas.drawLine(0f, 0f, 0f, height.toFloat(), brush)
        canvas.drawLine(width.toFloat(), 0f, width.toFloat(), height.toFloat(), brush)
    }
}