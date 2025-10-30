import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// === Interfaces alinhadas com o backend real ===
export interface ReportResponse {
  surveySummary: {
    surveyId: string;
    surveyTitle: string;
    questionResponses: {
      question: string;
      dailyResponses: {
        date: string;
        ranking: { response: string; quantity: number }[];
      }[];
    }[];
    participants: {
      totalPerSector: Record<string, number>;
      total: number;
    };
    engagement: number;
  };

  weekSummary: {
    mostVotedResponses: Record<string, Record<string, number>>;
    overallEngagement: number;
    participants: {
      totalPerSector: Record<string, number>;
      total: number;
    };
  };

  previousHealthyPercentage: number;
  currentHealthyPercentage: number;
  healthyPercentage: number;
  startOfWeek: string;
  endOfWeek: string;
  alerts: string[];
  moodSummary: {
    mainMoodOfWeek: {
      value: number;
      key: string;
    };
    moods: Record<string, number>;
  };
}

@Injectable({ providedIn: 'root' })
export class ReportService {

  private readonly API_URL = 'http://localhost:8000/reports';

  constructor(private http: HttpClient) {}

  getAdminReport(): Observable<ReportResponse> {
    return this.http.get<ReportResponse>(this.API_URL);
  }
}
