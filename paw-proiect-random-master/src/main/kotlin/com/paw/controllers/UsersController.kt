package com.paw.controllers

import com.paw.business.interfaces.I_UsersService
import com.paw.persistence.entities.Users
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
@RequestMapping(value = ["/api/users"])
class UsersController {
    @Autowired
    private lateinit var usersService: I_UsersService
    private val playersEmitters = CopyOnWriteArrayList<SseEmitter>()
    private var maxUsers = false
    private var usersListLen = 0

    companion object {
        var lastModifiedDate = LocalDateTime.now()
        var currentModifiedDate = lastModifiedDate.minus(Duration.ofMillis(500))
    }

    @RequestMapping(value = ["/addUser"], method = [RequestMethod.PUT])
    fun addUser(@RequestBody userName: String?): ResponseEntity<String?> {
        if (userName != null) {
            var user_id = usersService.addUser(userName)

            lastModifiedDate = LocalDateTime.now()
            return ResponseEntity(user_id, HttpStatus.OK)
        }

        return ResponseEntity("Username-ul nu poate fi null", HttpStatus.BAD_REQUEST)
    }

    @RequestMapping(value = ["/getUser"], method = [RequestMethod.GET])
    fun getUserByID(@RequestParam id: String?): ResponseEntity<Optional<Users?>> {

        if (id != null) {
            var user = usersService.getUserById(id)
            return ResponseEntity(user, HttpStatus.OK)
        }

        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @RequestMapping(value = ["/getUserByString"], method = [RequestMethod.GET])
    fun getUserByString(@RequestParam userName: String, @RequestParam table_id: String): ResponseEntity<String?> {

        if (userName != null && table_id != null) {
            val user_id = usersService.getUserIdByString(userName, table_id)
            if (user_id === null) {
                return ResponseEntity(user_id, HttpStatus.NOT_FOUND)
            } else {
                return ResponseEntity(user_id, HttpStatus.OK)
            }
        }

        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @RequestMapping(value = ["/getTableId"], method = [RequestMethod.GET])
    fun getTableId(@RequestParam user_id: String?): ResponseEntity<Optional<String?>> {

        if (user_id != null) {
            val tableID = usersService.getUserTableId(user_id)
            if (tableID != null) {
                return ResponseEntity(HttpStatus.NO_CONTENT)
            }
            return ResponseEntity(tableID, HttpStatus.FOUND)
        }
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @RequestMapping(value = ["/setTableId"], method = [RequestMethod.PUT])
    fun setTableid(@RequestBody data: Map<String, String>): ResponseEntity<HttpStatus> {
        var user_id = data.get("user_id")
        var table_id = data.get("table_id")

        if (user_id != null && table_id != null) {
            usersService.setTableId(user_id, table_id)
            return ResponseEntity(HttpStatus.OK)
        }

        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @RequestMapping(value = ["/getUserVotes"], method = [RequestMethod.GET])
    fun getUserVotes(@RequestParam user_id: String?): ResponseEntity<List<Users?>?> {
        if (!user_id.isNullOrBlank()) {
            val votes = usersService.getUserVotes(user_id)
            return ResponseEntity(votes, HttpStatus.OK)
        }
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @RequestMapping(value = ["/setUserVotes"], method = [RequestMethod.PUT])
    fun setUserVotes(@RequestBody data: Map<String, String>): ResponseEntity<HttpStatus> {
        val user_id = data.get("user_id")
        val votes = data.get("votes")?.toInt()
        if (user_id != null && votes != null) {
            usersService.setVotes(user_id, votes)
            return ResponseEntity(HttpStatus.OK)
        }
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @RequestMapping(value = ["/deleteUser"], method = [RequestMethod.DELETE])
    fun deleteUserByID(@RequestParam user_id: String?): ResponseEntity<HttpStatus> {

        if (user_id != null) {
            usersService.deleteUser(user_id)
            return ResponseEntity(HttpStatus.OK)
        }
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @RequestMapping(value = ["/getLobbyUsers"], method = [RequestMethod.GET])
    fun getAllUsers(@RequestParam table_id: String): ResponseEntity<List<Users?>?> {
        if (!table_id.isNullOrBlank()) {
            val allUsers = usersService.getAllUsers(table_id)
            return ResponseEntity(allUsers, HttpStatus.OK)
        }
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @RequestMapping(value = ["/lobby/getUsers"], method = [RequestMethod.GET])
    fun getUsers(response: HttpServletResponse, @RequestParam table_id: String?): SseEmitter? {
        response.setHeader("Cache-Control", "no-store")
        val emitter = SseEmitter(-1L)
        maxUsers = false
        lastModifiedDate = LocalDateTime.now()
        currentModifiedDate = lastModifiedDate.minus(Duration.ofMillis(500))

        this.playersEmitters.add(emitter)
        emitter.onCompletion { this.playersEmitters.remove(emitter) }
        emitter.onTimeout { this.playersEmitters.remove(emitter) }
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
                    sendUsers(table_id)
                }
            }
        }.start()
        if (maxUsers) {
            emitter.complete()
        }
        return emitter
    }

    @EventListener
    fun sendUsers(table_id: String?) {
        val deadEmitters: MutableList<SseEmitter> = ArrayList()
        this.playersEmitters.forEach(Consumer { emitter: SseEmitter ->
            try {
                val usersList = usersService.getAllUsers(table_id!!)
                emitter.send(usersList)
                usersListLen = usersList.size
                if (usersListLen == 6) maxUsers = true
            } catch (e: Exception) {
                deadEmitters.add(emitter)
            }
        })
        this.playersEmitters.removeAll(deadEmitters)
    }
}