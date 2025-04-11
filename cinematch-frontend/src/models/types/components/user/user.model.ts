export interface User {
    id: number;
    name: string;
    email: string;
    bio: string;
    avatarUrl?: string;
    birthDate?: string;
    location?: string;
    favoriteGenres?: string[];
  }
  