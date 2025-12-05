import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrls: ['./register.css']
})
export class RegisterComponent {

  firstName = '';
  lastName = '';
  email = '';
  country = '';
  password = '';
  confirmPassword = '';

  errorMessage = '';
  successMessage = '';

  isLoading = false;

  constructor(private auth: AuthService, private router: Router) {}

 onRegister() {
    this.errorMessage = '';
    this.successMessage = '';

    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Les mots de passe ne correspondent pas.';
      return;
    }

    const data = {
      firstName: this.firstName,
      lastName: this.lastName,
      email: this.email,
      country: this.country,
      password: this.password
    };

    this.isLoading = true;

    this.auth.register(data).subscribe({
      next: (res: string) => {
        console.log('Register response:', res);
        this.isLoading = false;

        // Message renvoyé par le backend OU message par défaut
        this.successMessage =
          res && res.trim().length > 0
            ? res
            : "Compte créé. Vérifiez votre adresse email pour l’activer.";

        // Optionnel : vider les champs
        this.password = '';
        this.confirmPassword = '';
      },
      error: (err) => {
        console.error('Register error:', err);
        this.isLoading = false;

        // Essayer de lire un message d'erreur
        const backendMsg =
          err?.error?.message ||
          err?.error ||
          'Une erreur est survenue pendant linscription.';

        this.errorMessage = backendMsg;
      }
    });
  }

}
