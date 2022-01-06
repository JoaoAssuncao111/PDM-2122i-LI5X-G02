package pt.isel.pdm.chess4android.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import pt.isel.pdm.chess4android.history.GameActivity.Companion.buildPuzzleIntent
import pt.isel.pdm.chess4android.HistoryAdapter
import pt.isel.pdm.chess4android.databinding.ActivityHistoryBinding


class HistoryActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHistoryBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<HistoryActivityViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.puzzleList.layoutManager = LinearLayoutManager(this)

        // Get the list of quotes, if we haven't fetched it yet
        (viewModel.history ?: viewModel.loadHistory()).observe(this) {
            binding.puzzleList.adapter = HistoryAdapter(it) { puzzleDto ->
                startActivity(buildPuzzleIntent(this, puzzleDto))
            }
        }
    }
}