import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms'; // Import FormsModule
import { CommonModule } from '@angular/common'; // Import CommonModule

@Component({
  selector: 'app-user-form',
  standalone: true, // Declare as standalone
  imports: [CommonModule, FormsModule], // Import dependencies
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css'],
})
export class UserFormComponent {
  user = { name: '', email: '' }; // Model for user
  userId: number | null = null; // ID to track if editing

  saveUser(): void {
    console.log(this.userId ? 'Updating User' : 'Creating User', this.user);
  }
}
