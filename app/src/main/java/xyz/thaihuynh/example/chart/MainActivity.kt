package xyz.thaihuynh.example.chart

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import widget.chart.RingChart
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    val mData = ArrayList<Pair<Float, Int>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val random = Random()
        var num1 = random.nextFloat() * 100
        var num2 = random.nextFloat() * (100 - num1)
        var num3 = random.nextFloat() * (100 - num1 - num2)

        mData.add(Pair(num1, Color.RED))
        mData.add(Pair(num2, Color.GREEN))
        mData.add(Pair(num3, Color.BLUE))
        ringChart.setGoal(100)
        ringChart.setData(mData)

        ringChart.setOnRingClickListener(object : RingChart.OnItemClickListener {
            override fun onItemClick(parent: View, position: Int) {
                when (position) {
                    0 -> {
                        num1 = random.nextFloat() * (100 - num2 - num3)
                        mData[0] = Pair(num1, Color.RED)
                        ringChart.setData(mData)
                    }
                    1 -> {
                        num2 = random.nextFloat() * (100 - num1 - num3)
                        mData[1] = Pair(num2, Color.GREEN)
                        ringChart.setData(mData)
                    }
                    2 -> {
                        num3 = random.nextFloat() * (100 - num1 - num2)
                        mData[2] = Pair(num3, Color.BLUE)
                        ringChart.setData(mData)
                    }
                }
            }
        })
    }
}
