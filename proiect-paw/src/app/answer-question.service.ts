import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';

const headers = new HttpHeaders().append('header', 'value');

@Injectable({
  providedIn: 'root'
})
export class AnswerQuestionService {

  constructor(private http: HttpClient) { }

  getRoundDetails = (gameCode) => {
    return this.http.get('http://localhost:8080/api/round/getRoundsByTableId?idGameTable=' + gameCode);
  }

  getQuestion = (questionId) => {
    return this.http.get('http://localhost:8080/api/getQuestion?id=' + questionId);
  }

  answerQuestion = (idRound, user1, user2, answer) => {
    if (user1)
    {
      return this.http.put('http://localhost:8080/api/round/setAnswer', {idRound, answer_user_1: answer});
    } else {
      return this.http.put('http://localhost:8080/api/round/setAnswer', {idRound, answer_user_2: answer});
    }
  }

  getAnswersCount = (gameCode) => {
    return this.http.get('http://localhost:8080/api/round/getAnswersCount?idGameTable=' + gameCode);
  }

}
