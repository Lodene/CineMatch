import { Component, OnInit, AfterViewInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { ToastrService } from 'ngx-toastr';
import { tap } from 'rxjs';
import { Movie } from '../../models/movie';
import { PaginatedMovieResponse } from '../../models/paginated-movie-reponse';
import { MovieService } from '../../services/movie/movie.service';
import { LoaderService } from '../../services/loader/loader.service';
import { CommonModule } from '@angular/common';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MovieCardComponent } from '../common-component/movie-card/movie-card.component';
import { TranslatePipe } from '@ngx-translate/core';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-first-connexion-movies',
  standalone: true,
  imports: [CommonModule, MatPaginatorModule, MovieCardComponent, TranslatePipe, MatButtonModule],
  templateUrl: './first-connexion-movies.component.html',
  styleUrl: './first-connexion-movies.component.scss'
})
export class FirstConnexionMoviesComponent implements OnInit, AfterViewInit {

  movies: Movie[] = [];
  selectedMovies: Set<number> = new Set();
  totalElements = 0;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private movieService: MovieService,
    private loaderService: LoaderService,
    private toastr: ToastrService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadMovies();
    this.movieService.getMovieCount().subscribe({
      next: (count) => this.totalElements = count,
      error: (err) => console.error(err)
    });
  }

  ngAfterViewInit(): void {
    this.paginator.page.pipe(
      tap(() => {
        this.loadMovies(this.paginator.pageIndex, this.paginator.pageSize);
      })
    ).subscribe();
  }

  loadMovies(page: number = 0, size: number = 10): void {
    this.loaderService.show();
    this.movieService.getAllMovies(page, size).subscribe({
      next: (res: PaginatedMovieResponse) => this.movies = res.content,
      error: (err) => console.error(err),
      complete: () => this.loaderService.hide()
    });
  }

  toggleSelection(id: number): void {
    if (this.selectedMovies.has(id)) {
      this.selectedMovies.delete(id);
    } else {
      this.selectedMovies.add(id);
    }
  }

  validateSelection(): void {
    if (this.selectedMovies.size === 0) {
      this.toastr.warning("Veuillez sélectionner au moins un film.");
      return;
    }

    const selectedIds = Array.from(this.selectedMovies);

    this.loaderService.show();
    this.movieService.likeMultipleMovies(selectedIds).subscribe({
      next: () => {
        this.toastr.success("Merci pour votre sélection !");
        this.router.navigate(['/']);
      },
      error: (err) => {
        console.error(err);
        this.toastr.error("Une erreur est survenue lors de l'enregistrement.");
      },
      complete: () => {
        this.loaderService.hide();
      }
    });
  }



  isSelected(id: number): boolean {
    return this.selectedMovies.has(id);
  }
}
