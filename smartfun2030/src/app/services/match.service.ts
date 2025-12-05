import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Match } from '../models/match.model';

@Injectable({
  providedIn: 'root'
})
export class MatchService {

  // ⚠️ adapte le port selon ton microservice (8084 ? 8085 ?)
  private readonly API_URL = 'http://localhost:8080/api/matches';

  constructor(private http: HttpClient) {}

  // 🔹 Tous les matchs (pour /matches)
  getAllMatches(): Observable<Match[]> {
    return this.http.get<Match[]>(`${this.API_URL}/all`);
  }

  // 🔹 Matchs d'un stade (pour StadiumDetailComponent)
  getMatchesByStadium(stadiumId: string): Observable<Match[]> {
    return this.http.get<Match[]>(`${this.API_URL}/stadium/${stadiumId}`);
  }
}
