package aj.dev.event.view.list

import aj.dev.event.R
import aj.dev.event.databinding.EventListFragmentBinding
import aj.dev.event.view.DialogUtils
import aj.dev.event.view.list.vm.EventListViewModel
import aj.dev.event.view.list.vm.TemperatureListPresenter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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

                binding.tvMessage.isVisible = true
                binding.tvMessage.text = description

                DialogUtils.showDialog(
                    requireContext(),
                    getString(R.string.operate_unavailable),
                    description,
                    getString(R.string.ok)
                ) { _, _ ->
                    viewModel.clearError()
                }
            }
        }
    }

    private fun setupAdapter() {
        val listener = object : OnEventItemClick {
            override fun onEventItemClicked(item: TemperatureListPresenter) {
                viewModel.onItemClicked(item.id)
            }
        }

        val eventItemAdapter = EventItemAdapter(listener)
        binding.rvEvent.adapter = eventItemAdapter

        viewModel.events.observe(this.viewLifecycleOwner) {
            it?.let { events ->
                binding.tvMessage.isVisible = it.isEmpty()
                eventItemAdapter.submitList(events)
            }
        }
    }
}