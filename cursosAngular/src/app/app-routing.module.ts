import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AlumnosComponent } from './components/alumnos/alumnos.component';
import { ExamenesComponent } from './components/examenes/examenes.component';
import { CursosComponent } from './components/cursos/cursos.component';
import { AlumnosFormComponent } from './components/alumnos/alumnos-form.component';
import { CursosFormComponent } from './components/cursos/cursos-form.component';
import { ExamenesFormComponent } from './components/examenes/examenes-form.component';
import { AsignarAlumnosComponent } from './components/cursos/asignar-alumnos.component';
import { AsignarExamenesComponent } from './components/cursos/asignar-examenes.component';
import { ResponderExamenComponent } from './components/alumnos/responder-examen.component';

// En este módulo se configuran las rutas y se mapean con los distintos componentes de nuestra aplicación

const routes: Routes = [
  // Por defecto, si no se indica la propiedad "pathMatch" en las rutas, su valor es "prefix" y signifca que el router empieza a comprobar por la izquierda si hay alguna coincidencia con alguna de esas ruta y para cuando detecta alguna coincidencia
  // Cuando la propiedad "pathMatch" es igual al valor "full", el router comprueba de principio a fin la ruta para detectar si hay alguna coincidencia. Este valor es útil cuando se quiere hacer redirecciones con rutas vacías y así evitar bucles infinitos, porque si se deja o se pone el valor "prefix" en una ruta vacía con una redirección, el router siempre va a detectar coincidencias con esa ruta aplicándose siempre esa redirección en un bucle infinito 
  { path: "", pathMatch: "full", redirectTo: "cursos" }, // Cuando la ruta sea exactamente(de principio a fin) una ruta vacía(página de inicio), hacemos una redirección a la ruta "/cursos" que está mapeada con el componente "CursosComponent"
  { path: "alumnos", component: AlumnosComponent }, // creamos la ruta "/alumnos" y la mapeamos con el componente "AlumnosComponent"
  { path: "alumnos/form", component: AlumnosFormComponent }, // creamos la ruta "/alumnos/form" y la mapeamos con el componente "AlumnosFormComponent"
  { path: "alumnos/form/:id", component: AlumnosFormComponent }, // creamos la ruta "/alumnos/form/:id", pasándole el id de un alumno, y la mapeamos con el componente "AlumnosFormComponent"
  { path: "alumnos/responder-examen/:id", component: ResponderExamenComponent }, // creamos la ruta "/alumnos/responder-examen/:id", pasándole el id de un alumno, y la mapeamos con el componente "ResponderExamenComponent"
  { path: "cursos", component: CursosComponent }, // creamos la ruta "/cursos" y la mapeamos con el componente "CursosComponent"
  { path: "cursos/form", component: CursosFormComponent }, // creamos la ruta "/cursos/form" y la mapeamos con el componente "CursosFormComponent"
  { path: "cursos/form/:id", component: CursosFormComponent }, // creamos la ruta "/cursos/form/:id", pasándole el id de un curso, y la mapeamos con el componente "CursosFormComponent"
  { path: "cursos/asignar-alumnos/:id", component: AsignarAlumnosComponent }, // creamos la ruta "/cursos/asignar-alumnos/:id", pasándole el id de un curso, y la mapeamos con el componente "AsignarAlumnosComponent"
  { path: "examenes", component: ExamenesComponent }, // creamos la ruta "/examenes" y la mapeamos con el componente "ExamenesComponent"
  { path: "examenes/form", component: ExamenesFormComponent }, // creamos la ruta "/examenes/form" y la mapeamos con el componente "ExamenesFormComponent"
  { path: "examenes/form/:id", component: ExamenesFormComponent }, // creamos la ruta "/examenes/form/:id", pasándole el id de un exámen, y la mapeamos con el componente "ExamenesFormComponent"
  { path: "cursos/asignar-examenes/:id", component: AsignarExamenesComponent }, // creamos la ruta "/cursos/asignar-examenes/:id", pasándole el id de un curso, y la mapeamos con el componente "AsignarExamenesComponent"
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
