import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../../core/auth/auth-service';

export interface SurveyQuestion {
  text: string;
  type: 'SINGLE_CHOICE' | 'MULTIPLE_CHOICE' | 'TEXT' | 'SCALE';
  options?: string[];
}

export interface SurveyModel {
  id?: string;
  title: string;
  description: string;
  questions: SurveyQuestion[];
  active?: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class SurveyService {
  private readonly API_URL = 'http://localhost:8000/surveys';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  /** 🔹 Lista todas as pesquisas da empresa logada */
  getAll(): Observable<SurveyModel[]> {
    const user = this.authService.getUser();
    const companyId = (user as any)?.companyId;

    if (!companyId) {
      throw new Error('companyId não encontrado no usuário logado.');
    }

    return this.http.get<SurveyModel[]>(`${this.API_URL}/company/${companyId}`);
  }

  /** 🔹 Cria uma nova pesquisa */
  create(survey: SurveyModel): Observable<SurveyModel> {
    return this.http.post<SurveyModel>(this.API_URL, survey);
  }

  /** 🔹 Busca por ID */
  getById(id: string): Observable<SurveyModel> {
    return this.http.get<SurveyModel>(`${this.API_URL}/${id}`);
  }

  /** 🔹 Atualiza */
  update(id: string, survey: SurveyModel): Observable<SurveyModel> {
    return this.http.put<SurveyModel>(`${this.API_URL}/${id}`, survey);
  }

  /** 🔹 Exclui */
  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

  /** 🔹 Ativa uma pesquisa */
  activateSurvey(id: string): Observable<SurveyModel> {
    return this.http.post<SurveyModel>(`${this.API_URL}/${id}/activate`, {});
  }
}
