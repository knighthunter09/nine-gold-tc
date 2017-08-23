import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from '../_services/authentication.service';
import {Router} from '@angular/router';

@Component({
  moduleId: module.id,
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  model: any = {};
  loading = false;
  error = '';

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService) { }

  ngOnInit() {
    // reset login status
    this.authenticationService.logout();
  }

  // register
  register() {
    this.loading = true;
    this.authenticationService.register(this.model.username, this.model.password)
      .subscribe(result => {
        if (result === true) {
          this.router.navigate(['/profile']);
        } else {
          this.error = 'Username or password is incorrect';
          this.loading = false;
        }
      },
        (err) => {
          this.error = JSON.parse(err._body).message;
          this.loading = false;
        }
      );
  }

}
