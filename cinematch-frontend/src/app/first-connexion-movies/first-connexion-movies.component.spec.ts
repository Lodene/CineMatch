import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FirstConnexionMoviesComponent } from './first-connexion-movies.component';

describe('FirstConnexionMoviesComponent', () => {
  let component: FirstConnexionMoviesComponent;
  let fixture: ComponentFixture<FirstConnexionMoviesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FirstConnexionMoviesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FirstConnexionMoviesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
