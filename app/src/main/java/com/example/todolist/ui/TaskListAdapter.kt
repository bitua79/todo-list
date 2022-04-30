package com.example.todolist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.core.DateUtil
import com.example.todolist.core.getDate
import com.example.todolist.core.toDateWithSlash
import com.example.todolist.R
import com.example.todolist.data.model.Priority
import com.example.todolist.data.model.Task
import com.example.todolist.databinding.ItemTaskBinding

class TaskListAdapter(
    private val dateUtil: DateUtil,
    private val onItemDone: (t: Task) -> Unit = {},
    private val onItemEdit: (t: Task) -> Unit = {}
) : ListAdapter<Task, TaskListAdapter.PlotViewHolder>(
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
    inner class PlotViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.tvDone.setOnClickListener {
                // prevent crash when click on list in loading state
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION)
                    onItemDone(getItem(position))
            }

            binding.tvEdit.setOnClickListener {
                // prevent crash when click on list in loading state
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION)
                    onItemEdit(getItem(position))
            }
        }

        fun bind(t: Task) {
            with(binding) {
                tvDeadline.text = getDate(t.deadLine).toDateWithSlash()
                tvRemainTime.text =
                    dateUtil.getRemainTime(getDate(t.deadLine), binding.root.context)
                task = t

                val colorId = when (t.priority) {
                    Priority.High -> {
                        R.color.color_priority_high
                    }
                    Priority.Medium -> {
                        R.color.color_priority_medium
                    }
                    Priority.Low -> {
                        R.color.color_priority_low
                    }
                }
                viewDivider.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        colorId
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlotViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlotViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    public override fun getItem(position: Int): Task {
        return currentList[position]
    }
}