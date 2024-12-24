import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // Import FormsModule for ngModel

@Component({
  selector: 'app-create-user',
  standalone: true,
  imports: [CommonModule, FormsModule], // Include FormsModule
  template: `
    <h2>Create User</h2>
    <form (ngSubmit)="createUser()">
      <label>Name: <input [(ngModel)]="user.name" name="name" /></label><br />
      <label>Email: <input [(ngModel)]="user.email" name="email" /></label><br />
      <button type="submit">Create</button>
    </form>
  `,
})
export class CreateUserComponent {
  user = { name: '', email: '' }; // Initialize the user object

  createUser() {
    console.log('User Created:', this.user);
    // Add API call or logic to save the user
  }
}

