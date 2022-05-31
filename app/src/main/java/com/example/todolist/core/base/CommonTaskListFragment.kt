package com.example.todolist.core.base

import android.graphics.Canvas
import android.os.CountDownTimer
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_PX
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.data.model.Priority
import com.example.todolist.data.model.Task
import com.example.todolist.data.model.TaskType
import com.example.todolist.databinding.FragmentTaskListBinding
import com.example.todolist.ui.MainActivity
import com.example.todolist.util.getColor
import com.example.todolist.util.getListByType
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.util.*


@AndroidEntryPoint
abstract class CommonTaskListFragment(
    private val priority: Priority?,
    private val type: TaskType
) : BaseTaskListFragment<FragmentTaskListBinding>(R.layout.fragment_task_list, type) {

    override fun onInitDataBinding() {
        setupRecyclerview()
        setFabAction()
        observeData()
    }

    override fun initRecyclerView() {
        recyclerView = viewBinding.rvTasks

        // create callback and attach it to recyclerview
        val itemTouchHelper = ItemTouchHelper(createTouchHelperCallback())
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun createTouchHelperCallback(): ItemTouchHelper.SimpleCallback {
        return object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = listAdapter.getItem(viewHolder.bindingAdapterPosition)

                // Left direction is to remove item after 5 sec
                if (direction == ItemTouchHelper.LEFT) {
                    removeTask(item)

                    // Right direction is to done item after 5 sec
                } else if (direction == ItemTouchHelper.RIGHT) {
                    doneTask(item, true)
                }
            }

            // To lock swiping to right when task is done
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {

                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                var swipeFlags = ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
                if (type == TaskType.Done) {
                    swipeFlags = ItemTouchHelper.LEFT
                }
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val builder = RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                setupRecyclerViewSwipeDecorator(builder)

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
    }

    fun setupRecyclerViewSwipeDecorator(builder: RecyclerViewSwipeDecorator.Builder) {
        builder.addSwipeRightBackgroundColor(
            getColor(requireContext(),R.color.color_done_item)

        ).addSwipeLeftBackgroundColor(
           getColor(requireContext(),R.color.color_remove_item)
        )
            .addCornerRadius(TypedValue.COMPLEX_UNIT_DIP, 16)
            .addPadding(COMPLEX_UNIT_PX, 4F, 0F, 8F)
            .addSwipeRightActionIcon(R.drawable.ic_done)
            .addSwipeLeftActionIcon(R.drawable.ic_delete)
            .setActionIconTint(
                getColor(requireContext(),R.color.white)
            )
            .create()
            .decorate()
    }

    private fun setFabAction() {
        val activity = requireActivity() as MainActivity
        activity.binding.fabAdd.setOnClickListener {
            goToAddTaskPage()
        }
    }

    private fun observeData() {
        viewModel.allTasks.observe(viewLifecycleOwner) {
            listAdapter.submitList(it.getListByType(type, priority))
        }
    }
}