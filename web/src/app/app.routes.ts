import { Routes } from '@angular/router';
import { Login } from './auth/login/login';
import { Reset } from './auth/reset/reset';
import { Validate } from './auth/validate/validate';
import { Painel } from './portal/layout/painel/painel';
import { Dashboard } from './portal/pages/dashboard/dashboard';
import { Profile } from './portal/pages/profile/profile';
import { Reports } from './portal/pages/reports/reports';
import { Employee } from './portal/pages/employee/employee';
import { Survey } from './portal/pages/survey/survey';
import { EmployeeUpdate } from './portal/pages/employee/employee-update/employee-update';
import { EmployeeCreate } from './portal/pages/employee/employee-create/employee-create';
import { EmployeeDelete } from './portal/pages/employee/employee-delete/employee-delete';
import { SurveyManager } from './portal/pages/survey/survey-manager/survey-manager';
import { SupportCreate } from './portal/pages/support/support-create/support-create';
import { SupportUpdate } from './portal/pages/support/support-update/support-update';
import { SupportDelete } from './portal/pages/support/support-delete/support-delete';
import { Support } from './portal/pages/support/support';

export const routes: Routes = [
  { path: '', component: Login },
  { path: 'login', component: Login },
  { path: 'reset', component: Reset},
  { path: 'validate', component: Validate},
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
          { path: 'atualizar', component: EmployeeUpdate },
          { path: 'deletar', component: EmployeeDelete },
          { path: '', redirectTo: 'listar', pathMatch: 'full' }
        ]
      },
      { path: 'pesquisa',
        children: [
          { path: 'listar', component: Survey},
          { path: 'gerenciar-pesquisa', component: SurveyManager},
          { path: '', redirectTo: 'listar', pathMatch: 'full' }
        ]
      },
      { path: 'suporte',
        children: [
          { path: 'listar', component: Support },
          { path: 'cadastrar', component: SupportCreate },
          { path: 'atualisar', component: SupportUpdate },
          { path: 'deletar', component: SupportDelete },
          { path: '', redirectTo: 'listar', pathMatch: 'full' }
        ]
       },
      { path: 'perfil', component: Profile},
      { path: '', redirectTo:'dashboard', pathMatch: 'full'}
    ]
  },
  { path: '**', redirectTo: ''}
];
