import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-support-create',
  imports: [ ReactiveFormsModule, CommonModule],
  templateUrl: './support-create.html',
  styleUrl: './support-create.scss'
})
export class SupportCreate {
  supportPointForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.supportPointForm = this.fb.group({
      name: ['Central de Atendimento de RH', Validators.required],
      description: ['Atendimento relacionado a dúvidas de folha de pagamento e benefícios', Validators.required],
      contactNumber: this.fb.array([
        this.fb.control('+55 11 99999-9999', Validators.required),
        this.fb.control('+55 11 98888-8888', Validators.required)
      ])
    });
  }

  // Getter para acessar os contatos
  get contactNumbers(): FormArray {
    return this.supportPointForm.get('contactNumber') as FormArray;
  }

  addContact() {
    this.contactNumbers.push(this.fb.control('', Validators.required));
  }

  removeContact(index: number) {
    this.contactNumbers.removeAt(index);
  }

  onSubmit() {
    if (this.supportPointForm.valid) {
      console.log('Novo Ponto de Apoio:', this.supportPointForm.value);
      // Aqui você pode chamar o service para enviar ao backend:
      // this.supportPointService.create(this.supportPointForm.value).subscribe(...)
    }
  }
}
