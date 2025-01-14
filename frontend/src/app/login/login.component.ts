import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth-management/auth-service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  username = '';
  password = '';
  errorMessage = '';

  constructor(public authService: AuthService, private router: Router) {}

  login(): void {
    this.authService.login(this.username, this.password).subscribe({
      next: (token) => {
        this.authService.saveToken(token);

        const roles = this.authService.getUserRoles();
        if (roles.includes('ADMIN')) {
          this.router.navigate(['/users']);
        } else if (roles.includes('USER')) {
          this.router.navigate(['/home']);
        } else {
          this.router.navigate(['/login']);
        }
      },
      error: () => {
        this.errorMessage = 'Invalid username or password';
        this.password = ''; // Clear password on error
      },
    });
  }

  logout(): void {
    this.authService.logout();
  }
}
