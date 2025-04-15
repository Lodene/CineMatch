import { Component, OnInit } from '@angular/core';
import { LoaderService } from '../../../services/loader/loader.service';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-loader',
  imports: [MatProgressSpinnerModule, CommonModule],
  templateUrl: './loader.component.html',
  styleUrl: './loader.component.scss'
})
export class LoaderComponent implements OnInit {
  isLoading = false;

  constructor(private loaderService: LoaderService) {}

  ngOnInit(): void {
    this.loaderService.loading$.subscribe((state) => {
      this.isLoading = state;
    });
  }

}
