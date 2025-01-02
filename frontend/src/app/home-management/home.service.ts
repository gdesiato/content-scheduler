import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

interface HomePageDataDTO {
  username: string;
  roles: string[];
  welcomeMessage: string;
}

@Injectable({
  providedIn: 'root'
})
export class HomeService {

  private apiUrl = 'http://localhost:8080/api/home';

  constructor(private http: HttpClient) {}

  getHomePageData(): Observable<HomePageDataDTO> {
    return this.http.get<HomePageDataDTO>(this.apiUrl);
  }
}