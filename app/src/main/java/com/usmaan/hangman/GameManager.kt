package com.usmaan.hangman

import kotlin.random.Random

class GameManager {

    private var lettersUsed: String = ""
    private lateinit var underscoreWord: String
    private lateinit var wordToGuess: String
    private val maxTries = 7
    private var currentTries = 0
    private var drawable: Int = R.drawable.game0

    fun startNewGame(): GameState {
        lettersUsed = ""
        currentTries = 0
        drawable = R.drawable.game7
        val randomIndex = Random.nextInt(0, GameConstants.words.size)
        wordToGuess = GameConstants.words[randomIndex]
        generateUnderscores(wordToGuess)
        return getGameState()
    }

    fun generateUnderscores(word: String) {
        val sb = StringBuilder()
        word.forEach { char ->
            if (char == '/') {
                sb.append('/')
            } else {
                sb.append("_")
            }
        }
        underscoreWord = sb.toString()
    }

    fun play(letter: Char): GameState {
        if (lettersUsed.contains(letter)) {
            return GameState.Running(lettersUsed, underscoreWord, drawable)
        }

        lettersUsed += letter
        val indexes = mutableListOf<Int>()

        wordToGuess.forEachIndexed { index, char ->
            if (char.equals(letter, true)) {
                indexes.add(index)
            }
        }

        var finalUnderscoreWord = "" + underscoreWord // _ _ _ _ _ _ _ -> E _ _ _ _ _ _
        indexes.forEach { index ->
            val sb = StringBuilder(finalUnderscoreWord).also { it.setCharAt(index, letter) }
            finalUnderscoreWord = sb.toString()
        }

        if (indexes.isEmpty()) {
            currentTries++
        }

        underscoreWord = finalUnderscoreWord
        return getGameState()
    }

    private fun getHangmanDrawable(): Int {
        return when (currentTries) {
            0 -> R.drawable.game0
            1 -> R.drawable.game1
            2 -> R.drawable.game2
            3 -> R.drawable.game3
            4 -> R.drawable.game4
            5 -> R.drawable.game5
            6 -> R.drawable.game6
            7 -> R.drawable.game7
            else -> R.drawable.game7
        }
    }

    private fun getGameState(): GameState {
        if (underscoreWord.equals(wordToGuess, true)) {
            return GameState.Won(wordToGuess)
        }

        if (currentTries == maxTries) {
            return GameState.Lost(wordToGuess)
        }

        drawable = getHangmanDrawable()
        return GameState.Running(lettersUsed, underscoreWord, drawable)
    }
}