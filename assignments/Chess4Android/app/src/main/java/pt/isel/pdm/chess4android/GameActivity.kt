package pt.isel.pdm.chess4android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import pt.isel.pdm.chess4android.databinding.ActivityGameBinding
import pt.isel.pdm.chess4android.views.TileView


class GameActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityGameBinding.inflate(layoutInflater)
    }

    private val viewModel: GameActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        val initialIntent = intent
        val newIntent = Intent(this,AboutActivity::class.java)

        super.onCreate(savedInstanceState)


        if ((initialIntent.extras!!.getBoolean("Game"))) {
            //viewModel.getDailyPuzzle()
        } else {
            setupViews()
            findViewById<Button>(R.id.about_button).setOnClickListener{
                startActivity(newIntent)
            }

        }

        /*viewModel.dailyPuzzle.observe(this) {
            setupViews()
            findViewById<Button>(R.id.about_button).setOnClickListener{
                startActivity(newIntent)
            }
        }*/

    }

    private fun setupViews() {
        binding.boardView.setBoard(viewModel.board!!)
        setContentView(binding.root)

        binding.boardView.onTileClickedListener = { tile: TileView, row: Int, column: Int ->
            //var convertedRow = 7 - row
            var currentPiece = viewModel.currentPiece
            //Was a piece from the current army pressed
            if (viewModel.selectPiece(row, column)) {
                if (currentPiece != viewModel.currentPiece) {
                    viewModel.currentPieceMoves?.let {
                        binding.boardView.highlightMoves(
                            it,
                            tile
                        )
                    }
                }
            } else {
                if (viewModel.movePiece(row, column)) {
                    binding.boardView.drawMove()
                    if(viewModel.makeMoveIfPuzzle()){
                        binding.boardView.drawMove()
                    }
                }

            }
        }

    }

}