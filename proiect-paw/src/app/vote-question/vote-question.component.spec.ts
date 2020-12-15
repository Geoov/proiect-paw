import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VoteQuestionComponent } from './vote-question.component';

describe('VoteQuestionComponent', () => {
  let component: VoteQuestionComponent;
  let fixture: ComponentFixture<VoteQuestionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VoteQuestionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VoteQuestionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
