package xyz.thaihuynh.example.chart

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import widget.chart.RingChart
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    val mData = ArrayList<RingChart.Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val random = Random()
        val num1 = random.nextFloat() * 100
        val num2 = random.nextFloat() * (100 - num1)
        val num3 = random.nextFloat() * (100 - num1 - num2)

        mData.add(RingChart.Item(num1, Color.RED))
        mData.add(RingChart.Item(num2, Color.GREEN))
        mData.add(RingChart.Item(num3, Color.BLUE))
        ringChart.setGoal(100)
        ringChart.setData(mData)

        ringChart.setOnRingClickListener(object : RingChart.OnItemClickListener {
            override fun onItemClick(parent: View, position: Int) {
                Log.d("MainActivity", "onItemClick: position=$position")
                if (position < mData.size) {
                    mData[position].value = random.nextFloat() * (100 - (getTotal() - mData[position].value))
                    ringChart.notifyDataSetChanged()
                }
            }
        })

        addItem.setOnClickListener {
            val item = RingChart.Item(random.nextFloat() * (100 - getTotal()), random.nextInt())
            ringChart.addData(item)
        }

        clear.setOnClickListener {
            ringChart.clearData()
        }
    }

    private fun getTotal(): Float {
        return mData.sumByDouble { it.value.toDouble() }.toFloat()
    }
}
