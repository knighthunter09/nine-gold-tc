import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {UserService} from '../_services/index';

@Component({
  moduleId: module.id,
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent implements OnInit {
  model: any = {};
  loading = false;
  error = '';
  type = 'verify';

  constructor(
    private router: Router,
    private userService: UserService) { }

  ngOnInit() {
  }

  // verify email
  verifyEmail() {
    this.loading = true;
    this.userService.verifyEmail(this.model.username)
      .subscribe(res => {
          this.type = 'reset';
          this.loading = false;
      },
        (err) => {
          this.type = 'reset';
          this.loading = false;
        }
      );
  }

  // reset password
  resetPassword() {
    this.loading = true;
    this.userService.resetPassword(this.model.token, this.model.password )
      .subscribe(res => {
        if (res) {
          this.router.navigate(['/login']);
        } else {
          console.log(res);
          this.error = 'Invalid Token';
        }

      },
        (err) => {

        console.log('error', err);
          this.error = JSON.parse(err._body).message;
          this.loading = false;
        }
      );
  }
}
