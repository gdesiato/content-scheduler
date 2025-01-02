import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import jwt_decode from 'jwt-decode';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private tokenKey = 'token';
  private apiUrl = 'http://localhost:8080/api/authenticate';

  constructor(private http: HttpClient) {}

  // Save token to localStorage
  saveToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  // Get token from localStorage
  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  // Decode JWT token
  private decodeToken(): any {
    const token = this.getToken();
    return token ? jwt_decode(token) : null;
  }

  isTokenExpired(token: string): boolean {
    const decodedToken: any = jwt_decode(token);
    const currentTime = Math.floor(Date.now() / 1000); // Current time in seconds
    return decodedToken.exp < currentTime; // Check if token is expired
  }

  isAuthenticated(): boolean {
    const token = localStorage.getItem('jwtToken'); // Or wherever your token is stored
    return token !== null && !this.isTokenExpired(token);
  }

  // Check if user is logged in
  isLoggedIn(): boolean {
    const decodedToken = this.decodeToken();
    if (!decodedToken) return false;

    const expirationDate = new Date(decodedToken.exp * 1000);
    return expirationDate > new Date(); // Check token expiration
  }

  // Get roles from decoded token
  getUserRoles(): string[] {
    const decodedToken = this.decodeToken();
    return decodedToken?.roles || [];
  }

  // Check if user has a specific role
  hasRole(role: string): boolean {
    return this.getUserRoles().includes(role);
  }

  // Logout user and clear token
  logout(): void {
    localStorage.removeItem(this.tokenKey);
    window.location.href = '/login'; // Redirect to login page
  }

  // Login user and retrieve token
  login(username: string, password: string): Observable<string> {
    const body = new HttpParams()
      .set('username', username)
      .set('password', password);

    return this.http.post(this.apiUrl, body, {
      responseType: 'text',
      headers: new HttpHeaders({
        'Content-Type': 'application/x-www-form-urlencoded',
      }),
    }).pipe(
      catchError((error) => {
        console.error('Login failed:', error);
        return throwError(() => new Error('Invalid username or password'));
      })
    );
  }
}
