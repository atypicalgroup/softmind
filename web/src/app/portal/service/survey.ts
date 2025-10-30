import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SurveyQuestion {
  text: string;
  type: 'SINGLE_CHOICE' | 'MULTIPLE_CHOICE' | 'TEXT';
  options?: string[];
}

export interface SurveyModel {
  id?: string;
  title: string;
  description: string;
  questions: SurveyQuestion[];
}

@Injectable({
  providedIn: 'root'
})
export class SurveyService {
  private readonly API_URL = 'http://localhost:8000/surveys'; // ajuste conforme seu backend

  constructor(private http: HttpClient) {}

  getAll(): Observable<SurveyModel[]> {
    return this.http.get<SurveyModel[]>(this.API_URL);
  }

  getById(id: string): Observable<SurveyModel> {
    return this.http.get<SurveyModel>(`${this.API_URL}/${id}`);
  }

  create(survey: SurveyModel): Observable<SurveyModel> {
    return this.http.post<SurveyModel>(this.API_URL, survey);
  }

  update(id: string, survey: SurveyModel): Observable<SurveyModel> {
    return this.http.put<SurveyModel>(`${this.API_URL}/${id}`, survey);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
