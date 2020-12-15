package com.paw.business.interfaces

import com.paw.persistence.entities.Round
import java.util.*

interface I_RoundService {
    fun createRound(idGameTable: String?, idQuestion: Int?, idUser1: String?, idUser2: String?, contor: Int?)
    fun getRoundById(round_id: String?): Optional<Round?>
    fun getRounds(table_id: String?): List<Round?> //returneaza toate rundele de la o masa de joc
    fun updateVotes(round_id: String?, votesUser1: Int?, votesUser2: Int?)                    //in functie de cum au fost votati playerii facem update in DB
    fun updateAnswer(round_id: String?, answerUser1: String?, answerUser2: String?)
    //
//    TODO: La urmatoarea clasa va fi adaugat un parametru dataClass in care vor fi stocate informatiile
//    TODO: primite de la frontEnd ([4] in SchitaPAW.txt)
    fun updateRound()                    //populam coloanele de voturi si raspunsuri

    fun getStatistic(table_id: String)   //TODO: aici iar facem un dataClass in care adaugam detaliile din JSON-ul de la final [SchitaPAW.txt]
    fun getAnswersCount(table_id: String?): Int?
    fun getVotesCount(round_id: String?) : Int?
}