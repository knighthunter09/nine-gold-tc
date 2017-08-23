import {Injectable} from '@angular/core';
import {Http, Headers, Response} from '@angular/http';
import {Observable} from 'rxjs';
import 'rxjs/add/operator/map';

@Injectable()
export class AuthenticationService {
  public token: string;

  constructor(private http: Http) {
    // set token if saved in local storage
    const currentUser = JSON.parse(localStorage.getItem('currentUser'));
    this.token = currentUser && currentUser.token;
  }

  login(username: string, password: string): Observable<boolean> {
    const headers: Headers = new Headers();
    headers.append('Authorization', 'Basic ' + btoa(username + ':' + password));
    headers.append('Content-Type', 'application/json');
    return this.http.post('//localhost:8080/auth/login', JSON.stringify({
      username: username,
      password: password
    }), {headers: headers})
      .map((response: any) => {

        const resBody = JSON.parse(response._body);
        if (response.status < 200 || response.status >= 300) {
          throw new Error('This request has failed ' + response.status);
        }
        const token = btoa(username + ':' + password);
        if (token) {
          // set token property
          this.token = token;
          resBody.token = token;

          // store username and jwt token in local storage to keep user logged in between page refreshes
          localStorage.setItem('currentUser', JSON.stringify(resBody));

          // return true to indicate successful login
          return true;
        }
        if (response.error) {
          return false;
        }
      });
  }

  register(username: string, password: string): Observable<boolean> {
    const headers: Headers = new Headers();
    headers.append('Content-Type', 'application/json');
    return this.http.post('/users/basic-monthly', JSON.stringify({
      email: username,
      password: password
    }), {headers: headers})
      .map((response: any) => {
        const resBody = JSON.parse(response._body);
        if (response.status < 200 || response.status >= 300) {
          throw new Error('This request has failed ' + response.status);
        }
        const token = btoa(username + ':' + password);
        if (token) {
          // set token property
          this.token = token;
          resBody.token = token;

          // store username and jwt token in local storage to keep user logged in between page refreshes
          localStorage.setItem('currentUser', JSON.stringify(resBody));

          // return true to indicate successful login
          return true;
        }
      });
  }

  logout(): Observable<void> {
    // clear token remove user from local storage to log user out
    if (this.token) {

      const headers: Headers = new Headers();
      headers.append('Authorization', 'Basic ' + this.token);
      headers.append('Content-Type', 'application/json');
      return this.http.get('/auth/logout', {headers: headers})
        .map((response: Response) => {

            this.token = null;
            localStorage.removeItem('currentUser');
          },
          (error: any) => {
          });
    }
  }
}
