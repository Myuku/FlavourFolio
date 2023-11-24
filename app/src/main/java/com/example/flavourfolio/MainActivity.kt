package com.example.flavourfolio

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.flavourfolio.tabs.fridge.FridgeFragment
import com.example.flavourfolio.tabs.recipes.RecipesFragment
import com.example.flavourfolio.tabs.steps.StepsFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy

class MainActivity : AppCompatActivity() {

    private lateinit var recipesFragment: RecipesFragment
    private lateinit var stepsFragment: StepsFragment
    private lateinit var fridgeFragment: FridgeFragment

    private lateinit var vpViewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var tabConfigurationStrategy: TabConfigurationStrategy
    private lateinit var tabLayoutMediator: TabLayoutMediator

    private lateinit var mainFragmentStateAdapter: MainFragmentStateAdapter
    private lateinit var fragments: ArrayList<Fragment>
    private val tabTitles = arrayOf("Recipes", "Step-By-Step", "Fridge")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

}