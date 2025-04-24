import { Routes } from '@angular/router';
import { AboutComponent } from './about/about.component';
import { ContactComponent } from './contact/contact.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { ProfilComponent } from './profil/profil.component';
import { MainComponent } from './main/main.component';
import { AuthGuard } from '../services/auth/auth-guard.service';
import { WatchListComponent } from './watch-list/watch-list.component';
import { MovieDetailsComponent } from './movie-details/movie-details.component';
import { RecommendationComponent } from './recommendation/recommendation.component';
import { PrivacyPolicyComponent } from './privacy-policy/privacy-policy.component';
import { SettingsComponent } from './settings/settings.component';
import { TermServiceComponent } from './term-service/term-service.component';
import { FavoriteComponent } from './favorite/favorite.component';
import { SearchComponent } from './search/search.component';
import { FriendsListComponent } from './friends-list/friends-list.component';

export const routes: Routes = [
  { path: '', component: HomeComponent }, // Page d'accueil
  { path: 'about', component: AboutComponent }, // Page "À propos"
  { path: 'contact', component: ContactComponent }, 
  { path: 'privacy', component: PrivacyPolicyComponent }, 
  { path: 'terms', component: TermServiceComponent }, 
  { path: 'settings', component: SettingsComponent }, 
  { path: 'login', component: LoginComponent},
  { path: 'signup', component: SignupComponent},
  { path: 'search', component: SearchComponent},
  { path: 'main', component: MainComponent, canActivate:[AuthGuard]},
  { path: 'profile', component: ProfilComponent, canActivate:[AuthGuard]},
  { path: 'watchlist', component: WatchListComponent, canActivate:[AuthGuard]},
  { path: 'recommendations', component: RecommendationComponent, canActivate:[AuthGuard]},
  { path: 'favorites', component: FavoriteComponent, canActivate:[AuthGuard]},
  //{ path: 'friends', component: FriendsListComponent, canActivate:[AuthGuard]},
  { path: 'friends', component: FriendsListComponent},
  { path: 'movie/:id', component: MovieDetailsComponent },
  // { path: '**', redirectTo: '' }, // Redirection vers l'accueil pour les routes inconnues
  
];
