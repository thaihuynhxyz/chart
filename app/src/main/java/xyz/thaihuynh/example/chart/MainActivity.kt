package xyz.thaihuynh.example.chart

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ringChart.setGoal(100)
        randomChart()
        ringChart.setOnRingClickListener(View.OnClickListener { randomChart() })
    }

    private fun randomChart() {
        val random = Random()
        ringChart.clearData()
        val num1 = random.nextFloat() * 100
        val num2 = random.nextFloat() * (100 - num1)
        val num3 = random.nextFloat() * (100 - num1 - num2)
        ringChart.addItem(num1, Color.RED)
        ringChart.addItem(num2, Color.GREEN)
        ringChart.addItem(num3, Color.BLUE)
        ringChart.notifyDataSetChanged()
    }
}
