package vn.dl.lmmdfa.ui.edit

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import vn.dl.lmmdfa.R
import vn.dl.lmmdfa.base.BaseFragment
import vn.dl.lmmdfa.database.AppDatabase
import vn.dl.lmmdfa.extension.asApp
import vn.dl.lmmdfa.extension.bindView

class EditNoteFragment : BaseFragment<EditState>() {

    private val backPressCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            this.isEnabled = false
            this.remove()
            val text = textInputLayout.editText?.text.toString()
            if (text.isNotEmpty()) {
                editNoteViewModel.handleNote(text)
            } else {
                activity?.onBackPressed()
            }
        }
    }

    private val editNoteViewModel: EditNoteViewModel by fragmentViewModel {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val dao = AppDatabase.getInstance(requireContext().asApp()).noteDao()
                return EditNoteViewModel(dao) as T
            }
        }
    }

    private val textInputLayout: TextInputLayout by bindView(R.id.text_input_layout)

    override fun layoutRes(): Int {
        return R.layout.fragment_edit
    }

    override fun postAfterViewCreated(view: View) {

        editNoteViewModel.savedNote.observe(viewLifecycleOwner, Observer { note ->
            textInputLayout.editText?.setText(note.content)
        })

        arguments?.let { bundle ->
            val noteId = EditNoteFragmentArgs.fromBundle(bundle).noteId
            if (noteId.isNotEmpty()) {
                editNoteViewModel.setNoteIdAndLoadSavedNote(noteId)
            }
        }

        editNoteViewModel.newNote.observe(viewLifecycleOwner, Observer { newNote ->
            Toast.makeText(requireContext(), R.string.added_new_note, Toast.LENGTH_SHORT).show()
            activity?.onBackPressed()
        })

        editNoteViewModel.error.observe(viewLifecycleOwner, Observer { e ->
            Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show()
        })

        editNoteViewModel.modifiedNote.observe(this, Observer {
            Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
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
}