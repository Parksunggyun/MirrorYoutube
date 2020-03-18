package thomas.park.mirroryoutube

import android.content.Context
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.Transformation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class RecommendListAdapter (private val context: Context, private val mainLayout: MotionLayout) :
RecyclerView.Adapter<RecommendListAdapter.YouTubeDemoViewHolder>() {

    companion object {
        val TAG = RecommendListAdapter::class.java.simpleName
        var reverse = false
        var height = 0
        var uploaderName = ""
        var title = ""
        var description = ""
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YouTubeDemoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(viewType, parent, false)

        return when (viewType) {
            R.layout.item_header -> YouTubeDemoViewHolder.TextHeaderViewHolder(
                itemView
            )
            R.layout.item_description -> YouTubeDemoViewHolder.TextDescriptionViewHolder(
                itemView
            )
            R.layout.item_row -> YouTubeDemoViewHolder.FishRowViewHolder(
                itemView
            )
            else -> throw IllegalStateException("Unknown viewType $viewType")
        }
    }

    fun updateVideoInfo(videoInfo: VideoInfo) {
        uploaderName = videoInfo.videoUploader
        title = videoInfo.videoTitle
        description = videoInfo.videoDescription
        notifyItemChanged(0)
    }

    override fun onBindViewHolder(holder: YouTubeDemoViewHolder, position: Int) {
        when (holder) {
            is YouTubeDemoViewHolder.TextHeaderViewHolder -> {
                Log.e("TextHeaderViewholder", "on bind view holder")
                holder.contentDescriptionTextView.viewTreeObserver.addOnGlobalLayoutListener(
                    object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            holder.contentDescriptionTextView.viewTreeObserver.removeOnGlobalLayoutListener(
                                this
                            )
                            height = holder.contentDescriptionTextView.height
                            height = holder.contentDescriptionTextView.measuredHeight
                            Log.e("TextHeaderViewholder", "$height")
                            holder.contentDescriptionTextView.visibility = View.GONE
                        }
                    })

                holder.textDescriptionTextView.apply {
                    text = title
                }
                holder.descriptionTextView.apply {
                    text = description
                }
                holder.uploaderName.apply {
                    text = uploaderName
                }

                holder.uploaderThumbnail.apply {
                    background = ShapeDrawable(OvalShape())
                    clipToOutline = true
                }

                holder.layoutDescriptionConstraintLayout.setOnClickListener {
                    reverse = !reverse
                    val animation: Animation
                    if (!reverse) {
                        animation =
                            AnimationUtils.loadAnimation(context, R.anim.rotation_arrow_reverse)
                        collapse(holder.contentDescriptionTextView)
                    } else {
                        animation = AnimationUtils.loadAnimation(context, R.anim.rotation_arrow)
                        expand(holder.contentDescriptionTextView)
                    }
                    holder.imageDescriptionImageView.animation = animation
                    holder.imageDescriptionImageView.startAnimation(animation)
                }


                holder.headerNav.apply {
                    setOnNavigationItemSelectedListener(onClick)
                    Log.e(TAG, "headerNav apply")
                    val thumbUp = menu.findItem(R.id.header_thumb_up)
                    val thumbDown = menu.findItem(R.id.header_thumb_down)
/*                    val share = menu.findItem(R.id.header_share)
                    val fileDownload = menu.findItem(R.id.header_file_download)
                    val fileDownload2 = menu.findItem(R.id.header_file_download2)*/


                    thumbUp.title = context.getString(R.string.text_header_thumb_up, "15")
                    thumbDown.title = context.getString(R.string.text_header_thumb_down, "15")

                }

            }
            is YouTubeDemoViewHolder.TextDescriptionViewHolder -> {
            }
            is YouTubeDemoViewHolder.FishRowViewHolder -> {
                val imagePosition = position - 2
                holder.textView.text =
                    holder.textView.resources.getString(R.string.fish_n, imagePosition)
                Glide.with(context)
                    .load(Videos.thumbnail[imagePosition])
                    .into(holder.imageView)
            }
        }
    }

    private fun expand(view: View) {
        val targetHeight = height

        view.layoutParams.height = 1
        view.visibility = View.VISIBLE
        val animation = object : Animation() {

            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                view.layoutParams.height =
                    if (interpolatedTime == 1.0f) ConstraintLayout.LayoutParams.WRAP_CONTENT
                    else ((targetHeight * interpolatedTime)).toInt()
                view.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        animation.duration = 250
        view.animation = animation
        view.startAnimation(animation)
    }

    private fun collapse(view: View) {
        val initialHeight = view.measuredHeight

        val animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                if (interpolatedTime.toInt() == 1) view.visibility = View.GONE
                else {
                    view.layoutParams.height =
                        initialHeight - (initialHeight * interpolatedTime).toInt()
                    view.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        height = initialHeight
        animation.duration = 250
        view.animation = animation
        view.startAnimation(animation)

    }

    private val onClick = object : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.header_thumb_up -> {
                    Snackbar.make(mainLayout, "좋아요는 추후에 추가할 예정입니다", Snackbar.LENGTH_SHORT).show()
                    return true
                }
                R.id.header_thumb_down -> {
                    Snackbar.make(mainLayout, "싫어요는 추후에 추가할 예정입니다", Snackbar.LENGTH_SHORT).show()
                    return true
                }
                R.id.header_share -> {
                    Snackbar.make(mainLayout, "공유하기 기능은 추후에 추가할 예정입니다", Snackbar.LENGTH_SHORT)
                        .show()

                    return true
                }
                R.id.header_file_download -> {
                    Snackbar.make(mainLayout, "갤러리에 저장 기능은 추후에 추가할 예정입니다", Snackbar.LENGTH_SHORT)
                        .show()
                    return true
                }
            }
            return false
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> R.layout.item_header
            1 -> R.layout.item_description
            else -> R.layout.item_row
        }
    }

    override fun getItemCount() = Videos.video.size + 2

    sealed class YouTubeDemoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        class TextHeaderViewHolder(
            itemView: View
        ) : YouTubeDemoViewHolder(itemView) {

            val layoutDescriptionConstraintLayout =
                itemView.findViewById(R.id.layout_description) as ConstraintLayout

            val textDescriptionTextView =
                itemView.findViewById(R.id.text_description) as AppCompatTextView

            val imageDescriptionImageView =
                itemView.findViewById(R.id.image_description) as AppCompatImageView

            val headerNav = itemView.findViewById(R.id.header_nav) as BottomNavigationView

            val contentDescriptionTextView =
                itemView.findViewById(R.id.content_description) as ConstraintLayout

            val descriptionTextView =
                itemView.findViewById(R.id.content_description_textview) as AppCompatTextView

            val uploaderThumbnail =
                itemView.findViewById(R.id.uploaderThumbnail) as AppCompatImageView

            val uploaderName =
                itemView.findViewById(R.id.uploaderName) as AppCompatTextView
        }

        class TextDescriptionViewHolder(
            itemView: View
        ) : YouTubeDemoViewHolder(itemView)

        class FishRowViewHolder(
            itemView: View
        ) : YouTubeDemoViewHolder(itemView) {
            val imageView = itemView.findViewById(R.id.image_row) as ImageView
            val textView = itemView.findViewById(R.id.text_row) as TextView
        }

    }
}