package aj.dev.event.view.checkin

import aj.dev.event.R
import aj.dev.event.databinding.EventCheckinDialogBinding
import aj.dev.event.view.checkin.vm.EventCheckinViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckinFormDialog : DialogFragment() {
    private lateinit var binding: EventCheckinDialogBinding
    private val viewModel by activityViewModels<EventCheckinViewModel>()
    private val args by navArgs<CheckinFormDialogArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EventCheckinDialogBinding.inflate(inflater, container, false)
        this.dialog?.window?.setBackgroundDrawableResource(R.drawable.bg_rounded_stroke)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBtClose()
        setupBtCheckIn()
        setupObserverLoad()
        setupObserverErrors()
        setupObserverCheckIn()
    }

    private fun setupObserverCheckIn() {
        viewModel.checkInResult.observe(viewLifecycleOwner) {
            it?.let {
                binding.btCheckin.isVisible = !it
                binding.llForm.isVisible = !it
                binding.tvCheckinMessage.isVisible = !it
                viewModel.clearCheckInResult()
            }
        }
    }

    private fun setupObserverErrors() {
        viewModel.errorService.observe(viewLifecycleOwner) {

        }

        viewModel.errorEmail.observe(viewLifecycleOwner) {
            it?.let {
                binding.etEmail.error = it
                viewModel.clearErrorEmail()
            }
        }

        viewModel.errorName.observe(viewLifecycleOwner) {
            it?.let {
                binding.etName.error = it
                viewModel.clearErrorName()
            }
        }
    }

    private fun setupBtClose() {
        binding.ibClose.setOnClickListener { dismiss() }
    }

    private fun setupBtCheckIn() {
        binding.btCheckin.setOnClickListener {
            viewModel.checkIn(
                args.id,
                binding.etName.text.toString(),
                binding.etEmail.text.toString()
            )
        }
    }

    private fun setupObserverLoad() {
        viewModel.loading.observe(viewLifecycleOwner) {
            it?.let {
                binding.pgLoading.isVisible = it
                viewModel.clearLoading()
            }
        }
    }
}