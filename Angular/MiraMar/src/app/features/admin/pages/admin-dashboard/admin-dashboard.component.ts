import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AdminStats } from '../../../../core/models/entities';
import { AdminService } from '../../../../core/services/admin.service';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  stats?: AdminStats;

  constructor(
    private adminService: AdminService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.adminService.getStats().subscribe({
      next: (data) => this.stats = data,
      error: (err) => console.error('Error loading admin stats:', err)
    });
  }

  logout(): void {
    this.authService.logout().subscribe(() => {
      this.router.navigate(['/']);
    });
  }
}
