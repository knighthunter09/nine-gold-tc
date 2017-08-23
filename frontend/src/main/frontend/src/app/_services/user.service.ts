﻿import {Injectable} from '@angular/core';
import {Http, Headers, RequestOptions, Response} from '@angular/http';
import {Observable} from 'rxjs';
import 'rxjs/add/operator/map';

import {AuthenticationService} from './index';
import {User} from '../_models/index';

@Injectable()
export class UserService {
  constructor(private http: Http,
              private authenticationService: AuthenticationService) {
  }

  // verify email
  verifyEmail(username: string): Observable<boolean> {
    // add authorization header with jwt token
    const headers: Headers = new Headers();
    headers.append('Content-Type', 'application/json');
    const options = new RequestOptions({headers: headers});

    const urlSearchParams = new URLSearchParams();
    urlSearchParams.append('email', username);
    const body = urlSearchParams.toString();

    // get users from api
    return this.http.put(`/users/forgotPassword?email=${username}`, options)
      .map((response: Response) => {
        return true;
      });
  }

  // reset password
  resetPassword(token: string, password: string): Observable<any> {
    // add authorization header with jwt token
    const headers: Headers = new Headers();
    headers.append('Content-Type', 'application/json');
    const options = new RequestOptions({headers: headers});

    // get users from api
    return this.http.put('/users/updatePassword', JSON.stringify({
      token: token,
      newPassword: password
    }), options)
      .map((response: Response) => {
        if (response) {
          return true;
        } else {
          return response.json();
        }
      });
  }

  // update user
  update(id: string, username: string, password: string): Observable<any> {
    // add authorization header with jwt token
    const headers: Headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('Authorization', 'Basic ' + this.authenticationService.token);
    const options = new RequestOptions({headers: headers});

    // get users from api
    return this.http.put(`/users/${id}`, JSON.stringify({
      email: username,
      id: id,
      password: password
    }), options)
      .map((response: Response) => {
        if (response) {
          return true;
        } else {
          return response.json();
        }
      });
  }

  // update user
  updatePayment(token: string): Observable<any> {
    // add authorization header with jwt token
    const headers: Headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('Authorization', 'Basic ' + this.authenticationService.token);
    const options = new RequestOptions({headers: headers});

    // get users from api
    return this.http.put(`/users/updatePayment`, JSON.stringify({
      token: token
    }), options)
      .map((response: Response) => {
        if (response) {
          return true;
        } else {
          return response.json();
        }
      });
  }

  // generate services
  generate(modal: any): Observable<User[]> {
    // add authorization header with jwt token
    const headers = new Headers({'Authorization': 'Basic ' + this.authenticationService.token});
    const options = new RequestOptions({headers: headers});

    // get users from api
    return this.http.get('/users/generate', options)
      .map((response: Response) => response.json());
  }
}
