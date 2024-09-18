import { Injectable } from '@angular/core';
import { Examen } from '../models/examen';
import { CommonService } from './common.service';
import { HttpClient } from '@angular/common/http';
import { BASE_ENDPOINT } from '../config/app';
import { Asignatura } from '../models/asignatura';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

/* Este componente ha sido creado con el comando "ng g s services/examen --skipTests=true"
   La opción "--skipTests=true" no genera el fichero "spec.ts" de pruebas unitarias
*/

@Injectable({ // Este decorador indica que esta clase es un servicio y nos permite inyectar esta clase o servicio en otra parte de la aplicación, como por ejemplo, en los componentes
  providedIn: 'root' // Esta propiedad hace que no sea necesario registrar este servicio en el módulo principal de la aplicación "app.module.ts"
})
export class ExamenService extends CommonService<Examen>{
  // Si no se indica un modificador de visibilidad en las propiedades de una clase, por defecto son públicas
  // Las propiedades privadas sólo se pueden usar dentro de esta clase
  // Las propiedades protected se pueden usar en la clase padre y en aquellas clases hijas que hereden esa clase padre

  constructor(httpClient: HttpClient){ // Inyectamos HttpClient en el constructor a través de la propiedad "http" para poder realizar peticiones http a nuestro backend API REST
    super(httpClient);
    this.baseEndPoint = BASE_ENDPOINT + "/examenes"; // Establecemos el valor de la propiedad "baseEndPoint" de la clase padre "CommonService" para indicar la url del Gateway de nuestro backend API REST donde se encuentra nuestro microservicio "microservicio-examenes"
  }

  public findAllAsignaturas(): Observable<Asignatura[]>{
    // Realiza una petición http Get a nuestro backend API REST a la url o ruta formada por el contenido de la propiedad "baseEndPoint" de la clase padre "CommonService" más el texto "/asignaturas"
    // El método "get" devuelve un Observable genérico de tipo "any" que es necesario convertirlo a nuestro tipo deseado, es decir, a un Observable de tipo Asignatura[]
    return this.httpClient.get<Asignatura[]>(this.baseEndPoint + "/asignaturas"); // En este caso, en lugar de usar el método "pipe" junto con el operador "map" para convertir el Observable de tipo genérico "any", devuelto por el método "get", a un Observable de tipo Asignatura[], dicha conversión la hacemos directamente usando la expresión "<Asignatura[]>" sobre el método "get"
  }

  filtrarPorNombre(nombre: string): Observable<Examen[]>{
    // Realiza una petición http Get a nuestro backend API REST a la url o ruta indicada por la propiedad "baseEndPoint" de esta clase + el texto "/filtrar/" + el término de búsqueda
    // El método "get" devuelve un Observable genérico de tipo "any" que es necesario convertirlo a nuestro tipo deseado, es decir, a un Observable de tipo Examen[]
    // El método "pipe" nos permite aplicar operadores como "map" a un flujo Observable
    return this.httpClient.get(`${this.baseEndPoint}/filtrar/${nombre}`).pipe( // En lugar de usar el método "pipe" con el operador "map" para transformar el flujo Observable genérico de tipo "any", devuelto por el método "get", a un flujo Observable de tipo Examen[], podemos usar directamente la expresión "<Examen[]>" sobre el método "get"
      map(resp => resp as Examen[]) // El operador "as" realiza un casting para convertir, en este caso, el Observable genérico de tipo "any" contenido en "resp" en un Observable de tipo Examen[]
    );
  }
}
