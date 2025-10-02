import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SurveyManager } from './survey-manager';

describe('SurveyManager', () => {
  let component: SurveyManager;
  let fixture: ComponentFixture<SurveyManager>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SurveyManager]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SurveyManager);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
