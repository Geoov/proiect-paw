import {Component, OnInit} from '@angular/core';
import {timeout} from 'rxjs/operators';

@Component({
  selector: 'app-show-votes',
  templateUrl: './show-votes.component.html',
  styleUrls: ['./show-votes.component.css']
})
export class ShowVotesComponent implements OnInit {
  counter = 5;
  color1 = 'red';
  color2 = 'blue';
  votes1 = 5;
  votes2 = 4;
  player1: boolean;
  player2: boolean;

  constructor() {
    const intervalId = setInterval(() => {
      this.counter = this.counter - 1;
      if (this.counter === 0) {
        this.votes1 > this.votes2 ? this.player1 = true : this.player2 = false;
        // this.votes1 == this.votes2 ? this.player1 = true : this.player2 = true;
        clearInterval(intervalId);
      }
    }, 1500);
  }

  ngOnInit(): void {
    this.player1 = false;
    this.player2 = false;
  }

}
