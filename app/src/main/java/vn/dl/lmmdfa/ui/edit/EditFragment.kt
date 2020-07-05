package vn.dl.lmmdfa.ui.edit

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import vn.dl.lmmdfa.R
import vn.dl.lmmdfa.base.BaseFragment
import vn.dl.lmmdfa.common.navigraph.OnNavigateUpInvoker
import vn.dl.lmmdfa.database.AppDatabase
import vn.dl.lmmdfa.extension.asApp
import vn.dl.lmmdfa.extension.bindView

class EditFragment : BaseFragment<EditState>(), OnNavigateUpInvoker {

    private val backPressCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            this.isEnabled = false
            this.remove()
            val text = textInputLayout.editText?.text.toString()
            editViewModel.handleTodoOnBackPress(text)
        }
    }

    private val editViewModel: EditViewModel by fragmentViewModel {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val dao = AppDatabase.getInstance(requireContext().asApp()).todoDao()
                return EditViewModel(dao) as T
            }
        }
    }

    private val textInputLayout: TextInputLayout by bindView(R.id.text_input_layout)

    override fun navigateUpInvoke() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(textInputLayout.editText?.windowToken, 0)
        val text = textInputLayout.editText?.text.toString()
        editViewModel.handleTodoOnBackPress(text)
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_edit
    }

    override fun postAfterViewCreated(view: View) {

        editViewModel.savedTodo.observe(viewLifecycleOwner, Observer { todo ->
            textInputLayout.editText?.setText(todo.content)
        })

        arguments?.let { bundle ->
            val noteId = EditFragmentArgs.fromBundle(bundle).todoId
            if (noteId.isNotEmpty()) {
                editViewModel.getTodoFromDatabase(noteId)
            }
        }

        editViewModel.newTodo.observe(viewLifecycleOwner, Observer { newTodo ->
            Toast.makeText(requireContext(), R.string.added_new_note, Toast.LENGTH_SHORT).show()
            activity?.onBackPressed()
        })

        editViewModel.error.observe(viewLifecycleOwner, Observer { e ->
            Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show()
        })

        editViewModel.modifiedTodo.observe(this, Observer {
            Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
            activity?.onBackPressed()
        })

        editViewModel.backPress.observe(this, Observer {
            activity?.onBackPressed()
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            backPressCallback
        )
    }

    override fun onViewModelStateChanged(state: EditState) {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}