package aj.dev.event.view.list

import aj.dev.event.databinding.EventItemBinding
import aj.dev.event.view.list.vm.TemperatureListPresenter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class EventItemAdapter(private val listener: OnEventItemClick) :
    ListAdapter<TemperatureListPresenter, EventItemAdapter.ViewHolder>(DiffCallback) {

    private object DiffCallback : DiffUtil.ItemCallback<TemperatureListPresenter>() {
        override fun areItemsTheSame(
            oldItem: TemperatureListPresenter,
            newItem: TemperatureListPresenter
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: TemperatureListPresenter,
            newItem: TemperatureListPresenter
        ) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            EventItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), listener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class ViewHolder(
        private val binding: EventItemBinding,
        private val listener: OnEventItemClick
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TemperatureListPresenter) {
            binding.temperature = item
            binding.root.setOnClickListener { listener.onEventItemClicked(item) }
            binding.btDetails.setOnClickListener { listener.onEventItemClicked(item) }
        }
    }
}