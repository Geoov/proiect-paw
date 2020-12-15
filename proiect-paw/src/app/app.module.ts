import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { JoinGameComponent } from './join-game/join-game.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import {ReactiveFormsModule} from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LobbyComponent } from './lobby/lobby.component';
import { UserComponent } from './user/user.component';
import { AnswerQuestionComponent } from './answer-question/answer-question.component';
import {MatIconModule} from '@angular/material/icon';
import { VoteQuestionComponent } from './vote-question/vote-question.component';
import { ShowVotesComponent } from './show-votes/show-votes.component';
import { LeaderboardComponent } from './leaderboard/leaderboard.component';
import {HttpClientModule} from '@angular/common/http';
import {IncrementPipe} from './leaderboard/increment.pipe';

@NgModule({
  declarations: [
    AppComponent,
    JoinGameComponent,
    LobbyComponent,
    UserComponent,
    AnswerQuestionComponent,
    VoteQuestionComponent,
    ShowVotesComponent,
    LeaderboardComponent,
    IncrementPipe
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
