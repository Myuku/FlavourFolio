package com.example.flavourfolio.tabs.steps

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.flavourfolio.R

class StepsFragment : Fragment() {

    private lateinit var viewModel: StepsViewModel
    private lateinit var tvCounter: TextView
    private lateinit var button: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[StepsViewModel::class.java]
        return inflater.inflate(R.layout.fragment_steps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvCounter = view.findViewById(R.id.counter)
        tvCounter.text = viewModel.counter.toString()

        button = view.findViewById(R.id.button)
        button.setOnClickListener {
            viewModel.incrementCounter()
            tvCounter.text = viewModel.counter.toString()
        }
    }
}