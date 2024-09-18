import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Alumno } from 'src/app/models/alumno';
import { Curso } from 'src/app/models/curso';
import { Examen } from 'src/app/models/examen';
import { Pregunta } from 'src/app/models/pregunta';
import { Respuesta } from 'src/app/models/respuesta';


/* Este componente ha sido creado con el comando "ng g c components/alumnos/responder-examen-modal --flat --skipTests=true"
   La opción "--flat" es para que no cree una carpeta adicional(En este caso, la carpeta "responder-examen-modal") dentro de la ruta "components/alumnos/"
   La opción "--skipTests=true" no genera el fichero "spec.ts" de pruebas unitarias
   Este comando registra de manera automática el componente en el módulo principal de la aplicación "app.module.ts"
*/

// Este componente se corresponde con un diálogo de tipo Modal que se lanza, o ejecuta, en tiempo de ejecución de la aplicación

@Component({
  selector: 'app-responder-examen-modal',
  templateUrl: './responder-examen-modal.component.html',
  styleUrls: ['./responder-examen-modal.component.css']
})
export class ResponderExamenModalComponent implements OnInit {
  // Si no se indica un modificador de visibilidad en las propiedades de una clase, por defecto son públicas
  // Las propiedades de esta clase, que se quieran usar en la vista(html) asociada a este componente, tienen que ser públicas
  // Las propiedades privadas sólo se pueden usar dentro de esta clase y no tienen visibilidad en la vista(html)
  // Las propiedades protected se pueden usar en esta clase y en aquellas otras clases que hereden este clase

  curso: Curso; // Propiedad que contiene el curso obtenido desde los datos pasados a este componente tipo Modal
  alumno: Alumno; // Propiedad que contiene el alumno obtenido desde los datos pasados a este componente tipo Modal
  examen: Examen; // Propiedad que contiene el exmane obtenido desde los datos pasados a este componente tipo Modal
  // Almacenar cada respuesta asociándola a su correspondiente pregunta por el id de esa pregunta permite que el alumno pueda modificar el texto de las respuestas de cada pregunta tanta veces como quiera antes de ser enviadas a nuestro backend
  respuestas: Map<number, Respuesta> = new Map<number, Respuesta>(); // Propiedad para almacenar valores de pares con el identificador de cada pregunta del examen y su correspondiente respuesta

  // El constructor se debe usar para inyectar servicios y para realizar inicializaciones simples de propiedades
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any, // Inyectamos los datos que son pasados a este constructor desde el lugar del código(componente "ResponderExamenComponent") donde se lanza, o se abre, el modal en tiempo de ejecución
    public modalRef: MatDialogRef<ResponderExamenModalComponent> // Inyectamos la referencia del modal que se pasa a este constructor desde el lugar del código(componente "ResponderExamenComponent") donde se lanza, o se abre, el modal en tiempo de ejecución
  )
  { }

  // Este método del ciclo de vida de un componente se debe usar para realizar inicializaciones complejas como, por ejemplo, realizar invocaciones a métodos de servicios que realizan peticiones http a backends
  ngOnInit(): void {
    // Obtenemos el curso, el alumno y el examen desde los datos que son pasados a este componente tipo Modal cuando es lanzado, o abierto, en tiempo de ejecución
    this.curso = this.data.curso as Curso; // Como la propiedad "data" es de tipo "any"(cualquier tipo de dato), su propiedad "curso" también lo es. Entonces, tenemos que haces un casting al tipo de dato "Curso" que es el tipo que se corresponde con el tipo de la propiedad "curso" de esta clase
    this.alumno = this.data.alumno as Alumno; // Como la propiedad "data" es de tipo "any"(cualquier tipo de dato), su propiedad "alumno" también lo es. Entonces, tenemos que haces un casting al tipo de dato "Alumno" que es el tipo que se corresponde con el tipo de la propiedad "alumno" de esta clase
    this.examen = this.data.examen as Examen; // Como la propiedad "data" es de tipo "any"(cualquier tipo de dato), su propiedad "examen" también lo es. Entonces, tenemos que haces un casting al tipo de dato "Examen" que es el tipo que se corresponde con el tipo de la propiedad "examen" de esta clase
  }

  // Método que cierra la ventana del modal sin hacer nada más
  cancelar(): void{
    // Cerramos la ventana del modal a través de la referencia del modal inyectada en el constructor de esta clase
    this.modalRef.close();
  }

  // Método que crea una instancia de tipo Respuesta, establece los datos de la respuesta en esa instancia y la almacena para posteriormente enviar todas las respuestas del alumno al backend
  responder(pregunta: Pregunta, event): void{
    const texto: string = event.target.value; // Obtenemos el texto de la respuesta escrita por alumno desde los datos del evento que se recibe en este método como argumento de entrada
    const respuesta: Respuesta = new Respuesta(); // Creamos una instancia de tipo Respuesta
    // Establecemos el alumno, la pregunta, el examen y el texto en la instancia de tipo Respuesta que acabamos de crear
    respuesta.alumno = this.alumno;
    respuesta.pregunta = pregunta;
    const examen: Examen = new Examen();
    examen.id = this.examen.id;
    examen.nombre = this.examen.nombre;
    respuesta.pregunta.examen = examen;
    respuesta.texto = texto;
    // Almacenamos la instancia de tipo Respuesta asociada al identificador de la pregunta correspondiente en la propiedad "respuestas" de esta clase
    this.respuestas.set(pregunta.id, respuesta);
  }

}
