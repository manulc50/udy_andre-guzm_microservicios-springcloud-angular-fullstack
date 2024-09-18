import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { BASE_ENDPOINT } from '../config/app';
import { Alumno } from '../models/alumno';
import { Examen } from '../models/examen';
import { Respuesta } from '../models/respuesta';

/* Este componente ha sido creado con el comando "ng g s services/respuesta --skipTests=true"
   La opción "--skipTests=true" no genera el fichero "spec.ts" de pruebas unitarias
*/

@Injectable({ // Este decorador indica que esta clase es un servicio y nos permite inyectar esta clase o servicio en otra parte de la aplicación, como por ejemplo, en los componentes
  providedIn: 'root' // Esta propiedad hace que no sea necesario registrar este servicio en el módulo principal de la aplicación "app.module.ts"
})
export class RespuestaService {
  // Si no se indica un modificador de visibilidad en las propiedades de una clase, por defecto son públicas
  // Las propiedades privadas sólo se pueden usar dentro de esta clase
  // Las propiedades protected se pueden usar en la clase padre y en aquellas clases hijas que hereden esa clase padre

  private baseEndPoint: string = BASE_ENDPOINT + '/respuestas'; // url del Gateway de nuestro backend API REST donde se encuentra el microservicio de respuestas
  private headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' }); // cabecera para indicar el tipo de contenido que envíamos a nuestro backend API REST mediante peticiones http de tipo Post y Put. Concretamente, estamos indicando que los datos que enviamos a nuestro backend API REST van en formato json

  constructor(private httpClient: HttpClient) { // Inyectamos en el constructor el "HttpClient" de Angular, usando la propiedad "httpClient", para poder usarlo en esta clase y así poder realizar peticiones http a nuestro backend
  }

  crear(respuestas: Respuesta[]): Observable<Respuesta[]>{
    // Realiza una petición http Post a nuestro backend API REST a la url o ruta indicada por la propiedad "baseEndPoint" de esta clase pasándole el array de respuestas que se van a crear en este backend y un objeto de opciones con la cabecera que indica que el tipo de contenido que vamos a enviar va en formato json
    // El método "post" devuelve un Observable genérico de tipo "any" que es necesario convertirlo a nuestro tipo deseado, es decir, a un Observable de tipo Respuesta[]
    return this.httpClient.post<Respuesta[]>(this.baseEndPoint, respuestas, { headers: this.headers }); // En este caso, en lugar de usar el método "pipe" junto con el operador "map" para convertir el Observable de tipo genérico "any", devuelto por el método "post", a un Observable de tipo Respuesta[], dicha conversión la hacemos directamente usando la expresión "<Respuesta[]>" sobre el método "post"
  }

  obtenerRespuestasPorAlumnoPorExamen(alumno: Alumno, examen: Examen): Observable<Respuesta[]>{
    // Realiza una petición http Get a nuestro backend API REST a la url o ruta indicada por la propiedad "baseEndPoint" de esta clase + el texto "/alumno/" + el id del alumno + el texto "/examen/" + el id del examen
    // El método "get" devuelve un Observable genérico de tipo "any" que es necesario convertirlo a nuestro tipo deseado, es decir, a un Observable de tipo Respuesta[]
    // El método "pipe" nos permite aplicar operadores como "map" a un flujo Observable
    return this.httpClient.get(`${this.baseEndPoint}/alumno/${alumno.id}/examen/${examen.id}`).pipe( // En lugar de usar el método "pipe" con el operador "map" para transformar el flujo Observable genérico de tipo "any", devuelto por el método "get", a un flujo Observable de tipo Respuesta[], podemos usar directamente la expresión "<Respuesta[]>" sobre el método "get"
      map(respuesta => respuesta as Respuesta[]) // El operador "as" realiza un casting para convertir, en este caso, el Observable genérico de tipo "any" contenido en "respuesta" en un Observable de tipo Respuesta[]
    );
  }
}
