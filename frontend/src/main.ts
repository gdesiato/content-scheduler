import { Routes } from '@angular/router';
import { LoginComponent } from './app/login/login.component';
import { UserListComponent } from './app/user-management/user-list/user-list.component';
import { PostListComponent } from './app/post-management/post-list/post-list.component';
import { AuthGuard } from './app/guards/auth.guard';
import { RoleGuard } from './app/guards/role.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'users', component: UserListComponent, canActivate: [AuthGuard, RoleGuard], data: { roles: ['ADMIN'] } },
  { path: 'posts', component: PostListComponent, canActivate: [AuthGuard, RoleGuard], data: { roles: ['USER', 'ADMIN'] } },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
];