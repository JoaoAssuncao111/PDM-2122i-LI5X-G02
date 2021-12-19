package pt.isel.pdm.chess4android.daily

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import pt.isel.pdm.chess4android.history.GameActivity
import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.history.HistoryActivity

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.fetch_button).setOnClickListener {
            //viewModel.fetchDailyPuzzle()
            startActivity(Intent(this, HistoryActivity::class.java))
            }

        findViewById<Button>(R.id.fetch_button_game).setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }
    }
}
