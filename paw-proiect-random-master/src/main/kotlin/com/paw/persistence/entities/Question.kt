package com.paw.persistence.entities

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "question")
@Entity
class Question : Serializable {
    @Id
    @Column(name = "id", nullable = false)
    var id: Int? = null

    @Column(name = "text", nullable = false)
    var text: String? = null

    override fun toString(): String {
        return "Question{" +
                "id=" + id + '\'' +
                "text=" + text + '\'' +
                '}'
    }

    constructor()

    companion object {
        private const val serialVersionUID = 1L
    }
}