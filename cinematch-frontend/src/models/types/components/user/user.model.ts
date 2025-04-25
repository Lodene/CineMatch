
export class User {
  username: string;
  name: string;
  description?: string;
  profilPicture?: string;
  watchedMoviesCount: number;
  reviewsCount: number;
  watchlistCount: number;
  child: boolean;

  constructor(data?: Partial<User>) {
    this.username = data?.username || '';
    this.name = data?.name || '';
    this.description = data?.description || '';
    this.profilPicture = data?.profilPicture || 'assets/default-avatar.png';
    this.watchedMoviesCount = data?.watchedMoviesCount || 0;
    this.reviewsCount = data?.reviewsCount || 0;
    this.watchlistCount = data?.watchlistCount || 0;
    this.child = data?.child || false;
  }
}
export interface FriendRequest {
  id: number;
  sender: User;
  receiver: User;
  createdAt: string;
}

export interface IncomingFriendRequestDto {
  id: number;
  sender: User;
  createdAt: string;
}

