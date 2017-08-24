import {NgModule}      from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule}    from '@angular/forms';
import {HttpModule} from '@angular/http';

// used to create fake backend
import {MockBackend, MockConnection} from '@angular/http/testing';
import {BaseRequestOptions} from '@angular/http';

import {AppComponent} from './app.component';
import {routing} from './app.routing';

import {AuthGuard} from './_guards/index';
import {AuthenticationService, UserService, GlobalEventsManager} from './_services/index';
import {LoginComponent} from './login/index';
import {FaqComponent} from './faq/faq.component';
import {ServicesGenerationComponent} from './services-generation/services-generation.component';
import {RegisterComponent} from './register/register.component';
import {ForgotPasswordComponent} from './forgot-password/forgot-password.component';
import {ProfileComponent} from './profile/profile.component';
import {EqualValidatorDirective} from './_directives/EqualValidator';
import {HomeComponent} from "./home/home.component";

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    routing
  ],
  declarations: [
    AppComponent,
    LoginComponent,
    FaqComponent,
    ServicesGenerationComponent,
    RegisterComponent,
    ForgotPasswordComponent,
    ProfileComponent,
    EqualValidatorDirective,
    HomeComponent
  ],
  providers: [
    AuthGuard,
    AuthenticationService,
    UserService,
    MockBackend,
    BaseRequestOptions,
    GlobalEventsManager
  ],
  bootstrap: [AppComponent]
})

export class AppModule {
}
