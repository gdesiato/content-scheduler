import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css'],
})
export class UserListComponent implements OnInit {
  users: any[] = []; // Initialize as an empty array
  errorMessage: string | null = null;

  constructor(private http: HttpClient) {} // Inject HttpClient

  ngOnInit(): void {
    this.fetchUsers(); // Fetch users on component initialization
  }

  fetchUsers(): void {
    this.http.get<any[]>('http://localhost:8080/api/users').subscribe({
      next: (data) => (this.users = data), // Assign fetched data to users
      error: (error) => {
        this.errorMessage = 'Failed to load users';
        console.error(error);
      },
    });
  }

  deleteUser(id: number): void {
    console.log('User deleted with ID:', id);
    this.users = this.users.filter((user) => user.id !== id); // Remove user from the list
  }
}
