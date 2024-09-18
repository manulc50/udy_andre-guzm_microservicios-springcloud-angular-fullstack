import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute } from '@angular/router';
import { Alumno } from 'src/app/models/alumno';
import { Curso } from 'src/app/models/curso';
import { Examen } from 'src/app/models/examen';
import { Respuesta } from 'src/app/models/respuesta';
import { AlumnoService } from 'src/app/services/alumno.service';
import { CursoService } from 'src/app/services/curso.service';
import { RespuestaService } from 'src/app/services/respuesta.service';
import Swal from 'sweetalert2';
import { ResponderExamenModalComponent } from './responder-examen-modal.component';
import { VerExamenModalComponent } from './ver-examen-modal.component';

/* Este componente ha sido creado con el comando "ng g c components/alumnos/responder-examen --flat --skipTests=true"
   La opción "--flat" es para que no cree una carpeta adicional(En este caso, la carpeta "responder-examen") dentro de la ruta "components/alumnos/"
   La opción "--skipTests=true" no genera el fichero "spec.ts" de pruebas unitarias
   Este comando registra de manera automática el componente en el módulo principal de la aplicación "app.module.ts"
*/


@Component({
  selector: 'app-responder-examen',
  templateUrl: './responder-examen.component.html',
  styleUrls: ['./responder-examen.component.css']
})
export class ResponderExamenComponent implements OnInit {
  // Si no se indica un modificador de visibilidad en las propiedades de una clase, por defecto son públicas
  // Las propiedades de esta clase, que se quieran usar en la vista(html) asociada a este componente, tienen que ser públicas
  // Las propiedades privadas sólo se pueden usar dentro de esta clase y no tienen visibilidad en la vista(html)
  // Las propiedades protected se pueden usar en esta clase y en aquellas otras clases que hereden este clase

  alumno: Alumno; // Propiedad que contiene el alumno localizado en nuestro backend
  curso: Curso; // Propiedad que contiene el curso asignado al alumno localizado también en nuestro backend
  examenes: Examen[] = []; // Array de exámenes asignados al curso
  dataSource: MatTableDataSource<Examen>; // Origen de datos para la tabla de exámenes pertenecientes al curso(Útil para poder asignarle el paginador a la tabla)
  // El objeto "{ static: true }" se corresponde con la configuración recomendada para el paginador en la documentación de Angular Material para poder usarlo dentro del método "OnInit" perteneciente al ciclo de vida de este componente
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator; // Usando el decorador "@ViewChild", inyectamos el paginador de la vista asociada a este componente en esta propiedad "paginator" 
  pageSizeOptions: number[] = [3, 5, 10, 20, 50]; // Propiedad que contiene un array con opciones de cambio del número de resgistros por página del paginador
  mostrarColumnasExamenes: string[] = ['id', 'nombre', 'asignaturas', 'preguntas', 'responder', 'ver']; // Array con los nombres de las columnas que se tienen que mostrar en la tabla de exámenes pertenecientes al curso

  // El constructor se debe usar para inyectar servicios y para realizar inicializaciones simples de propiedades
  constructor(
    private route: ActivatedRoute, // Inyectamos en el constructor el "ActivatedRoute" de Angular, usando la propiedad "route", para poder usarlo en esta clase y así poder recuperar variables y parámetros de la url
    private cursoService: CursoService, // Inyectamos en el constructor nuestro servicio "CursoService" usando la propiedad "cursoService" para poder usarlo en esta clase
    private alumnoService: AlumnoService,  // Inyectamos en el constructor nuestro servicio "AlumnoService" usando la propiedad "alumnoService" para poder usarlo en esta clase
    private respuestaService: RespuestaService,  // Inyectamos en el constructor nuestro servicio "RespuestaService" usando la propiedad "respuestaService" para poder usarlo en esta clase
    public dialog: MatDialog // Inyectamos este objeto de tipo "MatDialog" para poder abrir nuestro Modal en tiempo de ejecución con las respuestas del exámen. Según la documentación de este componente de Angular Material, se tiene que inyectar en el constructor como public
    )
  { }

  // Este método del ciclo de vida de un componente se debe usar para realizar inicializaciones complejas como, por ejemplo, realizar invocaciones a métodos de servicios que realizan peticiones http a backends
  ngOnInit(): void {
    // Obtenemos un Observable con los parámetros que viajan en la url y nos suscribimos a este Observable para recuperar el parámetro "id" que se corresponde con el id del alumno
    this.route.paramMap.subscribe(params => {
      // Como el método "get" del objeto "params" devuelve el valor de parámetro "id" como una cadena de texto o String, usamos '+' para convertirlo a un tipo de dato numérico
      const id: number = +params.get("id");
      // Invocamos al método "ver", a través de nuestro servicio "alumnoService", para obtener los datos del alumno existente en nuestro backend API REST cuyo id se ha recuperado de la url en el punto anterior
      // Como este método nos devuelve un Observable de tipo Alumno con los datos del alumno, nos suscribimos a este Observable para poder pasar esos datos a la propiedad "alumno" de esta clase y para poder obtener a continuación el curso asignado a ese alumno
      this.alumnoService.ver(id).subscribe(alumno => {
        this.alumno = alumno;
        // Invocamos al método "obtenerCursoPorAlumnoId", a través de nuestro servicio "cursoService", para obtener desde nuestro backend API REST los datos del curso asignado al alumno usando, para ello, el alumno recuperado también desde nuestro backend en el punto anterior
        // Como este método nos devuelve un Observable de tipo Curso con los datos del curso, nos suscribimos a este Observable para poder pasar esos datos a la propiedad "curso" de esta clase y para poder pasar también los datos de los exámenes asociados a ese curso a la propiedad "examenes" también de esta clase
        // También nos suscribimos a este Observable de tipo Curso, para inicializar el paginador a partir del contenido del array de exámenes pertenecientes al curso
        this.cursoService.obtenerCursoPorAlumnoId(this.alumno).subscribe(curso => {
          this.curso = curso;
          // Nos aseguramos previamente de que el alumno está asignado a un curso porque puede haber alumnos que no estén asignados a ningún curso
          // Para ello, si el método anterior "obtenerCursoPorAlumnoId" nos devuelve un Observable con un curso que existe y está definido, significa que el alumno está asignado a ese curso
          // Y, además, si en el objeto del curso existe y está definida la propiedad "examenes", signfica que ese curso tiene asignados exámenes que el alumno puede responder
          this.examenes = (curso && curso.examenes) ? curso.examenes : [];
          // Inicialización del paginador
          // Crea una instancia para la tabla de exámenes pertenecientes al curso de tipo MatTableDataSource<Examen> usando la propiedad "dataSource" de esta clase
          // Usamos el array de exámenes asignados al curso, propiedad "examenes", para el contenido de la tabla
          this.dataSource = new MatTableDataSource<Examen>(this.examenes);
          // Asignamos el paginador, que hemos inyectado en esta clase a través de la propiedad "paginator", a la tabla de exámenes pertenecientes al curso
          this.dataSource.paginator = this.paginator;
          // Establecemos el nombre del label, o etiqueta, del paginador que se refiere al número de registros por página
          this.paginator._intl.itemsPerPageLabel = 'Registros por página';
        });
      });
    });
  }

  // Método que abre una ventana con nuestro componente de tipo Modal "ResponderExamenModalComponent" con las preguntas del examen para que el alumno pueda responderlas
  // Este método también envía a nuestro backend las respuestas del alumno cuando se cierra la ventada del modal
  responderExamen(examen: Examen): void{
    // Usando la propiedad "dialog" de esta clase, abrimos nuestro componente de tipo Modal "ResponderExamenModalComponent"
    // Indicamos en un objeto Json la configuración de la ventana del modal
    // El método "open" del objeto de la propiedad "dialog" de esta clase nos devuelve una referencia al modal
    const modalRef = this.dialog.open(ResponderExamenModalComponent,{
      width: '750px', // Ancho de la ventana del modal
      data: { // Pasamos, como datos del modal, el curso, el alumno y el examen
        curso: this.curso,
        alumno: this.alumno,
        examen // Versión simplificada de la expresion "examen: examen"
      }
    });

    // Nos suscribimos al evento "afterClosed" del modal. Este evento se lanza después de cerrarse la ventana del modal
    // Cuando se cierra la ventana del modal usando su botón de envío(no el de cancelar), significa que el alumno ha respondido las preguntas del examen y enviamos las respuestas a nuestro backend
    // Si el alumno o usuario cierra la ventana del modal usando su botón de envío, "respuestasMap"  existe y está definido porque en este caso emitimos, o enviamos, un Map de tipo Map<number,Respuesta> con las respuestas del alumno 
    // Si el alumno o usuario cierra la ventana del modal usando su botón de cancelación, "respuestasMap" es undefinded porque en este caso no emitimos, o no enviamos, nada
    modalRef.afterClosed().subscribe(respuestasMap => {
      // Comprobamos que "respuestasMap" existe y está definido porque, si se cierra la ventana del modal usando su botón de cancelar, no se emite, o no se envía, nada
      // Sólo se emiten, o se envían, las respuestas del alumno cuando se cierra la ventana del modal usando su botón de envío
      if(respuestasMap){
        // "respuestasMap" es de tipo Map<number,Respuesta> y necesitamos un array de tipo Respuesta para enviar las respuestas a nuestro backend
        const respuestas: Respuesta[] = Array.from(respuestasMap.values()); // Pasamos los valores del map "respuestasMap", que son las respuestas del alumno, a un array de tipo Respuesta para poder enviarlas a nuestro backend
        // Invocamos al método "crear", a través de nuestro servicio "respuestaService", para enviar las respuestas del alumno a nuestro backend API REST para crearlas a partir del array de respuestas obtenido en el punto anterior
        // Como este método nos devuelve un Observable de tipo Respuesta[] con un array que contiene los datos de las respuestas enviadas, nos suscribimos a este Observable para establecer el examen como respondido, y así poder deshabilitar el botón para evitar que el alumno vuelva a repetir examen, y para lanzar una alerta visual de éxito de Sweetalert2 al usuario
        this.respuestaService.crear(respuestas).subscribe(rs => {
          examen.respondido = true;
          Swal.fire('Enviadas:', 'Respuestas enviadas con exito', 'success');
        });
      }
    });
  }

  // Método que abre una ventana con nuestro componente de tipo Modal "VerExamenModalComponent" con las preguntas y las respuestas del examen respondidas por el alumno
  // Para ello, este método obtiene desde nuestro backend las respuestas del examen respondidas por el alumno a través de nuestro servicio "respuestaService"
  verExamen(examen: Examen): void{
    // Invocamos al método "obtenerRespuestasPorAlumnoPorExamen", a través de nuestro servicio "respuestaService", para obtener de nuestro backend API REST las respuestas del examen respondidas por el alumno para enviarlas, como datos, al modal
    // Como este método nos devuelve un Observable de tipo Respuesta[] con un array que contiene los datos de las respuestas del alumno, nos suscribimos a este Observable para abrir la ventana del modal y pasarle, como datos, el curso, el alumno y esas respuestas
    this.respuestaService.obtenerRespuestasPorAlumnoPorExamen(this.alumno, examen).subscribe(rs => {
      // Usando la propiedad "dialog" de esta clase, abrimos nuestro componente de tipo Modal "VerExamenModalComponent"
      // Indicamos en un objeto Json la configuración de la ventana del modal
      // El método "open" del objeto de la propiedad "dialog" de esta clase nos devuelve una referencia al modal
      const modalRef = this.dialog.open(VerExamenModalComponent,{
        width: '750px', // Ancho de la ventana del modal
        data: { // Pasamos, como datos del modal, el curso, el examen y las respuestas
          curso: this.curso,
          examen, // Versión simplificada de la expresión "examen: examen"
          respuestas: rs
        }
      });

      // Nos suscribimos al evento "afterClosed" del modal. Este evento se lanza después de cerrarse la ventana del modal
      // Cuando se cierra la ventana del modal, no hacemos nada porque se trata simplemente de un modal que muestra información sobre las preguntas y respuestas del examen respondidas por el alumno
      modalRef.afterClosed().subscribe(() => console.log('Modal ver-examen cerrado'));
    });
  }

}
