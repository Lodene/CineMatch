import { Routes } from '@angular/router';
import { AboutComponent } from './about/about.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { ProfilComponent } from './profil/profil.component';
import { MainComponent } from './main/main.component';
import { AuthGuard } from '../services/auth/auth-guard.service';
import { WatchListComponent } from './watch-list/watch-list.component';
import { MovieDetailsComponent } from './movie-details/movie-details.component';
import { RecommendationComponent } from './recommendation/recommendation.component';

export const routes: Routes = [
  { path: '', component: HomeComponent }, // Page d'accueil
  { path: 'about', component: AboutComponent }, // Page "À propos"
  { path: 'login', component: LoginComponent},
  { path: 'signup', component: SignupComponent},
  { path: 'main', component: MainComponent, canActivate:[AuthGuard]},
  { path: 'profile', component: ProfilComponent, canActivate:[AuthGuard]},
  { path: 'watchlist', component: WatchListComponent, canActivate:[AuthGuard]},
  { path: 'recommendations', component: RecommendationComponent, canActivate:[AuthGuard]},
  { path: 'movie/:id', component: MovieDetailsComponent },
  // { path: '**', redirectTo: '' }, // Redirection vers l'accueil pour les routes inconnues
  
];
