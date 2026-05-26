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
import { ChatbotPageComponent } from './features/chatbot/pages/chatbot-page/chatbot-page.component';
import { AuthGuard } from './core/guards/auth.guard';
import { AdminGuard } from './core/guards/admin.guard';
import { OperatorGuard } from './core/guards/operator.guard';
import { AutoRedirectGuard } from './core/guards/auto-redirect.guard';

const routes: Routes = [
  // --- Públicas ---
  { path: '', component: LandingPageComponent, canActivate: [AutoRedirectGuard] },
  { path: 'rooms', component: RoomsListComponent },

  // --- Rooms — static BEFORE parameterized ---
  { path: 'rooms/table', component: RoomsTableComponent, canActivate: [OperatorGuard] },
  { path: 'rooms/add', component: RoomFormComponent, canActivate: [OperatorGuard] },
  { path: 'rooms/:id/edit', component: RoomFormComponent, canActivate: [OperatorGuard] },
  { path: 'rooms/:id', component: RoomTypeDetailComponent },

  // --- Room Types — static BEFORE parameterized ---
  { path: 'roomtypes', component: RoomTypeListComponent },
  { path: 'roomtypes/new', component: RoomTypeFormComponent, canActivate: [OperatorGuard] },
  { path: 'roomtypes/:id/edit', component: RoomTypeFormComponent, canActivate: [OperatorGuard] },
  { path: 'roomtypes/:id', component: RoomTypeDetailComponent },

  // --- Hotel Services — static BEFORE parameterized ---
  { path: 'services', component: HotelServiceListComponent },
  { path: 'services/table', component: HotelServiceTableComponent, canActivate: [OperatorGuard] },
  { path: 'services/new', component: HotelServiceFormComponent, canActivate: [OperatorGuard] },
  { path: 'services/:id/edit', component: HotelServiceFormComponent, canActivate: [OperatorGuard] },
  { path: 'services/:id', component: HotelServiceDetailComponent },

  // --- Reservaciones ---
  { path: 'reservations', component: ReservationsTableComponent, canActivate: [AuthGuard] },
  { path: 'reservations/add', component: ReservationFormComponent, canActivate: [AuthGuard] },
  { path: 'reservations/user/:id', component: ReservationUserDetailComponent, canActivate: [AuthGuard] },
  { path: 'reservations/:id', component: ReservationDetailComponent, canActivate: [AuthGuard] },

  // --- Usuarios ---
  { path: 'usuarios', component: UsuariosTableComponent, canActivate: [OperatorGuard] },
  { path: 'usuarios/add', component: UsuarioFormComponent, canActivate: [AdminGuard] },
  { path: 'usuarios/edit/:id', component: UsuarioFormComponent, canActivate: [OperatorGuard] },
  { path: 'usuarios/self/edit/:id', component: UsuarioSelfFormComponent, canActivate: [AuthGuard] },
  { path: 'usuarios/me', component: UsuarioDetailComponent, canActivate: [AuthGuard] },
  { path: 'usuarios/self/edit', component: UsuarioSelfFormComponent, canActivate: [AuthGuard] },  
  { path: 'usuarios/:id', component: UsuarioDetailComponent, canActivate: [AuthGuard] },

  // --- Operadores ---
  { path: 'operadores', component: OperadoresTableComponent, canActivate: [OperatorGuard] },

  // --- Chatbot ---
  { path: 'chatbot', component: ChatbotPageComponent, canActivate: [AuthGuard] },

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
