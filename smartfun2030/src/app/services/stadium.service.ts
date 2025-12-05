import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Stadium } from '../models/stadium.model';

@Injectable({
  providedIn: 'root'
})
export class StadiumService {

  private readonly API_URL = 'http://localhost:8080/api/stadiums';

  constructor(private http: HttpClient) {}

  getAllStadiums(): Observable<Stadium[]> {
    return this.http.get<Stadium[]>(`${this.API_URL}/all`);
  }
  getStadiumById(id: string): Observable<Stadium> {
  return this.http.get<Stadium>(`${this.API_URL}/${id}`);
}

}
