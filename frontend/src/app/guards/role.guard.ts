import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../auth-management/auth-service';

@Injectable({
  providedIn: 'root',
})
export class RoleGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: any): boolean {
    const requiredRoles: string[] = route.data.roles || [];
    const userHasAccess = requiredRoles.some((role) => this.authService.hasRole(role));

    if (!userHasAccess) {
      this.router.navigate(['/login']); // Redirect to login if access is denied
      return false;
    }

    return true;
  }
}
