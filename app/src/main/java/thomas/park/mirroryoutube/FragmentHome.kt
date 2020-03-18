package thomas.park.mirroryoutube

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FragmentHome: Fragment() {

    companion object {
        private val TAG = FragmentHome::class.java.simpleName
    }
    var itemTouch = false

    lateinit var mainLayout: MotionLayout
    lateinit var videoImage: SurfaceView
    lateinit var recommendListAdapter: RecommendListAdapter
    lateinit var videoTitle: TextView
    lateinit var videoUploader: TextView
    var mediaPlayer : MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val homeRecyclerView = view.findViewById<RecyclerView>(R.id.homeRecyclerView)


        val surfaceHolder = videoImage.holder
        surfaceHolder.addCallback(surfaceViewCallback)

        homeRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
            adapter = FragmentHomeAdapter(context, Videos)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    itemTouch = false
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })

            addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                    Log.e("onTouchEvent", "asdsada")
                }

                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    if (MotionEvent.ACTION_UP == e.action && itemTouch) {
                        Log.e("onIntercept", "trjegwl")
                        val child = rv.findChildViewUnder(e.x, e.y)
                        val pos = rv.getChildAdapterPosition(child!!)
                        if (pos != -1) {
                            Log.e(TAG, "pos = $pos")
                            val uploader =
                                child.findViewById(R.id.videoUploaderTxtView) as AppCompatTextView
                            val txtView =
                                child.findViewById(R.id.videoDescriptionTxtView) as AppCompatTextView
                            recommendListAdapter.updateVideoInfo(
                                VideoInfo(
                                    uploader.text.toString(),
                                    txtView.text.toString(),
                                    txtView.text.toString()
                                )
                            )
                            mediaPlayer = MediaPlayer.create(context, Videos.video[pos - 1])
                            mediaPlayer!!.setDisplay(surfaceHolder)
                            mediaPlayer!!.start()
                            videoTitle.text = Videos.videoNames[pos - 1]
                            videoUploader.text = uploader.text.toString()
                            mainLayout.transitionToState(R.id.expanded)
                        }
                    } else if (MotionEvent.ACTION_DOWN == e.action) {
                        itemTouch = true
                    }
                    return false
                }

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                }
            })
        }


        return view
    }

    private val surfaceViewCallback = object : SurfaceHolder.Callback2 {
        override fun surfaceRedrawNeeded(p0: SurfaceHolder?) {
        }

        override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
        }

        override fun surfaceDestroyed(p0: SurfaceHolder?) {
        }

        override fun surfaceCreated(p0: SurfaceHolder?) {
        }
    }
}