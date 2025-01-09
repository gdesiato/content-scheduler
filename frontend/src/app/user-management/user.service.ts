import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserRequestDTO } from '../models/user-request.dto';
import { UserResponseDTO } from '../models/user-response.dto';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwtToken');
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  getUsers(): Observable<UserResponseDTO[]> {
    return this.http.get<UserResponseDTO[]>(this.apiUrl, { headers: this.getHeaders() });
  }

  getUserById(id: number): Observable<UserResponseDTO> {
    return this.http.get<UserResponseDTO>(`${this.apiUrl}/${id}`, { headers: this.getHeaders() });
  }

  createUser(user: UserRequestDTO): Observable<UserResponseDTO> {
    return this.http.post<UserResponseDTO>(this.apiUrl, user, { headers: this.getHeaders() });
  }

  updateUser(id: number, user: UserRequestDTO): Observable<UserResponseDTO> {
    return this.http.put<UserResponseDTO>(`${this.apiUrl}/${id}`, user, { headers: this.getHeaders() });
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.getHeaders() });
  }

  searchUsers(query: string): Observable<UserResponseDTO[]> {
    return this.http.get<UserResponseDTO[]>(`${this.apiUrl}?search=${query}`, { headers: this.getHeaders() });
  }
}

