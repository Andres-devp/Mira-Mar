import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../../../core/services/user.service';

@Component({
  selector: 'app-usuario-form',
  templateUrl: './usuario-form.component.html',
  styleUrls: ['./usuario-form.component.css']
})
export class UsuarioFormComponent {
  usuarioForm: FormGroup;
  isNew = true;
  currentId: number | null = null;
  pageTitle = 'Nuevo Usuario';
  submitLabel = 'Crear usuario';

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.usuarioForm = this.fb.group({
      nombre: ['', Validators.required],
      usuario: ['', Validators.required],
      contrasena: ['', Validators.required],
      email: [''],
      telefono: ['']
    });

    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.isNew = false;
        this.currentId = Number(id);
        this.pageTitle = 'Editar Usuario';
        this.submitLabel = 'Guardar cambios';
        this.userService.getById(this.currentId).subscribe({
          next: (user) => {
            this.usuarioForm.patchValue({
              nombre: user.nombre,
              usuario: user.usuario,
              contrasena: '',
              email: user.email,
              telefono: user.telefono || ''
            });
          },
          error: (err) => console.error('Error loading user:', err)
        });
      }
    });
  }

  save(): void {
    if (this.usuarioForm.valid) {
      const payload = this.usuarioForm.value;
      if (this.isNew) {
        this.userService.create(payload).subscribe({
          next: () => this.router.navigate(['/usuarios']),
          error: (err) => console.error('Error creating user:', err)
        });
      } else if (this.currentId !== null) {
        this.userService.update(this.currentId, payload).subscribe({
          next: () => this.router.navigate(['/usuarios']),
          error: (err) => console.error('Error updating user:', err)
        });
      }
    }
  }
}
