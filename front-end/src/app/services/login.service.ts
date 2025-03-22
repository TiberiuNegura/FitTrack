import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { jwtDecode } from 'jwt-decode';

export interface JwtPayload {
    sub: string;
}

export function getUserIdFromToken(token: string): number | null {
    try {
        const decoded = jwtDecode<JwtPayload>(token);
        const userId = Number(decoded.sub);

        if (isNaN(userId)) {
            console.error('Invalid user ID in JWT');
            return null;
        }

        return userId;
    } catch (error) {
        console.error('Failed to decode JWT:', error);
        return null;
    }
}

@Injectable({
    providedIn: 'root',
})
export class LoginService {
    private readonly apiUrl = 'http://localhost:8081/user/login';

    constructor(private http: HttpClient) { }

    loginUser(loginData: {
        firstName: string;
        lastName: string;
        password: string;
    }): Observable<{ token: string }> {
        return this.http.post<{ token: string }>(this.apiUrl, loginData).pipe(
            tap((response) => {
                if (response.token) {
                    localStorage.setItem('jwt', response.token);
                }
            })
        );
    }

    getToken(): string | null {
        return localStorage.getItem('jwt');
    }

    getUserId(): number | null {
        const token = this.getToken();
        return token ? getUserIdFromToken(token) : null;
    }

    logout(): void {
        localStorage.removeItem('jwt');
    }

}