import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({
    providedIn: 'root',
})
export class RegisterService {
    private readonly apiUrl = 'http://localhost:8081/user/register';

    constructor(private http: HttpClient) { }

    registerUser(userData: {
        firstName: string;
        lastName: string;
        password: string;
        bodyWeight: string;
        height: string;
        age: string;
    }): Observable<{ token: string }> {
        return this.http.post<{ token: string }>(this.apiUrl, userData).pipe(
            tap((response) => {
                if (response.token) {
                    localStorage.setItem('jwt', response.token);
                }
            })
        );
    }
}
