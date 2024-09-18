import { Injectable } from '@angular/core';
import { CommonService } from './common.service';
import { Curso } from '../models/curso';
import { BASE_ENDPOINT } from '../config/app';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Alumno } from '../models/alumno';
import { map } from 'rxjs/operators';
import { Examen } from '../models/examen';

/* Este componente ha sido creado con el comando "ng g s services/curso --skipTests=true"
   La opción "--skipTests=true" no genera el fichero "spec.ts" de pruebas unitarias
*/

@Injectable({ // Este decorador indica que esta clase es un servicio y nos permite inyectar esta clase o servicio en otra parte de la aplicación, como por ejemplo, en los componentes
  providedIn: 'root' // Esta propiedad hace que no sea necesario registrar este servicio en el módulo principal de la aplicación "app.module.ts"
})
export class CursoService extends CommonService<Curso>{
  // Si no se indica un modificador de visibilidad en las propiedades de una clase, por defecto son públicas
  // Las propiedades privadas sólo se pueden usar dentro de esta clase
  // Las propiedades protected se pueden usar en la clase padre y en aquellas clases hijas que hereden esa clase padre

  constructor(httpClient: HttpClient){ // Inyectamos HttpClient en el constructor a través de la propiedad "http" para poder realizar peticiones http a nuestro backend API REST
    super(httpClient);
    this.baseEndPoint = BASE_ENDPOINT + "/cursos"; // Establecemos el valor de la propiedad "baseEndPoint" de la clase padre "CommonService" para indicar la url del Gateway de nuestro backend API REST donde se encuentra nuestro microservicio "microservicio-cursos"
  }

  asignarAlumnos(curso: Curso, alumnos: Alumno[]): Observable<Curso>{
    // Realiza una petición http Put a nuestro backend API REST a la url o ruta formada por el contenido de la propiedad "baseEndPoint" de esta clase junto con el id del curso y el texto "/asignar-alumnos" pasándole el array de alumnos que se van a asignar al curso en este backend y un objeto de opciones con la cabecera que indica que el tipo de contenido que vamos a enviar va en formato json
    // El método "put" devuelve un Observable genérico de tipo "any" que es necesario convertirlo a nuestro tipo deseado, es decir, a un Observable de tipo Curso
    return this.httpClient.put<Curso>(`${this.baseEndPoint}/${curso.id}/asignar-alumnos`, alumnos, { headers: this.headers }); // En este caso, en lugar de usar el método "pipe" junto con el operador "map" para convertir el Observable de tipo genérico "any", devuelto por el método "put", a un Observable de tipo Curso, dicha conversión la hacemos directamente usando la expresión "<Curso>" sobre el método "put"
  }

  eliminarAlumno(curso: Curso, alumno: Alumno): Observable<Curso>{
    // Realiza una petición http Put a nuestro backend API REST a la url o ruta formada por el contenido de la propiedad "baseEndPoint" de esta clase junto con el id del curso y el texto "/eliminar-alumno" pasándole el alumno a eliminar del curso en este backend y un objeto de opciones con la cabecera que indica que el tipo de contenido que vamos a enviar va en formato json
    // El método "put" devuelve un Observable genérico de tipo "any" que es necesario convertirlo a nuestro tipo deseado, es decir, a un Observable de tipo Curso
    return this.httpClient.put(`${this.baseEndPoint}/${curso.id}/eliminar-alumno`, alumno, { headers: this.headers }).pipe( // En lugar de usar el método "pipe" con el operador "map" para transformar el flujo Observable genérico de tipo "any", devuelto por el método "put", a un flujo Observable de tipo Curso, podemos usar directamente la expresión "<Curso>" sobre el método "put"
      map(respuesta => respuesta as Curso) // El operador "as" realiza un casting para convertir, en este caso, el Observable genérico de tipo "any" contenido en "respuesta" en un Observable de tipo Curso
    );
  }

  asignarExamenes(curso: Curso, examenes: Examen[]): Observable<Curso>{
    // Realiza una petición http Put a nuestro backend API REST a la url o ruta formada por el contenido de la propiedad "baseEndPoint" de esta clase junto con el id del curso y el texto "/asignar-examenes" pasándole el array de exámenes que se van a asignar al curso en este backend y un objeto de opciones con la cabecera que indica que el tipo de contenido que vamos a enviar va en formato json
    // El método "put" devuelve un Observable genérico de tipo "any" que es necesario convertirlo a nuestro tipo deseado, es decir, a un Observable de tipo Curso
    return this.httpClient.put<Curso>(`${this.baseEndPoint}/${curso.id}/asignar-examenes`, examenes, { headers: this.headers }); // En este caso, en lugar de usar el método "pipe" junto con el operador "map" para convertir el Observable de tipo genérico "any", devuelto por el método "put", a un Observable de tipo Curso, dicha conversión la hacemos directamente usando la expresión "<Curso>" sobre el método "put"
  }

  eliminarExamen(curso: Curso, examen: Examen): Observable<Curso>{
    // Realiza una petición http Put a nuestro backend API REST a la url o ruta formada por el contenido de la propiedad "baseEndPoint" de esta clase junto con el id del curso y el texto "/eliminar-examen" pasándole el examen a eliminar del curso en este backend y un objeto de opciones con la cabecera que indica que el tipo de contenido que vamos a enviar va en formato json
    // El método "put" devuelve un Observable genérico de tipo "any" que es necesario convertirlo a nuestro tipo deseado, es decir, a un Observable de tipo Curso
    return this.httpClient.put(`${this.baseEndPoint}/${curso.id}/eliminar-examen`, examen, { headers: this.headers }).pipe( // En lugar de usar el método "pipe" con el operador "map" para transformar el flujo Observable genérico de tipo "any", devuelto por el método "put", a un flujo Observable de tipo Curso, podemos usar directamente la expresión "<Curso>" sobre el método "put"
      map(respuesta => respuesta as Curso) // El operador "as" realiza un casting para convertir, en este caso, el Observable genérico de tipo "any" contenido en "respuesta" en un Observable de tipo Curso
    );
  }

  obtenerCursoPorAlumnoId(alumno: Alumno): Observable<Curso>{
    // Realiza una petición http Get a nuestro backend API REST a la url o ruta formada por el contenido de la propiedad "baseEndPoint" de esta clase junto con el texto "/alumno/" y el id del alumno obtenido del objeto de tipo Alumno que se pasa como argumento de entrada a este método
    // El método "get" devuelve un Observable genérico de tipo "any" que es necesario convertirlo a nuestro tipo deseado, es decir, a un Observable de tipo Curso
    return this.httpClient.get<Curso>(`${this.baseEndPoint}/alumno/${alumno.id}`); // En este caso, en lugar de usar el método "pipe" junto con el operador "map" para convertir el Observable de tipo genérico "any", devuelto por el método "get", a un Observable de tipo Curso, dicha conversión la hacemos directamente usando la expresión "<Curso>" sobre el método "get"
  }

}
