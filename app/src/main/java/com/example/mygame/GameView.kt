package com.example.mygame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

class GameView(context: Context, private val gameTask: GameTask) : View(context) {
    private var myPaint: Paint? = null
    private var speed = 1 // Initial speed of the game
    private var time = 0 // Time elapsed in the game
    private var score = 0 // Player's score
    private var myCarPosition = 0 // Player's car position
    private val otherCars = ArrayList<HashMap<String, Any>>() // List to store other cars

    var viewWidth = 0
    var viewHeight = 0

    init {
        myPaint = Paint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewWidth = this.measuredWidth
        viewHeight = this.measuredHeight

        // Generate other cars randomly
        if (time % 700 < 10 + speed) {
            val map = HashMap<String, Any>()
            map["lane"] = (0..2).random()
            map["startTime"] = time
            otherCars.add(map)
        }
        time = time + 10 + speed
        val carWidth = viewWidth / 5
        val carHeight = carWidth + 10

        // Draw player's car
        val d = resources.getDrawable(R.drawable.cars_reds, context.theme)
        d.setBounds(
            myCarPosition * viewWidth / 3 + viewWidth / 15 + 25,
            viewHeight - 2 - carHeight,
            myCarPosition * viewWidth / 3 + viewWidth / 15 + carWidth - 25,
            viewHeight - 2
        )
        d.draw(canvas)

        // Draw other cars and handle collisions
        for (i in otherCars.indices) {
            try {
                val carX = otherCars[i]["lane"] as Int * viewWidth / 3 + viewWidth / 15
                var carY = time - otherCars[i]["startTime"] as Int
                val d2 = resources.getDrawable(R.drawable.cars_yellows, context.theme)
                d2.setBounds(
                    carX + 25, carY - carHeight, carX + carWidth - 25, carY
                )
                d2.draw(canvas)
                if (otherCars[i]["lane"] as Int == myCarPosition) {
                    if (carY > viewHeight - 2 - carHeight && carY < viewHeight - 2) {
                        gameTask.closeGame(score) // Game over if collision occurs
                    }
                }
                if (carY > viewHeight + carHeight) {
                    otherCars.removeAt(i)
                    score++
                    speed = 1 + Math.abs(score / 8) // Increase speed as score increases
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Draw score and speed
        myPaint!!.color = Color.WHITE
        myPaint!!.textSize = 40f
        canvas.drawText("Score : $score", 80f, 80f, myPaint!!)
        canvas.drawText("Speed : $speed", 380f, 80f, myPaint!!)
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                val x1 = event.x
                if (x1 < viewWidth / 2) {
                    if (myCarPosition > 0) {
                        myCarPosition--
                    }
                }
                if (x1 > viewWidth / 2) {
                    if (myCarPosition < 2) {
                        myCarPosition++
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {}
        }
        return true
    }
}
