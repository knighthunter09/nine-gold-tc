import {Component, OnInit, DoCheck} from '@angular/core';
import {UserService} from '../_services/user.service';

@Component({
  selector: module.id,
  templateUrl: './services-generation.component.html',
  styleUrls: ['./services-generation.component.scss']
})
export class ServicesGenerationComponent implements OnInit, DoCheck {

  public currentUser: any = {};
  model: any = {};
  loading = false;
  error = '';
  success = '';
  downloadUrl = null;

  constructor(public  userService: UserService) { }

  ngOnInit() {
    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
  }

  ngDoCheck() {
    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
  }

  // generate
  generate() {
    this.loading = true;
    this.userService.generate(this.model).subscribe((result) => {
      this.downloadUrl = result['downloadUrl'];
      this.loading = false;
    }, (err) => {
      this.loading = false;
    });
  }

}
