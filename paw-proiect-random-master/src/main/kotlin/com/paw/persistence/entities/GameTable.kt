package com.paw.persistence.entities

import java.io.Serializable
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "game_table")
class GameTable : Serializable {
    @Id
    @Column(name = "id", nullable = false)
    var id: String? = null

    @Column(name = "id_host_user", nullable = false)
    var idHostUser: String? = null

    @Column(name = "round_number", nullable = false)
    var roundNumber: Int? = null

    @Column(name = "users_number", nullable = false)
    var usersNumber: Int? = null

    @Column(name = "players_ready", nullable = false)
    var readyPlayers: Int? = null

    override fun toString(): String {

        return "GameTable{" +
                "id=" + id + '\'' +
                "idHostUser=" + idHostUser + '\'' +
                "roundNumber=" + roundNumber + '\'' +
                "usersNumber=" + usersNumber + '\'' +
                "readyPlayers=" + readyPlayers + '\'' +
                '}'
    }

    constructor(id: String?, idHostUser: String?, roundNumber: Int?, usersNumber: Int?, readyPlayers: Int?){
        this.id = id
        this.idHostUser = idHostUser
        this.roundNumber = roundNumber
        this.usersNumber = usersNumber
        this.readyPlayers = readyPlayers
    }

    constructor(id: String?, idHostUser: String?){
        this.id = id
        this.idHostUser = idHostUser
    }

    constructor(){
        val uuid = UUID.randomUUID().toString()
        this.id = uuid.substring(0, 6);
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}