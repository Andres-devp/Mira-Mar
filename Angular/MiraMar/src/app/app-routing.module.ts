import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingPageComponent } from './features/landing/pages/landing-page/landing-page.component';
import { RoomTypeDetailComponent } from './features/room-type/pages/room-type-detail/room-type-detail.component';
import { RoomTypeFormComponent } from './features/room-type/pages/room-type-form/room-type-form.component';
import { RoomTypeListComponent } from './features/room-type/pages/room-type-list/room-type-list.component';
import { HotelServiceDetailComponent } from './features/hotel-service/pages/hotel-service-detail/hotel-service-detail.component';
import { HotelServiceFormComponent } from './features/hotel-service/pages/hotel-service-form/hotel-service-form.component';
import { HotelServiceListComponent } from './features/hotel-service/pages/hotel-service-list/hotel-service-list.component';

// Nuevas páginas migradas
import { LoginPageComponent } from './features/auth/pages/login-page/login-page.component';
import { RegisterPageComponent } from './features/auth/pages/register-page/register-page.component';
import { AdminDashboardComponent } from './features/admin/pages/admin-dashboard/admin-dashboard.component';
import { UserListComponent } from './features/user/pages/user-list/user-list.component';
import { ReservationListComponent } from './features/reservation/pages/reservation-list/reservation-list.component';
import { RoomListComponent } from './features/room/pages/room-list/room-list.component';
import { OperatorListComponent } from './features/operator/pages/operator-list/operator-list.component';
import { AdminGuard } from './core/guards/admin.guard';

const routes: Routes = [
  { path: '', component: LandingPageComponent },

  // Auth
  { path: 'login', component: LoginPageComponent },
  { path: 'register', component: RegisterPageComponent },

  // Tipos de habitación (público)
  { path: 'roomtypes', component: RoomTypeListComponent },
  { path: 'roomtypes/new', component: RoomTypeFormComponent },
  { path: 'roomtypes/:id/edit', component: RoomTypeFormComponent },
  { path: 'roomtypes/:id', component: RoomTypeDetailComponent },

  // Servicios del hotel (público)
  { path: 'services', component: HotelServiceListComponent },
  { path: 'services/new', component: HotelServiceFormComponent },
  { path: 'services/:id/edit', component: HotelServiceFormComponent },
  { path: 'services/:id', component: HotelServiceDetailComponent },

  // Admin
  { path: 'admin', component: AdminDashboardComponent, canActivate: [AdminGuard] },
  { path: 'admin/users', component: UserListComponent, canActivate: [AdminGuard] },
  { path: 'admin/reservations', component: ReservationListComponent, canActivate: [AdminGuard] },
  { path: 'admin/rooms', component: RoomListComponent, canActivate: [AdminGuard] },
  { path: 'admin/operators', component: OperatorListComponent, canActivate: [AdminGuard] },

  // Fallback
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
