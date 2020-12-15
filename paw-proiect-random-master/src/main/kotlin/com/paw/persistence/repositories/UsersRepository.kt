package com.paw.persistence.repositories

import com.paw.persistence.entities.Users
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.*
import javax.transaction.Transactional

interface UsersRepository : JpaRepository<Users?, String?>{
    @Query("SELECT * FROM users x WHERE x.id_game_table = ?1", nativeQuery = true)
    fun getUsers(tableId: String): List<Users?>

    @Query("SELECT id FROM users x WHERE x.name = ?1 AND x.id_game_table = ?2", nativeQuery = true)
    fun findByString(name: String, tableId: String): String?

    @Query("SELECT * FROM users x WHERE x.id = ?1", nativeQuery = true)
    fun getIdGameTableByUserID(id_game_table: String): Optional<Users?>

    @Query("SELECT * FROM users x WHERE x.id_game_table = ?1", nativeQuery = true)
    fun getUserVotes(table_id: String?): List<Users>?

    @Query("DELETE * FROM users x WHERE x.id_game_table = ?1", nativeQuery = true)
    fun deleteByGameTableId(id: String?)

    @Modifying
    @Transactional
    @Query("DELETE FROM users x WHERE x.id = ?1", nativeQuery = true)
    fun deleteUserById(userId: String?)

    @Modifying
    @Transactional
    @Query("UPDATE users x SET votes = ?2 WHERE x.id = ?1", nativeQuery = true)
    fun setVotesById(id: String?, votes: Int)

    @Modifying
    @Transactional
    @Query("UPDATE users x SET id_game_table = ?2 WHERE x.id = ?1", nativeQuery = true)
    fun setGameTableById(userId: String, tableId: String)
}