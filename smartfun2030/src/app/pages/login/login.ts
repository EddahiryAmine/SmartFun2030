import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    RouterLink,
    CommonModule
  ],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent {

  email: string = '';
  password: string = '';
  errorMessage: string = '';
  infoMessage: string = '';

  constructor(
    private auth: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.queryParamMap.subscribe(params => {
      const msg = params.get('verified');
      if (msg) {
        this.infoMessage = msg;
      }
    });
  }

  onLogin() {
    const data = { email: this.email, password: this.password };

    this.auth.login(data).subscribe({
      next: (res) => {

        // 1) Stocker le token
        this.auth.saveToken(res.token);

        // 2) Récupérer les rôles
        const roles = this.auth.getRoles();

        // 3) Redirection selon le rôle
        if (roles.includes('ADMIN')) {
          this.router.navigate(['/admin']);
        } else {
          this.router.navigate(['/home']);
        }
      },

      error: (err) => {
        console.error(err);
        this.errorMessage = err.error || 'Email ou mot de passe incorrect';
      }
    });
  }
}
