import { Component} from '@angular/core';
import { CommonFormComponent } from '../common-form.component';
import { Examen } from '../../models/examen';
import { ExamenService } from '../../services/examen.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Asignatura } from '../../models/asignatura';
import { Pregunta } from 'src/app/models/pregunta';
import Swal from 'sweetalert2';

/* Este componente ha sido creado con el comando "ng g c components/examenes/examenes-form --flat --skipTests=true"
   La opción "--flat" es para que no cree una carpeta adicional(En este caso, la carpeta "examenes-form") dentro de la ruta "components/examenes/"
   La opción "--skipTests=true" no genera el fichero "spec.ts" de pruebas unitarias
   Este comando registra de manera automática el componente en el módulo principal de la aplicación "app.module.ts"
*/

@Component({
  selector: 'app-examenes-form',
  templateUrl: './examenes-form.component.html',
  styleUrls: ['./examenes-form.component.css']
})
export class ExamenesFormComponent extends CommonFormComponent<Examen,ExamenService>{
  // Si no se indica un modificador de visibilidad en las propiedades de una clase, por defecto son públicas
  // Las propiedades de esta clase, que se quieran usar en la vista(html) asociada a este componente, tienen que ser públicas
  // Las propiedades privadas sólo se pueden usar dentro de esta clase y no tienen visibilidad en la vista(html)
  // Las propiedades protected se pueden usar en la clase padre y en aquellas clases hijas que hereden esa clase padre

  asignaturasPadre: Asignatura[] = [];
  asignaturasHija: Asignatura[] = [];
  asignaturaPadreSeleccionada: Asignatura;
  errorPreguntas: string;

  // El constructor se debe usar para inyectar servicios y para realizar inicializaciones simples de propiedades
  constructor(
    service: ExamenService, // Inyectamos en el constructor nuestro servicio "ExamenService" usando la propiedad "service" para poder usarlo en esta clase
    router: Router, // Inyectamos en el constructor el "Router" de Angular, usando la propiedad "router", para poder usarlo en esta clase y así poder navegar a otros componentes a través de sus rutas
    route: ActivatedRoute // Inyectamos en el constructor el "ActivatedRoute" de Angular, usando la propiedad "route", para poder usarlo en esta clase y así poder recuperar variables y parámetros de la url
    ) { 
      super(service,router,route); // Invocamos al constructor de la clase padre "CommonFormComponent" y le pasamos nuestro servicio "service" y el "router" y el "route" de Angular que han sido inyectados previamente en el constructor de esta clase
      this.nombreModelo = "Examen"; // Establecemos el valor de la propiedad "nombreModelo" que heredamos de "CommonFormComponent"
      this.entidad = new Examen(); // Creamos una instancia de tipo Examen y la almacenamos en la propiedad "entidad" que heredamos de "CommonFormComponent"
      this.redirect = "/examenes"; // Establecemos el valor de la propiedad "redirect" que heredamos de "CommonFormComponent"
  }

  // Sobrescribimos este método de la clase padre "CommonFormComponent", que se corresponde con un método del ciclo de vida de un componente, que se debe usar para realizar inicializaciones complejas como, por ejemplo, realizar invocaciones a métodos de servicios que realizan peticiones http a backends
  ngOnInit(): void {
    // Obtenemos un Observable con los parámetros que viajan en la url y nos suscribimos a este Observable para recuperar el parámetro "id" que se corresponde con el id de la entidad
    this.route.paramMap.subscribe(params => {
      // Como el método "get" del objeto "params" devuelve el valor de parámetro "id" como una cadena de texto o String, usamos '+' para convertirlo a un tipo de dato numérico
      const id: number = +params.get("id");

      // Invocamos al método "findAllAsignaturas", a través de nuestro servicio "service", para obtener todas las asignaturas existentes en nuestro backend API REST 
      // Como este método nos devuelve un Observable de tipo Asignaturas[] con los datos de las asignaturas existentes, nos suscribimos a este Observable para poder pasar esos datos a la propiedad "asignaturasPadre" de esta clase y para realizar más tareas
      // Como este método nos está devolviendo tanto las asignaturas padres como las asignaturas hijas y, por ahora, sólo nos interesa las asignaturas padres, usamos el método "filter" para filtar el array de asignaturas del Observable para quedarnos sólo con las asignaturas padres, es decir, aquellas asignaturas cuya propiedad "padre" es null
      this.service.findAllAsignaturas().subscribe(asignaturas => {
        this.asignaturasPadre = asignaturas.filter(a => !a.padre);
        // Como este componente está asociado tanto a la ruta para crear una nueva entidad Examen como a la ruta para editar una entidad Examen existente a través de su id, si el parámetro "id" recuperado en el paso anterior existe, significa que estamos en una edición de una entidad Examen y hacemos lo siguiente:
        if(id){
          // Establecemos el valor de la propiedad "titulo" de esta clase con el texto correspondiente para ser mostrado en la vista(html) asociada a este componente con el formulario de edición de un exámen
          this.titulo = `Editar ${this.nombreModelo}`;
          // Invocamos al método "ver" de nuestro servicio "service", pasándole el id de la entidad recuperado de la url, para obtener de nuestro backend API REST los datos de la entidad a partir de ese id
          // Como este método nos devuelve un Observable con los datos de la entidad Examen localizada a partir de su id, nos suscribimos a este Observable para porder pasar esos datos a la propiedad "entidad" de esta clase, pasar los datos de la asignatura padre de esta entidad Examen a la propiedad "asignaturaPadreSeleccionada" de esta clase e invocar al método "cargarHijas" también de esta clase
          this.service.ver(id).subscribe(examen => {
            this.entidad = examen;
            this.asignaturaPadreSeleccionada = examen.asignatura.padre;
            this.cargarHijas();
          });
        }
        // En caso contrario, es decir, si el parámetro "id" recuperado de la url no existe, significa que estamos en la creación de una nueva entidad Examen y hacemos lo siguiente:
        else
          // Establecemos el valor de la propiedad "titulo" de esta clase con el texto correspondiente para ser mostrado en la vista(html) asociada a este componente con el formulario de creación de un exámen
          this.titulo = `Crear ${this.nombreModelo}`;
      });
    });
  }

  // Sobrescribimos este método de la clase padre "CommonFormComponent"
  crear(): void{
    // Si el nuevo exámen a crear no tiene preguntas asociadas, ponemos un mensaje de error en la propiedad "errorPreguntas" de esta clase para que se muestre el contenedor o div relacionado con la validación de errores sobre las preguntas del exámen en la vista(html) asociada a este componente y nos salimos de este método
    if(this.entidad.preguntas.length === 0){
      this.errorPreguntas = "El exámen debe tener preguntas asociadas";
      return;
    }
    // En caso contrario, es decir, si el exámen tiene preguntas asociadas, hacemos lo siguiente:
    // Ponemos la propiedad "errorPreguntas" de esta clase al valor undefinded para que no se muestre el contenedor o div relacionado con la validación de errores sobre las preguntas del exámen en la vista(html) asociada a este componente
    this.errorPreguntas = undefined;
    // Invocamos al método "eliminarPreguntasVacias" de está clase para eliminar las preguntas vacías que pudieran haber antes de llamar a nuestro backend API REST para crear el nuevo exámen
    this.eliminarPreguntasVacias();
    // Invocamos al método "crear" de la clase padre "CommonFormComponent" para que cree el nuevo exámen en nuestro backend API REST mediante una petición http Post
    super.crear();
  }

  // Sobrescribimos este método de la clase padre "CommonFormComponent"
  editar(): void{
    // Si el exámen a editar no tiene preguntas asociadas, ponemos un mensaje de error en la propiedad "errorPreguntas" de esta clase para que se muestre el contenedor o div relacionado con la validación de errores sobre las preguntas del exámen en la vista(html) asociada a este componente y nos salimos de este método
    if(this.entidad.preguntas.length === 0){
      this.errorPreguntas = "El exámen debe tener preguntas asociadas";
      return;
    }
    // En caso contrario, es decir, si el exámen tiene preguntas asociadas, hacemos lo siguiente:
    // Ponemos la propiedad "errorPreguntas" de esta clase al valor undefinded para que no se muestre el contenedor o div relacionado con la validación de errores sobre las preguntas del exámen en la vista(html) asociada a este componente
    this.errorPreguntas = undefined;
    // Invocamos al método "eliminarPreguntasVacias" de está clase para eliminar las preguntas vacías que pudieran haber antes de llamar a nuestro backend API REST para editar el exámen existente
    this.eliminarPreguntasVacias();
    // Invocamos al método "editar" de la clase padre "CommonFormComponent" para que actualice el exámen existente en nuestro backend API REST mediante una petición http Put
    super.editar();
  }

  // Método que obtiene la lista de asignaturas hijas correspondiente a una asignatura padre que ha sido seleccionada por el usuario en el formulario de la vista(html) asociada a este componente
  // Este método está asociada al evento "Change" del elemento select correspondiente a las asignaturas padres del formulario de la vista(html) asociada a este componente. De esta manera, cada vez que se seleccione una asignatura padre en este elemento select, se ejecutará este método
  cargarHijas(): void{
    const asignaturaPadre: Asignatura = this.asignaturaPadreSeleccionada ? this.asignaturasPadre.filter(a => a.id === this.asignaturaPadreSeleccionada.id)[0] : undefined;
    this.asignaturasHija = asignaturaPadre ? asignaturaPadre.hijos : [];
  }

  // Método que comprueba si dos asignaturas son iguales y devuelve true, si son iguales, o false, si no lo son
  // Este método está asociado a la directiva "compareWith" de Angular para los elementos "select" de las asignaturas padres y de las asignaturas hijas que se encuentran en la vista(html) asociada a este componente  
  // Este método recibe 2 argumentos de entrada de tipo Asignatura que se corresponden con las asignaturas a comparar
  compararAsignatura(a1: Asignatura,a2: Asignatura): boolean{
    // Si las 2 asignaturas son undefined, devolvemos true porque son iguales
    if(a1 === undefined && a2 === undefined)
      return true;

    // Si alguna de las 2 asignaturas es null o undefined, pero la otra no lo es, devolvemos false porque no son iguales
    // Por último, si las 2 asignaturas no son null ni undefined, devolvemos el resultado de comparar sus ids
    return (a1 === null || a2 === null || a1 === undefined || a2 === undefined) ? false : (a1.id === a2.id);
  }

  // Método que agrega una nueva instancia de tipo Pregunta a la lista de preguntas asociada a un exámen
  // Este método está asociado al evento "Click" del botón "Agregar Pregunta" de la vista(html) asociada a este componente. De esta manera, cada vez que se apriete este botón, se ejcutará este método
  agregarPregunta(): void{
    this.entidad.preguntas.push(new Pregunta());
  }

  // Método para asignar el texto, introducido por el usuario en el input de la pregunta correspondiente del formulario, a la pregunta de un exámen
  // Este método está asociado al evento "Change" del input de la pregunta correspondiente del formulario de la vista(html) asociada a este componente. De esta manera, cada vez que haya cambios en este input, se ejecutará este método
  // Este método recibe los argumentos de entrada "pregunta" de tipo Pregunta, que se corresponde con la pregunta del exámen cuyo texto se establecerá con el texto introducido por el usuario en el input de la pregunta correpondiente del formulario, y "event" de tipo any, que se corresponde con el objeto que contiene los datos del evento de tipo Change producido en ese input
  asignarTexto(pregunta: Pregunta, event: any): void{
    pregunta.texto = event.target.value as string;
  }

  // Método para eliminar una pregunta de la lista de preguntas asociada a un exámen
  // Este método está asociado al evento "Click" del botón "x" de la vista(html) asociada a este componente. De esta manera, cada vez que se apriete este botón, se ejeuctará este método
  // Este método recibe el argumento de entrada "pregunta" de tipo Pregunta que se corresponde con la pregunta a eliminar del exámen
  eliminarPregunta(pregunta: Pregunta): void{
    this.entidad.preguntas = this.entidad.preguntas.filter(p => pregunta.texto !== p.texto);
  }

  // Método para eliminar las preguntas vacías que podrían estár asociadas a un exámen antes de crearlo o editarlo en nuestro backend API REST
  eliminarPreguntasVacias(): void{
    this.entidad.preguntas = this.entidad.preguntas.filter(p => p.texto != null && p.texto.length > 0);
  }

}
