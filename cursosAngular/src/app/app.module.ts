import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms'; // ReactiveFormsModule es necesario para que funcione correctamente el componente "mat-checkbox" de Angular Material
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatPaginatorModule } from '@angular/material/paginator'; // Importamos el módulo "MatPaginatorModule" de Angular Material para implementar la paginación en las tablas
import { MatTableModule } from '@angular/material/table'; // Importamos el módulo "MatTableModule" de Angular Material para usar sus tablas
import { MatInputModule } from '@angular/material/input'; // Importamos el módulo "MatInputModule" de Angular Material para usar sus inputs
import { MatCheckboxModule } from '@angular/material/checkbox'; // Importamos el módulo "MatCheckboxModule" de Angular Material para usar sus checkboxs
import { MatButtonModule } from '@angular/material/button'; // Importamos el módulo "MatButtonModule" de Angular Material para usar sus buttons
import { MatCardModule } from '@angular/material/card'; // Importamos el módulo "MatCardModule" de Angular Material para usar sus cards
import { MatTabsModule } from '@angular/material/tabs'; // Importamos el módulo "MatTabsModule" de Angular Material para usar sus tabs
import { MatAutocompleteModule } from '@angular/material/autocomplete'; // Importamos el módulo "MatAutocompleteModule" de Angular Material para usar sus autocompletes
import { MatDialogModule } from '@angular/material/dialog'; // Importamos el módulo "MatDialogModule" de Angular Material para usar sus diálogos de tipo Modal
import { MatExpansionModule } from '@angular/material/expansion'; // Importamos el módulo "MatExpansionModule" de Angular Material para usar sus paneles expansivos tipo acordeón

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

// Nuestros componentes del módulo principal
import { AlumnosComponent } from './components/alumnos/alumnos.component';
import { CursosComponent } from './components/cursos/cursos.component';
import { ExamenesComponent } from './components/examenes/examenes.component';
import { AlumnosFormComponent } from './components/alumnos/alumnos-form.component';
import { CursosFormComponent } from './components/cursos/cursos-form.component';
import { ExamenesFormComponent } from './components/examenes/examenes-form.component';
import { AsignarAlumnosComponent } from './components/cursos/asignar-alumnos.component';

// Nuestros módulos
import { LayoutModule } from './layout/layout.module';
import { AsignarExamenesComponent } from './components/cursos/asignar-examenes.component';
import { ResponderExamenComponent } from './components/alumnos/responder-examen.component';
import { ResponderExamenModalComponent } from './components/alumnos/responder-examen-modal.component';
import { VerExamenModalComponent } from './components/alumnos/ver-examen-modal.component';


@NgModule({
  // En este array se registran los componentes del módulo principal de la aplicación
  declarations: [
    AppComponent,
    AlumnosComponent,
    CursosComponent,
    ExamenesComponent,
    AlumnosFormComponent,
    CursosFormComponent,
    ExamenesFormComponent,
    AsignarAlumnosComponent,
    AsignarExamenesComponent,
    ResponderExamenComponent,
    ResponderExamenModalComponent,
    VerExamenModalComponent
  ],
  // En este array se registran los componentes que se ejecutan en tiempo de ejecución como son, por ejemplo, los componentes de diálogo tipo Modal
  entryComponents: [
    ResponderExamenModalComponent,
    VerExamenModalComponent
  ],
  // En este array se registran los módulos que se van a usar dentro del módulo princial de la aplicación
  imports: [
    BrowserModule,
    AppRoutingModule,
    LayoutModule, // registramos nuestro módulo "LayoutModule" que contiene nuestro componente "navbar.component"
    HttpClientModule, // registramos este módulo para poder realizar peticiones http a nuestro backend API REST
    FormsModule, // registramos este módulo para poder manejar formularios en Angular
    BrowserAnimationsModule,
    MatPaginatorModule, // registamos este módulo de Angular Material para poder implementar la paginación en las tablas
    MatTableModule, // registamos este módulo de Angular Material para poder usar sus tablas
    MatInputModule, // registamos este módulo de Angular Material para poder usar sus inputs
    MatCheckboxModule, // registamos este módulo de Angular Material para poder usar sus checkboxs
    MatButtonModule, // registamos este módulo de Angular Material para poder usar sus buttons
    MatCardModule, // registamos este módulo de Angular Material para poder usar sus cards
    MatTabsModule, // registamos este módulo de Angular Material para poder usar sus tabs
    MatAutocompleteModule, // registamos este módulo de Angular Material para poder usar sus autocompletes
    MatDialogModule, // registamos este módulo de Angular Material para poder usar sus diálogos de tipo Modal
    MatExpansionModule, // registamos este módulo de Angular Material para poder usar sus paneles expansivos tipo acordeón
    ReactiveFormsModule // registamos este módulo para poder user el componente "mat-checkbox" de Angular Material
  ],
  // En este array se registran los servicios que se van a usar dentro del módulo princial de la aplicación
  // Si los servicios tienen la propiedad "providedIn" con el valor "root" dentro del decorador @Injectable, no es necesario registrarlos aquí
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
