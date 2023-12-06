package com.example.flavourfolio.tabs.steps

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.constraintlayout.widget.Guideline
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.flavourfolio.FlavourFolioApplication
import com.example.flavourfolio.MainActivity
import com.example.flavourfolio.R
import com.example.flavourfolio.enums.StepViewState
import com.example.flavourfolio.service.TimerService
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.floor


class StepsFragment : Fragment() {


    // View elements
    private lateinit var vfViewFlipper: ViewFlipper
    private lateinit var pbProgressBar: ProgressBar
    private lateinit var tvCurrentStep: TextView
    private lateinit var aboveButtons: Guideline
    private lateinit var ivWebImageSmall: ShapeableImageView
    private lateinit var btnNextStep: Button
    private lateinit var btnPrevStep: Button
    // Timer View elements
    private lateinit var tvTimer: TextView
    private lateinit var btnStartTimer: Button
    private lateinit var btnStopTimer: Button
    // Timer Components
    private lateinit var timerReceiver: BroadcastReceiver
    private var timerRunning: Boolean = false
    // Text elements
    private lateinit var inTextView: TextView
    private lateinit var inTextViewLabel: TextView
    private lateinit var forTextView: TextView
    private lateinit var forTextViewLabel: TextView
    private lateinit var untilTextView: TextView
    private lateinit var untilTextViewLabel: TextView
    private lateinit var recipeNameTextView: TextView
    private lateinit var recipeLabelTextView: TextView
    private lateinit var actionLabelTextView: TextView
    private lateinit var actionTextTextView: TextView

    private lateinit var view: View
    private var sharedPref: SharedPreferences? = null

    private val viewModel: StepsViewModel by viewModels {
        StepsViewModel.StepsViewModelFactory(
            (requireActivity().application as FlavourFolioApplication).recipeRepository,
            (requireActivity().application as FlavourFolioApplication).stepRepository,
            (requireActivity().application as FlavourFolioApplication).actionRepository
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_steps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timerReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                updateTimerUI(intent)
            }
        }
        this.view = view
        initializeViews()
        initializeButtons()
        initializeTimer()
    }

    private fun getPassedValue() {
        sharedPref = activity?.getSharedPreferences(
            "recipe passing",
            Context.MODE_PRIVATE
        ) ?: return
        val newRecipeId = sharedPref!!.getInt("recipe_id_key", -1)
        sharedPref!!.edit().clear().apply()

        if (viewModel.recipeId == -1) {
            initiateStartPage()
        }
        if (newRecipeId != -1) {
            // Set all elements visible in case they were not
            btnNextStep.visibility = VISIBLE
            btnPrevStep.visibility = VISIBLE
            tvCurrentStep.visibility = VISIBLE
            pbProgressBar.visibility = VISIBLE
            actionLabelTextView.visibility = VISIBLE
            actionTextTextView.visibility = VISIBLE
            inTextViewLabel.visibility = VISIBLE
            inTextView.visibility = VISIBLE
            forTextViewLabel.visibility = VISIBLE
            forTextView.visibility = VISIBLE
            untilTextViewLabel.visibility = VISIBLE
            untilTextView.visibility = VISIBLE
            recipeNameTextView.visibility = VISIBLE
            recipeLabelTextView.visibility = VISIBLE
            // Update all the values to the new values
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.updateRecipe(newRecipeId).join()
                determineView('n')
                recipeNameTextView.text = viewModel.currRecipe.name
                tvCurrentStep.text = resources.getString(R.string.sbs_lo_step_counter, viewModel.currProgress)
                pbProgressBar.max = viewModel.maxSteps
                pbProgressBar.progress = viewModel.currProgress
                btnPrevStep.alpha = 0.5f
                btnNextStep.alpha = 1.0f
            }
        }
        // if equal, dont do anything
    }

    private fun initializeViews() {
        // Initialize view flipper
        vfViewFlipper = view.findViewById(R.id.vfViewFlipper)
        startView()
        // Initialize progress bar
        pbProgressBar = view.findViewById(R.id.pbProgressBar)
        // Initialize current step
        tvCurrentStep = view.findViewById(R.id.tvStepCounter)
        tvCurrentStep.text = resources.getString(R.string.sbs_lo_step_counter, viewModel.currProgress)
        // Initialize line at which snack-bar is toasted
        aboveButtons = view.findViewById(R.id.lineButtons)
        // Initialize Buttons
        btnNextStep = view.findViewById(R.id.btnNextStep)
        btnPrevStep = view.findViewById(R.id.btnPrevStep)
        // Initialize Text
        inTextViewLabel = view.findViewById(R.id.inTextLabel)
        inTextView = view.findViewById(R.id.inText)
        forTextViewLabel = view.findViewById(R.id.forTextLabel)
        forTextView = view.findViewById(R.id.forText)
        untilTextViewLabel = view.findViewById(R.id.untilTextLabel)
        untilTextView = view.findViewById(R.id.untilText)
        recipeNameTextView = view.findViewById(R.id.recipeNameText)
        recipeLabelTextView = view.findViewById(R.id.recipeNameLabel)
        actionLabelTextView = view.findViewById(R.id.actionTextLabel)
        actionTextTextView = view.findViewById(R.id.actionText)
    }

    private fun initializeTimer() {
        tvTimer = view.findViewById(R.id.tvTimer)

        btnStartTimer = view.findViewById(R.id.btnStartTimer)
        btnStartTimer.setOnClickListener {
            startTimer(viewModel.recipeTimer)
        }

        btnStopTimer = view.findViewById(R.id.btnStopTimer)
        btnStopTimer.setOnClickListener {
            cancelTimer()
        }
    }

    private fun initializeButtons() {

        btnNextStep.setOnClickListener {
            if (viewModel.currProgress == viewModel.maxSteps - 1) {
                btnNextStep.alpha = 0.5f
            }
            if (viewModel.incrementStep() == 1) {
                val snack = Snackbar.make(
                    view, getString(R.string.sbs_v_step_max_alert), MainActivity.LENGTH_VERY_SHORT
                )
                snack.anchorView = aboveButtons
                snack.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.dark_pink))
                snack.show()
            } else {
                lifecycleScope.launch {
                // Switching to new view
                    determineView('r')
                }
                btnPrevStep.alpha = 1.0f
                pbProgressBar.progress = viewModel.currProgress
                tvCurrentStep.text =
                    resources.getString(R.string.sbs_lo_step_counter, viewModel.currProgress)
            }
        }

        btnPrevStep.setOnClickListener {
            if (viewModel.currProgress == 2) {
                btnPrevStep.alpha = 0.5f
            }
            if (viewModel.decrementStep() == 1) {
                val snack = Snackbar.make(
                    view, getString(R.string.sbs_v_step_min_alert), MainActivity.LENGTH_VERY_SHORT
                )
                snack.anchorView = aboveButtons
                snack.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.dark_pink))
                snack.show()
            } else {
                // Switching to new view
                lifecycleScope.launch {
                    determineView('l')
                }
                btnNextStep.alpha = 1.0f
                pbProgressBar.progress = viewModel.currProgress
                tvCurrentStep.text =
                    resources.getString(R.string.sbs_lo_step_counter, viewModel.currProgress)
            }
        }
    }

    private fun initializeImage() {
        //ivWebImageLarge = view.findViewById(R.id.ivWebImageLarge)
        ivWebImageSmall = view.findViewById(R.id.ivWebImageSmall)
        var ivWebImageSmall2 : ShapeableImageView = view.findViewById(R.id.ivWebImageSmall2)

        CoroutineScope(IO).launch {
            val currentStep = viewModel.currSteps[viewModel.currProgress-1]
            val imageString = ImageRetriever.retrieveImageLink(
                "${currentStep.action}ing ${currentStep.food}")
            val url = URL(imageString)
            val img = BitmapFactory.decodeStream(withContext(IO) {
                url.openConnection().getInputStream()
            })
            withContext(Main) {
                // Scale up image to fit properly
                val currentBitmapWidth = img.width
                val currentBitmapHeight = img.height
                val ivWidth = 155

                val newHeight =
                    floor(currentBitmapHeight.toDouble() *
                            (ivWidth.toDouble() / currentBitmapWidth.toDouble())).toInt()

                val scaledImg = Bitmap.createScaledBitmap(img, ivWidth, newHeight, true)
                //ivWebImageLarge.setImageBitmap(scaledImg)
                ivWebImageSmall.setImageBitmap(scaledImg)
                ivWebImageSmall2.setImageBitmap(scaledImg)
            }
        }
    }

    private fun initializeText() {

        actionLabelTextView.text = viewModel.currSteps[viewModel.currProgress-1].action
        actionTextTextView.text = viewModel.currSteps[viewModel.currProgress-1].food

        if (viewModel.actionIn?.detail == null) {
            inTextViewLabel.visibility = GONE
            inTextView.visibility = GONE
        } else {
            inTextViewLabel.visibility = VISIBLE
            inTextView.visibility = VISIBLE
            inTextView.text = viewModel.actionIn?.detail
        }

        if (viewModel.actionFor?.detail == null) {
            forTextViewLabel.visibility = GONE
            forTextView.visibility = GONE
        } else {
            forTextViewLabel.visibility = VISIBLE
            forTextView.visibility = VISIBLE
            forTextView.text = viewModel.actionFor?.detail
        }

        if (viewModel.actionUntil?.detail == null) {
            untilTextViewLabel.visibility = GONE
            untilTextView.visibility = GONE
        } else {
            untilTextViewLabel.visibility = VISIBLE
            untilTextView.visibility = VISIBLE
            untilTextView.text = viewModel.actionUntil?.detail
        }
    }


    // ALL VIEW FLIPPER STUFF
    @SuppressLint("NewApi")
    private suspend fun determineView(direction: Char) {
        // if we have reached the max step, show the done page
        if (viewModel.currProgress == viewModel.maxSteps) {
            showView(StepViewState.DONE, direction)
            return
        }

        // if we have not reached the max step, first get the step we requested
        val currentStep = viewModel.currSteps[viewModel.currProgress-1]
        viewModel.retrieveActions(currentStep.sid) // then get what activity is on this step
        Log.d("action", viewModel.actionFor.toString())
        if (viewModel.actionFor != null) { // if the activity isn't empty
            // With Timer
            val localTime = LocalTime.parse(
                viewModel.actionFor!!.detail,
                DateTimeFormatter.ofPattern("HH-mm-ss")
            )
            viewModel.recipeTimer = (localTime.toSecondOfDay() * 1000).toLong()

            initializeImage()
            initializeText()
            showView(StepViewState.TIMER, direction)
        } else {
            // Without Timer
            initializeImage()
            initializeText()
            showView(StepViewState.PICTURE, direction)
        }
        //setActionViews()
    }

    private fun setActionViews() {
        TODO("Not yet implemented")
    }

    private fun showView(type: StepViewState, direction: Char) {
        when (direction) {
            'r' -> {
                //vfViewFlipper.setInAnimation(requireContext(), R.anim.slide_in_right)
                //vfViewFlipper.setOutAnimation(requireContext(), R.anim.slide_out_left)
            }
            'l' -> {
                //vfViewFlipper.setInAnimation(requireContext(), R.anim.slide_in_left)
                //vfViewFlipper.setOutAnimation(requireContext(), R.anim.slide_out_right)
            }
            else -> {
                vfViewFlipper.inAnimation = null
                vfViewFlipper.outAnimation = null
            }
        }

        when (type) {
            StepViewState.PICTURE -> vfViewFlipper.displayedChild = StepViewState.PICTURE.idx
            StepViewState.TIMER -> vfViewFlipper.displayedChild = StepViewState.TIMER.idx
            StepViewState.START -> vfViewFlipper.displayedChild = StepViewState.START.idx
            StepViewState.DONE -> vfViewFlipper.displayedChild = StepViewState.DONE.idx
        }
    }

    private fun startView() {
        vfViewFlipper.flipInterval = 0
        vfViewFlipper.inAnimation = null
        vfViewFlipper.outAnimation = null
        vfViewFlipper.displayedChild = StepViewState.START.idx
    }

    private fun initiateStartPage() {
        btnNextStep.visibility = INVISIBLE
        btnPrevStep.visibility = INVISIBLE
        tvCurrentStep.visibility = INVISIBLE
        pbProgressBar.visibility = INVISIBLE
        startView()
    }


    // ALL TIMER SERVICE STUFF
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireActivity().registerReceiver(timerReceiver, IntentFilter(TimerService.COUNTDOWN_BR), Context.RECEIVER_NOT_EXPORTED)
        } else {
            requireActivity().registerReceiver(timerReceiver, IntentFilter(TimerService.COUNTDOWN_BR))
        }
        getPassedValue()
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(timerReceiver)
    }

    private fun startTimer(duration: Long) {
        // Prompt an error message if they try to start a timer when there already is one
        if (timerRunning) {
            val snack = Snackbar.make(
                requireView(),
                getString(R.string.sbs_v_timer_running_alert), MainActivity.LENGTH_VERY_SHORT
            )
            snack.anchorView = aboveButtons
            snack.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.dark_pink))
            snack.show()
            return
        }
        // Reset colors back to normal
        tvTimer.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_pink))
        tvTimer.alpha = 1.0f
        btnStopTimer.isClickable = true
        btnStopTimer.alpha = 1.0f
        // Start service
        val intent = Intent(requireContext(), TimerService::class.java)
        intent.putExtra(TimerService.DURATION_KEY, duration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireActivity().startForegroundService(intent)
        } else {
            requireContext().startService(intent)
        }
    }

    private fun cancelTimer() {
        val intent = Intent(requireContext(), TimerService::class.java)
        requireContext().stopService(intent)
        // Gray out button and text when pressed
        tvTimer.setTextColor(Color.GRAY)
        tvTimer.alpha = 0.5f
        btnStopTimer.alpha = 0.5f
        btnStopTimer.isClickable = false
    }

    private fun updateTimerUI(intent: Intent) {
        if (intent.extras != null) {
            val millisUntilFinished = intent.getLongExtra(TimerService.TIME_KEY, 0)
            val seconds = millisUntilFinished / 1000 % 60
            val minutes = millisUntilFinished / (1000 * 60) % 60
            val hours = millisUntilFinished / (1000 * 60 * 60) % 60

            tvTimer.text = String.format("%02d : %02d : %02d", hours, minutes, seconds)

            timerRunning = intent.getBooleanExtra(TimerService.RUNNING_KEY, false)
            val timerFinished = intent.getBooleanExtra(TimerService.FINISHED_KEY, false)
            if (timerFinished) {
                tvTimer.text = getString(R.string.sbs_v_timer_completed_status)
            }
        }
    }
}