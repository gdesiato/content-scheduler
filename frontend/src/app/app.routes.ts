import { Routes } from '@angular/router';
import { UserListComponent } from './user-management/user-list/user-list.component';
import { UserFormComponent } from './user-management/user-form/user-form.component';

export const routes: Routes = [
  { path: '', redirectTo: 'users', pathMatch: 'full' },
  { path: 'users', component: UserListComponent },
  { path: 'users/new', component: UserFormComponent },
  { path: 'users/:id', component: UserFormComponent },
];