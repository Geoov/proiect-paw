package com.paw.controllers

import com.paw.business.interfaces.I_GameTableService
import com.paw.business.interfaces.I_QuestionsService
import com.paw.business.interfaces.I_RoundService
import com.paw.persistence.entities.Question
import org.apache.catalina.connector.ClientAbortException
import org.hibernate.bytecode.BytecodeLogger.LOGGER
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.*
import javax.servlet.http.HttpServletResponse


@Controller
@RequestMapping(value = ["/api"])
class GameController {
    @Autowired
    private lateinit var gameTableService: I_GameTableService
    @Autowired
    private lateinit var questionsService: I_QuestionsService
    @Autowired
    private lateinit var roundService: I_RoundService


    //GET http://localhost:8080/api/test
    @RequestMapping(value = ["/getQuestion"], method = [RequestMethod.GET])
    fun getQuestionByID(@RequestParam id: Int) : ResponseEntity<Optional<Question?>> {

        var question = questionsService.findQuestionsByID(id)

        if(question.isPresent){
            return ResponseEntity(question, HttpStatus.OK)
        }else{
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
    @RequestMapping(value = ["/getQuestionByText"], method = [RequestMethod.GET])
    fun getQuestionByText(@RequestParam text: String) : ResponseEntity<Question?> {

        var question = questionsService.getQuestionByText(text)


        return ResponseEntity(question, HttpStatus.OK)
    }
}