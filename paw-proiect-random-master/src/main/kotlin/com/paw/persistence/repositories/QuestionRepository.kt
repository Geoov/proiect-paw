package com.paw.persistence.repositories

import com.paw.persistence.entities.Question
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional


interface QuestionRepository : JpaRepository<Question?, Int?>{
//    @Modifying
//    @Transactional
//    @Query("SELECT * FROM question x WHERE x.text = ?1", nativeQuery = true)
//    fun getQuestionByText(text: String): List<Question?>
    fun getByText(text:String): Question?
}