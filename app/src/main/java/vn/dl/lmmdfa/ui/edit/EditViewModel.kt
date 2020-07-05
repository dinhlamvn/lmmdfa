package vn.dl.lmmdfa.ui.edit

import androidx.lifecycle.LiveData
import io.reactivex.schedulers.Schedulers
import vn.dl.lmmdfa.base.BaseViewModel
import vn.dl.lmmdfa.common.SingleLiveEvent
import vn.dl.lmmdfa.database.TodoDao
import vn.dl.lmmdfa.model.Todo
import vn.dl.lmmdfa.util.DataMapper

class EditViewModel(private val dao: TodoDao) :
    BaseViewModel<EditState>(EditState()) {

    private val _savedTodo = SingleLiveEvent<Todo>()
    val savedTodo: LiveData<Todo> = _savedTodo

    private val _newTodo = SingleLiveEvent<Todo>()
    val newTodo: LiveData<Todo> = _newTodo

    private val _error = SingleLiveEvent<Throwable>()
    val error: LiveData<Throwable> = _error

    private val _modifiedTodo = SingleLiveEvent<Todo>()
    val modifiedTodo: LiveData<Todo> = _modifiedTodo

    fun getTodoFromDatabase(id: String) {
        setState { copy(newTodo = false, todoId = id) }
        getState { state ->
            dao.getTodo(state.todoId)
                .map { todo -> DataMapper.todoEntityToTodo(todo) }
                .subscribeOn(Schedulers.io())
                .subscribe { todo, _ ->
                    _savedTodo.postValue(todo)
                }
                .disposeOnClear()
        }
    }

    fun handleTodoOnBackPress(content: String) = getState { state ->
        if (state.newTodo) {
            addNewTodo(content)
        } else {
            modifyExistTodo(content)
        }
    }

    private fun addNewTodo(content: String) {
        val newTodo = DataMapper.newTodoEntity(content)
        dao.insert(newTodo)
            .subscribeOn(Schedulers.io())
            .subscribe({
                _newTodo.postValue(DataMapper.todoEntityToTodo(newTodo))
            }, { e ->
                _error.postValue(e)
            })
            .disposeOnClear()
    }

    private fun modifyExistTodo(content: String) = getState { state ->
        val todoId = state.todoId
        val modifiedTodo = DataMapper.todoToTodoEntity(
            Todo(
                uuid = todoId,
                content = content,
                createdAt = System.currentTimeMillis()
            )
        )
        dao.update(modifiedTodo)
            .subscribeOn(Schedulers.io())
            .subscribe({
                _modifiedTodo.postValue(DataMapper.todoEntityToTodo(modifiedTodo))
            }, { e ->
                _error.postValue(e)
            })
            .disposeOnClear()
    }
}