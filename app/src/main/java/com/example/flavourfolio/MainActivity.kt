package com.example.flavourfolio

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.flavourfolio.tabs.fridge.FridgeFragment
import com.example.flavourfolio.tabs.recipes.RecipesFragment
import com.example.flavourfolio.tabs.steps.StepsFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy

class MainActivity : AppCompatActivity() {

    companion object {
        const val LENGTH_VERY_SHORT = 1000
    }

    private lateinit var recipesFragment: RecipesFragment
    private lateinit var stepsFragment: StepsFragment
    private lateinit var fridgeFragment: FridgeFragment

    private lateinit var vpViewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var tabConfigurationStrategy: TabConfigurationStrategy
    private lateinit var tabLayoutMediator: TabLayoutMediator

    private lateinit var mainFragmentStateAdapter: MainFragmentStateAdapter
    private lateinit var fragments: ArrayList<Fragment>
    private lateinit var tabTitles: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            supportActionBar?.hide()
        }
        requestPermissions()

        tabTitles = arrayOf(getString(R.string.tab_recipes),
        getString(R.string.tab_step_by_step), getString(R.string.tab_fridge))
        vpViewPager = findViewById(R.id.vpViewPager)
        tabLayout = findViewById(R.id.tabTab)

        initializeFragments()
        setupTabs()
    }

    private fun setupTabs() {
        mainFragmentStateAdapter = MainFragmentStateAdapter(this, fragments)
        vpViewPager.adapter = mainFragmentStateAdapter

        tabConfigurationStrategy = TabConfigurationStrategy { tab: TabLayout.Tab, pos: Int ->
            tab.text = tabTitles[pos]
        }
        tabLayoutMediator = TabLayoutMediator(tabLayout, vpViewPager, tabConfigurationStrategy)
        tabLayoutMediator.attach()
    }

    private fun initializeFragments() {
        recipesFragment = RecipesFragment()
        stepsFragment = StepsFragment()
        fridgeFragment = FridgeFragment()

        fragments = ArrayList()
        fragments.add(recipesFragment)
        fragments.add(stepsFragment)
        fragments.add(fridgeFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    fun switchTab(pos: Int) {
        tabLayout.getTabAt(pos)?.select()
    }


    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE)
            != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.FOREGROUND_SERVICE,
                        Manifest.permission.POST_NOTIFICATIONS,
                        Manifest.permission.INTERNET),
                    0
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.INTERNET),
                    0
                )
            }
        }
    }
}