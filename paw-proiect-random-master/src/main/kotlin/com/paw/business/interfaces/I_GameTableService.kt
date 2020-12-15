package com.paw.business.interfaces

import com.paw.persistence.entities.GameTable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

interface I_GameTableService {
    fun createGameTable() : String?   //returneaza un table_id
    fun getTableById(table_id: String) : Optional<GameTable?>
    fun addHostUser(table_id: String?, host_id: String?)
    fun incrementPlayersNr(table_id: String)
    fun decrementPlayersNr(table_id: String)
    fun incrementRoundNr(table_id: String)
    fun getNrOfPlayersConnected(table_id: String):Int?
    fun getRoundCount(table_id: String):Int?
    fun getPlayersReady(table_id: String):Int?
    fun incrementPlayersReady(table_id: String)
    fun decrementPlayersReady(table_id: String)
}