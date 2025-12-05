import { HttpClient } from '@angular/common/http';
import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

interface LoginRequest {
  email: string;
  password: string;
}

interface AuthResponse {
  token: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private readonly API_URL = 'http://localhost:8080/api/auth';
  private readonly TOKEN_KEY = 'smartfun_token';

  constructor(
    private http: HttpClient,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  login(data: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, data);
  }

  register(data: any) {
    return this.http.post(`${this.API_URL}/register`, data, { responseType: 'text' });
  }

  checkEmail(email: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.API_URL}/check-email?email=${email}`);
  }

  verify(token: string): Observable<string> {
    return this.http.get(`${this.API_URL}/verify?token=${token}`, { responseType: 'text' });
  }
isLoggedIn(): boolean {
  const token = this.getToken();
  return !!token;
}
getUserId(): string | null {
  const payload = this.decodePayload();
  if (!payload) return null;

  
  return (
    payload.userId ||    
    payload.id ||       
    payload.sub ||      
    null
  );
}

  // ========== TOKEN HANDLING ===========
  saveToken(token: string) {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem(this.TOKEN_KEY, token);
    }
  }

  getToken(): string | null {
    if (isPlatformBrowser(this.platformId)) {
      return localStorage.getItem(this.TOKEN_KEY);
    }
    return null;
  }

 

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  private decodePayload(): any | null {
  const token = this.getToken();
  if (!token) return null;

  const parts = token.split('.');
  if (parts.length !== 3) return null;

  try {
    const payload = JSON.parse(atob(parts[1]));
    console.log('JWT payload = ', payload);
    return payload;
  } catch {
    return null;
  }
}
getEmail(): string | null {
  const payload = this.decodePayload();
  return payload?.sub || null;
}



  getRoles(): string[] {
    const payload = this.decodePayload();
    if (!payload) return [];

    return payload.roles || [];
  }

  hasRole(role: string): boolean {
    return this.getRoles().includes(role);
  }
  logout() {
  if (isPlatformBrowser(this.platformId)) {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.clear(); // si tu veux TOUT vider
  }

  this.router.navigate(['/login']);
}

}
