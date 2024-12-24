import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { UserListComponent } from './user-management/user-list/user-list.component';
import { CreateUserComponent } from './user-management/create-user/create-user.component';
import { EditUserComponent } from './user-management/edit-user/edit-user.component';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent }, // Public route
  { path: 'users', component: UserListComponent, canActivate: [AuthGuard] }, // User list
  { path: 'user/new', component: CreateUserComponent, canActivate: [AuthGuard] }, // Create user
  { path: 'user/:id', component: EditUserComponent, canActivate: [AuthGuard] }, // Edit user
  { path: '', redirectTo: 'login', pathMatch: 'full' }, // Default redirect
  { path: '**', redirectTo: 'login', pathMatch: 'full' }, // Wildcard route
];