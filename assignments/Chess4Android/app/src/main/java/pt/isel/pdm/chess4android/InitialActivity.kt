package pt.isel.pdm.chess4android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class IniActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)

        val call = dailyPuzzleService.getPuzzle()

        findViewById<Button>(R.id.fetch_button).setOnClickListener {

            }
            startActivity(intent)
        }
    }
