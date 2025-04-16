
export class User {
  username: string;
  name: string;
  bio?: string;
  picture?: string;
  watchedMoviesCount: number;
  reviewsCount: number;
  watchlistCount: number;

  constructor(data?: Partial<User>) {
    this.username = data?.username || '';
    this.name = data?.name || '';
    this.bio = data?.bio || '';
    this.picture = data?.picture || 'assets/default-avatar.png';
    this.watchedMoviesCount = data?.watchedMoviesCount || 0;
    this.reviewsCount = data?.reviewsCount || 0;
    this.watchlistCount = data?.watchlistCount || 0;
  }
}
