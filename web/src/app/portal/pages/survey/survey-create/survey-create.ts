import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SurveyService } from '../../../service/survey';

@Component({
  selector: 'app-survey-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './survey-create.html',
  styleUrl: './survey-create.scss'
})
export class SurveyCreate {
  form: FormGroup;
  loading = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private surveyService: SurveyService
  ) {
    this.form = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      questions: this.fb.array([this.createQuestion()])
    });
  }

  get questions(): FormArray {
    return this.form.get('questions') as FormArray;
  }

  createQuestion(): FormGroup {
    return this.fb.group({
      text: ['', Validators.required],
      type: ['SINGLE_CHOICE', Validators.required],
      options: this.fb.array([this.fb.control('', Validators.required)])
    });
  }

  addQuestion(): void {
    this.questions.push(this.createQuestion());
  }

  removeQuestion(index: number): void {
    this.questions.removeAt(index);
  }

  addOption(questionIndex: number): void {
    const options = this.questions.at(questionIndex).get('options') as FormArray;
    options.push(this.fb.control('', Validators.required));
  }

  removeOption(questionIndex: number, optionIndex: number): void {
    const options = this.questions.at(questionIndex).get('options') as FormArray;
    options.removeAt(optionIndex);
  }
  getOptions(questionIndex: number): FormArray {
    return this.questions.at(questionIndex).get('options') as FormArray;
  }


  onSubmit(): void {
    if (this.form.invalid) return;

    this.loading = true;
    const payload = this.form.value;

    this.surveyService.create(payload).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'Pesquisa cadastrada com sucesso!';
        setTimeout(() => this.router.navigate(['/portal/pesquisas']), 1500);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = `Erro ao salvar pesquisa (${err.status || 'desconhecido'})`;
        console.error(err);
      }
    });
  }
}
