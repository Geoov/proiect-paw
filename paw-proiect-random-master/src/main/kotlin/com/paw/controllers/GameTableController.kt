package com.paw.controllers

import com.paw.business.interfaces.I_GameTableService
import com.paw.persistence.entities.GameTable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.Consumer
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping(value = ["/api/gameTable"])
class GameTableController {
    @Autowired
    private lateinit var gameTableService: I_GameTableService
    private val readyPlayersEmitters = CopyOnWriteArrayList<SseEmitter>()

    companion object {
        var lastModifiedDate = LocalDateTime.now()
        var currentModifiedDate = lastModifiedDate.minus(Duration.ofMillis(500))
    }

    @RequestMapping(value = ["/createGameTable"], method = [RequestMethod.PUT])
    fun createGameTable(): ResponseEntity<String?> {
        var table_id = gameTableService.createGameTable()
        if(table_id.isNullOrEmpty())
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        else
            return ResponseEntity(table_id, HttpStatus.OK)
    }

    @RequestMapping(value = ["/getGameTable"], method = [RequestMethod.GET])
    fun getGameTable(@RequestParam id: String): ResponseEntity<Optional<GameTable?>> {
        var table = gameTableService.getTableById(id)
        if(!table.isEmpty)
            return ResponseEntity(table, HttpStatus.OK)
        else
            return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @RequestMapping(value = ["/addHostId"], method = [RequestMethod.PUT])
    fun addHostId(@RequestBody data:Map<String, String>): ResponseEntity<String?> {
        var host_id = data.get("host_id")
        var table_id = data.get("table_id")
        var table = gameTableService.addHostUser(table_id, host_id)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(value = ["/incrementPlayersNr"], method = [RequestMethod.PUT])
    fun incrementPlayersNr(@RequestBody table_id: String?): ResponseEntity<String?> {
        if(table_id != null) {
            var table = gameTableService.incrementPlayersNr(table_id)
            return ResponseEntity(HttpStatus.OK)
        }
        return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = ["/decrementPlayersNr"], method = [RequestMethod.PUT])
    fun decrementPlayersNr(@RequestBody table_id: String): ResponseEntity<String?> {
        var table = gameTableService.decrementPlayersNr(table_id)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(value = ["/incrementRoundNr"], method = [RequestMethod.PUT])
    fun incrementRoundNr(@RequestBody table_id: String): ResponseEntity<String?> {
        var table = gameTableService.incrementRoundNr(table_id)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(value = ["/getNrOfPlayersConnected"], method = [RequestMethod.GET])
    fun getNrOfPlayersConnected(@RequestParam table_id: String): ResponseEntity<Int?> {
        var nrOfPlayersConnected = gameTableService.getNrOfPlayersConnected(table_id)
        return ResponseEntity(nrOfPlayersConnected, HttpStatus.OK)
    }

    @RequestMapping(value = ["/getRoundCount"], method = [RequestMethod.GET])
    fun getRoundCount(@RequestParam table_id: String): ResponseEntity<Int?> {
        var roundCount = gameTableService.getRoundCount(table_id)
        return ResponseEntity(roundCount ,HttpStatus.OK)
    }

    @RequestMapping(value = ["/getPlayersReady"], method = [RequestMethod.GET])
    fun getPlayersReady(@RequestParam table_id: String): ResponseEntity<Int?> {
        var playersReady = gameTableService.getPlayersReady(table_id)
        return ResponseEntity(playersReady, HttpStatus.OK)
    }

    @RequestMapping(value = ["/decrementPlayersReady"], method = [RequestMethod.PUT])
    fun decrementPlayersReady(@RequestBody table_id: String): ResponseEntity<String?> {
        var table = gameTableService.decrementPlayersReady(table_id)
        lastModifiedDate = LocalDateTime.now()
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(value = ["/incrementPlayersReady"], method = [RequestMethod.PUT])
    fun incrementPlayersReady(@RequestBody table_id: String): ResponseEntity<String?> {
        gameTableService.incrementPlayersReady(table_id)
        lastModifiedDate = LocalDateTime.now()
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(value = ["/lobby/getReadyPlayers"], method = [RequestMethod.GET])
    fun getReadyPlayers(response: HttpServletResponse, @RequestParam table_id: String?): SseEmitter? {
        response.setHeader("Cache-Control", "no-store")
        val emitter = SseEmitter(-1L)
        currentModifiedDate = lastModifiedDate.minus(Duration.ofMillis(500))
        lastModifiedDate = LocalDateTime.now()


        this.readyPlayersEmitters.add(emitter)
        emitter.onCompletion { this.readyPlayersEmitters.remove(emitter) }
        emitter.onTimeout { this.readyPlayersEmitters.remove(emitter) }
        Thread {
            while (true) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }

                if(currentModifiedDate < lastModifiedDate) {
                    currentModifiedDate = lastModifiedDate
                    sendReadyPlayers(table_id)
                }
            }
        }.start()

        return emitter
    }

    @EventListener
    fun sendReadyPlayers(table_id: String?) {
        val deadEmitters: MutableList<SseEmitter> = ArrayList()
        this.readyPlayersEmitters.forEach(Consumer { emitter: SseEmitter ->
            try {
                val usersList = gameTableService.getPlayersReady(table_id!!)
                emitter.send(usersList)

            } catch (e: Exception) {
                deadEmitters.add(emitter)
            }
        })
        this.readyPlayersEmitters.removeAll(deadEmitters)
    }
}