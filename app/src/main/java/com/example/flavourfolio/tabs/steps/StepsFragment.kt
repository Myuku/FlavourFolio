package com.example.flavourfolio.tabs.steps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flavourfolio.MainActivity
import com.example.flavourfolio.R
import com.google.android.material.snackbar.Snackbar


class StepsFragment : Fragment() {

    // Enum to make it easier to know what state is being used
    enum class ViewState(val idx: Int) {
        PICTURE(0),
        TIMER(1),
        START(2),
        DONE(3),
    }
    // View Model
    private lateinit var viewModel: StepsViewModel
    // View elements
    private lateinit var vfViewFlipper: ViewFlipper
    private lateinit var tvCurrentStep: TextView
    private lateinit var pbProgressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[StepsViewModel::class.java]
        return inflater.inflate(R.layout.fragment_steps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        initializeButtons(view)
    }

    private fun initializeViews(view: View) {
        // Initialize view flipper
        vfViewFlipper = view.findViewById(R.id.vfViewFlipper)
        vfViewFlipper.displayedChild = ViewState.START.ordinal

        // Initialize progress bar
        pbProgressBar = view.findViewById(R.id.pbProgressBar)
        pbProgressBar.max = viewModel.maxSteps
        pbProgressBar.progress = viewModel.currProgress

        // Initialize current step
        tvCurrentStep = view.findViewById(R.id.tvStepCounter)
        tvCurrentStep.text = resources.getString(R.string.sbs_lo_step_counter, viewModel.currProgress)
    }

    private fun initializeButtons(view: View) {
        val btnNextStep: Button = view.findViewById(R.id.btnNextStep)
        btnNextStep.setOnClickListener {
            if (viewModel.incrementStep() == 1) {
                val snack = Snackbar.make(
                    view, getString(R.string.sbs_v_step_max_alert), MainActivity.LENGTH_VERY_SHORT
                )
                snack.anchorView = view.findViewById(R.id.lineButtons)
                snack.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.dark_pink))
                snack.show()
            } else {
                nextView()
                pbProgressBar.progress = viewModel.currProgress
                tvCurrentStep.text =
                    resources.getString(R.string.sbs_lo_step_counter, viewModel.currProgress)
            }
        }

        val btnPrevStep: Button = view.findViewById(R.id.btnPrevStep)
        btnPrevStep.setOnClickListener {
            if (viewModel.decrementStep() == 1) {
                val snack = Snackbar.make(
                    view, getString(R.string.sbs_v_step_min_alert), MainActivity.LENGTH_VERY_SHORT
                )
                snack.anchorView = view.findViewById(R.id.lineButtons)
                snack.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.dark_pink))
                snack.show()
            } else {
                prevView()
                pbProgressBar.progress = viewModel.currProgress
                tvCurrentStep.text =
                    resources.getString(R.string.sbs_lo_step_counter, viewModel.currProgress)
            }
        }
    }


    // TODO: these two are only for testing, will only use showView()
    private fun nextView() {
        vfViewFlipper.setInAnimation(requireContext(), R.anim.slide_in_right)
        vfViewFlipper.setOutAnimation(requireContext(), R.anim.slide_out_left)
        vfViewFlipper.showNext()
    }
    private fun prevView() {
        vfViewFlipper.setInAnimation(requireContext(), R.anim.slide_in_left)
        vfViewFlipper.setOutAnimation(requireContext(), R.anim.slide_out_right)
        vfViewFlipper.showPrevious()
    }
    // Everything in here will be put into nextView and prevView with (idx: Int)
    fun showView(type: ViewState) {
        when (type) {
            ViewState.PICTURE -> vfViewFlipper.displayedChild = ViewState.PICTURE.idx
            ViewState.TIMER -> vfViewFlipper.displayedChild = ViewState.TIMER.idx
            ViewState.START -> vfViewFlipper.displayedChild = ViewState.START.idx
            ViewState.DONE -> vfViewFlipper.displayedChild = ViewState.DONE.idx
        }
    }

}