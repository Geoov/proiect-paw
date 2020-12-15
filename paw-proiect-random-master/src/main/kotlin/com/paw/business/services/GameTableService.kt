package com.paw.business.services

import com.paw.business.interfaces.I_GameTableService
import com.paw.persistence.entities.GameTable
import com.paw.persistence.repositories.GameTableRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class GameTableService: I_GameTableService {
    @Autowired
    private lateinit var gameTableRepository: GameTableRepository

    override fun createGameTable(): String? {
        var gameTable = GameTable()
        gameTableRepository.save(gameTable)
        return gameTable.id
    }

    override fun getTableById(id: String): Optional<GameTable?> {
        val table = gameTableRepository.findById(id)
        return table
    }

    override fun addHostUser(table_id: String?, host_id: String?){
        val table = gameTableRepository.findById(table_id)
        if(table.isPresent){
            val updatedTable = GameTable(table.get().id!!, host_id)
            gameTableRepository.save(updatedTable)
        }
    }

    override fun incrementPlayersNr(table_id: String) {
        val table = gameTableRepository.findById(table_id)
        if(table.isPresent){
            var playersNumber = table.get().usersNumber?.plus(1)
            if(playersNumber == null)
                playersNumber = 1
            gameTableRepository.setUsersNumber(table_id, playersNumber)
        }
    }

    override fun decrementPlayersNr(table_id: String) {
        val table = gameTableRepository.findById(table_id)
        if(table.isPresent){
            var playersNumber = table.get().usersNumber?.minus(1)
            if(playersNumber == null || playersNumber < 0)
                playersNumber = 0
            gameTableRepository.setUsersNumber(table_id, playersNumber)
        }
    }

    override fun incrementRoundNr(table_id: String) {
        val table = gameTableRepository.findById(table_id)
        if(table.isPresent){
            var roundNumber = table.get().roundNumber?.plus(1)
            if(roundNumber == null)
                roundNumber = 1
            gameTableRepository.setRoundNumber(table_id, roundNumber)
        }
    }

    override fun getNrOfPlayersConnected(table_id: String): Int? {
        val table = gameTableRepository.findById(table_id)
        return table.get().usersNumber
    }

    override fun getRoundCount(table_id: String): Int? {
        val table = gameTableRepository.findById(table_id)
        return table.get().roundNumber
    }

    override fun getPlayersReady(table_id: String): Int? {
        val table = gameTableRepository.findById(table_id)
        return table.get().readyPlayers
    }

    override fun incrementPlayersReady(table_id: String) {
        val table = gameTableRepository.findById(table_id)
        if(table.isPresent){
            var readyPlayers = table.get().readyPlayers?.plus(1)
            if(readyPlayers == null)
                readyPlayers = 0
            gameTableRepository.setPlayersReady(table_id, readyPlayers)
        }
    }

    override fun decrementPlayersReady(table_id: String) {
        val table = gameTableRepository.findById(table_id)
        if(table.isPresent){
            var readyPlayers = table.get().readyPlayers?.minus(1)
            if(readyPlayers == null || readyPlayers < 0)
                readyPlayers = 0
            gameTableRepository.setPlayersReady(table_id, readyPlayers)
        }
    }
}