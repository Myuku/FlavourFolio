package com.example.flavourfolio.tabs.steps

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flavourfolio.R

class StepsFragment : Fragment() {

    companion object {
        fun newInstance() = StepsFragment()
    }

    private lateinit var viewModel: StepsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_steps, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(StepsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}