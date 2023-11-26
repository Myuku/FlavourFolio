package com.example.flavourfolio.tabs.steps

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.constraintlayout.widget.Guideline
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flavourfolio.MainActivity
import com.example.flavourfolio.R
import com.example.flavourfolio.service.TimerService
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import kotlin.math.floor


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
    private lateinit var pbProgressBar: ProgressBar
    private lateinit var tvCurrentStep: TextView
    private lateinit var aboveButtons: Guideline
    private lateinit var ivWebImage: ShapeableImageView
    // Timer View elements
    private lateinit var tvTimer: TextView
    private lateinit var btnStartTimer: Button
    private lateinit var btnStopTimer: Button
    // Timer Components
    private lateinit var timerReceiver: BroadcastReceiver
    private var timerRunning: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[StepsViewModel::class.java]
        return inflater.inflate(R.layout.fragment_steps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timerReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                updateTimerUI(intent)
            }
        }
        initializeViews(view)
        initializeButtons(view)
        initializeTimer(view)
        initializeImage(view)
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireActivity().registerReceiver(timerReceiver, IntentFilter(TimerService.COUNTDOWN_BR), Context.RECEIVER_NOT_EXPORTED)
        } else {
            requireActivity().registerReceiver(timerReceiver, IntentFilter(TimerService.COUNTDOWN_BR))
        }
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(timerReceiver)
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

        // Initialize line at which snack-bar is toasted
        aboveButtons = view.findViewById(R.id.lineButtons)
    }

    private fun initializeTimer(view: View) {
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

    private fun initializeButtons(view: View) {
        val btnNextStep: Button = view.findViewById(R.id.btnNextStep)
        btnNextStep.setOnClickListener {
            if (viewModel.incrementStep() == 1) {
                val snack = Snackbar.make(
                    view, getString(R.string.sbs_v_step_max_alert), MainActivity.LENGTH_VERY_SHORT
                )
                snack.anchorView = aboveButtons
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
                snack.anchorView = aboveButtons
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

    private fun initializeImage(view: View) {
        ivWebImage = view.findViewById(R.id.ivWebImage)

        CoroutineScope(IO).launch {
            val imageString = ImageRetriever.retrieveImageLink("${viewModel.action}ing ${viewModel.subject}")
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

                ivWebImage.setImageBitmap(scaledImg)
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
    private fun showView(type: ViewState) {
        when (type) {
            ViewState.PICTURE -> vfViewFlipper.displayedChild = ViewState.PICTURE.idx
            ViewState.TIMER -> vfViewFlipper.displayedChild = ViewState.TIMER.idx
            ViewState.START -> vfViewFlipper.displayedChild = ViewState.START.idx
            ViewState.DONE -> vfViewFlipper.displayedChild = ViewState.DONE.idx
        }
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