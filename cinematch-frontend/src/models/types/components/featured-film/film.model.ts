
export interface Film {
    id: number;
    title: string;
    year: number;
    rating: number;
    runtime: number;
    genres: string[];
    overview: string;
    backdropUrl?: string;
    posterUrl?: string;
  }
  