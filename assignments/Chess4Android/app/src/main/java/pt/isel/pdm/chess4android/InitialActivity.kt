package pt.isel.pdm.chess4android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class InitialActivity : AppCompatActivity() {
    private val viewModel: InitialActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)



        //val call = dailyPuzzleService.getPuzzle()

        findViewById<Button>(R.id.fetch_button).setOnClickListener {
            val intent: Intent = Intent()
            intent.putExtra("Board",viewModel.dailyPuzzle.value)
            startActivity(Intent(this,MainActivity::class.java))
            }

        }
    }
