package vn.dl.lmmdfa.ui.list

import androidx.lifecycle.LiveData
import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.dl.lmmdfa.base.BaseViewModel
import vn.dl.lmmdfa.common.SingleLiveEvent
import vn.dl.lmmdfa.database.NoteDao
import vn.dl.lmmdfa.model.Note
import vn.dl.lmmdfa.util.DataMapper
import java.util.concurrent.TimeUnit

class ListViewModel(private val dao: NoteDao) :
    BaseViewModel<ListState>(ListState()) {

    private val _selectNoteEvent = SingleLiveEvent<Note>()
    val selectNoteEvent: LiveData<Note>
        get() = _selectNoteEvent

    private val _restoreDeletedNoteEvent = SingleLiveEvent<Note>()
    val restoreDeletedNoteEvent: LiveData<Note> = _restoreDeletedNoteEvent

    private val _deletedEvent = SingleLiveEvent<Unit>()
    val deletedEvent: LiveData<Unit> = _deletedEvent

    private var deleteNoteDisposable: Disposable? = null

    internal fun refresh() = getState { state ->
        getAllNotes(state.currentPage)
    }

    private fun getAllNotes(page: Int) {
        dao.getAll(20)
            .map { list -> list.map { DataMapper.entityNoteToModelNote(it) } }
            .subscribeOn(Schedulers.io())
            .execute { list: List<Note> ->
                copy(noteList = list)
            }
    }

    fun deleteNote(position: Int) = getState { state ->
        val note = state.noteList.getOrNull(position) ?: return@getState
        _restoreDeletedNoteEvent.postValue(note)
        deleteNoteDisposable = Completable.timer(5000, TimeUnit.MILLISECONDS)
            .andThen { source ->
                source.onComplete()
                dao.delete(DataMapper.modelNoteToEntityNote(note))
                    .subscribe {
                        _deletedEvent.postValue(Unit)
                    }
                    .disposeOnClear()
            }.subscribeOn(Schedulers.io())
            .subscribe()
            .disposeOnClear()
    }

    fun restoreNote(note: Note) {
        deleteNoteDisposable?.dispose()
        //submitState { copy() }
    }
}