package com.paw.business.services

import com.paw.business.interfaces.I_UsersService
import com.paw.persistence.entities.Users
import com.paw.persistence.repositories.UsersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UsersService: I_UsersService {
    @Autowired
    private lateinit var usersRepository: UsersRepository

    override fun addUser(name: String): String? {
        var user = Users(name)
        usersRepository.save(user)
        return user.id
    }

    override fun getUserById(id: String): Optional<Users?> {
        val user = usersRepository.findById(id)
        return user
    }

    override fun getUserIdByString(name: String, table_id: String) : String {
        val user = usersRepository.findByString(name, table_id).toString()
        return user
    }

    override fun getUserTableId(id_user: String): String? {
        var user = usersRepository.getIdGameTableByUserID(id_user)

        if(user.isPresent){
            return user.get().idGameTable
        }
        return null
    }

    override fun setTableId(user_id: String, table_id: String) {
        val user = usersRepository.findById(user_id)

        if(user.isPresent){
            val updatedUser = Users(user.get().id!!,user.get().name,table_id)
            usersRepository.save(updatedUser)
        }
    }

    override fun getUserVotes(table_id: String): List<Users>? {
        return usersRepository.getUserVotes(table_id)

    }

    override fun deleteUser(id_user: String) {  //verifica din controller sa nu fie null
        usersRepository.deleteUserById(id_user)
    }

    override fun deleteAllUser(id_table: String) {
        usersRepository.deleteByGameTableId(id_table)
    }

    override fun setVotes(id_user: String?, votes: Int) {
        usersRepository.setVotesById(id_user,votes)
    }

    override fun getAllUsers(table_id: String) : List<Users?> {
        return usersRepository.getUsers(table_id)
    }
}