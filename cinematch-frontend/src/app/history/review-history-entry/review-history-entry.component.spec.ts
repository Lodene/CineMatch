import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewHistoryEntryComponent } from './review-history-entry.component';

describe('ReviewHistoryEntryComponent', () => {
  let component: ReviewHistoryEntryComponent;
  let fixture: ComponentFixture<ReviewHistoryEntryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReviewHistoryEntryComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReviewHistoryEntryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
