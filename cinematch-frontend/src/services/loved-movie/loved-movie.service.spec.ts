import { TestBed } from '@angular/core/testing';

import { LovedMovieService } from './loved-movie.service';

describe('LovedMovieService', () => {
  let service: LovedMovieService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LovedMovieService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
