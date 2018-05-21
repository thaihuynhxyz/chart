package widget.chart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import java.util.Random
import kotlin.collections.ArrayList

class RingChart : View {

    private var mData = ArrayList<Item>()

    private var mTotal = 0f
    private var mGoal: Int = 0
    private var mColor: Int = 0
    private var mThickness: Float = 0f

    private var mPiePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var mClearPaint: Paint

    private var mDetector: GestureDetector? = null
    private var mListener: OnItemClickListener? = null

    private var mBounds: RectF? = null

    /**
     * Class constructor taking only a context. Use this constructor to create
     * [RingChart] objects from your own code.
     *
     * @param context context
     */
    constructor(context: Context) : super(context) {
        init()
    }

    /**
     * Class constructor taking a context and an attribute set. This constructor
     * is used by the layout engine to construct a [RingChart] from a set of
     * XML attributes.
     *
     * @param context context
     * @param attrs   An attribute set which can contain attributes from
     * StatsChart as well as attributes inherited
     * from [android.view.View].
     */
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        // attrs contains the raw values for the XML attributes
        // that were specified in the layout, which don't include
        // attributes set by styles or themes, and which may have
        // unresolved references. Call obtainStyledAttributes()
        // to get the final values for each attribute.
        //
        // This call uses R.styleable.PieChart, which is an array of
        // the custom attributes that were declared in attrs.xml.
        val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.RingChart,
                0, 0
        )

        try {
            // Retrieve the values from the TypedArray and store into
            // fields of this class.
            //
            // The R.styleable.StatsChart_* constants represent the index for
            // each custom attribute in the R.styleable.StatsChart array.
            mGoal = a.getInteger(R.styleable.RingChart_goal, 0)
            mColor = a.getColor(R.styleable.RingChart_color, 0)
            mThickness = a.getDimension(R.styleable.RingChart_thickness, (context.resources.displayMetrics.density * 8))
        } finally {
            // release the TypedArray so that it can be reused.
            a.recycle()
        }

        init()
    }

    /**
     * Add a new data item to this view. Adding an item adds a slice to the pie whose
     * size is proportional to the item's value. As new items are added, the size of each
     * existing slice is recalculated so that the proportions remain correct.
     *
     * @param item The ring slice item
     */
    fun addData(item: Item) {
        mData.add(item)
        notifyDataSetChanged()
    }

    fun setData(data: ArrayList<Item>) {
        mData = data
        notifyDataSetChanged()
    }

    fun clearData() {
        mData.clear()
        notifyDataSetChanged()
    }

    fun setOnRingClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Let the GestureDetector interpret this event
        return mDetector!!.onTouchEvent(event)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val right = w.toFloat()
        val bottom = h.toFloat()
        if (mBounds == null) {
            mBounds = RectF(0f, 0f, right, bottom)
        } else {
            mBounds!!.right = right
            mBounds!!.bottom = bottom
        }

        notifyDataSetChanged()
    }

    /**
     * Do all of the recalculations needed when the data array changes.
     */
    fun notifyDataSetChanged() {
        // When the data changes, we have to recalculate
        // all of the angles.
        var currentAngle = -90f
        mTotal = mData.sumByDouble { it.value.toDouble() }.toFloat()
        val total = Math.max(mTotal, mGoal.toFloat())
        for (it in mData) {
            it.startAngle = currentAngle
            it.endAngle = if (total == 0f) currentAngle else currentAngle + it.value / total * 360.0f
            if (it.endAngle > 270) it.endAngle = 270f
            currentAngle = it.endAngle
        }
        invalidate()
    }

    /**
     * Initialize the control. This code is in a separate method so that it can be
     * called from both constructors.
     */
    private fun init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        // Set up the paint for the pie slices
        mPiePaint.style = Paint.Style.FILL
        mClearPaint = Paint(mPiePaint)
        mClearPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

        // Create a gesture detector to handle onTouch messages
        mDetector = GestureDetector(context, GestureListener())

        // In edit mode it's nice to have some demo data, so add that here.
        if (this.isInEditMode) {
            val random = Random()
            val num1 = random.nextFloat() * 100
            val num2 = random.nextFloat() * (100 - num1)
            val num3 = random.nextFloat() * (100 - num1 - num2)
            addData(Item(num1, Color.RED))
            addData(Item(num2, Color.GREEN))
            addData(Item(num3, Color.BLUE))
            if (mGoal == 0) mGoal = 100
        }
    }

    fun setGoal(goal: Int) {
        mGoal = goal
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (it in mData) {
            mPiePaint.color = it.color
            if (it.endAngle > it.startAngle) {
                canvas.drawArc(mBounds!!,
                        it.startAngle,
                        it.endAngle - it.startAngle,
                        true, mPiePaint)
            }
        }

        if (mTotal == 0f || mTotal < mGoal) {
            mPiePaint.color = mColor
            val endAngle: Float = if (mData.isEmpty() || mTotal == 0f) -90f else mData[mData.size - 1].endAngle
            if (270f > endAngle) {
                canvas.drawArc(mBounds!!, endAngle, 270f - endAngle, true, mPiePaint)
            }
        }

        canvas.drawCircle(mBounds!!.centerX(), mBounds!!.centerY(), mBounds!!.width() / 2 - mThickness, mClearPaint)
    }

    /**
     * Maintains the state for a data item.
     */
    class Item(var value: Float, var color: Int) {

        // computed values
        internal var startAngle: Float = 0f
        internal var endAngle: Float = 0f
    }

    interface OnItemClickListener {
        fun onItemClick(parent: View, position: Int)
    }

    /**
     * Extends [GestureDetector.SimpleOnGestureListener] to provide custom gesture
     * processing.
     */
    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            return mListener != null
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            if (mListener != null) {
                val r = (measuredWidth / 2).toFloat()
                val x = e.x - r
                val y = e.y - r

                val distance = x * x + y * y
                if (distance > (r - 2 * mThickness) * (r - 2 * mThickness) && distance < (r + mThickness) * (r + mThickness)) {
                    var degrees = Math.toDegrees(Math.atan((y / x).toDouble()))
                    if (x < 0) {
                        degrees += 180
                    }
                    for (item in mData) {
                        if (degrees >= item.startAngle && degrees <= item.endAngle) {
                            mListener!!.onItemClick(this@RingChart, mData.indexOf(item))
                            return super.onSingleTapUp(e)
                        }
                    }
                    mListener!!.onItemClick(this@RingChart, mData.size)
                }
            }
            return super.onSingleTapUp(e)
        }
    }
}
