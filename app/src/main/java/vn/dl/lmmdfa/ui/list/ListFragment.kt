package vn.dl.lmmdfa.ui.list

import android.view.View
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import vn.dl.lmmdfa.R
import vn.dl.lmmdfa.base.BaseFragment
import vn.dl.lmmdfa.base.BaseListAdapter
import vn.dl.lmmdfa.common.RecyclerViewLoadMoreDetector
import vn.dl.lmmdfa.database.AppDatabase
import vn.dl.lmmdfa.extension.asApp
import vn.dl.lmmdfa.extension.bindView
import vn.dl.lmmdfa.model.Todo
import vn.dl.lmmdfa.ui.list.modelview.EmptyView
import vn.dl.lmmdfa.ui.list.modelview.LoadingView
import vn.dl.lmmdfa.ui.list.modelview.TodoView
import vn.dl.lmmdfa.util.ui.UiUtils

class ListFragment : BaseFragment<ListState>() {

    private val todoAdapter = BaseListAdapter()

    private val swipeDeleteCallback = SwipeToDeleteCallback { positionDeleted ->
        viewModel.deleteTodo(positionDeleted)
        todoAdapter.notifyItemRemoved(positionDeleted)
    }

    private val loadMoreDetector = RecyclerViewLoadMoreDetector {
        //viewModel.loadMore()
    }

    private val viewModel: ListViewModel by fragmentViewModel {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val dao = AppDatabase.getInstance(requireContext().asApp()).todoDao()
                return ListViewModel(dao) as T
            }
        }
    }

    private val fbtAdd: FloatingActionButton by bindView(R.id.fab)

    override fun layoutRes(): Int = R.layout.fragment_list

    override fun postAfterViewCreated(view: View) {
        val recyclerViewNoteList = view.findViewById<RecyclerView>(R.id.recyclerview_note_list)
        recyclerViewNoteList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        recyclerViewNoteList.adapter = todoAdapter

        loadMoreDetector.attachToRecyclerView(recyclerViewNoteList)

        val itemTouchHelper = ItemTouchHelper(swipeDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerViewNoteList)

        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToEditNoteFragment("")
            findNavController().navigate(action)
        }

        viewModel.restoreDeletedTodoEvent.observe(viewLifecycleOwner, Observer { deleteNote ->
            showRestoreNoteView(deleteNote)
        })

        viewModel.deletedEvent.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), R.string.deleted_note, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onViewModelStateChanged(state: ListState) {
        setTotalNoteToActionBar(state.total)

        val builds = mutableListOf<BaseListAdapter.BaseModelView>()

        val noteList = state.todoList

        if (noteList.isEmpty()) {
            builds.add(EmptyView)
        } else {
            val notes: List<BaseListAdapter.BaseModelView> = noteList.map { todo ->
                TodoView(
                    id = todo.uuid,
                    content = todo.content,
                    createdAt = todo.createdAt,
                    isSelected = todo.isSelected,
                    onViewClick = View.OnClickListener {
                        val action =
                            ListFragmentDirections.actionListFragmentToEditNoteFragment(todo.uuid)
                        findNavController().navigate(action)
                    }
                )
            }
            builds.addAll(notes)
        }

        if (noteList.isNotEmpty() && state.currentPage < state.lastPage) {
            builds.add(LoadingView("loading"))
        }

        todoAdapter.submitChange(builds)
    }

    private fun showRestoreNoteView(todo: Todo) {
        UiUtils.showSnakeBar(
            fbtAdd,
            R.string.list_screen_note_restore_confirm,
            R.string.undo_text,
            ContextCompat.getColor(requireContext(), R.color.color_snake_bar_action_color),
            Snackbar.LENGTH_LONG
        ) {
            viewModel.restoreTodo(todo)
        }
    }

    @UiThread
    private fun setTotalNoteToActionBar(total: Int) {
        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar)
        val title = getString(R.string.list_screen_tool_bar_title, total)
        //toolbar.title = title
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }
}