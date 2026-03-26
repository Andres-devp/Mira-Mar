import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './shared/components/navbar/navbar.component';
import { FooterComponent } from './shared/components/footer/footer.component';
import { LandingPageComponent } from './features/landing/pages/landing-page/landing-page.component';
import { RoomTypeListComponent } from './features/room-type/pages/room-type-list/room-type-list.component';
import { RoomTypeFormComponent } from './features/room-type/pages/room-type-form/room-type-form.component';
import { RoomTypeDetailComponent } from './features/room-type/pages/room-type-detail/room-type-detail.component';
import { AmenityCardComponent } from './shared/components/amenity-card/amenity-card.component';
import { RoomCardComponent } from './shared/components/room-card/room-card.component';
import { DiningCardComponent } from './shared/components/dining-card/dining-card.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    FooterComponent,
    LandingPageComponent,
    RoomTypeListComponent,
    RoomTypeFormComponent,
    RoomTypeDetailComponent,
    AmenityCardComponent,
    RoomCardComponent,
    DiningCardComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
