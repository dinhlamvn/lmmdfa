package vn.dl.lmmdfa.database

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import vn.dl.lmmdfa.entity.TodoEntity

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(todoEntity: TodoEntity): Completable

    @Update
    fun update(todoEntity: TodoEntity): Completable

    @Delete
    fun delete(todoEntity: TodoEntity): Completable

    @Query("SELECT * FROM todo_tb LIMIT :limit")
    fun getTodoList(limit: Int): Observable<List<TodoEntity>>

    @Query("SELECT * FROM todo_tb WHERE uuid = :todoId")
    fun getTodo(todoId: String): Single<TodoEntity>

    @Query("SELECT COUNT(uuid) FROM todo_tb")
    fun getTotal(): Single<Int>
}