import { Component} from '@angular/core';
import { AlumnoService } from '../../services/alumno.service';
import { Alumno } from '../../models/alumno';
import { CommonListarComponent } from '../common-listar.component';
import { BASE_ENDPOINT } from '../../config/app';

/* Este componente ha sido creado con el comando "ng g c components/alumnos --skipTests=true"
   La opción "--skipTests=true" no genera el fichero "spec.ts" de pruebas unitarias
   Este comando registra de manera automática el componente en el módulo principal de la aplicación "app.module.ts"
*/

@Component({
  selector: 'app-alumnos',
  templateUrl: './alumnos.component.html',
  styleUrls: ['./alumnos.component.css']
})
export class AlumnosComponent extends CommonListarComponent<Alumno,AlumnoService>{
  // Si no se indica un modificador de visibilidad en las propiedades de una clase, por defecto son públicas
  // Las propiedades de esta clase, que se quieran usar en la vista(html) asociada a este componente, tienen que ser públicas
  // Las propiedades privadas sólo se pueden usar dentro de esta clase y no tienen visibilidad en la vista(html)
  // Las propiedades protected se pueden usar en la clase padre y en aquellas clases hijas que hereden esa clase padre

  baseEndPoint = BASE_ENDPOINT + "/alumnos"; // Esta propiedad indica la url del Gateway de nuestro backend API REST donde se encuentra nuestro microservicio "microservicio-alumnos"

  // El constructor se debe usar para inyectar servicios y para realizar inicializaciones simples de propiedades
  constructor(service: AlumnoService) { // Inyectamos en el constructor nuestro servicio "AlumnoService" usando la propiedad "service" que heredamos de la clase padre "CommonListarComponent"
    super(service); // Invocamos al constructor de la clase padre "CommonListarComponent" y le pasamos nuestro servicio "service" que ha sido inyectado previamente en el constructor de esta clase
    this.titulo = "Listado de alumnos"; // Establecemos el valor de la propiedad "titulo" que heredamos de "CommonListarComponent"
    this.nombreModelo = "Alumno"; // Establecemos el valor de la propiedad "nombreModelo" que heredamos de "CommonListarComponent"
  }

}
