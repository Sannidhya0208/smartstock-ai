import { inject } from '@angular/core';
import {
  CanActivateFn,
  Router
} from '@angular/router';

import {
  AuthService
} from '../services/auth';

export const managerGuard: CanActivateFn = () => {

  const authService =
    inject(AuthService);

  const router =
    inject(Router);

  if (
    authService.isOwner()
    || authService.isManager()
  ) {
    return true;
  }

  return router.createUrlTree([
    '/dashboard'
  ]);
};
