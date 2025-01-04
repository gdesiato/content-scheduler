import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../user.service';
import { UserRequestDTO } from '../../models/user-request.dto';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.css']
})
export class EditUserComponent implements OnInit {
  user: UserRequestDTO = { username: '', email: '', password: '' };
  errorMessage: string = '';

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const userId = Number(this.route.snapshot.paramMap.get('id'));
    this.userService.getUserById(userId).subscribe({
      next: (data) => {
        this.user = { username: data.username, email: data.email, password: '' };
      },
      error: (err) => this.errorMessage = 'Failed to load user data'
    });
  }

  updateUser(): void {
    const userId = Number(this.route.snapshot.paramMap.get('id'));
    this.userService.updateUser(userId, this.user).subscribe({
      next: () => this.router.navigate(['/users']),
      error: (err) => this.errorMessage = 'Failed to update user'
    });
  }
}
