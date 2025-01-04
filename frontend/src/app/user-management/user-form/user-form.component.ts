import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css'],
})
export class UserFormComponent {
  user = { name: '', email: '' };
  userId: number | null = null;

  saveUser(): void {
    console.log(this.userId ? 'Updating User' : 'Creating User', this.user);
  }
}
