import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../user.service';
import { UserRequestDTO } from '../../models/user-request.dto';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.css']
})
export class CreateUserComponent {
  user: UserRequestDTO = { username: '', email: '', password: '' };
  errorMessage: string = '';

  constructor(private userService: UserService, private router: Router) {}

  createUser(): void {
    this.userService.createUser(this.user).subscribe({
      next: () => this.router.navigate(['/users']),
      error: (err) => this.errorMessage = 'Failed to create user'
    });
  }
}

