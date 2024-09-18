import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute } from '@angular/router';
import { flatMap, map } from 'rxjs/operators';
import { Curso } from 'src/app/models/curso';
import { Examen } from 'src/app/models/examen';
import { CursoService } from 'src/app/services/curso.service';
import { ExamenService } from 'src/app/services/examen.service';
import Swal from 'sweetalert2';

/* Este componente ha sido creado con el comando "ng g c components/cursos/asignar-examenes --flat --skipTests=true"
   La opción "--flat" es para que no cree una carpeta adicional(En este caso, la carpeta "asignar-examenes") dentro de la ruta "components/cursos/"
   La opción "--skipTests=true" no genera el fichero "spec.ts" de pruebas unitarias
   Este comando registra de manera automática el componente en el módulo principal de la aplicación "app.module.ts"
*/


@Component({
  selector: 'app-asignar-examenes',
  templateUrl: './asignar-examenes.component.html',
  styleUrls: ['./asignar-examenes.component.css']
})
export class AsignarExamenesComponent implements OnInit {
  // Si no se indica un modificador de visibilidad en las propiedades de una clase, por defecto son públicas
  // Las propiedades de esta clase, que se quieran usar en la vista(html) asociada a este componente, tienen que ser públicas
  // Las propiedades privadas sólo se pueden usar dentro de esta clase y no tienen visibilidad en la vista(html)
  // Las propiedades protected se pueden usar en esta clase y en aquellas otras clases que hereden este clase

  curso: Curso; // Propiedad que contiene el curso localizado en nuestro backend
  autocompleteControl: FormControl = new FormControl(); // Propiedad que nos permite manejar el componente de "matInput" de Angular Material de la vista asociada a este componente para el autocompletado
  examenesFiltrados: Examen[] = []; // Propiedad que contiene los exámenes filtrados por un término de búsqueda obtenidos desde nuestro backend para rellenar el componente de autocompletado
  examenesAsignar: Examen[] = []; // Propiedad que contiene los exámenes que van a ser asignados al curso
  examenes: Examen[] = []; // Array de exámenes asignados al curso
  dataSource: MatTableDataSource<Examen>; // Origen de datos para la tabla de exámenes pertenecientes al curso(Útil para poder asignarle el paginador a la tabla)
  // El objeto "{ static: true }" se corresponde con la configuración recomendada para el paginador en la documentación de Angular Material para poder usarlo dentro del método "OnInit" perteneciente al ciclo de vida de este componente
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator; // Usando el decorador "@ViewChild", inyectamos el paginador de la vista asociada a este componente en esta propiedad "paginator" 
  pageSizeOptions: number[] = [3, 5, 10, 20, 50]; // Propiedad que contiene un array con opciones de cambio del número de resgistros por página del paginador
  tabIndex: number = 0; // Índice de la pestaña del TabGroup(0 - Pestaña con la tabla para asignar exámenes al curso / 1 - Pestaña con la tabla con los exámenes pertenecientes al curso)
  mostrarColumnas: string[] = ['nombre', 'asignatura', 'eliminar']; // Array con los nombres de las columnas que se tienen que mostrar en la tabla de asignar exámenes al curso
  mostrarColumnasExamenes: string[] = ['id', 'nombre', 'asignaturas', 'eliminar']; // Array con los nombres de las columnas que se tienen que mostrar en la tabla de exámenes pertenecientes al curso

  // El constructor se debe usar para inyectar servicios y para realizar inicializaciones simples de propiedades
  constructor(
    private route: ActivatedRoute, // Inyectamos en el constructor el "ActivatedRoute" de Angular, usando la propiedad "route", para poder usarlo en esta clase y así poder recuperar variables y parámetros de la url
    private cursoService: CursoService, // Inyectamos en el constructor nuestro servicio "CursoService" usando la propiedad "cursoService" para poder usarlo en esta clase
    private examenService: ExamenService, // Inyectamos en el constructor nuestro servicio "ExamenService" usando la propiedad "examenService" para poder usarlo en esta clase
  )
  { }

  // Este método del ciclo de vida de un componente se debe usar para realizar inicializaciones complejas como, por ejemplo, realizar invocaciones a métodos de servicios que realizan peticiones http a backends
  ngOnInit(): void {
    // Obtenemos un Observable con los parámetros que viajan en la url y nos suscribimos a este Observable para recuperar el parámetro "id" que se corresponde con el id del curso
    this.route.paramMap.subscribe(params => {
      // Como el método "get" del objeto "params" devuelve el valor de parámetro "id" como una cadena de texto o String, usamos '+' para convertirlo a un tipo de dato numérico
      const id: number = +params.get("id");
      // Invocamos al método "ver", a través de nuestro servicio "cursoService", para obtener los datos del curso existente en nuestro backend API REST cuyo id se ha recuperado de la url en el punto anterior
      // Como este método nos devuelve un Observable de tipo Curso con los datos del curso, nos suscribimos a este Observable para poder pasar esos datos a la propiedad "curso" de esta clase y los exámenes de ese curso a la propiedad "examenes" de esta clase
      // También nos suscribimos a este Observable de tipo Curso, para inicializar el paginador a partir del contenido del array de exámenes pertenecientes al curso
      this.cursoService.ver(id).subscribe(c => {
        this.curso = c;
        this.examenes = this.curso.examenes;
        this.iniciarPaginador();
      });
    });
    
    // Por cada cambio en el componente "matInput", que está enlazado a un autocomplete, la propiedad "valueChanges", que es reactiva, devuelve un Observable con el valor seleccionado en ese autocomplete
    // El valor puedes ser, o un string que se cuando el usuario escribe en el componente "mat-input" un término de búsqueda para el examen, o un objeto de tipo Examen cuando el usuario selecciona un examen obtenido del backend desde el componente de autocompletado
    this.autocompleteControl.valueChanges.pipe(
      // Como los valores pueden ser objetos de tipo Examen o directamente strings, en caso de que sean objetos de tipo Examen, obtenemos el nombre de esos objetos a través de su propiedad "nombre". Y si los valores son directamente strings, no hacemos nada porque directamente son los términos de búsqueda de los exámenes y son de tipo strings
      // Es decir, para obtener los exámenes filtrados desde nuestro backend, simpre tenemos que enviar términos de búsqueda que son de tipo string
      map(valor => typeof valor === 'string' ? valor : valor.nombre),
      // Usamos el operador "flatMap" en lugar de "map" porque, como la propiedad "valueChanges" es reactiva y devuelve un Observable con el valor seleccionado del autocompletado y aquí estamos usando el método "filtrarPorNombre" de nuestro servicio "examenService" que devuelve otro Observable con los éxmanes devueltos por nuestro backend, tenemos 2 niveles de Observables que es necesario aplanar para que el resultado final sea un único Observable con esos exámenes
      flatMap(valor => valor ? this.examenService.filtrarPorNombre(valor) : [])
      // Por último, nos suscribimos a este Observable con los exámenes obtenidos del backend para asignarlos a la propiedad "examenesFiltrados" de esta clase
    ).subscribe(examenes => this.examenesFiltrados = examenes);
  }

  // Método que inicializa un paginador a partir del contenido del array de exámenes pertenecientes al curso
  // Cada vez que se actualice el array de exámenes perteneciente al curso, tenemos que invocar a este método para adaptar el paginador al nuevo contenido de este array de exámenes
  // Es privado porque sólo lo vamos a usar dentro de esta clase
  private iniciarPaginador(): void{
    // Crea una instancia para la tabla de exámenes pertenecientes al curso de tipo MatTableDataSource<Examen> usando la propiedad "dataSource" de esta clase
    // Usamos el array de exámenes asignados al curso, propiedad "examenes", para el contenido de la tabla
    this.dataSource = new MatTableDataSource<Examen>(this.examenes);
    // Asignamos el paginador, que hemos inyectado en esta clase a través de la propiedad "paginator", a la tabla de exámenes pertenecientes al curso
    this.dataSource.paginator = this.paginator;
    // Establecemos el nombre del label, o etiqueta, del paginador que se refiere al número de registros por página
    this.paginator._intl.itemsPerPageLabel = 'Registros por página';
  }

  // Método para que el componente "matInput" muestre el nombre del exámen cuando se selecciona una opción del autocompletado
  mostrarNombre(examen?: Examen): string{
    return examen ? examen.nombre: '';
  }

  // Método que se ejecuta cada vez que se selecciona un examen del componente de autocompletado asociado al componente "mat-input" de la vista de este componente 
  // Este método recibe como parámetro de entrada los datos del evento que contienen el examen seleccionado
  seleccionarExamen(event: MatAutocompleteSelectedEvent): void{
    // Obtenemos el exmane desde los datos del evento que se recibe en este método como parámetro de entrada
    const examen: Examen = event.option.value as Examen;

    // Antes de actualizar el array de exámenes que van a ser asignados al curso añadiendo el nuevo examen seleccionado por el usuario, verificamos que ese examen no esté previamente en ese array y que tampoco esté ya asignado al curso
    // Para ello, invocamos al método privado "existe" de esta clase
    if(!this.existe(examen.id)){
      // Añadimos el examen al array de exámenes que van a ser asignados al curso
      // Si añadimos el examen usando el método "push" de un array, la tabla de los exámenes que se van a asignar al curso no se entera de que el contenido del array a cambiado
      // Entonces, para que esa tabla se entere de los cambios del array que contiene los exámanes, es necesario asignarle cada vez un nuevo array de la siguiente manera
      this.examenesAsignar = this.examenesAsignar.concat(examen);
    }
    // En caso contrario, mostramos una alerta visual de error de Sweetalert2 al usuario
    else
      Swal.fire('Error:', `El examen ${ examen.nombre } ya está asignado al curso`, 'error');

    // Tanto si la validación anterior ha ido bien como si ha ido mal, hacemos una limpieza del autcompletado para prepararlo para una siguiente selección de examen
    // Limpiamos el valor del componente "mat-input" dejándolo como una cadena de texto vacía
    this.autocompleteControl.setValue('');
    // Desmarcamos la opción seleccionada
    event.option.deselect();
    // Dejamos el foco en el componente "mat-input"
    event.option.focus();
  }

  // Método que comprueba, a partir del id de un examen, si ese exámen ya se encuentra en la tabla de exámenes que van a ser asignados al curso o ya se encuentra asignado en ese curso 
  // Es privado porque sólo lo vamos a usar dentro de esta clase
  private existe(id: number): boolean{
    let existe = false;
    // Miramos los examenes que ya están en la tabla para ser asignados al curso y los exámenes que ya están asignados al curso
    this.examenesAsignar.concat(this.examenes).forEach(e => {
      if(id === e.id)
        existe = true;
      }
    );

    return existe;
  }

  // Método que eliminar un examen del array de exámenes que van a ser asignados al curso
  eliminarDelAsignar(examen: Examen): void{
    this.examenesAsignar = this.examenesAsignar.filter(e => e.id !== examen.id);
  }

  // Método que hace uso del servicio "cursoService" para asignar en el backend los exámenes seleccionados con el curso correspondiente
  asignar(): void{
    // Devuelve un Observable de tipo Curso al que nos suscribimos para actualizar el array de exámenes pertenecientes al curso con los nuevos exámenes que se acaban de asignar, para reiniciar el array de exámenes que se acaban de asignar al curso, para actualizar el índice del grupo de pestañas para que se vaya directamente a la pestaña con la tabla de exámenes pertenecientes al curso y para lanzar una alerta visual de éxito de Sweetalert2 al usuario
    this.cursoService.asignarExamenes(this.curso, this.examenesAsignar)
      .subscribe( c => {
        this.examenes = this.examenes.concat(this.examenesAsignar);
        // Como se acaba de actualizar el contenido del array de exámenes del curso, tenemos que volver a incializar el paginador para que tenga en cuenta este nuevo contenido de exámenes
        this.iniciarPaginador();
        this.examenesAsignar = [];
        this.tabIndex = 1;
        Swal.fire('Asignados:',`Exámenes asignados con éxito al curso ${c.nombre}`,'success');
      });
  }

  // Método que hace uso del servicio "cursoService" para eliminar en el backend un examen del curso correspondiente
  // Este examen se pasa a este método como parámetro de entrada
  eliminarExamenDelCurso(examen: Examen): void{
    // Creamos una alerta visual de Sweetalert2 usando su método "fire" de tipo aviso para que pregunte al usuario si realmente desea eliminar el examen del curso y lo confirme
    // Al método "fire" le pasamos un objeto Json con la configuración de nuestra alerta
    Swal.fire({
      title: 'Cuidado!', // Título de alerta
      text: `¿Seguro que deseas eliminar a ${examen.nombre}?`, // Texto de la alerta
      icon: 'warning', // Indicamos el tipo de icono a mostrar en la alerta. En este caso, queremos un icono tipo aviso o warning
      showCancelButton: true, // Hacemos que muestre el botón de cancelación en la alerta visual
      confirmButtonColor: '#3085d6', // Modificamos el color por defecto del botón de confirmación por este
      cancelButtonColor: '#d33', // Modificamos el color por defecto del botón de cancelación por este
      confirmButtonText: 'Sí, eliminar!', // Modificamos el texto por defecto del botón de confirmación por este
      cancelButtonText: 'No, cancelar!' // Modificamos el texto por defecto del botón de cancelación por este
    }).then((result) => {
      // Si el usuario confirma la eliminación del alumno del curso haciendo click en el botón de confirmación de la alerta, se hace lo siguiente:
      if (result.value) {
        // Invocamos al método "eliminarExamen" de nuestro servicio "cursoService", pasándole el curso y el examen a eliminar, para eliminar ese examen del curso en nuestro backend API REST
        // Como este método nos devuelve un Observable de tipo "Curso", nos suscribimos a este Observable para poder actualizar el array de exámenes pertenecientes al curso sin el examen que acabamos de eliminar, para volver a incializar el paginador en función de esta nueva actualización de exámenes y para poder mostrar al usuario otra alerta visual de éxito de Sweetaler2
        this.cursoService.eliminarExamen(this.curso, examen)
          .subscribe( curso => {
            this.examenes = this.examenes.filter(e => e.id !== examen.id);
            // Como se acaba de actualizar el contenido del array de exámenes del curso, tenemos que volver a incializar el paginador para que tenga en cuenta este nuevo contenido de exámenes
            this.iniciarPaginador();
            // Mostramos al usuario otra alerta visual de Sweetalert2 con el título "Eliminado:", un mensaje de éxito y un icono de éxito
            Swal.fire('Eliminado:', `El examen ${ examen.nombre } ha sido eliminado con éxito del curso ${ curso.nombre }`, 'success');
          });
      }
    });   

  }

}
