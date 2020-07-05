package vn.dl.lmmdfa.ui.edit

import androidx.lifecycle.LiveData
import io.reactivex.schedulers.Schedulers
import vn.dl.lmmdfa.base.BaseViewModel
import vn.dl.lmmdfa.common.SingleLiveEvent
import vn.dl.lmmdfa.database.NoteDao
import vn.dl.lmmdfa.model.Note
import vn.dl.lmmdfa.util.DataMapper

class EditNoteViewModel(private val dao: NoteDao) :
    BaseViewModel<EditState>(EditState()) {

    private val _savedNote = SingleLiveEvent<Note>()
    val savedNote: LiveData<Note> = _savedNote

    private val _newNote = SingleLiveEvent<Note>()
    val newNote: LiveData<Note> = _newNote

    private val _error = SingleLiveEvent<Throwable>()
    val error: LiveData<Throwable> = _error

    private val _modifiedNote = SingleLiveEvent<Note>()
    val modifiedNote: LiveData<Note> = _modifiedNote

    fun setNoteIdAndLoadSavedNote(id: String) {
        setState { copy(newNote = false, uuid = id) }
        getState {
            dao.getNote(id)
                .map { note -> DataMapper.entityNoteToModelNote(note) }
                .subscribeOn(Schedulers.io())
                .subscribe { note, _ ->
                    _savedNote.postValue(note)
                }
                .disposeOnClear()
        }
    }

    fun handleNote(content: String) = getState { state ->
        if (state.newNote) {
            addNewNote(content)
        } else {
            modifyNote(content)
        }
    }

    private fun addNewNote(content: String) {
        val newNote = DataMapper.newNoteEntity(content)
        dao.insert(newNote)
            .subscribeOn(Schedulers.io())
            .subscribe({
                _newNote.postValue(DataMapper.entityNoteToModelNote(newNote))
            }, { e ->
                _error.postValue(e)
            })
            .disposeOnClear()
    }

    private fun modifyNote(content: String) = getState { state ->
        val noteId = state.uuid
        val modifiedNote = DataMapper.modelNoteToEntityNote(
            Note(
                uuid = noteId,
                content = content,
                createdAt = System.currentTimeMillis()
            )
        )
        dao.update(modifiedNote)
            .subscribeOn(Schedulers.io())
            .subscribe({
                _modifiedNote.postValue(DataMapper.entityNoteToModelNote(modifiedNote))
            }, { e ->
                _error.postValue(e)
            })
            .disposeOnClear()
    }
}