package vn.dl.lmmdfa.util

import vn.dl.lmmdfa.entity.NoteEntity
import vn.dl.lmmdfa.model.Note

object DataMapper {

    @JvmStatic
    fun entitiesNoteToModelsNote(list: List<NoteEntity>): List<Note> =
        list.map { entityNoteToModelNote(it) }

    @JvmStatic
    fun entityNoteToModelNote(noteEntity: NoteEntity): Note {
        return Note(
            uuid = noteEntity.uuid,
            content = noteEntity.content,
            createdAt = noteEntity.createdAt
        )
    }

    @JvmStatic
    fun modelNoteToEntityNote(note: Note): NoteEntity {
        return NoteEntity(uuid = note.uuid, content = note.content, createdAt = note.createdAt)
    }

    @JvmStatic
    fun newNoteEntity(content: String): NoteEntity {
        return NoteEntity(content = content, createdAt = System.currentTimeMillis())
    }
}