package pt.isel.pdm.chess4android.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import pt.isel.pdm.chess4android.Army
import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.models.ChessPiece
import kotlin.reflect.KClass

/**
 * Custom view that implements a chess board tile.
 * Tiles are either black or white and can they can be empty or occupied by a chess piece.
 *
 * Implementation note: This view is not to be used with the designer tool.
 * You need to adapt this view to suit your needs. ;)
 *
 * @property type           The tile's type (i.e. black or white)
 * @property tilesPerSide   The number of tiles in each side of the chess board
 *
 */

@SuppressLint("ViewConstructor")
class TileView(
    private val ctx: Context,
    private val type: Type,
    private val tilesPerSide: Int,
    private val images: Map<Pair<Army, KClass<*>>, VectorDrawableCompat?>,
    initialPiece: ChessPiece? = null,
) : View(ctx) {

    var piece: ChessPiece? = initialPiece
        set(value) {
            field = value
            invalidate()
        }


    private var highlight: Boolean = false
    enum class Type { WHITE, BLACK}

    private val highlightBrush = Paint().apply { color = ctx.resources.getColor(R.color.highlighted_chess_board_blue,null)}
    //private checkBrush()
    private val brush = Paint().apply {
        color = ctx.resources.getColor(
            if (type == Type.WHITE) R.color.chess_board_white else R.color.chess_board_black,
            null)

        style = Paint.Style.FILL_AND_STROKE
    }

    fun highlightTile(){
        highlight = true
        invalidate()

    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val side = Integer.min(
            MeasureSpec.getSize(widthMeasureSpec),
            MeasureSpec.getSize(heightMeasureSpec)
        )
        setMeasuredDimension(side / tilesPerSide, side / tilesPerSide)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), if(highlight) highlightBrush else brush)
        if (piece != null) {
            images[Pair(piece!!.army,piece!!::class)]?.apply {
                val padding = 8
                setBounds(padding, padding, width-padding, height-padding)
                draw(canvas)
            }
        }
    }
}