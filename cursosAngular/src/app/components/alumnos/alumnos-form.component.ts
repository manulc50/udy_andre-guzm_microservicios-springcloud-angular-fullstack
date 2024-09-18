import { Component } from '@angular/core';
import { Alumno } from 'src/app/models/alumno';
import { AlumnoService } from 'src/app/services/alumno.service';
import { Router, ActivatedRoute } from '@angular/router';
import { CommonFormComponent } from '../common-form.component';
import Swal from 'sweetalert2'; // Importamos Swal para crear alertas visuales con Sweetalert2

/* Este componente ha sido creado con el comando "ng g c components/alumnos/alumnos-form --flat --skipTests=true"
   La opción "--flat" es para que no cree una carpeta adicional(En este caso, la carpeta "alumnos-form") dentro de la ruta "components/alumnos/"
   La opción "--skipTests=true" no genera el fichero "spec.ts" de pruebas unitarias
   Este comando registra de manera automática el componente en el módulo principal de la aplicación "app.module.ts"
*/

@Component({
  selector: 'app-alumnos-form',
  templateUrl: './alumnos-form.component.html',
  styleUrls: ['./alumnos-form.component.css']
})
export class AlumnosFormComponent extends CommonFormComponent<Alumno,AlumnoService>{
  // Si no se indica un modificador de visibilidad en las propiedades de una clase, por defecto son públicas
  // Las propiedades de esta clase, que se quieran usar en la vista(html) asociada a este componente, tienen que ser públicas
  // Las propiedades privadas sólo se pueden usar dentro de esta clase y no tienen visibilidad en la vista(html)
  // Las propiedades protected se pueden usar en la clase padre y en aquellas clases hijas que hereden esa clase padre

  private fotoSeleccionada: File; // Propiedad para almacenar la foto seleccionada por el usuario desde el formulario de la vista(html) asociada a este componente

  // El constructor se debe usar para inyectar servicios y para realizar inicializaciones simples de propiedades
  constructor(
    service: AlumnoService, // Inyectamos en el constructor nuestro servicio "AlumnoService" usando la propiedad "service" que heredamos de la clase padre "CommonFormComponent"
    router: Router, // Inyectamos en el constructor el "Router" de Angular, usando la propiedad "router", para poder usarlo en esta clase y así poder navegar a otros componentes a través de sus rutas
    route: ActivatedRoute // Inyectamos en el constructor el "ActivatedRoute" de Angular, usando la propiedad "route", para poder usarlo en esta clase y así poder recuperar variables y parámetros de la url
    ) { 
      super(service,router,route); // Invocamos al constructor de la clase padre "CommonFormComponent" y le pasamos nuestro servicio "service" y el "router" y el "route" de Angular que han sido inyectados previamente en el constructor de esta clase
      this.nombreModelo = "Alumno"; // Establecemos el valor de la propiedad "nombreModelo" que heredamos de "CommonFormComponent"
      this.entidad = new Alumno(); // Creamos una instancia de tipo Alumno y la almacenamos en la propiedad "entidad" que heredamos de "CommonFormComponent"
      this.redirect = "/alumnos"; // Establecemos el valor de la propiedad "redirect" que heredamos de "CommonFormComponent"
  }

  // Método asociado al evento Change del input de selección y subida de archivos del formulario de la vista(html) asociada a este componente.
  // De esta manera, cada vez que el usuario seleccione una imagen o foto desde este input, se ejecutará este método para almacenar esa foto en la propiedad "fotoSeleccionada" de esta clase
  // El parámetro de entrada "event" contiene todos los datos del evento producido
  seleccionarFoto(event): void{
    // Los archivos seleccionados se encuentran almacenados en la propiedad "files" de la propiedad "target" del objeto "event" que se recibe como parámetro de entrada en este método
    // Como sólo se permite seleccionar un archivo para la imagen del alumno cada vez que se use el input asociado a este método mediante su evento Change, siempre accedemos a la posición 0 del array de archivos contenido en la propiedad "files"
    this.fotoSeleccionada = event.target.files[0];
    // Mostramos por consola los datos del archivo seleccionado por el usuario
    console.info(this.fotoSeleccionada);
    // Si el archivo seleccionado por el usuario no es de tipo imagen, ponemos la propiedad "fotoSeleccionada" de esta clase a null porque no es un tipo de archivo válido y mostramos al usuario una alerta visual de Sweetalert2 de tipo error
    if(this.fotoSeleccionada.type.indexOf("image") < 0){
      this.fotoSeleccionada = null;
      Swal.fire("Error al seleccionar la foto:","El archivo debe ser de tipo imagen","error");
    }
  }

  // Sobrescribimos este método de la clase padre "CommonFormComponent" para crear un nuevo alumno con o foto o sin foto en nuestro backend API REST a partir de los datos del alumno, introducidos por el usuario en el formulario, contenidos en la propiedad "entidad" de esta clase, y a partir de la imagen o foto, si ha sido seleccionada previamente por el usuario en el formulario, contenida en la propiedad "fotoSeleccionada" de esta clase
  // Este método está asociado al evento OnClick del botón del formulario que se encuentra en la vista(html) asociada a este componente
  crear(): void{
    // Si la propiedad "fotoSeleccionada" no existe o no está definida, significa que el usuario no ha seleccionado ninguna imagen en el formulario y, por lo tanto, invocamos al método "crear" de la clase padre "CommonFormComponent" para que se cree un nuevo alumno sin foto asociada en nuestro backend API REST
    if(!this.fotoSeleccionada)
      super.crear();
    // En caso contrario, como el usuario sí ha seleccionado una imagen o foto en el formulario junto con los datos del alumno introducidos por él, invocamos al método "crearConFoto" de nuestro servicio "service", pasándole los datos del alumno introducidos, contenidos en la propiedad "entidad" de esta clase, y la foto o imagen seleccionada, contenida en la propiedad "fotoSeleccionada" de esta clase, para crear un nuevo alumno con foto asociada en nuestro backend API REST
    else{
      // Como este método nos devuelve un Observable de tipo Alumno con los datos del alumno creado, nos suscribimos a este Observable para poder mostrar esos datos por consola, mostrar también una alerta visual al usuario y navegar a la ruta establecida en la propiedad "redirect" de esta clase
      this.service.crearConFoto(this.entidad,this.fotoSeleccionada).subscribe(alumno => {
        console.log(alumno);
        Swal.fire('Creado:',`${this.nombreModelo} ${alumno.nombre} creado con éxito`,'success');
        this.router.navigate([this.redirect]);
      },
      // Si el método "crearConFoto" nos devuelve un Observable con algún error, significa que nuestro backend API REST ha devuelto ese error en relación con la creación del nuevo alumno
      err => {
        // Si el código de ese error es 400(Bad Request), se trata de un error de validación de datos y significa que los datos del alumno a crear enviados a nuestro backend API REST no son correctos
        // En este caso, pasamos el contenido del error, con los mensajes de error de validación de datos, a la propiedad "error" de esta clase para poder mostrar esos mensajes de error en el formulario de la vista(html) asociada a este componente y también mostramos el contenido de ese error por consola
        if(err.status === 400)
          this.error = err.error;
          console.log(this.error);
      });
    }
  }

  // Sobrescribimos este método de la clase padre "CommonFormComponent" para editar un alumno, con o foto o sin foto, existente en en nuestro backend API REST a partir de los nuevos datos del alumno, introducidos por el usuario en el formulario, contenidos en la propiedad "entidad" de esta clase, a partir de la imagen o foto, si ha sido seleccionada previamente por el usuario en el formulario, contenida en la propiedad "fotoSeleccionada" de esta clase, y también a partir del id de esa entidad
  // Este método está asociado al evento OnClick del botón del formulario que se encuentra en la vista(html) asociada a este componente
  editar(): void{
    // Si la propiedad "fotoSeleccionada" no existe o no está definida, significa que el usuario no ha seleccionado ninguna imagen en el formulario y, por lo tanto, invocamos al método "editar" de la clase padre "CommonFormComponent" para que se edite el alumno sin foto asociada en nuestro backend API REST
    if(!this.fotoSeleccionada)
      super.editar();
    // Si la propiedad "fotoSeleccionada" no existe o no está definida, significa que el usuario no ha seleccionado ninguna imagen en el formulario y, por lo tanto, invocamos al método "editarConFoto" de nuestro servicio "service", pasándole los nuevos datos del alumno, contenidos en la propiedad "entidad" de esta clase, la foto o imagen seleccionada, contenida en la propiedad "fotoSeleccionada" de esta clase, y el id de esa entidad, para actualizarlo en nuestro backend API REST
    else{
      // Como este método nos devuelve un Observable de tipo Alumno con los datos del alumno editado, nos suscribimos a este Observable para poder mostrar esos datos por consola, mostrar también una alerta visual al usuario y navegar a la ruta establecida en la propiedad "redirect" de esta clase
      this.service.editarConFoto(this.entidad,this.fotoSeleccionada,this.entidad.id).subscribe(alumno => {
        console.log(alumno);
        Swal.fire('Editado:',`${this.nombreModelo} ${alumno.nombre} actualizado con éxito`,'success');
        this.router.navigate([this.redirect]);
      },
      // Si el método "editarConFoto" nos devuelve un Observable con algún error, significa que nuestro backend API REST ha devuelto ese error en relación con la edición del nuevo alumno
      err => {
        // Si el código de ese error es 400(Bad Request), se trata de un error de validación de datos y significa que los datos del alumno a editar enviados a nuestro backend API REST no son correctos
        // En este caso, pasamos el contenido del error, con los mensajes de error de validación de datos, a la propiedad "error" de esta clase para poder mostrar esos mensajes de error en el formulario de la vista(html) asociada a este componente y también mostramos el contenido de ese error por consola
        if(err.status === 400)
          this.error = err.error;
          console.log(this.error);
      });
    }
  }

}
