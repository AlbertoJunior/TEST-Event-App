package aj.dev.event.view.list

import aj.dev.event.R
import aj.dev.event.data.model.Temperature
import aj.dev.event.databinding.EventListFragmentBinding
import aj.dev.event.view.list.vm.EventListViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EventListFragment : Fragment() {

    private lateinit var binding: EventListFragmentBinding

    private val viewModel by activityViewModels<EventListViewModel>()
    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EventListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupObserverError()
        setupObserverLoading()
        setupObserverNavigate()
        viewModel.fetchEvents()
    }

    private fun setupObserverLoading() {
        viewModel.loading.observe(viewLifecycleOwner) {
            it?.let {
                binding.pgLoading.isVisible = it
                viewModel.clearLoading()
            }
        }
    }

    private fun setupObserverNavigate() {
        viewModel.navigate.observe(viewLifecycleOwner) {
            it?.let { id ->
                navController.navigate(
                    EventListFragmentDirections.actionEventListFragmentToEventDetailFragment(
                        id
                    )
                )
                viewModel.clearNavigate()
            }
        }
    }

    private fun setupObserverError() {
        viewModel.error.observe(viewLifecycleOwner) {
            it?.let { description ->
                showDialog("Operação Indisponível", description, "Ok")
            }
        }
    }

    private fun setupAdapter() {
        val listener = object : OnEventItemClick {
            override fun onEventItemClicked(item: Temperature) {
                viewModel.onItemClicked(item.id)
            }
        }

        val eventItemAdapter = EventItemAdapter(listener)
        binding.rvEvent.adapter = eventItemAdapter

        viewModel.events.observe(this.viewLifecycleOwner) {
            it?.let { events ->
                eventItemAdapter.submitList(events)
            }
        }
    }

    private fun showDialog(title: String, message: String, positiveButton: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButton) { _, _ -> viewModel.clearError() }
            .show()
    }

    private fun showInvalidItemDialog(item: Temperature) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.ivalid_item_title))
            .setMessage(resources.getString(R.string.ivalid_item_message))
            .setPositiveButton(
                resources.getString(R.string.ivalid_item_button)
            ) { _, _ -> print(item.description) }
            .show()
    }
}