import { TestBed } from '@angular/core/testing';

import { VoteQuestionService } from './vote-question.service';

describe('VoteQuestionService', () => {
  let service: VoteQuestionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VoteQuestionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
