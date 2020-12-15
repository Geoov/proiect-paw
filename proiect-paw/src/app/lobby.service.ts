import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class LobbyService{
  private readyState = 0;

  constructor(private http: HttpClient) { }

  getLobbyUsers = gameCode => this.http.get('http://localhost:8080/api/users/getLobbyUsers?' + 'table_id=' + gameCode);

  incrementPlayersReady = (gameCode) => {
    return this.http.put('http://localhost:8080/api/gameTable/incrementPlayersReady', gameCode);
  }

  decrementPlayersReady = (gameCode) => {
    return this.http.put('http://localhost:8080/api/gameTable/decrementPlayersReady', gameCode);
  }

  getReadyPlayers = (gameCode) => {
    return this.http.get('http://localhost:8080/api/gameTable/getPlayersReady?table_id=' + gameCode,
      {responseType: 'text'});
  }

  getAllPlayers = (gameCode) => {
    return this.http.get('http://localhost:8080/api/gameTable/getNrOfPlayersConnected?table_id=' + gameCode,
      {responseType: 'text'});
  }

  createRound = (gameCode, users) => {
    return this.http.put('http://localhost:8080/api/round/createRound', {gameCode, users});
  }

}
