package com.paw.business.services

import com.paw.business.interfaces.I_RoundService
import com.paw.persistence.entities.GameTable
import com.paw.persistence.entities.Round
import com.paw.persistence.repositories.RoundRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class RoundService: I_RoundService {
    @Autowired
    private lateinit var roundRepository: RoundRepository

    override fun createRound(idGameTable: String?, idQuestion: Int?, idUser1: String?, idUser2: String?, contor: Int?) {
        //TODO DE TINUT CONT CA IdQ, IdU1, IdU2 trebuie sa fie generate in mod aleator ori aici ori in controller
        var round = Round(idGameTable, idQuestion, idUser1, idUser2, contor)
        roundRepository.save(round)
    }

    override fun getRoundById(round_id: String?): Optional<Round?> {
        return roundRepository.findById(round_id)
    }

    override fun getRounds(table_id: String?): List<Round?> {
        return roundRepository.getRoundsByTableId(table_id)
    }

    override fun updateVotes(round_id: String?, votesUser1: Int?, votesUser2: Int?) {
        val round = roundRepository.findById(round_id)
        if(round.isPresent){
            if(votesUser1 != null) {
                var votesUser = round.get().votesUser1?.plus(1)
                if (votesUser == null)
                    votesUser = 0
                roundRepository.setVotesUser1(round_id, votesUser)
            } else {
                var votesUser = round.get().votesUser2?.plus(1)

                if (votesUser == null)
                    votesUser = 0
                roundRepository.setVotesUser2(round_id, votesUser)
            }
        }

    }

    override fun updateAnswer(round_id: String?, answerUser1: String?, answerUser2: String?) {
        if(answerUser1 != null) {
            roundRepository.setAnswerUser1(round_id, answerUser1)
        } else {
            roundRepository.setAnswerUser2(round_id, answerUser2)
        }
    }

    override fun getAnswersCount(table_id: String?): Int? {
        return roundRepository.getAnswersCount(table_id)
    }

    override fun getVotesCount(round_id: String?) : Int? {
        return roundRepository.getVotesCount(round_id)
    }

    override fun updateRound() {
        TODO("Not yet implemented")
    }

    override fun getStatistic(table_id: String) {
        TODO("Not yet implemented")
//        TODO(List with all the users and votes on their answers)
    }
}