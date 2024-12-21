import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { UserListComponent } from './user-management/user-list/user-list.component';
import { PostListComponent } from './post-management/post-list/post-list.component';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent }, // Public route
  { path: 'users', component: UserListComponent, canActivate: [AuthGuard] }, // Protected route
  { path: 'posts', component: PostListComponent, canActivate: [AuthGuard] }, // Protected route
  { path: '', redirectTo: 'login', pathMatch: 'full' }, // Redirect to login by default
];
