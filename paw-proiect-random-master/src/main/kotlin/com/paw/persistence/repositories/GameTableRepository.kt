package com.paw.persistence.repositories

import com.paw.persistence.entities.GameTable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import javax.transaction.Transactional

interface GameTableRepository : JpaRepository<GameTable?, String?>{
    @Modifying
    @Transactional
    @Query("UPDATE game_table x SET users_number = ?2 WHERE x.id = ?1", nativeQuery = true)
    fun setUsersNumber(tableId: String, usersNumber: Int?)

    @Modifying
    @Transactional
    @Query("UPDATE game_table x SET round_number = ?2 WHERE x.id = ?1", nativeQuery = true)
    fun setRoundNumber(tableId: String, roundNumber: Int?)

    @Modifying
    @Transactional
    @Query("UPDATE game_table x SET players_ready = ?2 WHERE x.id = ?1", nativeQuery = true)
    fun setPlayersReady(tableId: String, players_ready: Int?)
}