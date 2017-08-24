import { Component, OnInit } from '@angular/core';
import {User} from '../_models/user';
import {UserService} from '../_services/user.service';
import { config } from '../app.config';

@Component({
  moduleId: module.id,
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  public currentUser: any = {};
  model: any = {};
  loading = false;
  error = '';
  success = '';


  constructor(public  userService: UserService) { }

  ngOnInit() {
    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    this.model.username = this.currentUser.email;
  }

  // openCheckout
  openCheckout() {
    const userService = this.userService;
    const handler = (<any>window).StripeCheckout.configure({
      key: config.stripePublishKey,
      locale: 'auto',
      token: function (token: any) {

        userService.updatePayment(token.id).subscribe();
        // You can access the token ID with `token.id`.
        // Get the token ID to your server-side code for use.
      }
    });

    handler.open({
      name: 'Ninegold',
      description: 'Add payment'
    });

  }


  // register
  updateUser() {
    this.loading = true;
    this.userService.update(this.currentUser.id, this.model.username, this.model.password)
      .subscribe(result => {
          this.loading = false;
          this.success = 'Successully Updated';
          this.error = '';
        },
        (err) => {
          this.loading = false;
          this.error = JSON.parse(err._body).message;
          this.success = '';
        }
      );
  }

}
