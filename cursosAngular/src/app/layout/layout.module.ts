import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './navbar/navbar.component';
import { AppRoutingModule } from '../app-routing.module';

/* Este módulo ha sido creado con el comando "ng g m layout" */

@NgModule({
  // En este array se registran los componentes del módulo Layout
  declarations: [NavbarComponent],
  // En esta array se registran los componentes de este módulo Layout que se quieren usar en otras partes de la aplicación
  exports: [NavbarComponent],
  // En este array se registran los módulos que se van a usar dentro de este módulo Layout
  imports: [
    CommonModule,
    AppRoutingModule
  ]
})
export class LayoutModule { }
