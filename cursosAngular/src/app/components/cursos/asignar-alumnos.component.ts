import { Component, OnInit, ViewChild } from '@angular/core';
import { Curso } from 'src/app/models/curso';
import { ActivatedRoute } from '@angular/router';
import { CursoService } from '../../services/curso.service';
import { AlumnoService } from '../../services/alumno.service';
import { Alumno } from 'src/app/models/alumno';
import { SelectionModel } from '@angular/cdk/collections';
import Swal from 'sweetalert2';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';


/* Este componente ha sido creado con el comando "ng g c components/cursos/asignar-alumnos --flat --skipTests=true"
   La opción "--flat" es para que no cree una carpeta adicional(En este caso, la carpeta "asignar-alumnos") dentro de la ruta "components/cursos/"
   La opción "--skipTests=true" no genera el fichero "spec.ts" de pruebas unitarias
   Este comando registra de manera automática el componente en el módulo principal de la aplicación "app.module.ts"
*/

@Component({
  selector: 'app-asignar-alumnos',
  templateUrl: './asignar-alumnos.component.html',
  styleUrls: ['./asignar-alumnos.component.css']
})
export class AsignarAlumnosComponent implements OnInit {
  // Si no se indica un modificador de visibilidad en las propiedades de una clase, por defecto son públicas
  // Las propiedades de esta clase, que se quieran usar en la vista(html) asociada a este componente, tienen que ser públicas
  // Las propiedades privadas sólo se pueden usar dentro de esta clase y no tienen visibilidad en la vista(html)
  // Las propiedades protected se pueden usar en esta clase y en aquellas otras clases que hereden este clase

  curso: Curso; // Propiedad que contiene el curso localizado en nuestro backend
  alumnosAsignar: Alumno[] = []; // Array de alumnos que no pertenecen al curso y se pueden asignar
  alumnos: Alumno[] = []; // Array de alumnos asignados al curso
  dataSource: MatTableDataSource<Alumno>; // Origen de datos para la tabla de alumnos pertenecientes al curso(Útil para poder asignarle el paginador a la tabla)
  // El objeto "{ static: true }" se corresponde con la configuración recomendada para el paginador en la documentación de Angular Material para poder usarlo dentro del método "OnInit" perteneciente al ciclo de vida de este componente
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator; // Usando el decorador "@ViewChild", inyectamos el paginador de la vista asociada a este componente en esta propiedad "paginator" 
  pageSizeOptions: number[] = [3, 5, 10, 20, 50]; // Propiedad que contiene un array con opciones de cambio del número de resgistros por página del paginador
  tabIndex: number = 0; // Índice de la pestaña del TabGroup(0 - Pestaña con la tabla para asignar alumnos al curso / 1 - Pestaña con la tabla con los alumnos pertenecientes al curso)
  mostrarColumnas: string[] = ['nombre', 'apellidos', 'seleccion']; // Array con los nombres de las columnas que se tienen que mostrar en la tabla de asignar alumnos al curso
  mostrarColumnasAlumnos: string[] = ['id','nombre', 'apellidos', 'email', 'eliminar']; // Array con los nombres de las columnas que se tienen que mostrar en la tabla de alumnos pertenecientes al curso
  // Propiedad para almacenar los alumnos seleccionados a través de los checkboxes
  seleccion: SelectionModel<Alumno> = new SelectionModel<Alumno>(true,[]); // Con true indicamos que se permite selección múltiple. Y también le pasamos un array vacío para inicializar la selección y así poder guardar los alumnos seleccionados

  // El constructor se debe usar para inyectar servicios y para realizar inicializaciones simples de propiedades
  constructor(
    private route: ActivatedRoute, // Inyectamos en el constructor el "ActivatedRoute" de Angular, usando la propiedad "route", para poder usarlo en esta clase y así poder recuperar variables y parámetros de la url
    private cursoService: CursoService, // Inyectamos en el constructor nuestro servicio "CursoService" usando la propiedad "cursoService" para poder usarlo en esta clase
    private alumnoService: AlumnoService  // Inyectamos en el constructor nuestro servicio "AlumnoService" usando la propiedad "alumnoService" para poder usarlo en esta clase
    )
  { }

  // Este método del ciclo de vida de un componente se debe usar para realizar inicializaciones complejas como, por ejemplo, realizar invocaciones a métodos de servicios que realizan peticiones http a backends
  ngOnInit(): void {
    // Obtenemos un Observable con los parámetros que viajan en la url y nos suscribimos a este Observable para recuperar el parámetro "id" que se corresponde con el id del curso
    this.route.paramMap.subscribe(params => {
      // Como el método "get" del objeto "params" devuelve el valor de parámetro "id" como una cadena de texto o String, usamos '+' para convertirlo a un tipo de dato numérico
      const id: number = +params.get("id");
      // Invocamos al método "ver", a través de nuestro servicio "cursoService", para obtener los datos del curso existente en nuestro backend API REST cuyo id se ha recuperado de la url en el punto anterior
      // Como este método nos devuelve un Observable de tipo Curso con los datos del curso, nos suscribimos a este Observable para poder pasar esos datos a la propiedad "curso" de esta clase y los alumnos de ese curso a la propiedad "alumnos" de esta clase
      // También nos suscribimos a este Observable de tipo Curso, para inicializar el paginador a partir del contenido del array de alumnos pertenecientes al curso
      this.cursoService.ver(id).subscribe(curso => {
        this.curso = curso;
        this.alumnos = this.curso.alumnos;
        this.iniciarPaginador();
      });
    });
  }

  // Método que inicializa un paginador a partir del contenido del array de alumnos pertenecientes al curso
  // Cada vez que se actualice el array de alumnos perteneciente al curso, tenemos que invocar a este método para adaptar el paginador al nuevo contenido de este array de alumnos
  // Es privado porque sólo lo vamos a usar dentro de esta clase
  private iniciarPaginador(): void{
    // Crea una instancia para la tabla de alumnos pertenecientes al curso de tipo MatTableDataSource<Alumno> usando la propiedad "dataSource" de esta clase
    // Usamos el array de alumnos asignados al curso, propiedad "alumnos", para el contenido de la tabla
    this.dataSource = new MatTableDataSource<Alumno>(this.alumnos);
    // Asignamos el paginador, que hemos inyectado en esta clase a través de la propiedad "paginator", a la tabla de alumnos pertenecientes al curso
    this.dataSource.paginator = this.paginator;
    // Establecemos el nombre del label, o etiqueta, del paginador que se refiere al número de registros por página
    this.paginator._intl.itemsPerPageLabel = 'Registros por página';
  }

  // Método que hace uso del servicio "alumnoService" para obtener de nuestro backend los alumnos que coinciden con el término de búsqueda que se pasa como parámetro de entrada a este método 
  filtrar(nombre: string): void{
    // Nos aseguramos de que el término de búsqueda exista y esté definido y eliminamos espacios en blanco que pudiera tener
    // Si no existe o no está definido, establecemos una cadena de texto vacía como término de búsqueda
    nombre = nombre !== undefined ? nombre.trim() : '';
    // Si al final el término de búsqueda existe, está definido y no es vacío, vamos al backend a por los alumnos que coincidan con dicho término
    if(nombre != '')
      // Devuelve un Observable de tipo Alumno[] al que nos suscribimos para poder obtener el array de alumnos devuelto por el backend
      this.alumnoService.filtrarPorNombre(nombre).subscribe(
        // Establecemos la propiedad "alumnosAsignar" de esta clase con los alumnos recibidos del backend filtrados para que no se incluyan los alumnos que previamente ya están asignados al curso
        alumnos => this.alumnosAsignar = alumnos.filter(a => {
          let filtrar = true;
          this.alumnos.forEach(ca => {
            // Si se cumple esta condición, significa que el alumno ya está asignado al curso y ponemos "filtrar" a false para que ese alumno no se incluya en el array de alumnos para asignar al curso
            if(a.id === ca.id)
              filtrar = false;
          });
          return filtrar;
        })
      );
  }

  // Método que devuelve true o false en función de si están todos los alumnos seleccionados o no
  estanTodosSeleccionados(): boolean{
    const seleccionados = this.seleccion.selected.length; // Número de alumnos seleccionados por el usuario
    const numAlumnos = this.alumnosAsignar.length; // Número total de alumnos devueltos por el backend

    return seleccionados === numAlumnos; // Si ambos números son iguales, devuelve true porque significa que el usuario ha seleccionado todos los alumnos. En caso contrario, devuelve false

  }

  // Método que deselecciona todos los alumnos en caso de estar todos ellos seleccionados y al revés 
  seleccionarDeseleccinarTodos(): void{
    this.estanTodosSeleccionados()
      ? this.seleccion.clear() // Deselecciona todos los alumnos
      : this.alumnosAsignar.forEach(a => this.seleccion.select(a)); // Recorre todos los alumnos recibidos por el backend para seleccionarlos
  }

  // Método que hace uso del servicio "cursoService" para asignar en el backend los alumnos seleccionados con el curso correspondiente
  asignar(): void{
    // Devuelve un Observable de tipo Curso al que nos suscribimos para actualizar el índice del grupo de pestañas para que se vaya directamente a la pestaña con la tabla de alumnos pertenecientes al curso y para lanzar una alerta visual de éxito de Sweetalert2 al usuario
    this.cursoService.asignarAlumnos(this.curso,this.seleccion.selected)
    .subscribe( c => {
      this.tabIndex = 1;
      Swal.fire('Asignados:',`Alumnos asignados con éxito al curso ${c.nombre}`,'success');
    },
    // Si el backend nos devuelve un error de tipo 500 y el mensaje de error contiene el texto "ConstraintViolationException", significa que el alumno no se puede asignar al curso porque ya se encuentra en otro, es decir, sólo se permite que un alumno esé asignado a un único curso
    // Si se cumple esto, mostramos al usuario una alerta visual de error de Sweetalert2
    e => {
      if(e.status === 500){
        const mensaje = e.error.message as string;
        if(mensaje.indexOf('ConstraintViolationException') > -1)
          Swal.fire('Cuidado:','No se puede asignar al alumno ya que está asociado a otro curso','error');
      }
    });

    // Actualizamos el array de alumnos pertenecientes al curso con los nuevos alumnos que se acaban de asignar
    this.alumnos = this.alumnos.concat(this.seleccion.selected);
    // Como se acaba de actualizar el contenido del array de alumnos del curso, tenemos que volver a incializar el paginador para que tenga en cuenta este nuevo contenido de alumnos
    this.iniciarPaginador();

    // Una vez realizada la asignación, limpiamos el array de alumnos devuelto por el backend y la selección de alumnos para la siguiente asignación
    this.alumnosAsignar = [];
    this.seleccion.clear();
  }

  // Método que hace uso del servicio "cursoService" para eliminar en el backend un alumno del curso correspondiente
  // Este alumno se pasa a este método como parámetro de entrada
  eliminarAlumno(alumno: Alumno): void{
    // Creamos una alerta visual de Sweetalert2 usando su método "fire" de tipo aviso para que pregunte al usuario si realmente desea eliminar el alumno del curso y lo confirme
    // Al método "fire" le pasamos un objeto Json con la configuración de nuestra alerta
    Swal.fire({
      title: 'Cuidado!', // Título de alerta
      text: `¿Seguro que deseas eliminar a ${alumno.nombre}?`, // Texto de la alerta
      icon: 'warning', // Indicamos el tipo de icono a mostrar en la alerta. En este caso, queremos un icono tipo aviso o warning
      showCancelButton: true, // Hacemos que muestre el botón de cancelación en la alerta visual
      confirmButtonColor: '#3085d6', // Modificamos el color por defecto del botón de confirmación por este
      cancelButtonColor: '#d33', // Modificamos el color por defecto del botón de cancelación por este
      confirmButtonText: 'Sí, eliminar!', // Modificamos el texto por defecto del botón de confirmación por este
      cancelButtonText: 'No, cancelar!' // Modificamos el texto por defecto del botón de cancelación por este
    }).then((result) => {
      // Si el usuario confirma la eliminación del alumno del curso haciendo click en el botón de confirmación de la alerta, se hace lo siguiente:
      if (result.value) {
        // Invocamos al método "eliminarAlumno" de nuestro servicio "cursoService", pasándole el curso y el alumno a eliminar, para eliminar ese alumno del curso en nuestro backend API REST
        // Como este método nos devuelve un Observable de tipo "Curso", nos suscribimos a este Observable para poder actualizar el array de alumnos pertenecientes al curso sin el alumno que acabamos de eliminar, para volver a incializar el paginador en función de esta nueva actualización de alumnos y para poder mostrar al usuario otra alerta visual de éxito de Sweetaler2
        this.cursoService.eliminarAlumno(this.curso, alumno)
          .subscribe( curso => {
            this.alumnos = this.alumnos.filter(a => a.id !== alumno.id);
            // Como se acaba de actualizar el contenido del array de alumnos del curso, tenemos que volver a incializar el paginador para que tenga en cuenta este nuevo contenido de alumnos
            this.iniciarPaginador();
            // Mostramos al usuario otra alerta visual de Sweetalert2 con el título "Eliminado:", un mensaje de éxito y un icono de éxito
            Swal.fire('Eliminado:', `El alumno ${ alumno.nombre } ha sido eliminado con éxito del curso ${ curso.nombre }`, 'success');
          });
      }
    });   

  }

}
