import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class VoteQuestionService {

  constructor(private http: HttpClient) { }

  getRoundDetails = (gameCode) => {
    return this.http.get('http://localhost:8080/api/round/getRoundsByTableId?idGameTable=' + gameCode);
  }

  voteQuestion = (idRound, user1, user2) => {
    if (user1)
    {
      return this.http.put('http://localhost:8080/api/round/setVotes', {idRound, votes_user_1: 1});
    } else {
      return this.http.put('http://localhost:8080/api/round/setVotes', {idRound, votes_user_2: 1});
    }
  }

  getQuestion = (questionId) => {
    return this.http.get('http://localhost:8080/api/getQuestion?id=' + questionId);
  }

  checkVotes = (idRound) => {
    return this.http.get('http://localhost:8080/api/round/vote/checkVotes?round_id=' + idRound);
  }
}
