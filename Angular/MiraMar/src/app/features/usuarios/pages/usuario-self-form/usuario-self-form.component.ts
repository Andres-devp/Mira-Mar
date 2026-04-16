import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../../../core/services/user.service';

@Component({
  selector: 'app-usuario-self-form',
  templateUrl: './usuario-self-form.component.html',
  styleUrls: ['./usuario-self-form.component.css']
})
export class UsuarioSelfFormComponent {
  usuarioForm: FormGroup;
  currentId: number | null = null;
  pageTitle = 'Editar Mi Usuario';
  submitLabel = 'Guardar cambios';

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
      if (!id) {
        this.router.navigate(['/']);
        return;
      }

      this.currentId = Number(id);
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
    });
  }

  save(): void {
    if (this.usuarioForm.invalid || this.currentId === null) {
      return;
    }

    const payload = this.usuarioForm.value;
    this.userService.update(this.currentId, payload).subscribe({
      next: () => this.router.navigate(['/usuarios', this.currentId]),
      error: (err) => console.error('Error updating own profile:', err)
    });
  }

  cancel(): void {
    if (this.currentId === null) {
      this.router.navigate(['/']);
      return;
    }

    this.router.navigate(['/usuarios', this.currentId]);
  }
}