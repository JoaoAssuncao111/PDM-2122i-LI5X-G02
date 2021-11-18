package pt.isel.pdm.chess4android

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import pt.isel.pdm.chess4android.databinding.ActivityMainBinding
import pt.isel.pdm.chess4android.views.BoardView
import pt.isel.pdm.chess4android.views.TileView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val dailyPuzzleService: DailyPuzzleService = Retrofit.Builder()
    .baseUrl(URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(DailyPuzzleService::class.java)


class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding.boardView.setBoard(viewModel.board!!)
        setContentView(binding.root)

        //val call = dailyPuzzleService.getPuzzle()
        //viewModel.dailyPuzzle.observe(this) {}

        /* call.enqueue(object : Callback<PuzzleInfo> {
            override fun onResponse(call: Call<PuzzleInfo>, response: Response<PuzzleInfo>) {
            TODO()
            //val game = response.body()?.game

            }

            override fun onFailure(call: Call<PuzzleInfo>, t: Throwable) {
                TODO()
            }
        })

        binding.boardView.onTileClickedListener = { tile: TileView, row: Int, column: Int ->
            var currentPiece = viewModel.currentPiece
            //Was a piece from the current army pressed
            if (viewModel.selectPiece(row, column)) {
                if (currentPiece != viewModel.currentPiece) {
                    TODO()
                }
            } else {
                    if(viewModel.movePiece(row,column)) //board view update
                        TODO()

            }
        }

        //Called when menu is created
        fun onCreateOptionsMenu(menu: Menu?): Boolean {
            return super.onCreateOptionsMenu(menu)

        }

        //Called when an option in the menu is selected
        fun onOptionsItemSelected(item: MenuItem): Boolean {
            return super.onOptionsItemSelected(item)
        } */
    }
}