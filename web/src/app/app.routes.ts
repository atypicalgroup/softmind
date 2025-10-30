import { Routes } from '@angular/router';
import { Login } from './core/auth/login/login';
import { Validate } from './core/auth/validate/validate';
import { Painel } from './portal/layout/painel/painel';
import { Dashboard } from './portal/pages/dashboard/dashboard';
import { Profile } from './portal/pages/profile/profile';
import { Reports } from './portal/pages/reports/reports';
import { Employee } from './portal/pages/employee/employee';
import { Survey } from './portal/pages/survey/survey';
import { EmployeeCreate } from './portal/pages/employee/employee-create/employee-create';
import { SupportCreate } from './portal/pages/support/support-create/support-create';
import { Support } from './portal/pages/support/support';
import { SurveyCreate } from './portal/pages/survey/survey-create/survey-create';
import { ResetPassword } from './core/auth/reset-password/reset-password';
import { ForgotPassword } from './core/auth/forgot-password/forgot-password';

export const routes: Routes = [
  { path: '', component: Login },
  { path: 'login', component: Login },
  { path: 'recuperar-senha', component: ForgotPassword},
  { path: 'validar-codigo', component: Validate},
  { path: 'redefinir-senha', component: ResetPassword},
  {
    path: 'portal',
    component: Painel,
    children: [
      { path: 'dashboard', component: Dashboard},
      { path: 'relatorios', component: Reports},
      { path: 'funcionario',
        children: [
          { path: 'listar', component: Employee },
          { path: 'cadastrar', component: EmployeeCreate },
          { path: '', redirectTo: 'listar', pathMatch: 'full' }
        ]
      },
      { path: 'pesquisa',
        children: [
          { path: 'listar', component: Survey},
          { path: 'cadastrar', component:SurveyCreate},
          { path: '', redirectTo: 'listar', pathMatch: 'full' }
        ]
      },
      { path: 'suporte',
        children: [
          { path: 'listar', component: Support },
          { path: 'cadastrar', component: SupportCreate },
          { path: '', redirectTo: 'listar', pathMatch: 'full' }
        ]
       },
      { path: 'perfil', component: Profile},
      { path: '', redirectTo:'dashboard', pathMatch: 'full'}
    ]
  },
  { path: '**', redirectTo: ''}
];
