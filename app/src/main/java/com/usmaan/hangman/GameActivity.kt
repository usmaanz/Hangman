package com.usmaan.hangman

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children

class GameActivity : AppCompatActivity() {

    private val gameManager = GameManager()

    private lateinit var wordTextView: TextView
    private lateinit var lettersUsedTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var gameLostTextView: TextView
    private lateinit var gameWonTextView: TextView
    private lateinit var newGameButton: Button
    private lateinit var lettersLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        imageView = findViewById(R.id.imageView)
        wordTextView = findViewById(R.id.wordTextView)
        lettersUsedTextView = findViewById(R.id.lettersUsedTextView)
        gameLostTextView = findViewById(R.id.gameLostTextView)
        gameWonTextView = findViewById(R.id.gameWonTextView)
        newGameButton = findViewById(R.id.newGameButton)
        lettersLayout = findViewById(R.id.lettersLayout)
        newGameButton.setOnClickListener {
            startNewGame()
        }
        val gameState = gameManager.startNewGame()
        updateUI(gameState)

        lettersLayout.children.forEach { letterView ->
            if (letterView is TextView) {
                letterView.setOnClickListener {
                    val gameState = gameManager.play((letterView).text[0])
                    updateUI(gameState)
                    letterView.visibility = View.GONE
                }
            }
        }
    }

    private fun updateUI(gameState: GameState) {
        when (gameState) {
            is GameState.Lost -> showGameLost(gameState.wordToGuess)
            is GameState.Running -> {
                wordTextView.text = gameState.underscoreWord
                lettersUsedTextView.text = "Letters used: ${gameState.lettersUsed}"
                imageView.setImageDrawable(ContextCompat.getDrawable(this, gameState.drawable))
            }
            is GameState.Won -> showGameWon(gameState.wordToGuess)
        }
    }

    private fun showGameLost(wordToGuess: String) {
        wordTextView.text = wordToGuess
        gameLostTextView.visibility = View.VISIBLE
        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.game7))
        lettersLayout.visibility = View.GONE
    }

    private fun showGameWon(wordToGuess: String) {
        wordTextView.text = wordToGuess
        gameWonTextView.visibility = View.VISIBLE
        lettersLayout.visibility = View.GONE
    }

    private fun startNewGame() {
        gameLostTextView.visibility = View.GONE
        gameWonTextView.visibility = View.GONE
        val gameState = gameManager.startNewGame()
        lettersLayout.visibility = View.VISIBLE
        lettersLayout.children.forEach { letterView ->
            letterView.visibility = View.VISIBLE
        }
        updateUI(gameState)
    }
}