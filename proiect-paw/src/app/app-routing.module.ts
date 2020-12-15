import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {JoinGameComponent} from './join-game/join-game.component';
import {LobbyComponent} from './lobby/lobby.component';
import {AnswerQuestionComponent} from './answer-question/answer-question.component';
import {VoteQuestionComponent} from './vote-question/vote-question.component';
import {ShowVotesComponent} from './show-votes/show-votes.component';
import {LeaderboardComponent} from './leaderboard/leaderboard.component';

const routes: Routes = [
  { path: '', component: JoinGameComponent },
  { path: 'lobby/:gameCode', component: LobbyComponent },
  { path: 'answer/:gameCode', component: AnswerQuestionComponent},
  { path: 'vote/:gameCode/:roundName', component: VoteQuestionComponent},
  { path: 'show-votes/:gameCode', component: ShowVotesComponent},
  { path: 'leaderboard/:gameCode', component: LeaderboardComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
