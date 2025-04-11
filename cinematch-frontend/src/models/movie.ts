export class Movie {
    id: number = 0;
    title: string = 'The Shawshank Redemption';
    description: string = 'Framed in the 1940s for the double murder of his wife and her lover, upstanding banker Andy Dufresne begins a new life at the Shawshank prison, where he puts his accounting skills to work for an amoral warden. During his long stretch in prison, Dufresne comes to be admired by the other inmates -- including an older prisoner named Red -- for his integrity and unquenchable sense of hope.';
    releaseDate: number = 2000;
    poster: string = 'https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg';
    
    genres: string[] = ['Drama', 'Crime'];
    rating: number = 9.99;
    runtime: number = 142;
    backdropUrl: string = "https://image.tmdb.org/t/p/original/kXfqcdQKsToO0OUXHcrrNCHDBzO.jpg";
}