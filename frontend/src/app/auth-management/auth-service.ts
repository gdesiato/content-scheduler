import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import jwt_decode from 'jwt-decode';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private tokenKey = 'jwtToken'; // Key for storing token in localStorage
  private apiUrl = 'http://localhost:8080/api/authenticate'; // Backend login endpoint

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

  // Check if token is expired
  isTokenExpired(): boolean {
    const token = this.getToken();
    if (!token) return true;

    const decodedToken: any = jwt_decode(token);
    const currentTime = Math.floor(Date.now() / 1000); // Current time in seconds
    return decodedToken.exp < currentTime; // Token is expired if current time is greater than exp
  }

  // Check if user is authenticated
  isAuthenticated(): boolean {
    return !!this.getToken() && !this.isTokenExpired();
  }

  // Alias for isAuthenticated (optional, for template use)
  isLoggedIn(): boolean {
    return this.isAuthenticated();
  }

  // Get user roles from decoded token
  getUserRoles(): string[] {
    const decodedToken = this.decodeToken();
    return decodedToken?.roles || []; // Return roles array or empty array if undefined
  }

  // Check if user has a specific role
  hasRole(role: string): boolean {
    return this.getUserRoles().includes(role);
  }

  // Logout user by removing token and redirecting to login
  logout(): void {
    localStorage.removeItem(this.tokenKey);
    window.location.href = '/login'; // Redirect to login page
  }

  // Login user by sending username and password to the backend
  login(username: string, password: string): Observable<string> {
    const body = new HttpParams()
      .set('username', username)
      .set('password', password);

    return this.http.post(this.apiUrl, body, {
      responseType: 'text', // Expect a plain text response (JWT token)
      headers: new HttpHeaders({
        'Content-Type': 'application/x-www-form-urlencoded', // URL-encoded form data
      }),
    }).pipe(
      catchError((error) => {
        console.error('Login failed:', error);
        return throwError(() => new Error('Invalid username or password'));
      })
    );
  }
}
