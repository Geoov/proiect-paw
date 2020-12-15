package com.paw.business.interfaces

import com.paw.persistence.entities.Question
import java.util.*

interface I_QuestionsService {
    fun getSomeQuestions(number: Int): Question?
    fun mixQuestionsList(list: MutableList<Question>) : MutableList<Question>
    fun findQuestionsByID(id: Int): Optional<Question?>
    fun getQuestionByText(text: String): Question?
    //TODO: DE VAZUT CE MAI ESTE NEVOIE
}