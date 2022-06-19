package com.example.todolist.core.base

import android.graphics.Canvas
import android.os.CountDownTimer
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_PX
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.data.model.Priority
import com.example.todolist.data.model.Task
import com.example.todolist.data.model.TaskType
import com.example.todolist.databinding.CustomSnackbarBinding
import com.example.todolist.databinding.FragmentTaskListBinding
import com.example.todolist.ui.main.MainActivity
import com.example.todolist.util.getColor
import com.example.todolist.util.getListByType
import com.example.todolist.util.invisible
import com.example.todolist.util.visible
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
abstract class CommonTaskListFragment(
    private val priority: Priority?,
    private val type: TaskType
) : BaseTaskListFragment<FragmentTaskListBinding>(R.layout.fragment_task_list, type) {

    private lateinit var snackbarBinding: CustomSnackbarBinding
    private lateinit var snackbar: Snackbar

    // region define colors
    private var colorSurface = 0
    private var colorOnSurface = 0
    private var colorDoneItem = 0
    private var colorRemoveItem = 0
    private var colorWhite = 0
    // endregion

    // region initialize views
    override fun onInitDataBinding() {
        colorSurface = getColor(requireContext(), R.color.color_surface)
        colorOnSurface = getColor(requireContext(), R.color.color_on_surface)
        colorDoneItem = getColor(requireContext(), R.color.color_done_item)
        colorRemoveItem = getColor(requireContext(), R.color.color_remove_item)
        colorWhite = getColor(requireContext(), R.color.white)

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
    //endregion

    // region recyclerview touch helper
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
                    setUpSnackBar(item, true)

                    // Right direction is to done item after 5 sec
                } else if (direction == ItemTouchHelper.RIGHT) {
                    doneTask(item, true)
                    setUpSnackBar(item, false)
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
        builder.addSwipeRightBackgroundColor(colorDoneItem)
            .addSwipeLeftBackgroundColor(colorRemoveItem)
            .addCornerRadius(TypedValue.COMPLEX_UNIT_DIP, 16)
            .addPadding(COMPLEX_UNIT_PX, 4F, 0F, 8F)
            .addSwipeRightActionIcon(R.drawable.ic_done)
            .addSwipeLeftActionIcon(R.drawable.ic_delete)
            .setActionIconTint(colorWhite)
            .create()
            .decorate()
    }
    //endregion

    //region snackbar progress
    fun setUpSnackBar(item: Task, isRemove: Boolean) {
        snackbar = Snackbar.make(
            recyclerView.rootView,
            "",
            Snackbar.LENGTH_INDEFINITE
        ).setBackgroundTint(colorSurface)
            .setActionTextColor(colorOnSurface)
            .setTextColor(colorOnSurface)
            .setAnchorView(R.id.bottom_navigation_view)

        // Add custom snackbar to snackbar layout
        val snackView: SnackbarLayout = snackbar.view as SnackbarLayout
        snackView.addView(customSnackbarInflater(item, isRemove))

        snackbar.show()
    }

    // Inflate custom snackbar and initialize views
    private fun customSnackbarInflater(item: Task, isRemove: Boolean): View {
        initSnackbarBinding(isRemove)

        // Set a 5 second timer
        val timer = object : CountDownTimer(5000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                progressOnTickHandler(millisUntilFinished)
            }

            override fun onFinish() {
                progressOnFinishedHandler()
            }
        }.start()

        // Undo removing item
        snackbarBinding.tvUndo.setOnClickListener {
            progressOnCancelHandler(timer, item, isRemove)
        }
        return snackbarBinding.root
    }

    private fun initSnackbarBinding(isRemove: Boolean) {
        snackbarBinding = CustomSnackbarBinding.inflate(layoutInflater)

        with(snackbarBinding) {
            this.isRemove = isRemove
            val rawRes = if (isRemove) R.raw.remove else R.raw.done
            lottieDone.setAnimation(rawRes)
        }
    }

    private fun progressOnTickHandler(millisUntilFinished: Long) {
        millisUntilFinished.toInt()
        val sec = millisUntilFinished.toInt() / 1000
        with(snackbarBinding) {
            tvTimer.text = String.format(java.util.Locale.getDefault(), "%d", sec)
            pbTimer.progress = sec
        }
    }

    private fun progressOnFinishedHandler() {
        with(snackbarBinding) {
            pbTimer.invisible()
            tvTimer.invisible()

            lifecycleScope.launch {
                lottieDone.playAnimation()
                lottieDone.visible()
                val delay = lottieDone.duration
                delay(delay)
                lottieDone.pauseAnimation()
                snackbar.dismiss()
            }
        }
    }

    private fun progressOnCancelHandler(timer: CountDownTimer, item: Task, isRemove: Boolean) {
        timer.cancel()

        // Undo action
        if (isRemove) addTask(item)
        else doneTask(item, false)

        snackbar.dismiss()
    }
    //endregion

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