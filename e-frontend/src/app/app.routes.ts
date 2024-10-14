import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { HomeComponent } from './components/commons/home/home.component';
import { NotFoundComponent } from './components/commons/not-found/not-found.component';
import { LoginComponent } from './components/commons/login/login.component';
import { ForgotPasswordComponent } from './components/commons/forgot-password/forgot-password.component';
import { AdminHomeComponent } from './components/admin/admin-home/admin-home.component';
import { AddCompanyComponent } from './components/admin/add-company/add-company.component';
import { HomeCompanyComponent } from './components/admin/home-company/home-company.component';
import { ServicesFormComponent } from './components/admin/services-form/services-form.component';
import { EmployeeFormComponent } from './components/admin/employee-form/employee-form.component';
import { PlaceFormComponent } from './components/admin/place-form/place-form.component';

export const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  {
    path: 'admin',
    children: [
      {
        path: 'init',
        component: AdminHomeComponent,
      },
      {
        path: 'addCompany',
        component: AddCompanyComponent,
      },
      {
        path: 'homeCompany',
        component: HomeCompanyComponent,
      },
      { path: 'services/new', component: ServicesFormComponent },
      { path: 'services/edit/:id', component: ServicesFormComponent },
      { path: 'employees/new', component: EmployeeFormComponent },
      { path: 'employees/edit/:id', component: EmployeeFormComponent },
      { path: 'places/new', component: PlaceFormComponent },
      { path: 'places/edit/:id', component: PlaceFormComponent },
    ],
  },
  { path: '**', component: NotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
