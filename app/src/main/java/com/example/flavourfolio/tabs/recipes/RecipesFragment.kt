package com.example.flavourfolio.tabs.recipes

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flavourfolio.FlavourFolioApplication
import com.example.flavourfolio.MainActivity
import com.example.flavourfolio.R
import com.example.flavourfolio.database.Recipe
import com.example.flavourfolio.tabs.addRecipe.AddRecipeDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class RecipesFragment : Fragment() {

    private lateinit var dessertsView: RecyclerView
    private lateinit var dinnerView: RecyclerView
    private lateinit var breakfastView: RecyclerView
    private lateinit var lunchView: RecyclerView
    private lateinit var fabAddRecipe: FloatingActionButton
    private lateinit var spnrSortBy: Spinner
    private lateinit var svSearch: SearchView


    private val viewModel: RecipesViewModel by viewModels {
        RecipesViewModelFactory((requireActivity().application as FlavourFolioApplication).recipeRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)

        fabAddRecipe = view.findViewById(R.id.fabAddRecipe)
        fabAddRecipe.setOnClickListener {
            val dialog = AddRecipeDialog()
            dialog.show(parentFragmentManager, "Add Recipe Dialog")
        }
    }

    private fun enableSwipeToDelete(view: View, recyclerView: RecyclerView) {
        val swipeToDeleteCallback: SwipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                val position = viewHolder.bindingAdapterPosition
                var item: Recipe? = null
                // Determine which recyclerView it is referring to and delete from there
                when (recyclerView.id) {
                    R.id.recipes_desserts_recyclerview -> {
                        item = viewModel.dessertsLiveData.value!![position]
                        viewModel.deleteDessertAt(position)
                    }
                    R.id.recipes_dinner_recyclerview -> {
                        item = viewModel.dinnerLiveData.value!![position]
                        viewModel.deleteDinnerAt(position)
                    }
                    R.id.recipes_breakfast_recyclerview -> {
                        item = viewModel.breakfastLiveData.value!![position]
                        viewModel.deleteBreakfastAt(position)
                    }
                    R.id.recipes_lunch_recyclerview -> {
                        item = viewModel.lunchLiveData.value!![position]
                        viewModel.deleteLunchAt(position)
                    }
                }
                val snackBar = Snackbar.make(
                        view,
                    getString(R.string.recipe_v_remove_message),
                        Snackbar.LENGTH_LONG
                    )
                snackBar.setAction(getString(R.string.recipe_v_undo)) {
                    viewModel.insert(item!!)
                }
                snackBar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.greenish_color))
                snackBar.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.pink))
                snackBar.show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun initializeViews(view: View) {
        dessertsView = view.findViewById(R.id.recipes_desserts_recyclerview)
        dinnerView = view.findViewById(R.id.recipes_dinner_recyclerview)
        breakfastView = view.findViewById(R.id.recipes_breakfast_recyclerview)
        lunchView = view.findViewById(R.id.recipes_lunch_recyclerview)

        dessertsView.layoutManager = LinearLayoutManager(requireContext())
        dinnerView.layoutManager = LinearLayoutManager(requireContext())
        breakfastView.layoutManager = LinearLayoutManager(requireContext())
        lunchView.layoutManager = LinearLayoutManager(requireContext())

        // Attach swipeToDelete on all recyclerViews
        enableSwipeToDelete(view, dessertsView)
        enableSwipeToDelete(view, dinnerView)
        enableSwipeToDelete(view, breakfastView)
        enableSwipeToDelete(view, lunchView)

        fillAdapters()

        initializeSort(view)
        initializeSearch(view)
    }

    private fun initializeSort(view: View) {
        spnrSortBy = view.findViewById(R.id.spnrSortBy)
        val sortAdapter = ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.sort_by_values,
            android.R.layout.simple_spinner_dropdown_item
        )
        spnrSortBy.adapter = sortAdapter
        spnrSortBy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            val items = resources.getStringArray(R.array.sort_by_values)
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (items[position]) {
                    "Alphabetical" -> viewModel.sortAlphabetical()
                    "Date" -> viewModel.sortByDate()
                }
            }
        }
    }

    private fun initializeSearch(view: View) {
        svSearch = view.findViewById(R.id.svSearch)
        svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    viewModel.searchBy(newText)
                } else {
                    when (spnrSortBy.selectedItem) {
                        "Alphabetical" -> viewModel.sortAlphabetical()
                        "Date" -> viewModel.sortByDate()
                    }
                }
                return true
            }
        })
        svSearch.setOnCloseListener {
            when (spnrSortBy.selectedItem) {
                "Alphabetical" -> viewModel.sortAlphabetical()
                "Date" -> viewModel.sortByDate()
            }
            true
        }
    }

    private fun fillAdapters() {
        // Create Adapter
        val dessertsAdapter = CardAdapter()
        val dinnerAdapter = CardAdapter()
        val breakfastAdapter = CardAdapter()
        val lunchAdapter = CardAdapter()
        // Set onClickListener
        dessertsAdapter.onItemClick = this::useRecipe
        dinnerAdapter.onItemClick = this::useRecipe
        breakfastAdapter.onItemClick = this::useRecipe
        lunchAdapter.onItemClick = this::useRecipe
        // Set Adapter
        dessertsView.adapter = dessertsAdapter
        dinnerView.adapter = dinnerAdapter
        breakfastView.adapter = breakfastAdapter
        lunchView.adapter = lunchAdapter
        // Set adapter to the custom card adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dessertsLiveData.observe(viewLifecycleOwner) { desserts ->
                dessertsAdapter.replace(desserts)
            }
            viewModel.dinnerLiveData.observe(viewLifecycleOwner) { dinner ->
                dinnerAdapter.replace(dinner)
            }
            viewModel.breakfastLiveData.observe(viewLifecycleOwner) { breakfast ->
                breakfastAdapter.replace(breakfast)
            }
            viewModel.lunchLiveData.observe(viewLifecycleOwner) { lunch ->
                lunchAdapter.replace(lunch)
            }
        }
    }

    private fun useRecipe(recipe: Recipe) {
        val sharedPref =  activity?.getSharedPreferences(
            "recipe passing",
            Context.MODE_PRIVATE
        ) ?: return
        with (sharedPref.edit()) {
            putInt("recipe_id_key", recipe.rid)
            apply()
        }
        (activity as MainActivity).switchTab(1)
    }

}