import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {JoinGameService} from '../join-game.service';
import {toInteger} from '@ng-bootstrap/ng-bootstrap/util/util';

@Component({
  selector: 'app-join-game',
  templateUrl: './join-game.component.html',
  styleUrls: ['./join-game.component.css']
})
export class JoinGameComponent implements OnInit {

  constructor(private router: Router, private joinGameService: JoinGameService) {
  }
  joinGameForm: FormGroup;
  buttonType: any;

  ngOnInit(): void {
    this.joinGameForm = new FormGroup({
      nickname: new FormControl('', [
          Validators.required,
          Validators.minLength(4),
          Validators.pattern('^[A-Za-zñÑáéíóúÁÉÍÓÚ ]+$')
      ]),
      code: new FormControl('', [
        Validators.pattern('^[A-Za-z0-9]+$'),
        Validators.maxLength(6)
      ])
    });
  }

  onSubmit(buttonType): void {
    let nickname = this.joinGameForm.value.nickname;
    const gameJoinCode = this.joinGameForm.value.code;
    if (buttonType === 'join')
    {
      this.joinGameService.getGameTable(gameJoinCode).subscribe(table => {
        // @ts-ignore
        if (table.id) {
          this.joinGameService.getUserByString(nickname, gameJoinCode).subscribe(res => {
            // tslint:disable-next-line:triple-equals
            if (res != 'null') {
              // tslint:disable-next-line:variable-name
              let initial_number = nickname.replace(/^\D+/g, '');
              initial_number = initial_number.substring(0, 1);
              if (initial_number) {
                // tslint:disable-next-line:radix variable-name
                let number = parseInt(initial_number);
                number = number + 1;
                nickname = nickname.replace(initial_number, number);
              } else {
                nickname = nickname + '(1)';
              }
            }
            this.joinGameService.addUser(nickname).subscribe(userId => {
              this.joinGameService.setTableId(userId, gameJoinCode).subscribe(_ => {
                // tslint:disable-next-line:no-shadowed-variable
                this.joinGameService.setVotesById(userId, 0).subscribe(_ => {
                  localStorage.clear();
                  this.joinGameService.incrementPlayer(gameJoinCode).subscribe( _ => {
                    let userData = {'id': userId, 'name': nickname, 'imHost': 0};
                    localStorage.setItem('myData', JSON.stringify(userData));
                    this.router.navigate(['/lobby', gameJoinCode]);
                  });
                });
              });
            });
          });
        }
      });
    }
    if (buttonType === 'create') {
      this.joinGameService.createGame()
        .subscribe(gameCode => {
          this.joinGameService.addUser(nickname)
            .subscribe(userId => {
              this.joinGameService.setTableId(userId, gameCode).subscribe(_ => {
                // tslint:disable-next-line:no-shadowed-variable
                this.joinGameService.addHostId(userId, gameCode).subscribe(_ => {
                  // tslint:disable-next-line:no-shadowed-variable
                  this.joinGameService.setVotesById(userId, 0).subscribe(_ => {
                    localStorage.clear();
                    this.joinGameService.incrementPlayer(gameCode).subscribe( _ => {
                      this.joinGameService.setReadyPlayersToZero(gameCode).subscribe(_ => {
                        let userData = {'id': userId, 'name': nickname, 'imHost': 'yes'};
                        localStorage.setItem('myData', JSON.stringify(userData));
                        this.router.navigate(['/lobby', gameCode]);
                      });
                    });
                  });
                });
              });
            });
        });
    }
  }
}
