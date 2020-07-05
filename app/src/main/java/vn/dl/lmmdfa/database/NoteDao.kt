package vn.dl.lmmdfa.database

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import vn.dl.lmmdfa.entity.NoteEntity

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(noteEntity: NoteEntity): Completable

    @Update
    fun update(noteEntity: NoteEntity): Completable

    @Delete
    fun delete(noteEntity: NoteEntity): Completable

    @Query("SELECT * FROM notes LIMIT :limit")
    fun getAll(limit: Int): Observable<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE uuid = :noteId")
    fun getNote(noteId: String): Single<NoteEntity>

    @Query("SELECT COUNT(uuid) FROM notes")
    fun getTotal(): Single<Int>
}