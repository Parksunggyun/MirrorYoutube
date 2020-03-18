package thomas.park.mirroryoutube

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var recommendListAdapter: RecommendListAdapter
    private val fragmentHome = FragmentHome()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recommendListAdapter = RecommendListAdapter(this@MainActivity, mainLayout)
        fragmentHome.recommendListAdapter = recommendListAdapter
        recommendListView.apply {
            adapter = recommendListAdapter
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(this@MainActivity)
        }


        bottom_nav.setOnNavigationItemSelectedListener(MenuItemSelectedListener())
        bottom_nav.selectedItemId = R.id.navigation_home

    }


    // BottomNavigationView TouchEvent
    inner class MenuItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            val transaction = supportFragmentManager.beginTransaction()
            when (item.itemId) {
                R.id.navigation_home -> {
                    fragmentHome.mainLayout = mainLayout
                    fragmentHome.videoImage = videoImage
                    fragmentHome.videoTitle = videoTitle
                    fragmentHome.videoUploader = videoUploaderName

                    transaction.replace(R.id.page_container, fragmentHome)
                        .commitAllowingStateLoss()
                }
            }
            return true
        }
    }
}
