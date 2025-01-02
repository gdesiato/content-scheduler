import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { UserListComponent } from './user-management/user-list/user-list.component';
import { CreateUserComponent } from './user-management/create-user/create-user.component';
import { EditUserComponent } from './user-management/edit-user/edit-user.component';
import { AuthGuard } from './guards/auth.guard';
import { HomePageComponent } from './home-management/home-page.component';  // Import your HomePageComponent

export const routes: Routes = [
  { path: 'login', component: LoginComponent },  // Public route
  { path: 'users', component: UserListComponent, canActivate: [AuthGuard] },  // Protected route
  { path: 'user/new', component: CreateUserComponent, canActivate: [AuthGuard] },  // Protected route
  { path: 'user/:id', component: EditUserComponent, canActivate: [AuthGuard] },  // Protected route
  { path: 'home', component: HomePageComponent, canActivate: [AuthGuard] },  // Home route
  { path: '', redirectTo: 'login', pathMatch: 'full' },  // Default route
  { path: '**', redirectTo: 'login', pathMatch: 'full' },  // Wildcard route
];