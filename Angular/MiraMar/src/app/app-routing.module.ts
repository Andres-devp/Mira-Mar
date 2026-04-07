import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingPageComponent } from './features/landing/pages/landing-page/landing-page.component';
import { RoomTypeDetailComponent } from './features/room-type/pages/room-type-detail/room-type-detail.component';
import { RoomTypeFormComponent } from './features/room-type/pages/room-type-form/room-type-form.component';
import { RoomTypeListComponent } from './features/room-type/pages/room-type-list/room-type-list.component';
import { HotelServiceDetailComponent } from './features/hotel-service/pages/hotel-service-detail/hotel-service-detail.component';
import { HotelServiceFormComponent } from './features/hotel-service/pages/hotel-service-form/hotel-service-form.component';
import { HotelServiceListComponent } from './features/hotel-service/pages/hotel-service-list/hotel-service-list.component';

const routes: Routes = [
  { path: '', component: LandingPageComponent },
  { path: 'roomtypes', component: RoomTypeListComponent },
  { path: 'roomtypes/new', component: RoomTypeFormComponent },
  { path: 'roomtypes/:id/edit', component: RoomTypeFormComponent },
  { path: 'roomtypes/:id', component: RoomTypeDetailComponent },
  { path: 'services', component: HotelServiceListComponent },
  { path: 'services/new', component: HotelServiceFormComponent },
  { path: 'services/:id/edit', component: HotelServiceFormComponent },
  { path: 'services/:id', component: HotelServiceDetailComponent },
  { path: 'rooms', redirectTo: '/roomtypes', pathMatch: 'full' },
  { path: 'rooms/:id', redirectTo: '/roomtypes/:id', pathMatch: 'full' },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
