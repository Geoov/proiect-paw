import {Component, NgZone, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {VoteQuestionService} from '../vote-question.service';
import {relativeToRootDirs} from '@angular/compiler-cli/src/transformers/util';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-vote-question',
  templateUrl: './vote-question.component.html',
  styleUrls: ['./vote-question.component.css']
})
export class VoteQuestionComponent implements OnInit, OnDestroy {
  gameCode;
  userData;
  color;
  hover1: boolean;
  hover2: boolean;
  voted1: boolean;
  voted2: boolean;
  idRound;
  roundCount;
  nextRoundCount;
  nextRound;
  answerUser1;
  answerUser2;
  idUser1;
  idUser2;
  votes; // cate voturi sunt, asta e in timp real
  votesCount; // la cate trebuie sa ajungem
  question;
  getPlayersVotes: EventSource;

  constructor(private router: Router, private voteQuestionService: VoteQuestionService, private route: ActivatedRoute,
              private ngZone: NgZone) {
    this.roundCount = this.router.getCurrentNavigation().extras.state.roundCount;
    console.log('current interation ' + this.roundCount);
  }

  ngOnInit(): void {
    this.route.params.subscribe((params: Params) => {
      this.gameCode = params.gameCode;

      this.assignData();
    });
  }

  // tslint:disable-next-line:typedef
  assignData() {
    this.userData = JSON.parse(localStorage.getItem('myData'));
    this.color = this.userData.color;
    this.hover1 = false;
    this.hover2 = false;
    this.voted1 = false;
    this.voted2 = false;
    this.votes = 0;

    this.voteQuestionService.getRoundDetails(this.gameCode).subscribe((roundDetails: any[]) => {

      // tslint:disable-next-line:prefer-for-of
      for (let i = 0; i < roundDetails.length; i++) {

        if (this.roundCount + 2 < roundDetails.length * 2)
        {
          this.nextRoundCount = this.roundCount + 2; // trecem la urmatoarea runda
        } else {
          this.nextRoundCount = 100; // final de joc
        }

        if (this.nextRoundCount !== 100) {
          // tslint:disable-next-line:no-unused-expression
          (this.nextRoundCount === roundDetails[i].contor) ? this.nextRound = roundDetails[i].id : '';
        } else {
          this.nextRound = 'finish';
        }

        if (roundDetails[i].contor === this.roundCount)
        {
          this.idRound = roundDetails[i].id;
          this.answerUser1 = roundDetails[i].answerUser1;
          this.idUser1 = roundDetails[i].idUser1;
          this.answerUser2 = roundDetails[i].answerUser2;
          this.idUser2 = roundDetails[i].idUser2;

          this.voteQuestionService.getQuestion(roundDetails[i].idQuestion).subscribe(question => this.question = question['text']);

        }

        this.votesCount = roundDetails.length * 2 - 2;

      }

      console.log(this.idRound);
      console.log('nextRound: ' + this.nextRound);
      console.log('nextRoundCount: ' + this.nextRoundCount);

      this.getPlayersVotes = new EventSource('http://localhost:8080/api/round/vote/getVoteCount?round_id=' + this.idRound);
      this.getPlayersVotes.addEventListener('message', (event) => {
        const votes = JSON.parse(event.data);
        this.votes = parseInt(votes.toString(), 10);
        // currentVotes = parseInt(votes.toString(), 10);
        // console.log('votes: ' + currentVotes);

        if (this.votesCount === this.votes) // 4 == 4 la 6 jucatori
        {

          this.voteQuestionService.checkVotes(this.idRound).subscribe(voteCount => {
              console.log(voteCount);
              console.log('votes: ' + this.votes);

              if (voteCount === this.votesCount) {

                if (this.nextRound !== 'finish') {
                  this.router.routeReuseStrategy.shouldReuseRoute = () => false;
                  // this.router.onSameUrlNavigation = 'reload';
                  console.log(this.votesCount);

                  setTimeout(() => {
                    this.ngZone.run(() => this.router.navigate(['/vote', this.gameCode, this.nextRound],
                      {state: {roundCount: parseInt(this.nextRoundCount, 10)}}));
                  }, 100);

                } else {
                  this.ngZone.run(() => this.router.navigate(['/leaderboard', this.gameCode]));
                }
              }
          });

        }
      });
    });

  }

  enter1(): void {
    this.hover1 = true;
  }

  enter2(): void {
    this.hover2 = true;
  }

  leave1(): void {
    this.hover1 = false;
  }

  leave2(): void {
    this.hover2 = false;
  }

  // tslint:disable-next-line:typedef
  voteQuestion1() {
    if (this.idUser1 !== this.userData.id && this.idUser2 !== this.userData.id) {
      this.voted1 = true;
      this.voteQuestionService.voteQuestion(this.idRound, this.voted1, this.voted2).subscribe();
    }
  }

  // tslint:disable-next-line:typedef
  voteQuestion2() {
    if (this.idUser1 !== this.userData.id && this.idUser2 !== this.userData.id) {
      this.voted2 = true;
      this.voteQuestionService.voteQuestion(this.idRound, this.voted1, this.voted2).subscribe();
    }
  }

  // tslint:disable-next-line:typedef
  ngOnDestroy() {
    this.getPlayersVotes.close();
  }
}
