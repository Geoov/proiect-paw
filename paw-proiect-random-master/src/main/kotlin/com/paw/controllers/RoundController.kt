package com.paw.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.paw.business.interfaces.I_RoundService
import com.paw.persistence.entities.Round
import net.bytebuddy.description.method.MethodDescription
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import java.util.*
import org.springframework.http.MediaType
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.Consumer
import javax.servlet.http.HttpServletResponse
import kotlin.math.round

@Controller
@RequestMapping(value = ["/api/round"])
class RoundController {
    @Autowired
    private lateinit var roundService: I_RoundService
    private val answerEmitter = CopyOnWriteArrayList<SseEmitter>()
    private val votesEmitter = CopyOnWriteArrayList<SseEmitter>()

    companion object {
        var answerCount = true
        var voteCount = true
    }


    @RequestMapping(value = ["/createRound"], method = [RequestMethod.PUT])
    fun createGameRound(@RequestBody data: ObjectNode): ResponseEntity<String?> {
        val idGameTable = data.get("gameCode").toString()
        val users = data.get("users")
        val len = users.size()
        val userArray = arrayOfNulls<String>(len)
        var i = 0

        for (user in users) {

            userArray[i] = user.toString()
            i++
        }

        val randomQ = (1..21).shuffled().take(3)


        for (index in 0..userArray.size - 1 step 2) {
//            val idQ = kotlin.random.Random.Default.nextInt(1, 21)

            val idQ = randomQ[index/2]
            createRound(idGameTable.trim { it <= '"' }, idQ, userArray[index]?.trim { it <= '"' }, userArray[index + 1]?.trim { it <= '"' }, index)
        }

        return ResponseEntity(HttpStatus.OK)
    }

    fun createRound(idGameTable: String?, idQuestion: Int?, idUser1: String?, idUser2: String?, contor: Int?) {
        var round = roundService.createRound(idGameTable, idQuestion, idUser1, idUser2, contor)
    }

    @RequestMapping(value = ["/getRoundById"], method = [RequestMethod.GET])
    fun getRoundById(@RequestParam idRound: String?): ResponseEntity<Optional<Round?>> {
        var round = roundService.getRoundById(idRound)
        return ResponseEntity(round, HttpStatus.OK)
    }

    @RequestMapping(value = ["/getRoundsByTableId"], method = [RequestMethod.GET])
    fun getRoundsByTableId(@RequestParam idGameTable: String?): ResponseEntity<List<Round?>> {
        var round = roundService.getRounds(idGameTable)
        return ResponseEntity(round, HttpStatus.OK)
    }

    @RequestMapping(value = ["/setVotes"], method = [RequestMethod.PUT])
//    fun createRound(@RequestParam idRound: String?, @RequestParam votes_user_1: Int?, @RequestParam votes_user_2: Int?): ResponseEntity<String?> {
    fun createVoteRound(@RequestBody data: Map<String, String>): ResponseEntity<String?> {
        val idRound = data.get("idRound")
        val votes_user_1 = data.get("votes_user_1")?.toInt()
        val votes_user_2 = data.get("votes_user_2")?.toInt()

        var round = roundService.updateVotes(idRound, votes_user_1, votes_user_2)
        voteCount = true

        return ResponseEntity(HttpStatus.OK)
    }


    @RequestMapping(value = ["/setAnswer"], method = [RequestMethod.PUT])
//    fun createRound(@RequestBody idRound: String?, @RequestBody answer_user_1: String?, @RequestBody answer_user_2: String?): ResponseEntity<String?>{
    fun createRound(@RequestBody data: Map<String, String>): ResponseEntity<String?> {
        val idRound = data.get("idRound")
        val answer_user_1 = data.get("answer_user_1")
        val answer_user_2 = data.get("answer_user_2")
        answerCount = true

        var round = roundService.updateAnswer(idRound, answer_user_1, answer_user_2)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(value = ["/getAnswersCount"], method = [RequestMethod.GET])
    fun getAnswersCount(@RequestParam idGameTable: String?): ResponseEntity<Int?> {
        var answersCount = roundService.getAnswersCount(idGameTable)
        return ResponseEntity(answersCount, HttpStatus.OK)
    }

    @RequestMapping(value = ["/answer/getAnswerCount"], method = [RequestMethod.GET])
    fun getAnswers(response: HttpServletResponse, @RequestParam table_id: String?): SseEmitter? {
        response.setHeader("Cache-Control", "no-store")
        answerCount = true
        val emitter = SseEmitter(-1L)

        this.answerEmitter.add(emitter)
        emitter.onCompletion { this.answerEmitter.remove(emitter) }
        emitter.onTimeout { this.answerEmitter.remove(emitter) }
        Thread {
            while (true) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }

                if (answerCount)
                    sendAnswers(table_id)

            }
        }.start()

        return emitter
    }

    @EventListener
    fun sendAnswers(table_id: String?) {
        val deadEmitters: MutableList<SseEmitter> = ArrayList()
        this.answerEmitter.forEach(Consumer { emitter: SseEmitter ->
            try {
                var answersCount = roundService.getAnswersCount(table_id!!)
                answerCount = false
                emitter.send(answersCount)
            } catch (e: Exception) {
                deadEmitters.add(emitter)
            }
        })
        this.answerEmitter.removeAll(deadEmitters)
    }

    @RequestMapping(value = ["/vote/checkVotes"], method = [RequestMethod.GET])
    fun checkVotes(@RequestParam round_id: String?): ResponseEntity<Int?> {
        var count = roundService.getVotesCount(round_id)
        return ResponseEntity(count, HttpStatus.OK)
    }

    @RequestMapping(value = ["/vote/getVoteCount"], method = [RequestMethod.GET])
    fun getVotes(response: HttpServletResponse, @RequestParam round_id: String?): SseEmitter? {
        response.setHeader("Cache-Control", "no-store")
        voteCount = true
        val emitter = SseEmitter(-1L)

        this.votesEmitter.add(emitter)
        emitter.onCompletion { this.votesEmitter.remove(emitter) }
        emitter.onTimeout { this.votesEmitter.remove(emitter) }
        Thread {
            while (true) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }

                if (voteCount)
                    sendVotes(round_id)

            }
        }.start()

        return emitter
    }

    @EventListener
    fun sendVotes(round_id: String?) {
        val deadEmitters: MutableList<SseEmitter> = ArrayList()
        this.votesEmitter.forEach(Consumer { emitter: SseEmitter ->
            try {
                var votesCount = roundService.getVotesCount(round_id!!)
                voteCount = false
                emitter.send(votesCount)
            } catch (e: Exception) {
                deadEmitters.add(emitter)
            }
        })
        this.votesEmitter.removeAll(deadEmitters)
    }

}