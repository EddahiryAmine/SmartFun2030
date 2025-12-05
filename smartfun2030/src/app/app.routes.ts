import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login';
import { RegisterComponent } from './pages/register/register';
import { Home } from './pages/home/home';
import { adminGuard } from './guards/admin.guard';
import { AdminDashboard } from './pages/admin/admin-dashboard/admin-dashboard';
import { StadiumListComponent } from './pages/stadiums/stadiums';
import { StadiumDetailComponent } from './pages/stadiums/stadium-detail.component';
import { MatchesListComponent } from './pages/matches/matches-list.component';
import { PurchaseTicketComponent } from './pages/tickets/purchase-ticket/purchase-ticket';
import { authGuard } from './guards/auth.guard';
import { ProfileComponent } from './pages/profile/profile';
export const routes: Routes = [
{path:'stadiums', component:StadiumListComponent},
{ path: 'stadiums/:id', component: StadiumDetailComponent },
{ path: 'matches', component: MatchesListComponent },
{ path: 'login', component: LoginComponent },
{path:'register',component:RegisterComponent},
{ path: 'home', component: Home },
 { path: 'profile', component: ProfileComponent },
{ path: '', redirectTo: 'home', pathMatch: 'full' },
 { path: 'tickets/acheter/:matchId',
    component: PurchaseTicketComponent,
    canActivate: [authGuard]
  },
{ path: 'admin',
    canActivate: [adminGuard],
    component: AdminDashboard
  },

  { path: '**', redirectTo: 'home' },

    
];
