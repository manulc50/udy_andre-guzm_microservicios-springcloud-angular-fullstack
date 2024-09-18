import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Alumno } from '../models/alumno';
import { CommonService } from './common.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { BASE_ENDPOINT } from '../config/app';

/* Este componente ha sido creado con el comando "ng g s services/alumno --skipTests=true"
   La opción "--skipTests=true" no genera el fichero "spec.ts" de pruebas unitarias
*/

@Injectable({ // Este decorador indica que esta clase es un servicio y nos permite inyectar esta clase o servicio en otra parte de la aplicación, como por ejemplo, en los componentes
  providedIn: 'root' // Esta propiedad hace que no sea necesario registrar este servicio en el módulo principal de la aplicación "app.module.ts"
})
export class AlumnoService extends CommonService<Alumno>{
  // Si no se indica un modificador de visibilidad en las propiedades de una clase, por defecto son públicas
  // Las propiedades privadas sólo se pueden usar dentro de esta clase
  // Las propiedades protected se pueden usar en la clase padre y en aquellas clases hijas que hereden esa clase padre
  
  constructor(httpClient: HttpClient){ // Inyectamos HttpClient en el constructor a través de la propiedad "httpClient" para poder realizar peticiones http a nuestro backend API REST
    super(httpClient);
    this.baseEndPoint = BASE_ENDPOINT + "/alumnos"; // Establecemos el valor de la propiedad "baseEndPoint" de la clase padre "CommonService" para indicar la url del Gateway de nuestro backend API REST donde se encuentra nuestro microservicio "microservicio-alumnos"
  }

  crearConFoto(alumno: Alumno,archivo: File): Observable<Alumno>{
    // Creamos un objeto de tipo FormData con la imagen o foto del alumno junto con sus datos para ser creados en nuestro backend API REST a través de una petición http Post
    const formData = new FormData();
    formData.append("archivo",archivo);
    formData.append("nombre",alumno.nombre);
    formData.append("apellidos",alumno.apellidos);
    formData.append("email",alumno.email);

    // Realiza una petición http Post a nuestro backend API REST a la url o ruta formada por el valor de la propiedad "baseEndPoint" de esta clase más el texto "/crear-con-foto" pasándole el objeto FormData con los datos del nuevo alumno y su imagen o foto para ser creados en este backend
    // En este caso, no pasamos a la petición http Post un objeto de opciones con una cabecera para indicar el tipo de contenido que vamos a enviar a nuestro backend API REST porque, al pasarle un objeto de tipo FormData, automáticamente se crear una cabecera con el tipo de contenido establecido a ese tipo de dato
    // El método "post" devuelve un Observable genérico de tipo "any" que es necesario convertirlo a nuestro tipo deseado, es decir, a un Observable de tipo Alumno
    // El método "pipe" nos permite aplicar operadores como "map" a un flujo Observable
    return this.httpClient.post(this.baseEndPoint + "/crear-con-foto",formData).pipe( // En lugar de usar el método "pipe" con el operador "map" para transformar el flujo Observable genérico de tipo "any", devuelto por el método "post", a un flujo Observable de tipo Alumno, podemos usar directamente la expresión "<Alumno>" sobre el método "post"
      map(respuesta => respuesta as Alumno) // El operador "as" realiza un casting para convertir, en este caso, el Observable genérico de tipo "any" contenido en "respuesta" en un Observable de tipo Alumno
    );
  }

  editarConFoto(alumno: Alumno,archivo: File,id: number): Observable<Alumno>{
    // Creamos un objeto de tipo FormData con la nueva imagen o foto del alumno junto con sus nuevos datos para ser actualizados en nuestro backend API REST a través de una petición http Put
    const formData = new FormData();
    formData.append("archivo",archivo);
    formData.append("nombre",alumno.nombre);
    formData.append("apellidos",alumno.apellidos);
    formData.append("email",alumno.email);

    // Realiza una petición http Put a nuestro backend API REST a la url o ruta formada por el contenido de la propiedad "baseEndPoint" de esta clase junto con el texto "/editar-con-foto/" y el valor del parámetro de entrada "id" pasándole el objeto FormData con los nuevos datos del alumno y su nueva imagen o foto para ser actualizados en este backend
    // En este caso, no pasamos a la petición http Put un objeto de opciones con una cabecera para indicar el tipo de contenido que vamos a enviar a nuestro backend API REST porque, al pasarle un objeto de tipo FormData, automáticamente se crear una cabecera con el tipo de contenido establecido a ese tipo de dato
    // El método "put" devuelve un Observable genérico de tipo "any" que es necesario convertirlo a nuestro tipo deseado, es decir, a un Observable de tipo Alumno
    return this.httpClient.put<Alumno>(`${this.baseEndPoint}/editar-con-foto/${id}`,formData); // En este caso, en lugar de usar el método "pipe" junto con el operador "map" para convertir el Observable de tipo genérico "any", devuelto por el método "put", a un Observable de tipo Alumno, dicha conversión la hacemos directamente usando la expresión "<Alumno>" sobre el método "put"
  }

  filtrarPorNombre(nombre: string): Observable<Alumno[]>{
    // Realiza una petición http Get a nuestro backend API REST a la url o ruta indicada por la propiedad "baseEndPoint" de esta clase + el texto "/filtrar/" + el término de búsqueda
    // El método "get" devuelve un Observable genérico de tipo "any" que es necesario convertirlo a nuestro tipo deseado, es decir, a un Observable de tipo Alumno[]
    // El método "pipe" nos permite aplicar operadores como "map" a un flujo Observable
    return this.httpClient.get(`${this.baseEndPoint}/filtrar/${nombre}`).pipe( // En lugar de usar el método "pipe" con el operador "map" para transformar el flujo Observable genérico de tipo "any", devuelto por el método "get", a un flujo Observable de tipo Alumno[], podemos usar directamente la expresión "<Alumno[]>" sobre el método "get"
      map(resp => resp as Alumno[]) // El operador "as" realiza un casting para convertir, en este caso, el Observable genérico de tipo "any" contenido en "resp" en un Observable de tipo Alumno[]
    );
  }
}
