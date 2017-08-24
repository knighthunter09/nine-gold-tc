# Ninegold

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 1.2.1.

## Development server

Run `npm start` for a dev server. 
Development host and port con be configured in `apps > defaults > serve` node in `.angular-cli.json` file. 
Defaults is `http://localhost:4200/`.
The app will automatically reload if you change any of the source files.

## Proxy setting

Frontend requests will be go through proxy according to the setting in `proxy.config.json`, 
You can edit `proxy.config.json` to change development backend server host & port you want to send to. Default is `http://localhost:8080`.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `-prod` flag for a production build.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).
Before running the tests make sure you are serving the app via `ng serve`.

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI README](https://github.com/angular/angular-cli/blob/master/README.md).

## Strip Checkout

Stripe publish key stored in `src/app/app.config.ts` change it as needed
