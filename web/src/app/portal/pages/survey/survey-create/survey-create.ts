import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-survey-create',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './survey-create.html',
  styleUrl: './survey-create.scss'
})
export class SurveyCreate {
  surveyForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.surveyForm = this.fb.group({
      title: ['Pesquisa de Clima Organizacional', Validators.required],
      description: ['Pesquisa sobre satisfação dos colaboradores', Validators.required],
      questions: this.fb.array([
        this.createQuestion()
      ])
    });
  }

  // Getter para acessar as perguntas
  get questions(): FormArray {
    return this.surveyForm.get('questions') as FormArray;
  }

  // Cria um grupo de pergunta
  createQuestion(): FormGroup {
    return this.fb.group({
      text: ['Nova pergunta', Validators.required],
      type: ['TEXT', Validators.required],
      options: this.fb.array([
        this.fb.control('') // opção inicial
      ])
    });
  }

  addQuestion() {
    this.questions.push(this.createQuestion());
  }

  removeQuestion(index: number) {
    this.questions.removeAt(index);
  }

  // Getter para opções dentro de uma pergunta
  getOptions(questionIndex: number): FormArray {
    return this.questions.at(questionIndex).get('options') as FormArray;
  }

  addOption(questionIndex: number) {
    this.getOptions(questionIndex).push(this.fb.control(''));
  }

  removeOption(questionIndex: number, optionIndex: number) {
    this.getOptions(questionIndex).removeAt(optionIndex);
  }

  onSubmit() {
    if (this.surveyForm.valid) {
      console.log('Nova Pesquisa:', this.surveyForm.value);
      // aqui entra o service que envia para o backend
      // this.surveyService.create(this.surveyForm.value).subscribe(...)
    }
  }
}
