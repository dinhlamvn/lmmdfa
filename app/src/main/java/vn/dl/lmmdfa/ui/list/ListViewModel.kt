package vn.dl.lmmdfa.ui.list

import androidx.lifecycle.LiveData
import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.dl.lmmdfa.base.BaseViewModel
import vn.dl.lmmdfa.common.SingleLiveEvent
import vn.dl.lmmdfa.database.TodoDao
import vn.dl.lmmdfa.model.Todo
import vn.dl.lmmdfa.util.DataMapper
import java.util.concurrent.TimeUnit

class ListViewModel(private val dao: TodoDao) :
    BaseViewModel<ListState>(ListState()) {

    private val _selectNoteEvent = SingleLiveEvent<Todo>()
    val selectTodoEvent: LiveData<Todo>
        get() = _selectNoteEvent

    private val _restoreDeletedNoteEvent = SingleLiveEvent<Todo>()
    val restoreDeletedTodoEvent: LiveData<Todo> = _restoreDeletedNoteEvent

    private val _deletedEvent = SingleLiveEvent<Unit>()
    val deletedEvent: LiveData<Unit> = _deletedEvent

    private var deleteNoteDisposable: Disposable? = null

    internal fun refresh() = getState { state ->
        getAllTodo(state.currentPage)
    }

    private fun getAllTodo(page: Int) {
        dao.getAll(20)
            .map { list -> list.map { DataMapper.todoEntityToTodo(it) } }
            .subscribeOn(Schedulers.io())
            .execute { list: List<Todo> ->
                copy(todoList = list)
            }
    }

    fun deleteTodo(position: Int) = getState { state ->
        val todo = state.todoList.getOrNull(position) ?: return@getState
        _restoreDeletedNoteEvent.postValue(todo)
        deleteNoteDisposable = Completable.timer(5000, TimeUnit.MILLISECONDS)
            .andThen { source ->
                source.onComplete()
                dao.delete(DataMapper.todoToTodoEntity(todo))
                    .subscribe {
                        _deletedEvent.postValue(Unit)
                    }
                    .disposeOnClear()
            }.subscribeOn(Schedulers.io())
            .subscribe()
            .disposeOnClear()
    }

    fun restoreTodo(todo: Todo) {
        deleteNoteDisposable?.dispose()
        //submitState { copy() }
    }
}