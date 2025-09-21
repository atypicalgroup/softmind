import { Routes } from '@angular/router';
import { Login } from './auth/login/login';
import { Reset } from './auth/reset/reset';
import { Validate } from './auth/validate/validate';
import { Painel } from './portal/layout/painel/painel';
import { Dashboard } from './portal/pages/dashboard/dashboard';
import { Profile } from './portal/pages/profile/profile';
import { Reports } from './portal/pages/reports/reports';

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
      { path: 'perfil', component: Profile},
      { path: '', redirectTo:'dashboard', pathMatch: 'full'}
    ]
  },
  { path: '**', redirectTo: ''}
];
