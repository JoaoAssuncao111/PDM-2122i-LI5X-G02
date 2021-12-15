package pt.isel.pdm.chess4android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.getDailyPuzzle()

        val intent  = Intent(this,GameActivity::class.java)

        findViewById<Button>(R.id.fetch_button).setOnClickListener {
            intent.putExtra("Game",true)
            startActivity(intent)
            }

        findViewById<Button>(R.id.fetch_button_game).setOnClickListener {
            intent.putExtra("Game",false)
            startActivity(intent)
        }

    }
}
