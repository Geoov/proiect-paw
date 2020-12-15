import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {AnswerQuestionService} from '../answer-question.service';

@Component({
  selector: 'app-answer-question',
  templateUrl: './answer-question.component.html',
  styleUrls: ['./answer-question.component.css']
})
export class AnswerQuestionComponent implements OnInit {
  gameCode: string;
  userId: string;
  question: string;
  roundId: string;
  user1: boolean;
  user2: boolean;
  isDisabled: boolean;
  firstRound: string;
  rounds = 0;
  answers: number;

  constructor(private route: ActivatedRoute, private answerService: AnswerQuestionService, private router: Router) { }

  ngOnInit(): void {
    this.route.params.subscribe((params: Params) => this.gameCode = params.gameCode);
    this.user1 = false;
    this.user2 = false;
    this.isDisabled = false;
    this.assignQuestions();

    const getPlayersAmwer = new EventSource('http://localhost:8080/api/round/answer/getAnswerCount?table_id=' + this.gameCode);
    getPlayersAmwer.addEventListener('message', (event) => {
      const answers = JSON.parse(event.data);
      this.answers = parseInt(answers.toString(), 10);

      if ((this.rounds * 2) === this.answers)
      {
        setTimeout( () => { this.router.navigate(['/vote', this.gameCode, this.firstRound], {state: {roundCount: 0}}); }, 1500);
      }

    });
  }

  // tslint:disable-next-line:typedef
  assignQuestions() {
    this.answerService.getRoundDetails(this.gameCode).subscribe((roundDetails: any[]) => {
      this.rounds = roundDetails.length;
      // tslint:disable-next-line:prefer-for-of
      for (let i = 0; i < roundDetails.length; i++) {

        if (roundDetails[i].contor === 0)
        {
          this.firstRound = roundDetails[i].id;
        }

        this.userId = JSON.parse(localStorage.getItem('myData')).id;

        if (this.userId === roundDetails[i].idUser1 || this.userId === roundDetails[i].idUser2)
        {
          (this.userId === roundDetails[i].idUser1) ? this.user1 = true : this.user2 = true;
          this.roundId = roundDetails[i].id;
          this.answerService.getQuestion(roundDetails[i].idQuestion).subscribe(question => this.question = question['text']);
        }
      }

    });
  }

  // tslint:disable-next-line:typedef
  answerQuestion(answer) {
    // this.answerService.answerQuestion
    this.isDisabled = true;
    this.answerService.answerQuestion(this.roundId, this.user1, this.user2, answer).subscribe();
  }
}
