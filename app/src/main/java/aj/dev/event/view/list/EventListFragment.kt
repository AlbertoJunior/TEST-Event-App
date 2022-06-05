package aj.dev.event.view.list

import aj.dev.event.data.model.Temperature
import aj.dev.event.databinding.EventListFragmentBinding
import aj.dev.event.vm.EventViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EventListFragment : Fragment() {

    private lateinit var binding: EventListFragmentBinding

    private val viewModel by activityViewModels<EventViewModel>()
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
        val eventItemAdapter = EventItemAdapter(object : OnEventItemClick {
            override fun onEventItemClicked(item: Temperature) {
                print(item.description)
                try {
                    item.id.toLong()
                } catch (e: Exception) {
                    0L
                }.also { id ->
                    if (id == 0L) {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Item inválido")
                            .setMessage("Este item está indisponível no momento")
                            .setPositiveButton(
                                "Voltar"
                            ) { _, _ -> print(item.description) }
                            .show()
                    } else {
                        navController.navigate(
                            EventListFragmentDirections.actionEventListFragmentToEventDetailFragment(
                                id
                            )
                        )
                    }
                }


            }
        })
        binding.rvEvent.adapter = eventItemAdapter

        viewModel.events.observe(this.viewLifecycleOwner) {
            it?.let { events ->
                eventItemAdapter.submitList(events)
            }
        }

        viewModel.fetchEvents()
    }
}