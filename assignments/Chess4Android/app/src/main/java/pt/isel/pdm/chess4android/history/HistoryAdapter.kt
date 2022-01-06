package pt.isel.pdm.chess4android

import android.animation.ValueAnimator
import android.graphics.drawable.GradientDrawable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat

class HistoryItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val dayView = itemView.findViewById<TextView>(R.id.day)
    private val isCompleted = itemView.findViewById<TextView>(R.id.state)

    /**
     * Binds this view holder to the given quote item
     */
    fun bindTo(puzzleInfoDTO: DailyPuzzleInfoDTO, onItemCLick: () -> Unit) {
        dayView.text = SimpleDateFormat("dd/MM/yyyy").format(puzzleInfoDTO.date)
        isCompleted.text = if(puzzleInfoDTO.state) "Solved" else "Unsolved"

        itemView.setOnClickListener {
            itemView.isClickable = false
            startAnimation {
                onItemCLick()
                itemView.isClickable = true
            }
        }
    }

    /**
     * Starts the item selection animation and calls [onAnimationEnd] once the animation ends
     */
    private fun startAnimation(onAnimationEnd: () -> Unit) {

        val animation = ValueAnimator.ofArgb(
            ContextCompat.getColor(itemView.context, R.color.list_item_background),
            ContextCompat.getColor(itemView.context, R.color.list_item_background_selected),
            ContextCompat.getColor(itemView.context, R.color.list_item_background)
        )

        animation.addUpdateListener { animator ->
            val background = itemView.background as GradientDrawable
            background.setColor(animator.animatedValue as Int)
        }

        animation.duration = 400
        animation.doOnEnd { onAnimationEnd() }

        animation.start()
    }
}



class HistoryAdapter (
    private val dataSource: List<DailyPuzzleInfoDTO>,
    private val onItemCLick: (DailyPuzzleInfoDTO) -> Unit
    ): RecyclerView.Adapter<HistoryItemViewHolder>() {

        /**
         * Factory method of view holders (and its associated views)
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_history_view, parent, false)
            return HistoryItemViewHolder(view)
        }

        /**
         * Associates (binds) the view associated to [viewHolder] to the item at [position] of the
         * data set to be adapted.
         */
        override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
            holder.bindTo(dataSource[position]) {
                onItemCLick(dataSource[position])
            }
        }

        /**
         * The size of the data set
         */
        override fun getItemCount() = dataSource.size
}
