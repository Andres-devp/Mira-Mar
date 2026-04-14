import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './shared/components/navbar/navbar.component';
import { FooterComponent } from './shared/components/footer/footer.component';
import { LandingPageComponent } from './features/landing/pages/landing-page/landing-page.component';
import { RoomTypeListComponent } from './features/room-type/pages/room-type-list/room-type-list.component';
import { RoomTypeFormComponent } from './features/room-type/pages/room-type-form/room-type-form.component';
import { RoomTypeDetailComponent } from './features/room-type/pages/room-type-detail/room-type-detail.component';
import { HotelServiceListComponent } from './features/hotel-service/pages/hotel-service-list/hotel-service-list.component';
import { HotelServiceFormComponent } from './features/hotel-service/pages/hotel-service-form/hotel-service-form.component';
import { HotelServiceDetailComponent } from './features/hotel-service/pages/hotel-service-detail/hotel-service-detail.component';
import { AmenityCardComponent } from './shared/components/amenity-card/amenity-card.component';
import { RoomCardComponent } from './shared/components/room-card/room-card.component';
import { DiningCardComponent } from './shared/components/dining-card/dining-card.component';

// Nuevas páginas migradas de Thymeleaf
import { LoginPageComponent } from './features/auth/pages/login-page/login-page.component';
import { RegisterPageComponent } from './features/auth/pages/register-page/register-page.component';
import { AdminDashboardComponent } from './features/admin/pages/admin-dashboard/admin-dashboard.component';
import { UserListComponent } from './features/user/pages/user-list/user-list.component';
import { ReservationListComponent } from './features/reservation/pages/reservation-list/reservation-list.component';
import { RoomListComponent } from './features/room/pages/room-list/room-list.component';
import { OperatorListComponent } from './features/operator/pages/operator-list/operator-list.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    FooterComponent,
    LandingPageComponent,
    RoomTypeListComponent,
    RoomTypeFormComponent,
    RoomTypeDetailComponent,
    HotelServiceListComponent,
    HotelServiceFormComponent,
    HotelServiceDetailComponent,
    AmenityCardComponent,
    RoomCardComponent,
    DiningCardComponent,
    // Nuevas páginas
    LoginPageComponent,
    RegisterPageComponent,
    AdminDashboardComponent,
    UserListComponent,
    ReservationListComponent,
    RoomListComponent,
    OperatorListComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
