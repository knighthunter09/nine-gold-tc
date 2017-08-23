import { Component, OnInit } from '@angular/core';
import {UserService} from '../_services/user.service';

@Component({
  selector: module.id,
  templateUrl: './services-generation.component.html',
  styleUrls: ['./services-generation.component.scss']
})
export class ServicesGenerationComponent implements OnInit {

  public currentUser: any = {};
  model: any = {};
  loading = false;
  error = '';
  success = '';
  generated= false;
  paymentToken= false;

  constructor(public  userService: UserService) { }

  ngOnInit() {
  }

  // generate
  generate() {
    this.userService.generate(this.model).subscribe();
  }

}
