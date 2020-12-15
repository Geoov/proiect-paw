import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import {catchError, map, retry} from 'rxjs/operators';
import {isError} from 'util';

const httpOptionsPlain = {
  headers: new HttpHeaders({
    Accept: 'text/plain',
    'Content-Type': 'text/plain'
  }),
  responseType: 'text' as 'text'
};

@Injectable({
  providedIn: 'root'
})
export class JoinGameService {
  constructor(private http: HttpClient) { }

  // tslint:disable-next-line:typedef
  createGame() {
    // @ts-ignore
    return this.http.put<string>('http://localhost:8080/api/gameTable/createGameTable', '', {responseType: 'text'});
  }

  // tslint:disable-next-line:typedef
  addUser(nickname) {
    return this.http.put('http://localhost:8080/api/users/addUser', nickname, {responseType: 'text'});
  }

  // tslint:disable-next-line:typedef variable-name
  setTableId(userId, gameCode) {
    return this.http.put('http://localhost:8080/api/users/setTableId', {user_id: userId, table_id: gameCode});
  }

  // tslint:disable-next-line:typedef variable-name
  addHostId(userId, gameCode) {
    return this.http.put('http://localhost:8080/api/gameTable/addHostId', {host_id: userId, table_id: gameCode});
  }

  // tslint:disable-next-line:typedef variable-name
  incrementPlayer(gameCode) {
    return this.http.put('http://localhost:8080/api/gameTable/incrementPlayersNr', gameCode);
  }

  // tslint:disable-next-line:typedef
  getUserByString(nickname, gameCode) {
    return this.http.get('http://localhost:8080/api/users/getUserByString?' + 'userName=' + nickname + '&table_id=' + gameCode,
      {responseType: 'text'});
  }

  // tslint:disable-next-line:typedef
  setVotesById(nickname, votes) {
    return this.http.put('http://localhost:8080/api/users/setUserVotes', {user_id: nickname, votes: votes});
  }

  // tslint:disable-next-line:typedef
  getGameTable(gameCode) {
    return this.http.get('http://localhost:8080/api/gameTable/getGameTable?' + 'id=' + gameCode);
  }

  // tslint:disable-next-line:typedef
  setReadyPlayersToZero(gameCode) {
    return this.http.put('http://localhost:8080/api/gameTable/incrementPlayersReady', gameCode);
  }
}
