package com.paw.persistence.entities

import java.io.Serializable
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "round")
class Round : Serializable {
    @Column(name = "answer_user_1")
    var answerUser1: String? = null

    @Column(name = "answer_user_2")
    var answerUser2: String? = null

    @Id
    @Column(name = "id", nullable = false)
    var id: String? = null

    @Column(name = "id_game_table", nullable = false)
    var idGameTable: String? = null

    @Column(name = "id_question", nullable = false)
    var idQuestion: Int? = null

    @Column(name = "id_user_1", nullable = false)
    var idUser1: String? = null

    @Column(name = "id_user_2", nullable = false)
    var idUser2: String? = null

    @Column(name = "votes_user_1")
    var votesUser1: Int? = null

    @Column(name = "votes_user_2")
    var votesUser2: Int? = null

    @Column(name = "contor")
    var contor: Int? = null

    constructor(idGameTable: String?, idQuestion: Int?, idUser1: String?, idUser2: String?, contor: Int?){
        val uuid = UUID.randomUUID().toString()
        this.id = uuid.substring(0, 2);
        this.idGameTable = idGameTable
        this.idQuestion = idQuestion
        this.idUser1 = idUser1
        this.idUser2 = idUser2
        this.contor = contor
        this.votesUser1 = 0
        this.votesUser2 = 0
    }

    constructor()

    override fun toString(): String {
        return "Round{" +
                "answerUser1=" + answerUser1 + '\'' +
                "answerUser2=" + answerUser2 + '\'' +
                "id=" + id + '\'' +
                "idGameTable=" + idGameTable + '\'' +
                "idQuestion=" + idQuestion + '\'' +
                "idUser1=" + idUser1 + '\'' +
                "idUser2=" + idUser2 + '\'' +
                "votesUser1=" + votesUser1 + '\'' +
                "votesUser2=" + votesUser2 + '\'' +
                '}'
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}