package thomas.park.mirroryoutube

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val fragmentHome = FragmentHome()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_nav.setOnNavigationItemSelectedListener(MenuItemSelectedListener())
        bottom_nav.selectedItemId = R.id.navigation_home

    }


    inner class MenuItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            val transaction = supportFragmentManager.beginTransaction()
            when (item.itemId) {
                R.id.navigation_home -> {

                    transaction.replace(R.id.page_container, fragmentHome)
                        .commitAllowingStateLoss()
                }
            }
            return true
        }
    }
}
