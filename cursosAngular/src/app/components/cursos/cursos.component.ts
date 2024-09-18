import { Component} from '@angular/core';
import { CommonListarComponent } from '../common-listar.component';
import { Curso } from '../../models/curso';
import { CursoService } from '../../services/curso.service';

/* Este componente ha sido creado con el comando "ng g c components/cursos --skipTests=true"
   La opción "--skipTests=true" no genera el fichero "spec.ts" de pruebas unitarias
   Este comando registra de manera automática el componente en el módulo principal de la aplicación "app.module.ts"
*/

@Component({
  selector: 'app-cursos',
  templateUrl: './cursos.component.html',
  styleUrls: ['./cursos.component.css']
})
export class CursosComponent extends CommonListarComponent<Curso,CursoService>{
  // Si no se indica un modificador de visibilidad en las propiedades de una clase, por defecto son públicas
  // Las propiedades de esta clase, que se quieran usar en la vista(html) asociada a este componente, tienen que ser públicas
  // Las propiedades privadas sólo se pueden usar dentro de esta clase y no tienen visibilidad en la vista(html)
  // Las propiedades protected se pueden usar en la clase padre y en aquellas clases hijas que hereden esa clase padre

  // El constructor se debe usar para inyectar servicios y para realizar inicializaciones simples de propiedades
  constructor(service: CursoService) { // Inyectamos en el constructor nuestro servicio "CursoService" usando la propiedad "service" que heredamos de la clase padre "CommonListarComponent"
    super(service); // Invocamos al constructor de la clase padre "CommonListarComponent" y le pasamos nuestro servicio "service" que ha sido inyectado previamente en el constructor de esta clase
    this.titulo = "Listado de cursos"; // Establecemos el valor de la propiedad "titulo" que heredamos de "CommonListarComponent"
    this.nombreModelo = "Curso"; // Establecemos el valor de la propiedad "nombreModelo" que heredamos de "CommonListarComponent"
  }

}
