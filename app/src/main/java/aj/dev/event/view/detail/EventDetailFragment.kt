package aj.dev.event.view.detail

import aj.dev.event.R
import aj.dev.event.databinding.EventDetailFragmentBinding
import aj.dev.event.view.detail.vm.EventDetailViewModel
import aj.dev.event.view.list.EventListFragmentDirections
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EventDetailFragment : Fragment() {

    private lateinit var binding: EventDetailFragmentBinding
    private val navController by lazy { findNavController() }
    private val viewModel by activityViewModels<EventDetailViewModel>()
    private val args by navArgs<EventDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EventDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserverEvent()
        setupObserverLoad()
        setupObserverNavigate()
        setupObserverError()
        fetchEvent()
    }

    private fun setupObserverError() {
        viewModel.error.observe(viewLifecycleOwner) {
            it?.let { error ->
                showDialog("Erro", error, "OK")
            }
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

    private fun setupObserverNavigate() {
        viewModel.navigate.observe(viewLifecycleOwner) {
            it?.let { id ->
                navController.navigate(
                    EventDetailFragmentDirections.actionEventDetailFragmentToEventCheckinDialog(
                        id
                    )
                )
                viewModel.clearNavigate()
            }
        }
    }

    private fun setupObserverEvent() {
        viewModel.events.observe(viewLifecycleOwner) {
            it?.let { event ->
                setupImage(event.image)
                setupBtCheckIn(event.id)
                binding.temperature = event
            }
        }
    }

    private fun setupBtCheckIn(id: String) {
        binding.btCheckin.isEnabled = true
        binding.btCheckin.setOnClickListener {
            viewModel.callCheckIn(id)
        }
    }

    private fun setupImage(imageUrl: String) {
        binding.pgLoadingImg.isVisible = true

        Glide.with(this)
            .load(imageUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.pgLoadingImg.isVisible = false
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.pgLoadingImg.isVisible = false
                    return false
                }
            })
            .centerCrop()
            .error(R.drawable.img_404)
            .placeholder(R.drawable.ic_image_search)
            .into(binding.ivImage)
    }

    private fun fetchEvent() {
        viewModel.fetchEvent(args.id)
    }

    private fun showDialog(title: String, message: String, positiveButton: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButton) { _, _ -> viewModel.clearError() }
            .show()
    }
}