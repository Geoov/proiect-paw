import { TestBed } from '@angular/core/testing';

import { AnswerQuestionService } from './answer-question.service';

describe('AnswerQuestionService', () => {
  let service: AnswerQuestionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AnswerQuestionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
