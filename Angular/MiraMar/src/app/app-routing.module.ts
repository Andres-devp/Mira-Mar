import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LandingPageComponent } from './features/landing/pages/landing-page/landing-page.component';
import { RoomTypeDetailComponent } from './features/room-type/pages/room-type-detail/room-type-detail.component';
import { RoomTypeFormComponent } from './features/room-type/pages/room-type-form/room-type-form.component';
import { RoomTypeListComponent } from './features/room-type/pages/room-type-list/room-type-list.component';
import { HotelServiceDetailComponent } from './features/hotel-service/pages/hotel-service-detail/hotel-service-detail.component';
import { HotelServiceFormComponent } from './features/hotel-service/pages/hotel-service-form/hotel-service-form.component';
import { HotelServiceListComponent } from './features/hotel-service/pages/hotel-service-list/hotel-service-list.component';
import { HotelServiceTableComponent } from './features/hotel-service/pages/hotel-service-table/hotel-service-table.component';
import { AdminDashboardComponent } from './features/admin/pages/admin-dashboard/admin-dashboard.component';
import { OperatorDashboardComponent } from './features/operator/pages/operator-dashboard/operator-dashboard.component';
import { ErrorPageComponent } from './features/error/pages/error-page/error-page.component';
import { RoomsListComponent } from './features/rooms/pages/rooms-list/rooms-list.component';
import { RoomsTableComponent } from './features/rooms/pages/rooms-table/rooms-table.component';
import { RoomFormComponent } from './features/rooms/pages/room-form/room-form.component';
import { ReservationsTableComponent } from './features/reservations/pages/reservations-table/reservations-table.component';
import { ReservationFormComponent } from './features/reservations/pages/reservation-form/reservation-form.component';
import { ReservationDetailComponent } from './features/reservations/pages/reservation-detail/reservation-detail.component';
import { ReservationUserDetailComponent } from './features/reservations/pages/reservation-user-detail/reservation-user-detail.component';
import { UsuariosTableComponent } from './features/usuarios/pages/usuarios-table/usuarios-table.component';
import { OperadoresTableComponent } from './features/usuarios/pages/operadores-table/operadores-table.component';
import { UsuarioFormComponent } from './features/usuarios/pages/usuario-form/usuario-form.component';
import { UsuarioDetailComponent } from './features/usuarios/pages/usuario-detail/usuario-detail.component';
import { UsuarioSelfFormComponent } from './features/usuarios/pages/usuario-self-form/usuario-self-form.component';
import { LoginComponent } from './features/auth/pages/login/login.component';
import { CreateAccountComponent } from './features/auth/pages/create-account/create-account.component';

const routes: Routes = [
  // --- Públicas ---
  { path: '', component: LandingPageComponent },
  { path: 'rooms', component: RoomsListComponent },

  // --- Rooms (admin) — static paths BEFORE parameterized ---
  { path: 'rooms/table', component: RoomsTableComponent },
  { path: 'rooms/add', component: RoomFormComponent },
  { path: 'rooms/:id/edit', component: RoomFormComponent },
  { path: 'rooms/:id', component: RoomTypeDetailComponent },

  // --- Room Types (admin) ---
  { path: 'roomtypes', component: RoomTypeListComponent },
  { path: 'roomtypes/new', component: RoomTypeFormComponent },
  { path: 'roomtypes/:id/edit', component: RoomTypeFormComponent },
  { path: 'roomtypes/:id', component: RoomTypeDetailComponent },

  // --- Hotel Services — static paths BEFORE parameterized ---
  { path: 'services', component: HotelServiceListComponent },
  { path: 'services/table', component: HotelServiceTableComponent },
  { path: 'services/new', component: HotelServiceFormComponent },
  { path: 'services/:id/edit', component: HotelServiceFormComponent },
  { path: 'services/:id', component: HotelServiceDetailComponent },

  // --- Reservaciones ---
  { path: 'reservations', component: ReservationsTableComponent },
  { path: 'reservations/add', component: ReservationFormComponent },
  { path: 'reservations/user/:id', component: ReservationUserDetailComponent },
  { path: 'reservations/:id', component: ReservationDetailComponent },

  // --- Usuarios ---
  { path: 'usuarios', component: UsuariosTableComponent },
  { path: 'usuarios/add', component: UsuarioFormComponent },
  { path: 'usuarios/edit/:id', component: UsuarioFormComponent },
  { path: 'usuarios/self/edit/:id', component: UsuarioSelfFormComponent },
  { path: 'usuarios/:id', component: UsuarioDetailComponent },

  // --- Operadores ---
  { path: 'operadores', component: OperadoresTableComponent },

  // --- Dashboards (sin navbar ni footer) ---
  {
    path: 'admin',
    component: AdminDashboardComponent,
    data: { showNavbar: false, showFooter: false },
  },
  {
    path: 'operator',
    component: OperatorDashboardComponent,
    data: { showNavbar: false, showFooter: false },
  },

  // --- Auth (sin navbar ni footer) ---
  {
    path: 'login',
    component: LoginComponent,
    data: { showNavbar: false, showFooter: false },
  },
  {
    path: 'create-account',
    component: CreateAccountComponent,
    data: { showNavbar: false, showFooter: false },
  },

  // --- Error (sin navbar ni footer) ---
  {
    path: 'error',
    component: ErrorPageComponent,
    data: { showNavbar: false, showFooter: false },
  },

  // --- Fallback ---
  { path: '**', redirectTo: '' },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
      scrollPositionRestoration: 'enabled',
      anchorScrolling: 'enabled',
    }),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
