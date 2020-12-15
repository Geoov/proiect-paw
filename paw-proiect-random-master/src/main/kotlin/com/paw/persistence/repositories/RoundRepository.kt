package com.paw.persistence.repositories

import com.paw.persistence.entities.Round
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
//import sun.security.ec.point.ExtendedHomogeneousPoint
//import sun.security.ec.point.ProjectivePoint
import javax.transaction.Transactional


interface RoundRepository : JpaRepository<Round?, String?>{
    @Query("SELECT * FROM round WHERE id_game_table = ?1", nativeQuery = true)
    fun getRoundsByTableId(tableId: String?): List<Round?>

    @Modifying
    @Transactional
    @Query("UPDATE round x SET votes_user_1 = ?2, votes_user_2 = ?3 WHERE x.id = ?1", nativeQuery = true)
    fun setVotes(round_id: String?, votesUser1: Int?, votesUser2: Int?)

    @Modifying
    @Transactional
    @Query("Update round x SET answer_user_1 = ?2, answer_user_2 = ?3 WHERE x.id = ?1", nativeQuery = true)
    fun setAnswer(round_id: String?, answerUser1: String?, answerUser2: String?)

    @Modifying
    @Transactional
    @Query("Update round x SET answer_user_1 = ?2 WHERE x.id = ?1", nativeQuery = true)
    fun setAnswerUser1(round_id: String?, answerUser1: String?)

    @Modifying
    @Transactional
    @Query("Update round x SET answer_user_2 = ?2 WHERE x.id = ?1", nativeQuery = true)
    fun setAnswerUser2(round_id: String?, answerUser2: String?)

    @Query("SELECT COUNT(answer_user_1) + COUNT(answer_user_2) as sum FROM round WHERE id_game_table=?1", nativeQuery = true)
    fun getAnswersCount(tableId: String?) : Int?

    @Query("SELECT votes_user_1 + votes_user_2 as sum FROM round WHERE id=?1", nativeQuery = true)
    fun getVotesCount(round_id: String?) : Int?

    @Modifying
    @Transactional
    @Query("Update round x SET votes_user_1 = ?2 WHERE x.id = ?1", nativeQuery = true)
    fun setVotesUser1(roundId: String?, votesUser1: Int?) : Int?

    @Modifying
    @Transactional
    @Query("Update round x SET votes_user_2 = ?2 WHERE x.id = ?1", nativeQuery = true)
    fun setVotesUser2(roundId: String?, votesUser2: Int?) : Int?
}