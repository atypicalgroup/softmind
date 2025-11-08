// src/app/features/service/report.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ReportResponse {
  surveySummary: SurveySummary[];
  weekSummary: WeekSummary;
  previousHealthyPercentage: number;
  currentHealthyPercentage: number;
  healthyPercentage: number;
  startOfWeek: string | null;
  endOfWeek: string | null;
  alerts: string[];
  moodSummary: MoodSummary;
}

export interface SurveySummary {
  surveyId: string;
  surveyTitle: string | null;
  questionResponses: QuestionResponse[];
  participants: {
    totalPerSector: Record<string, number>;
    total: number;
  };
  engagement: number;
}

export interface QuestionResponse {
  question: string;
  dailyResponses: {
    date: string;
    ranking: { response: string; quantity: number }[];
  }[];
}

export interface WeekSummary {
  mostVotedResponses: Record<string, Record<string, number>>;
  overallEngagement: number;
  participants: {
    totalPerSector: Record<string, number>;
    total: number;
  };
}

export interface MoodSummary {
  mainMoodOfWeek: Record<string, number>;
  moods: Record<string, number>;
}

@Injectable({ providedIn: 'root' })
export class ReportService {
  private readonly API_URL = 'http://91.108.126.24:8080/reports/admin'; // 🚀 backend real

  constructor(private http: HttpClient) {}

  getAdminReport(): Observable<ReportResponse> {
    return this.http.get<ReportResponse>(this.API_URL);
  }
}
