import {Injectable} from '@angular/core';
import {Router, CanActivate} from '@angular/router';
import {GlobalEventsManager} from '../_services/index';

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private globalEventsManager: GlobalEventsManager) {
  }

  canActivate() {
    if (localStorage.getItem('currentUser')) {
      // logged in so return true
      this.globalEventsManager.showNavBar.emit(true);
      return true;
    }

    // not logged in so redirect to login page
    this.router.navigate(['/login']);
    this.globalEventsManager.hideNavBar.emit(true);
    return false;
  }
}
