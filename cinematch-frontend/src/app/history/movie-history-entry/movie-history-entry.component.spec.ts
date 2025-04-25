import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovieHistoryEntryComponent } from './movie-history-entry.component';

describe('MovieHistoryEntryComponent', () => {
  let component: MovieHistoryEntryComponent;
  let fixture: ComponentFixture<MovieHistoryEntryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MovieHistoryEntryComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MovieHistoryEntryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
