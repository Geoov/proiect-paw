import {Component, OnDestroy, OnInit} from '@angular/core';
import {User} from '../user';
import {LobbyService} from '../lobby.service';
import {ActivatedRoute, Params, Router} from '@angular/router';

@Component({
  selector: 'app-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.css']
})
export class LobbyComponent implements OnInit, OnDestroy {
  users: User[] = [];
  alreadyInLobby = [];
  gameCode;
  color = '';
  readyPlayers: Notification;

  private getPlayersSource: any;
  private getReadyPlayersSource: any;
  constructor(private lobbyService: LobbyService, private route: ActivatedRoute, private router: Router) {
  }

  ngOnInit(): void {
    this.route.params.subscribe((params: Params) => this.gameCode = params.gameCode);
    this.connect();
    this.color = JSON.parse(localStorage.getItem('myData')).color;
  }

  connect(): void {
    this.alreadyInLobby = [];
    this.getPlayersSource = new EventSource('http://localhost:8080/api/users/lobby/getUsers?table_id=' + this.gameCode);
    this.getReadyPlayersSource = new EventSource('http://localhost:8080/api/gameTable/lobby/getReadyPlayers?table_id=' + this.gameCode);

    this.getPlayersSource.addEventListener('message', (event) => {
      let jsonUsers: Notification;
      jsonUsers = JSON.parse(event.data);
      // @ts-ignore
      // tslint:disable-next-line:prefer-for-of
      for (let i = 0; i < jsonUsers.length; i++) {
        const tempUser = [];

        // tslint:disable-next-line:forin
        for (const prop in jsonUsers[i]) {
          tempUser['id'] = jsonUsers[i].id;
          tempUser['name'] = jsonUsers[i].name;
          tempUser['score'] = jsonUsers[i].score;
          tempUser['color'] = jsonUsers[i].color;
        }
        const oldData = JSON.parse(localStorage.getItem('myData'));

        if (oldData.id === jsonUsers[i].id) {
          const newData = {
            'id': oldData.id,
            'name': oldData.name,
            'color': jsonUsers[i].color,
            'imHost': oldData.imHost
          };

          localStorage.setItem('myData', JSON.stringify(newData));

        }

        // @ts-ignore
        if (!this.alreadyInLobby.includes(tempUser['id'])) {
          // @ts-ignore
          this.users.push(tempUser);
          this.alreadyInLobby.push(tempUser['id']);
        }
      }
    });

    this.getReadyPlayersSource.addEventListener('message', (event) => {
      this.readyPlayers = JSON.parse(event.data);
      const currentReadyPlayers = parseInt(this.readyPlayers.toString(), 10);
      if (currentReadyPlayers === this.alreadyInLobby.length && this.alreadyInLobby.length % 2 === 0 && this.alreadyInLobby.length >= 4) {
        if (JSON.parse(localStorage.getItem('myData')).imHost === 'yes') {
          this.lobbyService.createRound(this.gameCode, this.alreadyInLobby).subscribe();
        }
        setTimeout( () => { this.router.navigate(['/answer', this.gameCode]); }, 2000);
      }
    });
  }

  // tslint:disable-next-line:typedef
  ngOnDestroy(){
    this.getPlayersSource.close();
    this.getReadyPlayersSource.close();
  }

}
