import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

// Shared
import { NavbarComponent } from './shared/components/navbar/navbar.component';
import { FooterComponent } from './shared/components/footer/footer.component';
import { AmenityCardComponent } from './shared/components/amenity-card/amenity-card.component';
import { RoomCardComponent } from './shared/components/room-card/room-card.component';
import { DiningCardComponent } from './shared/components/dining-card/dining-card.component';

// Landing
import { LandingPageComponent } from './features/landing/pages/landing-page/landing-page.component';

// Room Types
import { RoomTypeListComponent } from './features/room-type/pages/room-type-list/room-type-list.component';
import { RoomTypeFormComponent } from './features/room-type/pages/room-type-form/room-type-form.component';
import { RoomTypeDetailComponent } from './features/room-type/pages/room-type-detail/room-type-detail.component';

// Hotel Services
import { HotelServiceListComponent } from './features/hotel-service/pages/hotel-service-list/hotel-service-list.component';
import { HotelServiceTableComponent } from './features/hotel-service/pages/hotel-service-table/hotel-service-table.component';
import { HotelServiceFormComponent } from './features/hotel-service/pages/hotel-service-form/hotel-service-form.component';
import { HotelServiceDetailComponent } from './features/hotel-service/pages/hotel-service-detail/hotel-service-detail.component';

// Admin & Operator
import { AdminDashboardComponent } from './features/admin/pages/admin-dashboard/admin-dashboard.component';
import { OperatorDashboardComponent } from './features/operator/pages/operator-dashboard/operator-dashboard.component';

// Error
import { ErrorPageComponent } from './features/error/pages/error-page/error-page.component';

// Rooms
import { RoomsListComponent } from './features/rooms/pages/rooms-list/rooms-list.component';
import { RoomsTableComponent } from './features/rooms/pages/rooms-table/rooms-table.component';
import { RoomFormComponent } from './features/rooms/pages/room-form/room-form.component';

// Reservations
import { ReservationsTableComponent } from './features/reservations/pages/reservations-table/reservations-table.component';

// Usuarios
import { UsuariosTableComponent } from './features/usuarios/pages/usuarios-table/usuarios-table.component';
import { OperadoresTableComponent } from './features/usuarios/pages/operadores-table/operadores-table.component';
import { UsuarioFormComponent } from './features/usuarios/pages/usuario-form/usuario-form.component';
import { UsuarioDetailComponent } from './features/usuarios/pages/usuario-detail/usuario-detail.component';

// Auth
import { LoginComponent } from './features/auth/pages/login/login.component';
import { CreateAccountComponent } from './features/auth/pages/create-account/create-account.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    FooterComponent,
    AmenityCardComponent,
    RoomCardComponent,
    DiningCardComponent,
    LandingPageComponent,
    RoomTypeListComponent,
    RoomTypeFormComponent,
    RoomTypeDetailComponent,
    HotelServiceListComponent,
    HotelServiceTableComponent,
    HotelServiceFormComponent,
    HotelServiceDetailComponent,
    AdminDashboardComponent,
    OperatorDashboardComponent,
    ErrorPageComponent,
    RoomsListComponent,
    RoomsTableComponent,
    RoomFormComponent,
    ReservationsTableComponent,
    UsuariosTableComponent,
    OperadoresTableComponent,
    UsuarioFormComponent,
    UsuarioDetailComponent,
    LoginComponent,
    CreateAccountComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
