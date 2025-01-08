import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeService } from './home.service';
import { AuthService } from '../auth-management/auth-service';

@Component({
  selector: 'app-home-page',
  standalone: true, // Mark as standalone
  imports: [CommonModule], // Include CommonModule
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css'],
})
export class HomePageComponent implements OnInit {
  homePageData: any;
  errorMessage: string | null = null;

  constructor(private homeService: HomeService, private authService: AuthService) {}

  ngOnInit(): void {
    this.loadHomePageData();
  }

  loadHomePageData(): void {
    this.homeService.getHomePageData().subscribe(
      (data) => {
        this.homePageData = data;
      },
      (error) => {
        this.errorMessage = 'Failed to load homepage data';
        console.error(error);
      }
    );
  }

  logout(): void {
    this.authService.logout();
  }
}