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
import { RoleComponent } from './components/admin/role/role.component';
import { RoleFormComponent } from './components/admin/role-form/role-form.component';
import { UsersComponent } from './components/admin/users/users.component';
import { UserFormComponent } from './components/admin/user-form/user-form.component';
import { ConfigScheduleComponent } from './components/admin/config-schedule/config-schedule.component';
import { ClientHomeComponent } from './components/client/client-home/client-home.component';
import { CompanyComponent } from './components/client/company/company.component';
import { ReservationComponent } from './components/client/reservation/reservation.component';
import { CourtReservationComponent } from './components/client/court-reservation/court-reservation.component';
import { MyReservationComponent } from './components/client/my-reservation/my-reservation.component';
import { VerifyEmailComponent } from './components/commons/verify-email/verify-email.component';

export const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: 'verify-email/:token/:email', component: VerifyEmailComponent },
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
      { path: 'roles', component: RoleComponent },
      { path: 'roles/new', component: RoleFormComponent },
      { path: 'roles/edit/:id', component: RoleFormComponent },
      { path: 'users', component: UsersComponent },
      { path: 'users/new', component: UserFormComponent },
      { path: 'users/edit/:id', component: UserFormComponent },
      { path: 'config', component: ConfigScheduleComponent },
      { path: '**', redirectTo: 'init' },
    ],
  },
  {
    path: 'client',
    children: [
      {
        path: 'init',
        component: ClientHomeComponent
      },
      {
        path: 'company',
        component: CompanyComponent
      },
      {
        path: 'reservation',
        component: ReservationComponent
      },
      
      {
        path: 'courtReservation',
        component: CourtReservationComponent
      },
      
      {
        path: 'myReservation',
        component: MyReservationComponent
      },
      {
        path: '**',
        redirectTo: 'init'
      }
    ]
  },
  { path: '**', component: NotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
