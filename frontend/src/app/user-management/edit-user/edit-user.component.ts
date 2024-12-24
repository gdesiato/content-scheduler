import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-edit-user',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <h2>Edit User</h2>
    <form (ngSubmit)="updateUser()">
      <label>
        Name: <input [(ngModel)]="user.name" name="name" />
      </label><br />
      <label>
        Email: <input [(ngModel)]="user.email" name="email" />
      </label><br />
      <button type="submit">Update</button>
    </form>
  `,
})
export class EditUserComponent implements OnInit {
  user = { id: null, name: '', email: '' }; // Initialize user object

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    const userId = this.route.snapshot.params['id'];
    console.log('Editing user with ID:', userId);

    // Fetch the user data based on userId (e.g., from an API or service)
    // Mocked fetched data for demonstration:
    this.user = {
      id: userId,
      name: 'John Doe',
      email: 'john.doe@example.com',
    };
  }

  updateUser() {
    console.log('Updated User:', this.user);
    // Add logic to save the updated user (e.g., API call)
  }
}

