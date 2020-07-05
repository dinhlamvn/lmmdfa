package vn.dl.lmmdfa.util

import vn.dl.lmmdfa.entity.TodoEntity
import vn.dl.lmmdfa.model.Todo

object DataMapper {

    @JvmStatic
    fun todoEntityToTodo(todoEntity: TodoEntity): Todo {
        return Todo(
            uuid = todoEntity.uuid,
            content = todoEntity.content,
            createdAt = todoEntity.createdAt
        )
    }

    @JvmStatic
    fun todoToTodoEntity(todo: Todo): TodoEntity {
        return TodoEntity(uuid = todo.uuid, content = todo.content, createdAt = todo.createdAt)
    }

    @JvmStatic
    fun newTodoEntity(content: String): TodoEntity {
        return TodoEntity(content = content, createdAt = System.currentTimeMillis())
    }
}