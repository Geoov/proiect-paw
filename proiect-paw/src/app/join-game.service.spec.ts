import { TestBed } from '@angular/core/testing';

import { JoinGameService } from './join-game.service';

describe('JoinGameService', () => {
  let service: JoinGameService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(JoinGameService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
