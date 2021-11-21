package pt.isel.pdm.chess4android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class InitialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
        var boolean = true
        val intent  = Intent(this,MainActivity::class.java)

        findViewById<Button>(R.id.fetch_button).setOnClickListener {
            intent.putExtra("Game",boolean)
            startActivity(intent)
            }

        findViewById<Button>(R.id.fetch_button_game).setOnClickListener {
            boolean = false
            intent.putExtra("Game",boolean)
            startActivity(intent)
        }

        }
    }
