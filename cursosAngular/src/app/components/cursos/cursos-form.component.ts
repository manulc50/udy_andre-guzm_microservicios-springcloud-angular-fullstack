import { Component} from '@angular/core';
import { CommonFormComponent } from '../common-form.component';
import { Curso } from '../../models/curso';
import { CursoService } from '../../services/curso.service';
import { Router, ActivatedRoute } from '@angular/router';

/* Este componente ha sido creado con el comando "ng g c components/cursos/cursos-form --flat --skipTests=true"
   La opción "--flat" es para que no cree una carpeta adicional(En este caso, la carpeta "cursos-form") dentro de la ruta "components/cursos/"
   La opción "--skipTests=true" no genera el fichero "spec.ts" de pruebas unitarias
   Este comando registra de manera automática el componente en el módulo principal de la aplicación "app.module.ts"
*/

@Component({
  selector: 'app-cursos-form',
  templateUrl: './cursos-form.component.html',
  styleUrls: ['./cursos-form.component.css']
})
export class CursosFormComponent extends CommonFormComponent<Curso,CursoService>{
  // Si no se indica un modificador de visibilidad en las propiedades de una clase, por defecto son públicas
  // Las propiedades de esta clase, que se quieran usar en la vista(html) asociada a este componente, tienen que ser públicas
  // Las propiedades privadas sólo se pueden usar dentro de esta clase y no tienen visibilidad en la vista(html)
  // Las propiedades protected se pueden usar en la clase padre y en aquellas clases hijas que hereden esa clase padre

  // El constructor se debe usar para inyectar servicios y para realizar inicializaciones simples de propiedades
  constructor(
    service: CursoService, // Inyectamos en el constructor nuestro servicio "CursoService" usando la propiedad "service" para poder usarlo en esta clase
    router: Router, // Inyectamos en el constructor el "Router" de Angular, usando la propiedad "router", para poder usarlo en esta clase y así poder navegar a otros componentes a través de sus rutas
    route: ActivatedRoute // Inyectamos en el constructor el "ActivatedRoute" de Angular, usando la propiedad "route", para poder usarlo en esta clase y así poder recuperar variables y parámetros de la url
    ) { 
      super(service,router,route); // Invocamos al constructor de la clase padre "CommonFormComponent" y le pasamos nuestro servicio "service" y el "router" y el "route" de Angular que han sido inyectados previamente en el constructor de esta clase
      this.nombreModelo = "Curso"; // Establecemos el valor de la propiedad "nombreModelo" que heredamos de "CommonFormComponent"
      this.entidad = new Curso(); // Creamos una instancia de tipo Curso y la almacenamos en la propiedad "entidad" que heredamos de "CommonFormComponent"
      this.redirect = "/cursos"; // Establecemos el valor de la propiedad "redirect" que heredamos de "CommonFormComponent"
  }

}
