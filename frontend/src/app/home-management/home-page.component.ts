import { Component, OnInit } from '@angular/core';
import { HomeService } from './home.service';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements OnInit {

  homePageData: any;
  errorMessage: string | null = null;

  constructor(private homeService: HomeService) { }

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
}
