import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css'],
})
export class UserListComponent implements OnInit {
  users: any[] = [];
  errorMessage: string | null = null;

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.fetchUsers(); // Fetch users on component initialization
  }

  fetchUsers(): void {
    const token = localStorage.getItem('jwtToken'); // Retrieve token from local storage
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.get<any[]>('http://localhost:8080/api/users', { headers }).subscribe({
      next: (data) => (this.users = data), // Assign fetched data to users
      error: (error) => {
        this.errorMessage = 'Failed to load users';
        console.error('Error fetching users:', error);
      },
    });
  }

  deleteUser(id: number): void {
    const token = localStorage.getItem('jwtToken'); // Retrieve JWT token from localStorage
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
  
    this.http.delete(`http://localhost:8080/api/users/${id}`, { headers }).subscribe({
      next: () => {
        this.users = this.users.filter(user => user.id !== id);
        console.log(`User with ID ${id} deleted successfully`);
      },
      error: (err) => {
        console.error(`Failed to delete user with ID ${id}`, err);
        this.errorMessage = `Failed to delete user with ID ${id}`;
      }
    });
  }

  navigateToEditUser(id: number): void {
    this.router.navigate([`/user/${id}`]);
  }
}
