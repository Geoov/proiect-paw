package com.paw.persistence.entities

import java.io.Serializable
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "users")
class Users : Serializable {
    @Id
    @Column(name = "id", nullable = false)
    var id: String? = null

    @Column(name = "id_game_table", nullable = false)
    var idGameTable: String? = null

    @Column(name = "name", nullable = false)
    var name: String? = null

    @Column(name = "votes")
    var votes: Int? = null

    @Column(name = "color")
    var color: String? = null

    constructor(name: String){
        this.id = UUID.randomUUID().toString()
        this.name = name
    }

    constructor(id: String, name: String?, table_id: String){
        this.id = id
        this.name = name
        this.idGameTable = table_id
        this.color = Color.random
    }

    constructor()

    override fun toString(): String {
        return "Users{" +
                "id=" + id + '\'' +
                "idGameTable=" + idGameTable + '\'' +
                "name=" + name + '\'' +
                "votes=" + votes + '\'' +
                "color=" + color + '\'' +
                '}'
    }

    companion object {
        private const val serialVersionUID = 1L
        var colorIndex: Int = 0
    }

    enum class Color {
        darkcyan, darkkhaki, darkred, limegreen, darkgoldenrod, darkviolet, darkorange, darkgrey, blue;

        companion object {
            val random: String

            get() {
                    val color = values()[(colorIndex).toInt()].toString()
                    if( colorIndex >= 9)
                        colorIndex = 0
                    colorIndex += 1
                    return color
            }
        }
    }

}