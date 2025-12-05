import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  // adapte ce test selon ton AuthService (token/roles...)
  if (auth.isLoggedIn && auth.isLoggedIn()) {
    return true;
  }

  // rediriger vers login avec redirect
  router.navigate(['/login'], {
    queryParams: { redirect: state.url }
  });
  return false;
};
