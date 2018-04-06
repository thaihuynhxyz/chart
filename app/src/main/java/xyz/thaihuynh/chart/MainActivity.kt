package xyz.thaihuynh.chart

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val random = Random()
        ringChart.setOnClickListener {
            ringChart.clearData()
            val num1 = random.nextFloat() * 100
            val num2 = random.nextFloat() * (100 - num1)
            val num3 = random.nextFloat() * (100 - num1 - num2)
            ringChart.addItem(num1, Color.RED)
            ringChart.addItem(num2, Color.GREEN)
            ringChart.addItem(num3, Color.BLUE)
            ringChart.setGoal(100)
            ringChart.notifyDataSetChanged()
        }
    }
}
