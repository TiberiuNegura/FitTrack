import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, map, of, tap } from 'rxjs';
import { LoginService } from './login.service';

@Injectable({ providedIn: 'root' })
export class HomeService {
  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient, private loginService : LoginService) {}

  getUserIdFromSession() {
    return this.loginService.getUserId();
  }

  getWorkoutsByDate(date: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/workout/alldate?date=${encodeURIComponent(date)}`, {withCredentials: true});
  }

  getAllExercisesByMuscleGroup(muscleGroup: string): Observable<string[]> {
    return this.http.get<{ [key: string]: { exerciseName: string; muscleGroup: string } }>(
        `${this.baseUrl}/workout/exercises?muscleGroup=${encodeURIComponent(muscleGroup)}`, 
        { withCredentials: true }
    ).pipe(
        map(response => Object.values(response)),
        map(exercises => exercises.map(e => e.exerciseName))
    );
}

  saveWorkout(workoutData: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/workout/save`, workoutData, {withCredentials: true});
  }

  updateWorkout(workoutData: any): Observable<any> {
    return this.http.put(`${this.baseUrl}/workout/update`, workoutData, {withCredentials: true});
  }

  deleteWorkout(workoutId: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/workout/deleteById?id=${encodeURIComponent(workoutId)}`, {withCredentials: true});
  }

  logout() {
    console.log("Logout called");
    this.loginService.logout();
  }
}