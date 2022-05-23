package com.example.todolist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.core.DateUtil
import com.example.todolist.core.extensions.gone
import com.example.todolist.core.extensions.visible
import com.example.todolist.core.getDate
import com.example.todolist.data.model.Task
import com.example.todolist.databinding.ItemTaskBinding
import com.example.todolist.util.showDoneTaskDialog
import com.zerobranch.layout.SwipeLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TaskListAdapter(
    private val dateUtil: DateUtil,
    private val onItemClicked: (t: Task) -> Unit = {},
    private val onItemDone: (t: Task) -> Unit = {},
    private val onItemRemove: (t: Task) -> Unit = {}
) : ListAdapter<Task, TaskListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<Task>() {
        // Id should be unique
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
) {
    inner class ViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.clDoneView.setOnClickListener {
                binding.swipeLayout.close(true)
                // prevent crash when click on list in loading state
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    binding.root.context.showDoneTaskDialog(item) {
                        CoroutineScope(Main).launch {
                            with(binding.lottieDoneLoad) {
                                visible()
                                playAnimation()
                                delay(duration)
                                gone()
                            }
                            onItemDone(item)
                        }
                    }
                }
            }

            binding.clRemoveView.setOnClickListener {
                // prevent crash when click on list in loading state
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    CoroutineScope(Main).launch {
                        binding.swipeLayout.close(true)
                        onItemRemove(getItem(position))
                    }
                }
            }

            binding.cvMain.setOnClickListener {
                onItemClicked(getItem(adapterPosition))
            }
        }

        fun bind(t: Task) {
            with(binding) {
                clDoneView.isEnabled = false
                clRemoveView.isEnabled = false

                tvRemainTime.text =
                    dateUtil.getRemainTime(getDate(t.deadLine), binding.root.context)
                task = t
                if (t.isDone) {
                    ivIcon.setImageResource(R.drawable.ic_tick)
                    ivIcon.alpha = 1F
                    swipeLayout.isEnabledSwipe = false
                }
                swipeLayout.setOnActionsListener(object : SwipeLayout.SwipeActionsListener {
                    override fun onOpen(direction: Int, isContinuous: Boolean) {
                        if (direction == SwipeLayout.LEFT)
                            clDoneView.isEnabled = true
                        if (direction == SwipeLayout.RIGHT)
                            clRemoveView.isEnabled = true
                    }

                    override fun onClose() {
                        clRemoveView.isEnabled = false
                        clDoneView.isEnabled = false
                    }
                })
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    public override fun getItem(position: Int): Task {
        return currentList[position]
    }
}