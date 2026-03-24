import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingPageComponent } from './features/landing/pages/landing-page/landing-page.component';
import { RoomTypeDetailComponent } from './features/room-type/pages/room-type-detail/room-type-detail.component';
import { RoomTypeFormComponent } from './features/room-type/pages/room-type-form/room-type-form.component';
import { RoomTypeListComponent } from './features/room-type/pages/room-type-list/room-type-list.component';

const routes: Routes = [
  { path: '', component: LandingPageComponent },
  { path: 'roomtypes', component: RoomTypeListComponent },
  { path: 'roomtypes/new', component: RoomTypeFormComponent },
  { path: 'roomtypes/:id/edit', component: RoomTypeFormComponent },
  { path: 'roomtypes/:id', component: RoomTypeDetailComponent },
  { path: 'rooms', redirectTo: '/roomtypes', pathMatch: 'full' },
  { path: 'rooms/:id', redirectTo: '/roomtypes/:id', pathMatch: 'full' },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
