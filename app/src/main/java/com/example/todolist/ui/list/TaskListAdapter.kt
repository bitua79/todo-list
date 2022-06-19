package com.example.todolist.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.data.model.Task
import com.example.todolist.databinding.ItemTaskBinding
import com.example.todolist.util.DateUtils
import com.example.todolist.util.getDate

class TaskListAdapter(
    private val dateUtil: DateUtils,
    private val onItemClicked: (t: Task) -> Unit = {}
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
            binding.cvMain.setOnClickListener {
                onItemClicked(getItem(bindingAdapterPosition))
            }
        }

        fun bind(t: Task) {
            with(binding) {

                tvRemainTime.text =
                    dateUtil.getRemainTime(getDate(t.deadLine), binding.root.context)
                task = t
                if (t.isDone) {
                    ivIcon.setImageResource(R.drawable.ic_tick)
                    ivIcon.alpha = 1F
                }
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