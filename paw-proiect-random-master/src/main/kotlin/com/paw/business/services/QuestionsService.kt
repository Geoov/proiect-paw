package com.paw.business.services

import com.paw.business.interfaces.I_QuestionsService
import com.paw.persistence.entities.Question
import com.paw.persistence.repositories.QuestionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class QuestionsService: I_QuestionsService {
    @Autowired
    private lateinit var questionRepository: QuestionRepository

    override fun getSomeQuestions(number: Int): Question? {
        TODO("Not yet implemented")
    }

    override fun mixQuestionsList(list: MutableList<Question>): MutableList<Question> {
        TODO("Not yet implemented")
        //Cred ca poate fi stearsa metoda asta, intrebarile ar trebui sa vina deja amestecate din functia de mai sus
    }

    override fun findQuestionsByID(id: Int): Optional<Question?> {
        return questionRepository.findById(id)
    }

    override fun getQuestionByText(text: String): Question? {
        return questionRepository.getByText(text)
    }
}