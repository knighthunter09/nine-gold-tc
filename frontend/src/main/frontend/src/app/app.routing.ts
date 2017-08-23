import { Routes, RouterModule } from '@angular/router';

import { LoginComponent } from './login/index';
import { AuthGuard } from './_guards/index';
import {FaqComponent} from './faq/faq.component';
import {ServicesGenerationComponent} from './services-generation/services-generation.component';
import {RegisterComponent} from './register/register.component';
import {ForgotPasswordComponent} from './forgot-password/forgot-password.component';
import {ProfileComponent} from './profile/profile.component';

const appRoutes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'forgot-password', component: ForgotPasswordComponent },
    { path: 'faq', component: FaqComponent, canActivate: [AuthGuard] },
    { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },
    { path: '', component: ServicesGenerationComponent, canActivate: [AuthGuard] },

    // otherwise redirect to home
    { path: '**', redirectTo: '' }
];

export const routing = RouterModule.forRoot(appRoutes);
