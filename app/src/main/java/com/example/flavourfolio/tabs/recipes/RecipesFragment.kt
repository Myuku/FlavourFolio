package com.example.flavourfolio.tabs.recipes

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flavourfolio.R
import com.google.android.material.snackbar.Snackbar

class RecipesFragment : Fragment() {

    companion object {
        fun newInstance() = RecipesFragment()
    }

    private lateinit var viewModel: RecipesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipes, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RecipesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}