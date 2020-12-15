package com.paw.business.interfaces

import com.paw.persistence.entities.Users
import java.util.*
import javax.swing.text.html.Option

interface I_UsersService {
    fun addUser(name: String) : String?             //se apeleaza doar cu un nume, restul se trateaza in serviciu
    fun getUserById(id: String) : Optional<Users?>         //returneaza toate detaliile pt un user
    fun getUserTableId(id_user: String): String? //returneaza id-ul mesei unui user
    fun setTableId(user_id: String, table_id: String)
    fun getUserVotes(table_id: String): List<Users>?      //returneaza toti userii de la o masa cu voturile
    fun deleteUser(id_user: String)             //la final probabil o sa apelam asta pt toti userii
    fun deleteAllUser(id_table: String)      //sterge toti userii de la o masa
    fun setVotes(id_user: String?, votes: Int)          //update la numarul de voturi
    fun getUserIdByString(name: String, table_id: String) : String?
    fun getAllUsers(table_id: String) : List<Users?>
}
