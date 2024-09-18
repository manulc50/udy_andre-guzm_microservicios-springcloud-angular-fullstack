import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Curso } from 'src/app/models/curso';
import { Examen } from 'src/app/models/examen';
import { Respuesta } from 'src/app/models/respuesta';


/* Este componente ha sido creado con el comando "ng g c components/alumnos/ver-examen-modal --flat --skipTests=true"
   La opción "--flat" es para que no cree una carpeta adicional(En este caso, la carpeta "ver-examen-modal") dentro de la ruta "components/alumnos/"
   La opción "--skipTests=true" no genera el fichero "spec.ts" de pruebas unitarias
   Este comando registra de manera automática el componente en el módulo principal de la aplicación "app.module.ts"
*/

// Este componente se corresponde con un diálogo de tipo Modal que se lanza, o ejecuta, en tiempo de ejecución de la aplicación

@Component({
  selector: 'app-ver-examen-modal',
  templateUrl: './ver-examen-modal.component.html',
  styleUrls: ['./ver-examen-modal.component.css']
})
export class VerExamenModalComponent implements OnInit {
  // Si no se indica un modificador de visibilidad en las propiedades de una clase, por defecto son públicas
  // Las propiedades de esta clase, que se quieran usar en la vista(html) asociada a este componente, tienen que ser públicas
  // Las propiedades privadas sólo se pueden usar dentro de esta clase y no tienen visibilidad en la vista(html)
  // Las propiedades protected se pueden usar en esta clase y en aquellas otras clases que hereden este clase

  curso: Curso; // Propiedad que contiene el curso obtenido desde los datos pasados a este componente tipo Modal
  examen: Examen; // Propiedad que contiene el examen obtenido desde los datos pasados a este componente tipo Modal
  respuestas: Respuesta[] = []; // Propiedad que contiene las respuestas del alumno obtenidas desde los datos pasados a este componente tipo Modal

  // El constructor se debe usar para inyectar servicios y para realizar inicializaciones simples de propiedades
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any, // Inyectamos los datos que son pasados a este constructor desde el lugar del código(componente "ResponderExamenComponent") donde se lanza, o se abre, el modal en tiempo de ejecución
    public modalRef: MatDialogRef<VerExamenModalComponent> // Inyectamos la referencia del modal que se pasa a este constructor desde el lugar del código(componente "ResponderExamenComponent") donde se lanza, o se abre, el modal en tiempo de ejecución
  )
  { }

  // Este método del ciclo de vida de un componente se debe usar para realizar inicializaciones complejas como, por ejemplo, realizar invocaciones a métodos de servicios que realizan peticiones http a backends
  ngOnInit(): void {
    // Obtenemos el curso, el alumno y las respuestas desde los datos que son pasados a este componente tipo Modal cuando es lanzado, o abierto, en tiempo de ejecución
    this.curso = this.data.curso as Curso; // Como la propiedad "data" es de tipo "any"(cualquier tipo de dato), su propiedad "curso" también lo es. Entonces, tenemos que haces un casting al tipo de dato "Curso" que es el tipo que se corresponde con el tipo de la propiedad "curso" de esta clase
    this.examen = this.data.examen as Examen; // Como la propiedad "data" es de tipo "any"(cualquier tipo de dato), su propiedad "examen" también lo es. Entonces, tenemos que haces un casting al tipo de dato "Examen" que es el tipo que se corresponde con el tipo de la propiedad "examen" de esta clase
    this.respuestas = this.data.respuestas as Respuesta[]; // Como la propiedad "data" es de tipo "any"(cualquier tipo de dato), su propiedad "respuestas" también lo es. Entonces, tenemos que haces un casting al tipo de dato "Respuesta[]" que es el tipo que se corresponde con el tipo de la propiedad "respuestas" de esta clase
  }

  // Método que cierra la ventana del modal sin hacer nada más
  cerrar(): void{
    // Cerramos la ventana del modal a través de la referencia del modal inyectada en el constructor de esta clase
    this.modalRef.close();
  }

}
