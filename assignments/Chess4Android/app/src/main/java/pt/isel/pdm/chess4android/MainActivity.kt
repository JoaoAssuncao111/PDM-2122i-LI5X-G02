package pt.isel.pdm.chess4android

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import pt.isel.pdm.chess4android.databinding.ActivityMainBinding
import pt.isel.pdm.chess4android.views.TileView



class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }



    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        intent.getParcelableExtra<Parcelable>("Board")
        super.onCreate(savedInstanceState)
        viewModel.getDailyPuzzle()


        viewModel.dailyPuzzle.observe(this) {
            binding.boardView.setBoard(viewModel.board!!)
            setContentView(binding.root)

            binding.boardView.onTileClickedListener = { tile: TileView, row: Int, column: Int ->
                //var convertedRow = 7 - row
                var currentPiece = viewModel.currentPiece
                //Was a piece from the current army pressed
                if (viewModel.selectPiece(row, column)) {
                    if (currentPiece != viewModel.currentPiece) {
                        viewModel.currentPieceMoves?.let { binding.boardView.highlightMoves(it,tile) }
                    }
                } else {
                    if(viewModel.movePiece(row,column))
                        binding.boardView.drawMove(tile)
                }
            }



            findViewById<Button>(R.id.about_button).setOnClickListener{
                startActivity(Intent(this,AboutActivity::class.java))
            }

        }

    }

}