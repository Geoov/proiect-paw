import {Component, OnInit} from '@angular/core';
import {LobbyService} from '../lobby.service';
import {User} from '../user';
import {ActivatedRoute, Params} from '@angular/router';
import {AnswerQuestionService} from '../answer-question.service';
import {IncrementPipe} from './increment.pipe';
import {delay} from 'rxjs/operators';

@Component({
  selector: 'app-leaderboard',
  templateUrl: './leaderboard.component.html',
  styleUrls: ['./leaderboard.component.css']
})
export class LeaderboardComponent implements OnInit {
  gameCode;
  counter;
  users: User[] = [];
  displayUser: User[] = [];

  constructor(private lobbyService: LobbyService, private answerQuestionService: AnswerQuestionService, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.route.params.subscribe((params: Params) => this.gameCode = params.gameCode);
    this.answerQuestionService.getRoundDetails(this.gameCode).subscribe((roundDetails: any[]) => {
      // @ts-ignore
      this.lobbyService.getLobbyUsers(this.gameCode).subscribe((user: any[]) => {
        // tslint:disable-next-line:prefer-const
        // tslint:disable-next-line:prefer-for-of
        for (let i = 0; i < user.length; i++) {
          const tempUser = [];

          // tslint:disable-next-line:forin
          for (const prop in user[i]) {
            tempUser['id'] = user[i].id;
            tempUser['name'] = user[i].name;
            tempUser['color'] = user[i].color;


            // tslint:disable-next-line:prefer-for-of
            for (let j = 0; j < roundDetails.length; j++) {

              if (roundDetails[j].idUser1 === user[i].id) {
                tempUser['score'] = roundDetails[j].votesUser1;
                // break;
              }

              if (roundDetails[j].idUser2 === user[i].id) {
                tempUser['score'] = roundDetails[j].votesUser2;
                // break;
              }
            }

          }
          // @ts-ignore
          this.users.push(tempUser);
        }
        this.users.sort((a, b) => (a.score < b.score) ? 1 : ((b.score < a.score) ? -1 : 0));

        for (let i = this.users.length - 1; i >= 0; i--){
          const u = this.users[i];
          setTimeout(() => {
            this.displayUser.push(u);
          }, 2000 * ( i + 1));
        }

      });
    });

  }


}
