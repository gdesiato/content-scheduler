import { Routes } from '@angular/router';
import { PostListComponent } from './post-management/post-list/post-list.component';
import { PostFormComponent } from './post-management/post-form/post-form.component';

export const routes: Routes = [
  { path: 'posts', component: PostListComponent },
  { path: 'posts/new', component: PostFormComponent },
  { path: 'posts/:id', component: PostFormComponent },
];