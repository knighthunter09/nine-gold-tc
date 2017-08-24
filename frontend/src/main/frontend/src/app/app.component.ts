import { Component, HostListener, Inject } from '@angular/core';
import { DOCUMENT } from '@angular/platform-browser';
import {GlobalEventsManager} from './_services/index';
import {Router} from '@angular/router';
import {AuthenticationService} from './_services/authentication.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent {
  title = 'app';

  public navIsFixed = false;
  public isLogIn = false;
  public activeMenuItem = '';

  constructor(@Inject(DOCUMENT) private document: Document, private globalEventsManager: GlobalEventsManager,
              private router: Router,
              private authenticationService: AuthenticationService) {

    // show navbar
    this.globalEventsManager.showNavBar.subscribe((mode: any) => {
      this.isLogIn = true;
    });

    // hide nav bar (login, register, reset password)
    this.globalEventsManager.hideNavBar.subscribe((mode: any) => {
      this.isLogIn = false;
    });

    // set active nav item
    router.events.subscribe((url: any) => {
      this.activeMenuItem = url.url.substring(1);
    });
  }

  // handle scroll effect
  @HostListener('window:scroll', [])
  onWindowScroll() {
    const number = this.document.body.scrollTop;
    if (number > 37) {
      this.navIsFixed = true;
    } else if (this.navIsFixed && number < 37) {
      this.navIsFixed = false;
    }
  }

  // logout
  logout() {
    this.authenticationService.logout().subscribe(
      () => {
        this.router.navigate(['/home']);
        this.globalEventsManager.hideNavBar.emit(true);
      }
    );
  }
}
