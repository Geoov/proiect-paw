import {Component, HostListener, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {User} from '../user';
import {LobbyService} from '../lobby.service';
import { EventEmitter } from '@angular/core';
import {Subscription} from 'rxjs';
import {NavigationStart, Router} from '@angular/router';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  @Input() user: User;
  @Input() gameCode;
  userData;
  ready;
  // @Output() canStartGame = new EventEmitter();
  // private subscription: Subscription;

  constructor(private lobbyService: LobbyService, public router: Router) {
  }

  ngOnInit(): void {
    this.userData = JSON.parse(localStorage.getItem('myData'));
    this.ready = false;
  }

  // tslint:disable-next-line:typedef
  @HostListener('window:beforeunload', ['$event']) unloadHandler(event: Event) {
    console.log('Processing before unload...');
    // Do more processing...
    this.lobbyService.decrementPlayersReady(this.gameCode).subscribe();
  }

  // tslint:disable-next-line:typedef
  onClick() {
    if (this.userData.id === this.user.id)
    {
      this.ready = !this.ready;
      if (this.ready === true) {
        this.lobbyService.incrementPlayersReady(this.gameCode).subscribe(_ => {
          // this.lobbyService.getReadyPlayers(this.gameCode).subscribe(readyPlayers => {
          //   if (parseInt(readyPlayers, 10) === 2)
          //   {
          //     this.canStartGame.emit(1);
          //   } else {
          //     this.canStartGame.emit(2);
          //   }
          // });
        });
      } else {
        this.lobbyService.decrementPlayersReady(this.gameCode).subscribe();
      }
    }
  }

}
