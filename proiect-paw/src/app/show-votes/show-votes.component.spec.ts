import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowVotesComponent } from './show-votes.component';

describe('ShowVotesComponent', () => {
  let component: ShowVotesComponent;
  let fixture: ComponentFixture<ShowVotesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowVotesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowVotesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
