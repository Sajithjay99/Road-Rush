package com.example.mygame

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), GameTask {

    // Declare variables
    private lateinit var rootLayout: LinearLayout
    private lateinit var startBtn: Button
    private lateinit var mGameview: GameView
    private lateinit var score: TextView
    private lateinit var highScoreTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views and SharedPreferences
        startBtn = findViewById(R.id.startBtn)
        rootLayout = findViewById(R.id.rootLayout)
        score = findViewById(R.id.score)
        highScoreTextView = findViewById(R.id.highScoreTextView)
        mGameview = GameView(this, this)
        sharedPreferences = getSharedPreferences("MyGamePreferences", Context.MODE_PRIVATE)

        // Retrieve and display the high score
        val highScore = sharedPreferences.getInt("highScore", 0)
        highScoreTextView.text = "High Score: $highScore"

        // Set onClickListener for startBtn
        startBtn.setOnClickListener {
            // Remove the previous game view if it exists
            rootLayout.removeView(mGameview)

            // Create a new game view and add it to the layout
            mGameview = GameView(this@MainActivity, this@MainActivity)
            mGameview.setBackgroundResource(R.drawable.road)
            rootLayout.addView(mGameview)

            // Hide views when game starts
            startBtn.visibility = View.GONE
            score.visibility = View.GONE
            highScoreTextView.visibility = View.GONE
            val logo = findViewById<View>(R.id.logo)
            logo.visibility = View.GONE
        }
    }

    // Method to close the game
    override fun closeGame(mScore: Int) {
        // Update high score if the current score is higher
        val highScore = sharedPreferences.getInt("highScore", 0)
        if (mScore > highScore) {
            with(sharedPreferences.edit()) {
                putInt("highScore", mScore)
                apply()
            }
            highScoreTextView.text = "High Score: $mScore" // Update high score text
        }

        // Update score text and show views
        score.text = "Score : $mScore"
        rootLayout.removeView(mGameview)
        startBtn.visibility = View.VISIBLE
        score.visibility = View.VISIBLE
        highScoreTextView.visibility = View.VISIBLE // Show high score text again when game ends
        val logo = findViewById<View>(R.id.logo)
        logo.visibility = View.VISIBLE // Show logo image view again when game ends
    }
}
